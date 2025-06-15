package net.oblique.shoving_shovels.server.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.oblique.shoving_shovels.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoveDamageHandler {
    private static final Map<Item, Double> SHOVE_DAMAGE_MAP = new HashMap<>();
    public static void loadFromConfig() {
        SHOVE_DAMAGE_MAP.clear();
        List<? extends String> configList = Config.shovelItemList.get();

        for (String entry : configList) {
            try {
                String[] parts = entry.split("=");
                if (parts.length != 2) {
                    continue;
                }
                ResourceLocation id = ResourceLocation.tryParse(parts[0]);
                if (id == null || !BuiltInRegistries.ITEM.containsKey(id)) {
                    continue;
                }
                Item item = BuiltInRegistries.ITEM.get(id);
                double damageDouble = Double.parseDouble(parts[1]);
                SHOVE_DAMAGE_MAP.put(item, damageDouble);
            } catch (Exception exception) {
                System.err.println("ShoveDamageHandler - Failed to parse config entry: " + entry);
                exception.printStackTrace();
            }
        }
    }
    public static double getShoveDamage(ItemStack itemStack) {
        return SHOVE_DAMAGE_MAP.getOrDefault(itemStack.getItem(), 2.0);
    }
}
