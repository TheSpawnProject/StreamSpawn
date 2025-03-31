package net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.goodies.util.accessor.ListAccessor;
import net.programmer.igoodie.streamspawn.javascript.base.ScriptHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.SpawnJSExceptions;
import net.programmer.igoodie.streamspawn.javascript.util.NativePromiseCallbackFn;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;

import java.net.URI;
import java.util.List;

public class SocketIOHost extends ScriptHost {

    protected URI url;
    protected Socket socket;
    protected IO.Options options;

    public SocketIOHost() {}

    public SocketIOHost(String url, IO.Options options) {
        this.url = URI.create(url);
        this.options = options;
        this.socket = IO.socket(this.url, this.options);
    }

    @JSConstructor
    public static SocketIOHost constructor(Context cx, Object[] args, Function ctor, boolean inNewExpr) {
        ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElseThrow();
        Object arg1 = argsAccessor.get(1)
                .map(v -> Context.jsToJava(v, IO.Options.class))
                .orElseGet(IO.Options::new);

        Scriptable scope = ctor.getParentScope();

        if (arg0 instanceof String url) {
            if (arg1 instanceof IO.Options options) {
                return bindToScope(new SocketIOHost(url, options), scope);
            }
        }

        throw SpawnJSExceptions.invalidArguments(ctor, args, ctor);
    }

    @Override
    public String getClassName() {
        return "SocketIO";
    }

    @JSFunction
    public void connect() {
        this.socket.connect();
    }

    @JSFunction
    public void disconnect() {
        this.socket.disconnect();
    }

    @JSFunction("io")
    public Manager getManager() {
        return this.socket.io();
    }

    @JSGetter
    public IO.Options getOptions() {
        return this.options;
    }

    @JSFunction
    public static void emit(Context cx, Scriptable thisObj, Object[] args, Function function) {
        ListAccessor<Object> argsAccessor = ListAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElse(null);
        List<Object> eventArgs = argsAccessor.subList(1, argsAccessor.size());

        SocketIOHost hostObj = (SocketIOHost) thisObj;

        if (arg0 instanceof String eventName) {
            hostObj.socket.emit(eventName, eventArgs);
            return;
        }

        throw SpawnJSExceptions.invalidArguments(thisObj, args, function);
    }

    @JSFunction
    public static Scriptable emitWithAck(Context cx, Scriptable thisObj, Object[] args, Function function) {
        ListAccessor<Object> argsAccessor = ListAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElse(null);
        List<Object> eventArgs = argsAccessor.subList(1, argsAccessor.size());

        SocketIOHost hostObj = (SocketIOHost) thisObj;

        if (arg0 instanceof String eventName) {
            return cx.newObject(thisObj.getParentScope(), "Promise", new Object[]{
                    new NativePromiseCallbackFn<>((resolve, reject) -> {
                        hostObj.socket.emit(eventName, eventArgs.toArray(), resolve::resolve);
                    })
            });
        }

        throw SpawnJSExceptions.invalidArguments(thisObj, args, function);
    }

    @JSFunction
    public static void on(Context cx, Scriptable thisObj, Object[] args, Function function) {
        ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElse(null);
        Object arg1 = argsAccessor.get(1).orElse(null);

        SocketIOHost hostObj = (SocketIOHost) thisObj;

        if (arg0 instanceof String eventName) {
            if (arg1 instanceof Function listener) {
                hostObj.socket.on(eventName, eventArgs ->
                        listener.call(cx, function.getParentScope(), thisObj, eventArgs));

                return;
            }
        }

        throw SpawnJSExceptions.invalidArguments(thisObj, args, function);
    }

    @JSFunction
    public static void once(Context cx, Scriptable thisObj, Object[] args, Function function) {
        ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElse(null);
        Object arg1 = argsAccessor.get(1).orElse(null);

        SocketIOHost hostObj = (SocketIOHost) thisObj;

        if (arg0 instanceof String eventName) {
            if (arg1 instanceof Function listener) {
                hostObj.socket.once(eventName, eventArgs ->
                        listener.call(cx, function.getParentScope(), thisObj, eventArgs));
                return;
            }
        }

        throw SpawnJSExceptions.invalidArguments(thisObj, args, function);
    }

}
