package net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network;

import net.programmer.igoodie.streamspawn.javascript.coercer.CoercibleFunction;
import net.programmer.igoodie.streamspawn.javascript.service.ScriptService;
import net.programmer.igoodie.streamspawn.javascript.util.Listeners;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class WebsocketHost extends ScriptService {

    protected URI url;
    protected Socket socket;
    protected Options options;

    // TODO: Integrate, once Wutax releases Interlude
    protected Listeners<Socket.Event, Listeners.GenericListener> listeners = new Listeners<>();

    @Override
    public String getClassName() {
        return "Websocket";
    }

    public WebsocketHost() {
        this.options = new Options();
    }

    @JSConstructor
    public WebsocketHost(String url) {
        this();
        this.url = URI.create(url);
    }

    @JSGetter
    public Socket getSocket() {
        return this.socket;
    }

    @JSGetter
    public Options getOptions() {
        return this.options;
    }

    @JSFunction
    public void on(String eventName, Function callback) {
        Socket.Event event = Socket.Event.valueOf(eventName.toUpperCase());
        CoercibleFunction listener = CoercibleFunction.makeCoercible(this.getParentScope(), callback);
        this.listeners.subscribe(event, listener::call);
    }

    @Override
    public void beginImpl() {
        this.socket = new Socket(this.url, this.options);

        if (this.options.reuseAddr)
            this.socket.setReuseAddr(true);
        if (this.options.tcpNoDelay)
            this.socket.setTcpNoDelay(true);

        this.socket.connect();
    }

    @Override
    public void terminateImpl() {
        if (this.socket.getReadyState() == ReadyState.OPEN) {
            this.socket.close();
        }
    }

    public class Socket extends WebSocketClient {

        public Socket(URI serverUri, WebsocketHost.Options options) {
            super(serverUri, options.handshakeHeaders);
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            WebsocketHost.this.listeners.invoke(Event.OPEN, l -> l.call(
                    serverHandshake
            ));
        }

        @Override
        public void onMessage(String message) {
            WebsocketHost.this.listeners.invoke(Event.MESSAGE, l -> l.call(
                    message
            ));
        }

        @Override
        public void onMessage(ByteBuffer bytes) {
            WebsocketHost.this.listeners.invoke(Event.MESSAGE_BUFFER, l -> l.call(
                    bytes
            ));
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            WebsocketHost.this.listeners.invoke(Event.CLOSE, l -> l.call(
                    code, reason, remote
            ));
        }

        @Override
        public void onError(Exception e) {
            WebsocketHost.this.listeners.invoke(Event.ERROR, l -> l.call(
                    e.getClass().getSimpleName(), e.getMessage()
            ));
        }

        public enum Event {
            OPEN,
            MESSAGE,
            MESSAGE_BUFFER,
            CLOSE,
            ERROR
        }

    }

    public static class Options {

        public boolean reuseAddr = false;
        public boolean tcpNoDelay = false;
        public Map<String, String> handshakeHeaders = new HashMap<>();

    }

}
