package net.programmer.igoodie.streamspawn.javascript.spawnjs.network.hosts;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import net.programmer.igoodie.streamspawn.javascript.base.ServiceObject;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

// TODO: Support SIOv1, SIOv2, SIOv3 and SIOv4
public class SocketIOHost extends ServiceObject {

    protected URI url;
    protected Socket socket;
    protected IO.Options options;
    protected Map<String, Emitter.Listener> listeners;

    @Override
    public String getClassName() {
        return "SocketIO";
    }

    public SocketIOHost() {
        this.options = new IO.Options();
        options.forceNew = true;
        options.reconnection = true;
        options.transports = new String[]{"websocket"};

        this.listeners = new HashMap<>();
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
        this.listeners.put(eventName, args -> bindListener(callback).call(args));
    }

    @JSFunction
    public void off(String eventName) {
        if (this.listeners.containsKey(eventName)) {
            this.listeners.remove(eventName);
            this.socket.off(eventName);
        }
    }

    @Override
    public void begin() {
        this.socket = IO.socket(url, options);
        listeners.forEach((eventName, listener) -> this.socket.on(eventName, listener));
        this.socket.connect();
    }

    @Override
    public void terminate() {
        this.socket.disconnect();
        this.socket.close();
        this.socket = null;
    }

}
