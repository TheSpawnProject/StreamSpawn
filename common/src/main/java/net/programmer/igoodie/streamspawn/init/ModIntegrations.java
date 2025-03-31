package net.programmer.igoodie.streamspawn.init;

import net.programmer.igoodie.goodies.registry.Registry;
import net.programmer.igoodie.streamspawn.integration.Integration;
import net.programmer.igoodie.streamspawn.integration.IntegrationManifest;
import net.programmer.igoodie.streamspawn.javascript.commonjs.CommonJS;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.SpawnJS;
import net.programmer.igoodie.streamspawn.javascript.streamspawn.StreamSpawnJS;
import net.programmer.igoodie.tsl.exception.TSLSyntaxException;
import net.programmer.igoodie.tsl.runtime.event.TSLEvent;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.io.File;
import java.io.IOException;

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
            IntegrationManifest manifest = IntegrationManifest.of(
                    "test-streamlabs",
                    "Streamlabs Integration",
                    "1.0.0");

            Integration integration = new Integration(manifest);

            // TODO: load up TSL events from the manifest
            ModRulesets.TSL.registerEvent(new TSLEvent("Clock Begin")
                    .addPropertyType(TSLEvent.PropertyBuilder.DOUBLE.create("time"))
            );
            ModRulesets.TSL.registerEvent(new TSLEvent("Clock Tick")
                    .addPropertyType(TSLEvent.PropertyBuilder.STRING.create("actor"))
                    .addPropertyType(TSLEvent.PropertyBuilder.DOUBLE.create("dt"))
            );
            ModRulesets.TSL.registerEvent(new TSLEvent("Clock End")
                    .addPropertyType(TSLEvent.PropertyBuilder.DOUBLE.create("time"))
            );

            File scriptFile = new File(ModIntegrations.class.getClassLoader()
                    .getResource("assets/streamspawn/integrations/clock_tick/integration.js")
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

    public static void main(String[] args) throws TSLSyntaxException, IOException {
        initialize();
        ModRulesets.initialize();
        start();
        Context.exit();
    }

}
