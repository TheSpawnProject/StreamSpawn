package net.programmer.igoodie.streamspawn.init;

import net.programmer.igoodie.goodies.registry.Registry;
import net.programmer.igoodie.streamspawn.integration.base.Integration;
import net.programmer.igoodie.streamspawn.integration.base.IntegrationManifest;
import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
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

            INTEGRATION_REGISTRY.register(integration);

            File scriptFile = new File(ModIntegrations.class.getClassLoader()
                    .getResource("assets/streamspawn/integrations/tcp_socket/integration.js")
                    .toURI()).getCanonicalFile();
            integration.loadScript(scriptFile);

            Scriptable integrationScope = JavascriptEngine.forkScope(topLevelScope);
            integration.install(integrationScope);
            integration.load(integrationScope);
            integration.start();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        initialize();
        Context.exit();
    }

}
