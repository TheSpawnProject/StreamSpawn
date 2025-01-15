package net.programmer.igoodie.streamspawn.init;

import net.programmer.igoodie.streamspawn.integration.base.Integration;
import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.base.ServiceObject;
import net.programmer.igoodie.streamspawn.javascript.global.EmitFn;
import net.programmer.igoodie.streamspawn.javascript.global.RegisterServiceFn;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.SpawnJS;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import java.util.HashMap;
import java.util.Map;

public class ModIntegrations {

    private static final Map<String, Integration> INTEGRATION_REGISTRY = new HashMap<>();

    public static void initialize() {
        ScriptableObject globalScope = SpawnJS.createGlobal();

        // TODO: Migrate those:
        globalScope.defineProperty("registerService", new RegisterServiceFn(INTEGRATION_REGISTRY), ScriptableObject.CONST);
        globalScope.defineProperty("emit", new EmitFn(), ScriptableObject.CONST);
        JavascriptEngine.eval(globalScope, "stopIntegration = console.log");


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
            integration.loadScript("""
                    const tcpClient = new Network.TcpClient("google.com", 80);
                    
                    tcpClient.underlyingSocket.setSoTimeout(1000);
                    
                    tcpClient.buffer = Buffer.alloc(1024);
                    
                    tcpClient.on("lookup", (err, addressType, resolvedAddress, hostname) => {
                      console.log(err, addressType, resolvedAddress, hostname);
                    });
                    
                    tcpClient.on("connect", () => {
                      console.log("Connected!");
                    });
                    
                    registerService(tcpClient);
                    
                    """);
            INTEGRATION_REGISTRY.put(integration.getName(), integration);
            ScriptableObject integrationScope = integration.createScope(globalScope);
            integration.getScript().exec(JavascriptEngine.CONTEXT.get(), integrationScope);

            integration.services.forEach(ServiceObject::begin);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        initialize();
        Context.exit();
    }

}
