package net.programmer.igoodie.streamspawn.init;

import net.programmer.igoodie.goodies.registry.Registry;
import net.programmer.igoodie.streamspawn.integration.base.Integration;
import net.programmer.igoodie.streamspawn.integration.base.IntegrationManifest;
import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.commonjs.CommonJS;
import net.programmer.igoodie.streamspawn.javascript.service.ScriptService;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.SpawnJS;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.globals.ServiceAPI;
import net.programmer.igoodie.streamspawn.javascript.streamspawn.StreamSpawnJS;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.commonjs.module.ModuleScope;

import java.io.File;

public class ModIntegrations {

    private static final Registry<String, Integration> INTEGRATION_REGISTRY = new Registry<>();

    public static void initialize() {
        CommonJS cjs = new CommonJS();
        ModuleScope topLevelScope = cjs.createTopLevelScope();

        SpawnJS spawnJS = new SpawnJS();
        spawnJS.install(topLevelScope);
        spawnJS.installModules(cjs);

        StreamSpawnJS streamSpawnJS = new StreamSpawnJS();
        streamSpawnJS.install(topLevelScope);
//        streamSpawnJS.installModules(cjs);

        new ServiceAPI(INTEGRATION_REGISTRY).install(topLevelScope);

        // TODO: Extract to StreamSpawn globals, as SpawnJS may not have integrations available
        JavascriptEngine.eval(topLevelScope, "stopIntegration = console.log");

        try {
            Integration integration = new Integration(
                    IntegrationManifest.of("test-streamlabs",
                            "Streamlabs Integration",
                            "1.0.0"));

            File integrationFile = new File(ModIntegrations.class.getClassLoader()
                    .getResource("assets/streamspawn/integrations/tcp_socket/integration.js")
                    .toURI()).getCanonicalFile();
            integration.loadScript(integrationFile);

            INTEGRATION_REGISTRY.register(integration);
            ScriptableObject integrationScope = integration.createScope(topLevelScope);
            Object result = integration.getScript().exec(JavascriptEngine.CONTEXT.get(), integrationScope);

            System.out.println("Executed integration, result = " + result);

            integration.services.forEach(ScriptService::begin);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        initialize();
        Context.exit();
    }

}
