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
                console.log(Buffer.alloc(11, 'aGVsbG8gd29ybGQ=', 'base64'));
                console.log(Buffer.alloc(9));
//                console.log(Buffer.alloc(5, 1700));
                console.log(Buffer.alloc(5, "a"));
                console.log(Buffer.alloc(5, 0xff));
//                console.log(Buffer.alloc(5, "a", "utf-8"));
                
//                console.log(Buffer.from([257, 257.5, -255, '1']));
                
//                const arr = new Uint16Array(2);
//                arr[0] = 5000;
//                arr[1] = 4000;
//                console.log(Buffer.from(arr));
//                arr[1] = 6000;
//                console.log(Buffer.from(arr));
//                
//                console.log(Buffer.alloc(8).fill(0x15));
//                    const tcpClient = new Network.TcpClient("127.0.0.1", 8080);
//                    
//                    tcpClient.bufferSize = 10000;
//                    
//                    tcpClient.onConnect = () => {
//                      console.log("Connected");
//                    };
//                    
//                    tcpClient.onError = (reason) => {
//                        console.log("Error:", reason);
//                    }
//                    
//                    tcpClient.onChunk = (buffer, readLength) => {
//                      console.log("Received =", readLength, buffer)
//                    }
//                    
//                    registerService(tcpClient);
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
