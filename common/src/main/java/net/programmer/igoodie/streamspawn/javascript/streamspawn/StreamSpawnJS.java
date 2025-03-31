package net.programmer.igoodie.streamspawn.javascript.streamspawn;

import net.programmer.igoodie.goodies.registry.Registry;
import net.programmer.igoodie.streamspawn.integration.base.Integration;
import net.programmer.igoodie.streamspawn.javascript.base.ScopeInstallable;
import net.programmer.igoodie.streamspawn.javascript.commonjs.CommonJS;
import net.programmer.igoodie.streamspawn.javascript.commonjs.ModuleInstallable;
import net.programmer.igoodie.streamspawn.javascript.streamspawn.modules.IntegrationsModule;
import org.mozilla.javascript.Scriptable;

public class StreamSpawnJS implements ScopeInstallable, ModuleInstallable {

    protected final Registry<String, Integration> integrationRegistry;

    public StreamSpawnJS(Registry<String, Integration> integrationRegistry) {
        this.integrationRegistry = integrationRegistry;
    }

    @Override
    public void install(Scriptable scope) {
        // TODO: Services/Integrations API
    }

    @Override
    public void installModules(CommonJS commonjs) {
        commonjs.registerIntrinsicModule(new IntegrationsModule());
    }

}
