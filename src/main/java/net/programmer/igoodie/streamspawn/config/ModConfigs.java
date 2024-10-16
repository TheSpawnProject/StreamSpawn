package net.programmer.igoodie.streamspawn.config;

import net.minecraftforge.fml.loading.FMLPaths;
import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.streamspawn.config.base.ModConfig;
import net.programmer.igoodie.streamspawn.config.file.CredentialsConfig;
import net.programmer.igoodie.streamspawn.exception.ExceptionContainer;

import java.io.File;

public class ModConfigs {

    public static final String CONFIG_DIR = FMLPaths.CONFIGDIR.get().toString() + File.separator + "StreamSpawn";

    public static ExceptionContainer ERRORS = new ExceptionContainer();

    public static CredentialsConfig CREDENTIALS;

    public static void loadConfigs() {
        ERRORS.reset();

        CREDENTIALS = loadConfig(new CredentialsConfig());
    }

    /* ----------------------------------- */

    private static <C extends JsonConfiGoodie & ModConfig> C loadConfig(C config) {
        String filePath = CONFIG_DIR + File.separator + config.getName();
        return config.readConfig(new File(filePath));
    }

}
