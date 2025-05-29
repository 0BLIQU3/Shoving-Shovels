package net.oblique.shoving_shovels.server.enchantment;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.oblique.shoving_shovels.ShovingShovels;
import net.oblique.shoving_shovels.server.registry.AttributeRegistry;
import net.oblique.shoving_shovels.server.util.ShovingShovelsTags;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> UPHEAVAL = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ShovingShovels.MODID, "upheaval"));
    public static final ResourceKey<Enchantment> ROWDY = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ShovingShovels.MODID, "rowdy"));

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var enchantments = context.lookup(Registries.ENCHANTMENT);
        var items = context.lookup(Registries.ITEM);

        register(context, UPHEAVAL, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ShovingShovelsTags.Items.SHOVING_SHOVEL_ENCHANTABLE),
                5,
                1,
                Enchantment.dynamicCost(5, 7),
                Enchantment.dynamicCost(25, 7),
                2,
                EquipmentSlotGroup.MAINHAND))
        );

        register(context, ROWDY, Enchantment.enchantment(Enchantment.definition(
                items.getOrThrow(ShovingShovelsTags.Items.SHOVING_SHOVEL_ENCHANTABLE),
                5,
                3,
                Enchantment.dynamicCost(5, 7),
                Enchantment.dynamicCost(25, 7),
                2,
                EquipmentSlotGroup.MAINHAND))
                .withEffect(
                        EnchantmentEffectComponents.ATTRIBUTES,
                        new EnchantmentAttributeEffect(
                                ResourceLocation.fromNamespaceAndPath(ShovingShovels.MODID, "enchantment.shoving_shovels.rowdy"),
                                AttributeRegistry.SHOVE_DAMAGE,
                                LevelBasedValue.perLevel(0.5F),
                                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        ))
        );
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.location()));
    }
}
