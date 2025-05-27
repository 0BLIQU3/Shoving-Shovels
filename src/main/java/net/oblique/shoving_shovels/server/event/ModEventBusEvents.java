package net.oblique.shoving_shovels.server.event;

import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.oblique.shoving_shovels.ShovingShovels;
import net.oblique.shoving_shovels.server.registry.AttributeRegistry;

@EventBusSubscriber(modid = ShovingShovels.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        //Gotta make sure that the attribute is also registered to the player so that we don't crash when checking for damage
        event.add(EntityType.PLAYER, AttributeRegistry.SHOVE_DAMAGE);
    }
}
