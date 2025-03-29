package net.programmer.igoodie.streamspawn.init;

import net.programmer.igoodie.streamspawn.integration.base.Integration;
import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.global.RegisterServiceFn;
import net.programmer.igoodie.streamspawn.javascript.service.ScriptService;
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
                    "use strict";
//                    Object.defineProperty(exports, "__esModule", { value: true });
                    const spawnjs_network_1 = require("spawnjs:network");
                    const sio = new spawnjs_network_1.SocketIO("https://sockets.streamlabs.com");
                    const x = 5;
                    sio.addServiceListener("service-starting", () => {
                        console.log("Lifecycle, service starting.")
                    });
                    sio.on("error", (error) => {
                        console.log("Error!", error);
                        stopIntegration("Stopping because of an error: " + error);
                    });
                    sio.on("connect", () => {
                        sio.socket.emit("ping", [], () => console.log("pong"));
                        console.log("Connected!", x);
                        setTimeout(() => console.log("Connected 5 seconds before this!"), 5000);
                    });
                    sio.on("disconnect", () => {
                        console.log("Disconnected!");
                        stopIntegration("Disconnected");
                    });
                    sio.on("event", (data) => {
                        var _a;
                        const eventType = data.type;
                        const eventFor = data.for;
                        const message = (_a = data.message[0]) !== null && _a !== void 0 ? _a : data.message;
                        console.log(eventType, eventFor, message);
                        emit("Twitch Follow", {
                            foo: "bar",
                            mods: [1, 2, 3, "admin"],
                        });
                    });
                    registerService(sio);
                    """);
            INTEGRATION_REGISTRY.put(integration.getName(), integration);
            ScriptableObject integrationScope = integration.createScope(globalScope);
            Object result = integration.getScript().exec(JavascriptEngine.CONTEXT.get(), integrationScope);

            System.out.println(result.getClass());

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
