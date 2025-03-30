package net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import net.programmer.igoodie.streamspawn.javascript.coercer.CoercibleFunction;
import net.programmer.igoodie.streamspawn.javascript.service.ScriptService;
import net.programmer.igoodie.streamspawn.javascript.util.Listeners;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;

import java.net.URI;

// TODO: Support SIOv1, SIOv2, SIOv3 and SIOv4
public class SocketIOHost extends ScriptService {

    protected URI url;
    protected Socket socket;
    protected IO.Options options;

    // TODO: Refactor, once Wutax releases Interlude
    protected Listeners<String, Emitter.Listener> listeners = new Listeners<>();

    @Override
    public String getClassName() {
        return "SocketIO";
    }

    public SocketIOHost() {
        this.options = new IO.Options();
        options.forceNew = true;
        options.reconnection = true;
        options.transports = new String[]{"websocket"};
    }

    @JSConstructor
    public SocketIOHost(String url) {
        this();
        this.url = URI.create(url);
    }

    @JSGetter
    public Socket getSocket() {
        return this.socket;
    }

    @JSGetter
    public IO.Options getOptions() {
        return this.options;
    }

    @JSFunction
    public void on(String eventName, Function callback) {
        CoercibleFunction listener = CoercibleFunction.makeCoercible(this.getParentScope(), callback);
        this.listeners.subscribe(eventName, listener::call);
    }

    @JSFunction
    public void off(String eventName) {
        // TODO: Also impl off(), off(eventName, listener)
        this.listeners.unsubscribeAll(eventName);
        this.socket.off(eventName);
    }

    @Override
    public void beginImpl() {
        this.socket = IO.socket(url, options);
        listeners.forEach((eventName, listener) -> this.socket.on(eventName, listener));
        this.socket.connect();
    }

    @Override
    public void terminateImpl() {
        this.socket.disconnect();
        this.socket.close();
        this.socket = null;
    }

}
