package net.programmer.igoodie.fabric;

import net.fabricmc.api.ModInitializer;
import net.programmer.igoodie.StreamSpawn;

public final class StreamSpawnFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        StreamSpawn.init();
    }

}
