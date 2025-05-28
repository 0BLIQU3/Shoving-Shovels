package net.oblique.shoving_shovels.server.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.oblique.shoving_shovels.ShovingShovels;
import net.oblique.shoving_shovels.server.enchantment.ModEnchantments;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ShovingShovelsDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.ENCHANTMENT, ModEnchantments::bootstrap);

    public ShovingShovelsDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(ShovingShovels.MODID));
    }

    //Gotta change the name, otherwise we have a duplicate provider man this is goofy ahh hell
    @Override
    public String getName() {
        return "Shoving Shovels Datapack Provider";
    }
}
