package net.programmer.igoodie.streamspawn.init;

import net.programmer.igoodie.streamspawn.integration.base.Integration;
import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.base.ServiceObject;
import net.programmer.igoodie.streamspawn.javascript.global.EmitFn;
import net.programmer.igoodie.streamspawn.javascript.global.PrintFn;
import net.programmer.igoodie.streamspawn.javascript.global.RegisterServiceFn;
import net.programmer.igoodie.streamspawn.javascript.network.SocketIOHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.core.TimerAPI;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.HashMap;
import java.util.Map;

public class ModIntegrations {

    private static final Map<String, Integration> INTEGRATION_REGISTRY = new HashMap<>();

    public static void initialize() {
        Scriptable globalScope = createSpawnJSScope();

        try {
            Integration integration = new Integration("test-streamlabs", "1.0.0");
//            integration.downloadScript("https://pastebin.com/raw/B1J0kQym");
//            integration.loadScript("" +
//                    "new Promise((resolve) => {\n" +
//                    "  print('Promise started')\n" +
//                    "  setTimeout(() => {\n" +
//                    "    print('Resolve!', resolve);\n" +
//                    "    resolve(\"DONE!\");\n" +
//                    "  }, 1000);\n" +
//                    "})" +
//                    "   .then(res => {print(res);return res + 'A'})" +
//                    "   .then(print)" +
//                    "   .then(res => new Promise(r => r(1)))" +
//                    "   .then(print)" +
//                    "   .then(res => { throw new Error('Wopsie') })" +
//                    "   .catch(err => print('Omg an error', err.getValue()));");
            INTEGRATION_REGISTRY.put(integration.getName(), integration);
            ScriptableObject integrationScope = integration.createScope(globalScope);
            integration.getScript().exec(JavascriptEngine.CONTEXT.get(), integrationScope);

            integration.services.forEach(ServiceObject::begin);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static Scriptable createSpawnJSScope() {
        ScriptableObject globalScope = JavascriptEngine.createScope();

        // Network Library
        ScriptableObject nsNetwork = JavascriptEngine.createScope();
        JavascriptEngine.defineClass(nsNetwork, SocketIOHost.class);
        globalScope.defineProperty("Network", nsNetwork, ScriptableObject.CONST);

        // Global Functions
//        globalScope.delete("Promise");
//        JavascriptEngine.defineClass(globalScope, PromiseHost.class);
//        System.out.println(((FunctionObject) globalScope.get("Promise")).getFunctionName());

        // Built-in APIs
        new TimerAPI().init(globalScope);

        // Global Constants
        globalScope.defineProperty("print", new PrintFn(), ScriptableObject.CONST);
//        globalScope.defineProperty("setTimeout", new SetTimeoutFn(), ScriptableObject.CONST);
        globalScope.defineProperty("registerService", new RegisterServiceFn(INTEGRATION_REGISTRY), ScriptableObject.CONST);
        globalScope.defineProperty("emit", new EmitFn(), ScriptableObject.CONST);
        globalScope.defineProperty("stopIntegration", new PrintFn(), ScriptableObject.CONST);

        globalScope.sealObject();
        return globalScope;
    }

    public static void main(String[] args) {
        initialize();
        Context.exit();
    }

}
