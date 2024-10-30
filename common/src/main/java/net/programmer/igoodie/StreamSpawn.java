package net.programmer.igoodie;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import io.socket.client.IO;
import net.minecraft.resources.ResourceLocation;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.javascript.JavascriptEngine;
import net.programmer.igoodie.javascript.network.SocketIOHost;
import net.programmer.igoodie.network.ModNetwork;
import net.programmer.igoodie.network.packet.ServerboundEventPacket;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;

import java.lang.reflect.InvocationTargetException;

public final class StreamSpawn {

    public static final String MOD_ID = "streamspawn";

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void init() {
//        try (Context context = Context.enter()) {
//            context.setOptimizationLevel(-1);
//            context.setLanguageVersion(Context.VERSION_ES6);
//            ScriptableObject globalScope = context.initSafeStandardObjects();
//            globalScope.defineProperty();
//            Object value = context.evaluateString(globalScope, "'Hi from Rhino'", "<root>", 1, null);
//            System.out.println(value);
//        }

        ScriptableObject globalScope = JavascriptEngine.createScope();
        ScriptableObject libNetwork = JavascriptEngine.createScope();
        try {
            ScriptableObject.defineClass(libNetwork, SocketIOHost.class);
            globalScope.defineProperty("Network", libNetwork, ScriptableObject.CONST);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        Object eval = JavascriptEngine.eval(globalScope, "" +
                "const sio = new Network.SocketIO('https://somewhere.com'); " +
                "sio.prepareOptions = (options) => { " +
                "   options.forceNew = false;" +
                "   options.reconnection = false;" +
                "   options.transports = ['websocket'];" +
                "   options.query = 'token=foobarbaz';" +
                "   return options; " +
                "}; " +
                "sio");
        Function onGeneratingOptions = ((SocketIOHost) eval).prepareOptions;
        Object options = onGeneratingOptions.call(
                JavascriptEngine.CONTEXT.get(),
                globalScope,
                null,
                new Object[]{new IO.Options()}
        );
        System.out.println(options);
//        System.out.println(JavascriptEngine.eval(globalScope, "let x = 5"));
//        System.out.println(JavascriptEngine.eval(globalScope, "new Websocket('ws://somewhere.com').count"));
        globalScope.sealObject();

//        ScriptableObject localScope1 = JavascriptEngine.createScope();
//        localScope1.setParentScope(globalScope);
//        System.out.println(JavascriptEngine.eval(localScope1, "let x = 15; x"));
//
//        ScriptableObject localScope2 = JavascriptEngine.createScope();
//        localScope2.setParentScope(globalScope);
//        System.out.println(JavascriptEngine.eval(localScope2, "x"));

//        ModIntegrations.CONTEXT.get().evaluateString(scope1, "const x = 5;", "<root>", 1, null);
//        ModIntegrations.CONTEXT.get().evaluateString(scope2, "const x = 5;", "<root>", 1, null);
//        ModIntegrations.CONTEXT.get().evaluateString(scope2, "const x = 5;", "<root>", 1, null);

        LifecycleEvent.SETUP.register(() -> {
            ModNetwork.initialize();
        });

        LifecycleEvent.SERVER_STARTING.register(server -> {
        });

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> {
            String eventName = "Testing";
            GoodieObject eventArgs = new GoodieObject();
            eventArgs.put("actor", "iGoodie");
            ModNetwork.CHANNEL.sendToServer(new ServerboundEventPacket(eventName, eventArgs));
        });
    }

}
