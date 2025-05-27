package net.oblique.shoving_shovels.server.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.oblique.shoving_shovels.ShovingShovels;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ShovingShovels.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Datagen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        try {
            //These are required as constructor parameters for most data generators
            DataGenerator generator = event.getGenerator();
            PackOutput output = generator.getPackOutput();
            ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
            CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

            generator.addProvider(event.includeServer(), new ShovingShovelsEnLangProvider(output));

        } catch (RuntimeException fail) {
            ShovingShovels.LOGGER.error("Failed to generate data, fail");
        }
    }
}
