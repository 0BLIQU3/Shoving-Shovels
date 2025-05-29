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
    }
}
