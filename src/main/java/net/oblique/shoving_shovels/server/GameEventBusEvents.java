package net.oblique.shoving_shovels.server;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.oblique.shoving_shovels.ShovingShovels;

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
    }

    private static double getYShove(Vec3 shoveMotion, Vec3 lookingAngle) {
        double yShove = shoveMotion.y;
        //Moderate reduction in y momentum as this can otherwise send people FLYING if you are directly under them
        yShove *= 0.5;
        //For very small looking negative angles, apply a little upwards momentum, so we're not bashing them into the ground and losing knockback
        //May make this configurable and also change this logic a bit later, but for now we just roll with it haha
        if (yShove < 0.02 && lookingAngle.y < 0 && lookingAngle.y > -0.6) {
            yShove += 0.2;
        }
        return yShove;
    }

    @SubscribeEvent
    public static void onEntityUpdate(EntityTickEvent.Post entityTickEvent) {
        if (!(entityTickEvent.getEntity() instanceof LivingEntity shovedTarget)) {
            return;
        }
        if (!(shovedTarget.getPersistentData().contains("Shoved"))) {
            return;
        }
        shovedTarget.getPersistentData().putInt("ShovedAge", shovedTarget.getPersistentData().getInt("ShovedAge") + 1);

        //1 second cooldown on Shoving mobs. May change this?
        if (shovedTarget.getPersistentData().getInt("ShovedAge") > 20) {
            shovedTarget.getPersistentData().remove("Shoved");
            shovedTarget.getPersistentData().remove("ShovedAge");
            shovedTarget.getPersistentData().remove("ShoveID");
            return;
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
            //Change persistent data to store Player for damage source soon
            target.hurt(shovedTarget.damageSources().generic(), 2);
            target.getPersistentData().putUUID("LastShoveHitID", shoveID);
        }
    }

}
