package net.programmer.igoodie.streamspawn.init;

import dev.architectury.platform.Platform;

import java.nio.file.Path;

public class ModPaths {

    public static Path TSL;
    public static Path INTEGRATIONS;
    public static Path BUILT_IN_MODULES;

    public static void initialize() {
        TSL = Platform.getGameFolder().resolve("tsl");
        INTEGRATIONS = TSL.resolve("integrations");
        BUILT_IN_MODULES = INTEGRATIONS.resolve("spawn_modules");
    }

    public static void _initializeMock() {
        // TODO: For testing later
    }

}
