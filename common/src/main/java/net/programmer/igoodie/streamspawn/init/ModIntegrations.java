package net.programmer.igoodie.streamspawn.init;

import net.programmer.igoodie.goodies.registry.Registry;
import net.programmer.igoodie.streamspawn.integration.base.Integration;
import net.programmer.igoodie.streamspawn.integration.base.IntegrationManifest;
import net.programmer.igoodie.streamspawn.javascript.commonjs.CommonJS;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.SpawnJS;
import net.programmer.igoodie.streamspawn.javascript.streamspawn.StreamSpawnJS;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.File;

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

        try {
            Integration integration = new Integration(
                    IntegrationManifest.of("test-streamlabs",
                            "Streamlabs Integration",
                            "1.0.0"));

            File scriptFile = new File(ModIntegrations.class.getClassLoader()
                    .getResource("assets/streamspawn/integrations/twitch_api/integration.js")
                    .toURI()).getCanonicalFile();
            integration.loadScript(scriptFile);
            integration.evaluateScript(topLevelScope);

            INTEGRATION_REGISTRY.register(integration);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void start() {
        INTEGRATION_REGISTRY.forEach(Integration::start);
    }

    public static void stop() {
        INTEGRATION_REGISTRY.forEach(Integration::stop);
    }

    public static void main(String[] args) {
        initialize();
        start();
        Context.exit();
    }

}
