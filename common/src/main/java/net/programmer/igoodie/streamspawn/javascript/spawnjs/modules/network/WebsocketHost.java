package net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network;

import com.neovisionaries.ws.client.*;
import net.programmer.igoodie.goodies.util.accessor.ArrayAccessor;
import net.programmer.igoodie.streamspawn.javascript.base.ScriptHost;
import net.programmer.igoodie.streamspawn.javascript.coercer.JavaUtilCoercer;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.SpawnJSExceptions;
import net.programmer.igoodie.streamspawn.javascript.util.Listeners;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebsocketHost extends ScriptHost {

    protected URI url;
    protected WebSocket socket;
    protected Options options;
    protected Hook hook;

    public WebsocketHost() {}

    public WebsocketHost(String url, Options options) {
        this.url = URI.create(url);
        this.options = options;

        WebSocketFactory factory = new WebSocketFactory();

        factory.setConnectionTimeout(this.options.connectionTimeout);
        factory.setSocketTimeout(this.options.socketTimeout);

        try {
            this.socket = factory.createSocket(this.url);

            this.options.handshakeHeaders.forEach(this.socket::addHeader);

            this.hook = new Hook();
            this.socket.addListener(this.hook);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @JSConstructor
    public static WebsocketHost constructor(Context cx, Object[] args, Function ctor, boolean inNewExpr) {
        ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElseThrow();
        Object arg1 = argsAccessor.get(1)
                .map(v -> Context.jsToJava(v, Options.class))
                .orElseGet(Options::new);

        Scriptable scope = ctor.getParentScope();

        if (arg0 instanceof String url) {
            if (arg1 instanceof Options options) {
                return bindToScope(new WebsocketHost(url, options), scope);
            }
        }

        throw SpawnJSExceptions.invalidArguments(ctor, args, ctor);
    }

    @Override
    public String getClassName() {
        return "Websocket";
    }

    @JSGetter
    public WebSocketState getState() {
        return socket.getState();
    }

    @JSFunction
    public void connect() {
        this.socket.connectAsynchronously();
    }

    @JSFunction
    public void disconnect() {
        this.socket.disconnect();
    }

    @JSFunction
    public static void send(Context cx, Scriptable thisObj, Object[] args, Function function) {
        ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElse(null);

        WebsocketHost hostObj = (WebsocketHost) thisObj;

        if (arg0 instanceof BufferHost buffer) {
            hostObj.socket.sendBinary(buffer.buffer);
            return;
        }

        if (arg0 instanceof String text) {
            hostObj.socket.sendText(text);
            return;
        }

        throw SpawnJSExceptions.invalidArguments(thisObj, args, function);
    }

    @JSFunction
    public void sendPing() {
    this.socket.sendPing();
    }

    @JSFunction
    public static void on(Context cx, Scriptable thisObj, Object[] args, Function function) {
        ArrayAccessor<Object> argsAccessor = ArrayAccessor.of(args);
        Object arg0 = argsAccessor.get(0).orElse(null);
        Object arg1 = argsAccessor.get(1).orElse(null);

        WebsocketHost hostObj = (WebsocketHost) thisObj;

        if (arg0 instanceof String eventName) {
            if (arg1 instanceof Function listener) {
                Hook.Event event = Hook.Event.valueOf(eventName.toUpperCase());

                hostObj.hook.listeners.subscribe(event, eventArgs ->
                        listener.call(cx, function.getParentScope(), thisObj, eventArgs));

                return;
            }
        }

        throw SpawnJSExceptions.invalidArguments(thisObj, args, function);
    }

    public class Hook extends WebSocketAdapter {

        public enum Event {
            CONNECTED,
            MESSAGE,
            MESSAGE_BUFFER,
            PONG,
            DISCONNECTED,
            ERROR
        }

        // TODO: Integrate, once Wutax releases Interlude
        protected Listeners<Event, Listeners.GenericListener> listeners = new Listeners<>();

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
            this.listeners.invoke(Event.CONNECTED, l -> l.call(
                    JavaUtilCoercer.Map.INSTANCE.coerceValue(headers, WebsocketHost.this.getParentScope())
            ));
        }

        @Override
        public void onTextMessage(WebSocket websocket, String text) {
            this.listeners.invoke(Event.MESSAGE, l -> l.call(text));
        }

        @Override
        public void onTextMessage(WebSocket websocket, byte[] data) {
            BufferHost buffer = new BufferHost(data);
            this.listeners.invoke(Event.MESSAGE_BUFFER, l -> l.call(buffer));
        }

        @Override
        public void onPongFrame(WebSocket websocket, WebSocketFrame frame) {
            BufferHost payloadBuffer = new BufferHost(frame.getPayload());
            this.listeners.invoke(Event.PONG, l -> l.call(payloadBuffer));
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
            int closeCode = serverCloseFrame.getCloseCode();
            String closeReason = serverCloseFrame.getCloseReason();
            this.listeners.invoke(Event.DISCONNECTED, l -> l.call(
                    closeCode, closeReason, closedByServer
            ));
        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) {
            this.listeners.invoke(Event.ERROR, l -> l.call(
                    cause.getClass().getSimpleName(), cause.getMessage()
            ));
        }

    }

    public static class Options {

        public int connectionTimeout = 0;
        public int socketTimeout = 0;
        public Map<String, String> handshakeHeaders = new HashMap<>();

    }

}
