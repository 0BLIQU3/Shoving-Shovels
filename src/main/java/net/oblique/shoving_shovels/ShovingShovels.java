package net.oblique.shoving_shovels;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.oblique.shoving_shovels.server.enchantment.ModEnchantmentEffects;
import net.oblique.shoving_shovels.server.registry.AttributeRegistry;
import org.slf4j.Logger;

@Mod(ShovingShovels.MODID)
public class ShovingShovels {
    public static final String MODID = "shoving_shovels";
    public static final Logger LOGGER = LogUtils.getLogger();
    //Initialization
    public ShovingShovels(IEventBus modEventBus, ModContainer modContainer) {

        //Registers
        AttributeRegistry.ATTRIBUTES.register(modEventBus);
        ModEnchantmentEffects.ENTITY_ENCHANTMENT_EFFECTS.register(modEventBus);

        //Register our mod's ModConfigSpec so that FML can create and load the config file for us (then add it to Neoforge's Config screen)
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
