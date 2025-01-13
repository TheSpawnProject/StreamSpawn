package net.programmer.igoodie.streamspawn.fabric;

import net.fabricmc.api.ModInitializer;
import net.programmer.igoodie.streamspawn.StreamSpawn;

public final class StreamSpawnFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        StreamSpawn.init();
    }

}
