package net.oblique.shoving_shovels.server.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.oblique.shoving_shovels.ShovingShovels;

public class ShovingShovelsTags {
    public static class Items {
        public static final TagKey<Item> SHOVING_SHOVEL_ENCHANTABLE = createTag("shoving_shovels_enchantable");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(ShovingShovels.MODID, name));
        }
    }
}
