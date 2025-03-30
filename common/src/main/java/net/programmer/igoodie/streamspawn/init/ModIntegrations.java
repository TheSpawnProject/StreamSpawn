package net.programmer.igoodie.streamspawn.init;

import net.programmer.igoodie.goodies.registry.Registry;
import net.programmer.igoodie.streamspawn.integration.base.Integration;
import net.programmer.igoodie.streamspawn.integration.base.IntegrationManifest;
import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.service.ScriptService;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.SpawnJS;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.globals.ServiceAPI;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import java.io.File;

public class ModIntegrations {

    private static final Registry<String, Integration> INTEGRATION_REGISTRY = new Registry<>();

    public static void initialize() {
        ScriptableObject globalScope = SpawnJS.createGlobal();
        new ServiceAPI(INTEGRATION_REGISTRY).install(globalScope);

        // TODO: Migrate those:
        JavascriptEngine.eval(globalScope, "stopIntegration = console.log");

        try {
            Integration integration = new Integration(
                    IntegrationManifest.of("test-streamlabs",
                            "Streamlabs Integration",
                            "1.0.0"));

            File integrationFile = new File(ModIntegrations.class.getClassLoader()
                    .getResource("assets/streamspawn/integrations/twitch_api/integration.js")
                    .toURI()).getCanonicalFile();
            integration.loadScript(integrationFile);

            INTEGRATION_REGISTRY.register(integration);
            ScriptableObject integrationScope = integration.createScope(globalScope);
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
