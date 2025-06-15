package net.oblique.shoving_shovels;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ShovelItem;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class Config {
    public static final ModConfigSpec SPEC;
    public static final Config INSTANCE;
    public static ModConfigSpec.DoubleValue yMomentumFactor;
    public static ModConfigSpec.DoubleValue horizontalKnockbackFactor;
    public static ModConfigSpec.DoubleValue totalKnockbackFactor;
    public static ModConfigSpec.ConfigValue<List<? extends String>> shovelItemList;

    static {
        Pair<Config, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Config::new);
        INSTANCE = pair.getLeft();
        SPEC = pair.getRight();
    }

    public Config(ModConfigSpec.Builder builder) {
        builder.translation("shoving_shovels.configuration.Physics").comment("Physics Multipliers").push("Physics");
        yMomentumFactor = builder
                .translation("shoving_shovels.configuration.yMomentumFactor")
                .comment("Vertical shove multiplier")
                .defineInRange("yMomentumFactor", 1.0, 0.0, 100.0);
        horizontalKnockbackFactor = builder
                .translation("shoving_shovels.configuration.horizontalKnockbackFactor")
                .comment("Horizontal shove multiplier")
                .defineInRange("horizontalKnockbackFactor", 1.0, 0.0, 100.0);
        totalKnockbackFactor = builder
                .translation("shoving_shovels.configuration.totalKnockbackFactor")
                .comment("Total shove multiplier")
                .defineInRange("totalKnockbackFactor", 1.0, 0.0, 100.0);
        builder.pop();

        builder.translation("shoving_shovels.configuration.Items").comment("Shove Items").push("Items");
        shovelItemList = builder
                .translation("shoving_shovels.configuration.shovelItems")
                .comment("List of shovel item IDs with damage values (format: 'modid:itemid=damage')")
                .comment("By default, this generates a list of all shovel items within your modpack, so if you're adding new mods with shovels, be sure to regenerate this config.")
                .defineList("shovelItems", getDefaultShovelItemList(), () -> "modid:itemid=1.0",
                        obj -> obj instanceof String && ((String) obj).contains("="));
        builder.pop();
    }
    //WAHHHHHH it was taking too long to figure out how to make it actually good, so we're parsing strings now
    private static List<String> getDefaultShovelItemList() {
        return BuiltInRegistries.ITEM.stream()
                .filter(item -> item instanceof ShovelItem)
                .map(item -> {
                    ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
                    double attackDamage = ((ShovelItem) item).getTier().getAttackDamageBonus();
                    return id + "=" + (attackDamage + 1); // Default damage
                })
                .toList();
    }
}
