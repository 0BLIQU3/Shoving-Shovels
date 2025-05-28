package net.oblique.shoving_shovels.server.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.oblique.shoving_shovels.ShovingShovels;
import net.oblique.shoving_shovels.server.util.ShovingShovelsTags;

import java.util.concurrent.CompletableFuture;

public class ShovingShovelsItemTagProvider extends ItemTagsProvider {
    public ShovingShovelsItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CompletableFuture.completedFuture(TagLookup.empty()), ShovingShovels.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ShovingShovelsTags.Items.SHOVING_SHOVEL_ENCHANTABLE)
                .add(Items.WOODEN_SHOVEL)
                .add(Items.STONE_SHOVEL)
                .add(Items.GOLDEN_SHOVEL)
                .add(Items.IRON_SHOVEL)
                .add(Items.DIAMOND_SHOVEL)
                .add(Items.NETHERITE_SHOVEL);
    }
    @Override
    public String getName() {
        return "Shoving Shovels Item Tags";
    }
}