package net.oblique.shoving_shovels.server.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.oblique.shoving_shovels.ShovingShovels;

public class ShovingShovelsEnLangProvider extends LanguageProvider {
    public ShovingShovelsEnLangProvider(PackOutput output) {
        super(output, ShovingShovels.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("attribute.shoving_shovels.shove_damage", "Shove Damage");

        add("enchantment.shoving_shovels.upheaval", "Upheaval");
        add("enchantment.shoving_shovels.upheaval.desc", "Dealing a critical hit launches the target into the air.");

        add("enchantment.shoving_shovels.rowdy", "Rowdy");
        add("enchantment.shoving_shovels.rowdy.desc", "Increases the damage of shoving.");

        add("enchantment.shoving_shovels.wallbash", "Wallbash");
        add("enchantment.shoving_shovels.wallbash.desc", "Targets can be hit into walls to inflict weakness/slowness and create a weak explosion.");


        //Everything related to config
        add("shoving_shovels.configuration.section.shoving.shovels.common.toml.title", "Shoving Shovels Config");
        add("shoving_shovels.configuration.Physics", "Physics");
        add("shoving_shovels.configuration.Physics.tooltip", "Physics Multipliers");
        add("shoving_shovels.configuration.Physics.button", "Edit...");
        add("shoving_shovels.configuration.yMomentumFactor", "Y Momentum Factor");
        add("shoving_shovels.configuration.yMomentumFactor.tooltip", "Vertical Shove Multiplier");
        add("shoving_shovels.configuration.horizontalKnockbackFactor", "Horizontal Knockback Factor");
        add("shoving_shovels.configuration.horizontalKnockbackFactor.tooltip", "Horizontal Shove Multiplier");
        add("shoving_shovels.configuration.totalKnockbackFactor", "Total Knockback Factor");
        add("shoving_shovels.configuration.totalKnockbackFactor.tooltip", "Total Shove Multiplier");
        add("shoving_shovels.configuration.Items", "Items");
        add("shoving_shovels.configuration.Items.tooltip", "Item Config");
        add("shoving_shovels.configuration.Items.button", "Edit...");
        add("shoving_shovels.configuration.shovelItems", "Shovel Items");
        add("shoving_shovels.configuration.shovelItems.tooltip", "Shovel Items Config");
        add("shoving_shovels.configuration.shovelItems.button", "Edit...");
        add("shoving_shovels.configuration.title", "Shoving Shovels");
        add("shoving_shovels.configuration.section.shoving.shovels.common.toml", "Shoving Shovels Common");

    }
}
