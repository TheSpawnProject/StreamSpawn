package net.programmer.igoodie.integration;

import net.programmer.igoodie.integration.base.Integration;
import net.programmer.igoodie.javascript.JavascriptEngine;
import net.programmer.igoodie.javascript.global.EmitFn;
import net.programmer.igoodie.javascript.global.PrintFn;
import net.programmer.igoodie.javascript.global.TimerFn;
import net.programmer.igoodie.javascript.network.RegisterSocketFn;
import net.programmer.igoodie.javascript.network.SocketHost;
import net.programmer.igoodie.javascript.network.SocketIOHost;
import org.mozilla.javascript.ScriptableObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModIntegrations {

    private static List<SocketHost> SOCKET_REGISTRY = new ArrayList<>();

    public static void initialize() {
        ScriptableObject globalScope = JavascriptEngine.createScope();

        ScriptableObject libNetwork = JavascriptEngine.createScope();
        JavascriptEngine.defineClass(libNetwork, SocketIOHost.class);

        globalScope.defineProperty("print", new PrintFn(), ScriptableObject.CONST);
        globalScope.defineProperty("setTimeout", new TimerFn(), ScriptableObject.CONST);
        globalScope.defineProperty("setInterval", new TimerFn(true), ScriptableObject.CONST);
        globalScope.defineProperty("Network", libNetwork, ScriptableObject.CONST);
        globalScope.defineProperty("registerSocket", new RegisterSocketFn(SOCKET_REGISTRY), ScriptableObject.CONST);
        globalScope.defineProperty("emit", new EmitFn(), ScriptableObject.CONST);
        globalScope.defineProperty("stopIntegration", new PrintFn(), ScriptableObject.CONST);
        globalScope.sealObject();

        try {
            Integration integration = new Integration();
            integration.downloadScript("https://pastebin.pl/view/raw/f186f919");
            ScriptableObject integrationScope = integration.createScope(globalScope);
            integration.getScript().exec(JavascriptEngine.CONTEXT.get(), integrationScope);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SOCKET_REGISTRY.forEach(SocketHost::start);
//        SOCKET_REGISTRY.forEach(SocketHost::stop);
    }

}
