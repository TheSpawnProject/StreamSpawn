package net.programmer.igoodie.streamspawn.javascript.spawnjs.globals;

import net.programmer.igoodie.goodies.registry.Registry;
import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.streamspawn.integration.base.Integration;
import net.programmer.igoodie.streamspawn.integration.base.IntegrationManifest;
import net.programmer.igoodie.streamspawn.javascript.base.ScopeInstallable;
import net.programmer.igoodie.streamspawn.javascript.service.ScriptService;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.SpawnJSExceptions;
import org.mozilla.javascript.*;

public class ServiceAPI implements ScopeInstallable {

    protected final Registry<String, Integration> integrations;

    public ServiceAPI(Registry<String, Integration> integrations) {
        this.integrations = integrations;
    }

    @Override
    public void install(ScriptableObject scope) {
        scope.defineProperty("registerService", new RegisterServiceFn(), ScriptableObject.CONST);
    }

    public class RegisterServiceFn extends BaseFunction {

        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
            Object arg0 = argsAccessor.get(0).orElse(null);

            if (arg0 instanceof ScriptService service) {
                IntegrationManifest manifest = (IntegrationManifest) Context.jsToJava(scope.get("integration", scope), IntegrationManifest.class);
                Integration integration = ServiceAPI.this.integrations.get(manifest.id);
                integration.services.add(service);
                return service;
            }

            throw SpawnJSExceptions.invalidArguments(this, args, ((Function) thisObj));
        }

    }

}
