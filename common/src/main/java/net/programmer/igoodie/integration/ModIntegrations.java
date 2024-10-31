package net.programmer.igoodie.integration;

import net.programmer.igoodie.integration.base.Integration;
import net.programmer.igoodie.javascript.JavascriptEngine;
import net.programmer.igoodie.javascript.base.ServiceObject;
import net.programmer.igoodie.javascript.global.EmitFn;
import net.programmer.igoodie.javascript.global.PrintFn;
import net.programmer.igoodie.javascript.global.PromiseHost;
import net.programmer.igoodie.javascript.global.SetTimeoutFn;
import net.programmer.igoodie.javascript.network.RegisterServiceFn;
import net.programmer.igoodie.javascript.network.SocketIOHost;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModIntegrations {

    private static final Map<String, Integration> INTEGRATION_REGISTRY = new HashMap<>();

    public static void initialize() {
        ScriptableObject globalScope = JavascriptEngine.createScope();

        ScriptableObject libNetwork = JavascriptEngine.createScope();
        JavascriptEngine.defineClass(libNetwork, SocketIOHost.class);

        JavascriptEngine.defineClass(globalScope, PromiseHost.class);
        globalScope.defineProperty("Promise", Undefined.class, ScriptableObject.CONST);

        globalScope.defineProperty("print", new PrintFn(), ScriptableObject.CONST);
        globalScope.defineProperty("setTimeout", new SetTimeoutFn(), ScriptableObject.CONST);
        globalScope.defineProperty("Network", libNetwork, ScriptableObject.CONST);
        globalScope.defineProperty("registerService", new RegisterServiceFn(INTEGRATION_REGISTRY), ScriptableObject.CONST);
        globalScope.defineProperty("emit", new EmitFn(), ScriptableObject.CONST);
        globalScope.defineProperty("stopIntegration", new PrintFn(), ScriptableObject.CONST);
        globalScope.sealObject();

        try {
            Integration integration = new Integration("test-streamlabs", "1.0.0");
            integration.downloadScript("https://pastebin.com/raw/B1J0kQym");
            integration.loadScript("" +
                    "new Bromise((resolve) => {\n" +
                    "  setTimeout(() => {\n" +
                    "    resolve(\"DONE!\");\n" +
                    "  }, 1000);\n" +
                    "})" +
                    "   .then(res => {print(res);return res + 'A'})" +
                    "   .then(print)" +
                    "   .then(res => { throw new Error('Wopsie') })" +
                    "   .catch(err => print('Omg an error', err.getValue()));");
            INTEGRATION_REGISTRY.put(integration.getName(), integration);
            ScriptableObject integrationScope = integration.createScope(globalScope);
            integration.getScript().exec(JavascriptEngine.CONTEXT.get(), integrationScope);

            integration.services.forEach(ServiceObject::begin);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        initialize();
        Context.exit();
    }

}
