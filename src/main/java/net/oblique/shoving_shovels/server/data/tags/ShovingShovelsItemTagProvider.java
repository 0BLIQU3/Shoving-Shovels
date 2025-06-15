package net.oblique.shoving_shovels.server.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
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
        var tagBuilder = this.tag(ShovingShovelsTags.Items.SHOVING_SHOVEL_ENCHANTABLE);
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof ShovelItem) {
                tagBuilder.add(item);
            }
        }
    }
    @Override
    public String getName() {
        return "Shoving Shovels Item Tags";
    }
}