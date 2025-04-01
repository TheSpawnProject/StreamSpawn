package net.programmer.igoodie.streamspawn.init;

import net.programmer.igoodie.goodies.registry.Registry;
import net.programmer.igoodie.goodies.util.FileUtils;
import net.programmer.igoodie.streamspawn.StreamSpawn;
import net.programmer.igoodie.streamspawn.integration.Integration;
import net.programmer.igoodie.streamspawn.integration.IntegrationLoader;
import net.programmer.igoodie.streamspawn.javascript.commonjs.CommonJS;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.SpawnJS;
import net.programmer.igoodie.streamspawn.javascript.streamspawn.StreamSpawnJS;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ModIntegrations {

    private static final Registry<String, Integration> INTEGRATION_REGISTRY = new Registry<>();

    public static void initialize() {
        CommonJS cjs = new CommonJS();
        Scriptable topLevelScope = cjs.createTopLevelScope();

        SpawnJS spawnJS = new SpawnJS();
        spawnJS.install(topLevelScope);
        spawnJS.installModules(cjs);

        StreamSpawnJS streamSpawnJS = new StreamSpawnJS(INTEGRATION_REGISTRY);
        streamSpawnJS.install(topLevelScope);
        streamSpawnJS.installModules(cjs);

        FileUtils.createFileIfAbsent(ModPaths.INTEGRATIONS.toFile());
        File[] integrationFolders = Objects.requireNonNull(ModPaths.INTEGRATIONS.toFile().listFiles(File::isDirectory));

        for (File integrationFolder : integrationFolders) {
            IntegrationLoader loader = new IntegrationLoader(INTEGRATION_REGISTRY, ModTSL.TSL, topLevelScope, integrationFolder);
            Integration integration = loader.load();

            if (loader.getState() == IntegrationLoader.State.FAILED) {
                StreamSpawn.LOGGER.error("Failed loading integration {}", integrationFolder);
                StreamSpawn.LOGGER.error("\t- Integration State: {}", integration.getState());
                StreamSpawn.LOGGER.error("\t- Loader Error:", loader.getError());
                continue;
            }

            INTEGRATION_REGISTRY.register(integration);
            StreamSpawn.LOGGER.info("Loaded StreamSpawn integration [{}@{}]({})",
                    integration.getId(), integration.getManifest().version, integrationFolder);
        }
    }

    public static void start() {
        INTEGRATION_REGISTRY.forEach(Integration::start);
    }

    public static void stop() {
        INTEGRATION_REGISTRY.forEach(Integration::stop);
    }

    public static void main(String[] args) throws TSLSyntaxException, IOException {
        initialize();
        ModTSL.initializePlatform();
        start();
        Context.exit();
    }

}
