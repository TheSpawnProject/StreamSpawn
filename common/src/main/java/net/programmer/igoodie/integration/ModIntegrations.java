package net.programmer.igoodie.integration;

import net.programmer.igoodie.javascript.JavascriptEngine;
import net.programmer.igoodie.javascript.base.EmitFn;
import net.programmer.igoodie.javascript.base.PrintFn;
import net.programmer.igoodie.javascript.network.RegisterSocketFn;
import net.programmer.igoodie.javascript.network.SocketHost;
import net.programmer.igoodie.javascript.network.SocketIOHost;
import org.mozilla.javascript.ScriptableObject;

import java.util.ArrayList;
import java.util.List;

public class ModIntegrations {

    private static List<SocketHost> SOCKET_REGISTRY = new ArrayList<>();

    public static void initialize() {
        ScriptableObject globalScope = JavascriptEngine.createScope();

        ScriptableObject libNetwork = JavascriptEngine.createScope();
        JavascriptEngine.defineClass(libNetwork, SocketIOHost.class);

        globalScope.defineProperty("print", new PrintFn(), ScriptableObject.CONST);
        globalScope.defineProperty("Network", libNetwork, ScriptableObject.CONST);
        globalScope.defineProperty("registerSocket", new RegisterSocketFn(SOCKET_REGISTRY), ScriptableObject.CONST);
        globalScope.defineProperty("emit", new EmitFn(), ScriptableObject.CONST);
        globalScope.sealObject();

        ScriptableObject scope = JavascriptEngine.createScope(globalScope);
        JavascriptEngine.eval(scope, "const integrationConfig = { token: 'DUMMY_TOKEN' };");
        JavascriptEngine.eval(scope, "" +
                "const sio = new Network.SocketIO(\"https://sockets.streamlabs.com\");" +
                "                                " +
                "                const x = 5;" +
                "" +
                "                sio.modifyOptions((options) => {" +
                "                  options.query = \"token=\" + integrationConfig.token;" +
                "                });" +
                "" +
                "                sio.on(\"error\", (arg0) => {" +
                "                  print(\"Error!\", arg0);" +
                "                });" +
                "" +
                "                sio.on(\"connect\", () => {" +
                "                  print(\"Connected!\", x);" +
                "                });" +
                "                sio.on(\"event\", (a) => {" +
                "                  print(\"Event!\", a.event_id, a.message[0].platform, a);" +
                "                });" +
                "" +
                "                sio.on(\"disconnect\", () => {" +
                "                  print(\"Disconnected!\");" +
                "                });" +
                "                registerSocket(sio); emit('Foo', { bar: 1, baz: [2,3] })");

        SOCKET_REGISTRY.forEach(SocketHost::start);
//        SOCKET_REGISTRY.forEach(SocketHost::stop);
    }

}
