package net.oblique.shoving_shovels.server.event;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.oblique.shoving_shovels.ShovingShovels;
import net.oblique.shoving_shovels.server.registry.AttributeRegistry;
import net.oblique.shoving_shovels.server.util.ShoveDamageHandler;

import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = ShovingShovels.MODID, bus = EventBusSubscriber.Bus.GAME)
public class GameEventBusEvents {

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent attackEntityEvent) {
        if (!(attackEntityEvent.getTarget() instanceof LivingEntity target)) {
            return;
        }
        Player player = attackEntityEvent.getEntity();
        //Check if we're using a shovel in the first place
        //WeaponItem is basically just MainhandItem but also accounts for Riptide apparently? might as well check ig
        ItemStack heldItem = player.getWeaponItem();
        if (!(heldItem.getItem() instanceof ShovelItem)) {
            return;
        }
        int shovingDamage = (int) player.getAttributeValue(AttributeRegistry.SHOVE_DAMAGE);

        Vec3 lookingAngle = player.getLookAngle(); //Already a unit vector
        //Actually apply the knockback
        double knockbackStrength = 2.0;
        //Make sure it scales properly with attack cooldown
        float strengthScale = player.getAttackStrengthScale(0.5F);
        knockbackStrength *= strengthScale;
        knockbackStrength *= 1.0 - target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);

        Vec3 shoveMotion = lookingAngle.scale(knockbackStrength);
        Vec3 currentMotion = target.getDeltaMovement();

        double yShove = getYShove(shoveMotion, lookingAngle);
        //Must set target to be off ground, or y momentum gets zero-ed out, thank you Minecraft
        target.setOnGround(false);
        target.setDeltaMovement(
                currentMotion.x * 0.5 + (shoveMotion.x * 1.1),
                currentMotion.y * 0.5 + yShove,
                currentMotion.z * 0.5 + (shoveMotion.z * 1.1)
        );
        target.hasImpulse = true;

        //Mark target for shoving logic
        UUID shoveID = UUID.randomUUID();
        target.getPersistentData().putBoolean("Shoved", true);
        target.getPersistentData().putInt("ShovedAge", 0);
        target.getPersistentData().putUUID("ShoveID", shoveID);
        target.getPersistentData().putInt("ShoveDamage", shovingDamage);
        target.getPersistentData().putUUID("ShoveSource", player.getUUID());
    }

    @SubscribeEvent
    public static void onEntityUpdate(EntityTickEvent.Post entityTickEvent) {
        if (!(entityTickEvent.getEntity() instanceof LivingEntity shovedTarget)) {
            return;
        }
        if (!(shovedTarget.getPersistentData().contains("Shoved"))) {
            return;
        }
        //If it's just dead anyway, don't even bother!
        if (!shovedTarget.isAlive()) {
            clearShoveData(shovedTarget);
            return;
        }
        shovedTarget.getPersistentData().putInt("ShovedAge", shovedTarget.getPersistentData().getInt("ShovedAge") + 1);
        //1 second cooldown on Shoving mobs. May change this?
        if (shovedTarget.getPersistentData().getInt("ShovedAge") > 20) {
            clearShoveData(shovedTarget);
            return;
        }
        //We need to create particles for the shoved mob for some good visual feedback
        if (shovedTarget.level() instanceof ServerLevel serverLevel) {
            if (shovedTarget.getPersistentData().getInt("ShovedAge") < 20) {
                //This should slightly shift the particles back to form a proper "trail"
                Vec3 back = shovedTarget.position().subtract(shovedTarget.getDeltaMovement().scale(1.0));
                serverLevel.sendParticles(
                        ParticleTypes.POOF,
                        back.x,
                        back.y + shovedTarget.getBbHeight() / 2,
                        back.z,
                        2,
                        0.2, 0.2, 0.2,
                        0.02
                );
            }
        }

        //Get shoveID of target: we'll want to check it against hit targets and then copy it to them if there's a mismatch (don't damage if it's the same)
        //This results in each shove being able to damage once, since shoving a target creates a new ID each time (so most of the time there will be a mismatch anyway)
        UUID shoveID = shovedTarget.getPersistentData().getUUID("ShoveID");

        //We now have an entity that has been Shoved recently, so let's do our damage
        AABB hitbox = shovedTarget.getBoundingBox().inflate(0.3);
        //Create a list of every living entity within the hitbox of the shove
        List<LivingEntity> targets = shovedTarget.level().getEntitiesOfClass(LivingEntity.class, hitbox,
                e -> e != shovedTarget && e.isAlive());

        for (LivingEntity target : targets) {
            //If the target has already been hit by this specific shove before, skip it
            if (target.getPersistentData().hasUUID("LastShoveHitID") && target.getPersistentData().getUUID("LastShoveHitID").equals(shoveID)) {
                continue;
            }
            int shoveDamage = shovedTarget.getPersistentData().getInt("ShoveDamage");
            UUID sourceUUID = shovedTarget.getPersistentData().getUUID("ShoveSource");
            //Make sure we're getting the correct source of damage (that being, the player)
            if (shovedTarget.level() instanceof ServerLevel serverLevel) {
                Player sourcePlayer = serverLevel.getPlayerByUUID(sourceUUID);
                if (sourcePlayer != null) {
                    target.hurt(serverLevel.damageSources().playerAttack(sourcePlayer), shoveDamage);
                }
                else {
                    target.hurt(serverLevel.damageSources().generic(), shoveDamage);
                }
            }
            target.getPersistentData().putUUID("LastShoveHitID", shoveID);
        }
    }

    @SubscribeEvent
    public static void onItemAttributeModifiers(ItemAttributeModifierEvent itemAttributeModifierEvent) {
        ItemStack itemStack = itemAttributeModifierEvent.getItemStack();
        if (!(itemStack.getItem() instanceof ShovelItem)) {
            return;
        }
        double baseDamage = ShoveDamageHandler.getShoveDamage(itemStack);

        itemAttributeModifierEvent.addModifier(AttributeRegistry.SHOVE_DAMAGE, new AttributeModifier(
                ResourceLocation.fromNamespaceAndPath(ShovingShovels.MODID, "shove_damage"),
                baseDamage,
                AttributeModifier.Operation.ADD_VALUE
        ),
                EquipmentSlotGroup.MAINHAND
        );
    }

    private static double getYShove(Vec3 shoveMotion, Vec3 lookingAngle) {
        //Moderate reduction in y momentum as this can otherwise send people FLYING if you are directly under them
        double yShove = shoveMotion.y * 0.58;
        //For very small looking negative angles, apply a little upwards momentum, so we're not bashing them into the ground and losing knockback
        //May make this configurable and also change this logic a bit later, but for now we just roll with it haha
        if (yShove < 0.05 && lookingAngle.y > -0.8) {
            yShove += 0.2;
        }
        return yShove;
    }

    private static void clearShoveData(LivingEntity entity) {
        entity.getPersistentData().remove("Shoved");
        entity.getPersistentData().remove("ShovedAge");
        entity.getPersistentData().remove("ShoveID");
        entity.getPersistentData().remove("LastShoveHitID");
        entity.getPersistentData().remove("ShoveDamage");
        entity.getPersistentData().remove("ShoveSource");
    }

}
