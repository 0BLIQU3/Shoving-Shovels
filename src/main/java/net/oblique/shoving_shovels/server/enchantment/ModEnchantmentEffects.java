package net.oblique.shoving_shovels.server.enchantment;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.oblique.shoving_shovels.ShovingShovels;

import java.util.function.Supplier;

public class ModEnchantmentEffects {
    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENTITY_ENCHANTMENT_EFFECTS =
            DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, ShovingShovels.MODID);

    public static final Supplier<MapCodec<? extends EnchantmentEntityEffect>> HEAVING =
            ENTITY_ENCHANTMENT_EFFECTS.register("heaving", () -> HeavingEnchantmentEffect.CODEC);
}
