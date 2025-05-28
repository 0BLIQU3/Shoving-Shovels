package net.oblique.shoving_shovels.server.enchantment;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.oblique.shoving_shovels.ShovingShovels;
import net.oblique.shoving_shovels.server.util.ShovingShovelsTags;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> HEAVING = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ShovingShovels.MODID, "heaving"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var enchantments = context.lookup(Registries.ENCHANTMENT);
        var items = context.lookup(Registries.ITEM);

        register(context, HEAVING, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ShovingShovelsTags.Items.SHOVING_SHOVEL_ENCHANTABLE),
                items.getOrThrow(ShovingShovelsTags.Items.SHOVING_SHOVEL_ENCHANTABLE),
                5,
                1,
                Enchantment.dynamicCost(5, 7),
                Enchantment.dynamicCost(25, 7),
                2,
                EquipmentSlotGroup.MAINHAND))
        );
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.location()));
    }
}
