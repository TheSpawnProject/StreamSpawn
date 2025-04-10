package net.programmer.igoodie.streamspawn.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.programmer.igoodie.streamspawn.StreamSpawn;

@Mod(StreamSpawn.MOD_ID)
public final class StreamSpawnForge {

    public StreamSpawnForge() {
        EventBuses.registerModEventBus(StreamSpawn.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        StreamSpawn.init();
    }

}
