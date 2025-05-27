package net.oblique.shoving_shovels.server.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Map;

public class ShoveDamageHandler {
    private static final Map<Item, Integer> SHOVE_DAMAGE_MAP = Map.of(
            Items.WOODEN_SHOVEL, 2,
            Items.STONE_SHOVEL, 3,
            Items.IRON_SHOVEL, 4,
            Items.GOLDEN_SHOVEL, 3,
            Items.DIAMOND_SHOVEL, 5,
            Items.NETHERITE_SHOVEL, 6
    );
    public static int getShoveDamage(ItemStack itemStack) {
        return SHOVE_DAMAGE_MAP.getOrDefault(itemStack.getItem(), 2);
    }
}
