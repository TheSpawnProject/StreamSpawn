package net.programmer.igoodie.quilt;

import net.programmer.igoodie.StreamSpawn;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public final class StreamSpawnQuilt implements ModInitializer {

    @Override
    public void onInitialize(ModContainer mod) {
        StreamSpawn.init();
    }

}
