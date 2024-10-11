package net.programmer.igoodie.streamspawn;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public class MixinConnector implements IMixinConnector {

    @Override
    public void connect() {
        String modConfigFile = String.format("assets/%s/%s.mixins.json", StreamSpawn.MOD_ID, StreamSpawn.MOD_ID);
        Mixins.addConfigurations(modConfigFile);
    }
}