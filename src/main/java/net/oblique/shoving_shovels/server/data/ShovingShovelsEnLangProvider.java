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
    }
}
