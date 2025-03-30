package net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network;

import com.neovisionaries.ws.client.*;
import net.programmer.igoodie.streamspawn.javascript.coercer.CoercibleFunction;
import net.programmer.igoodie.streamspawn.javascript.coercer.JavaUtilCoercer;
import net.programmer.igoodie.streamspawn.javascript.service.ScriptService;
import net.programmer.igoodie.streamspawn.javascript.util.Listeners;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebsocketHost extends ScriptService {

    protected URI url;
    protected WebSocket socket;
    protected Options options;

    // TODO: Integrate, once Wutax releases Interlude
    protected Listeners<Event, Listeners.GenericListener> listeners = new Listeners<>();

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
    public Object getSocket() {
        return Context.javaToJS(this.socket, this.getParentScope());
    }

    @JSGetter
    public Object getOptions() {
        return Context.javaToJS(this.options, this.getParentScope());
    }

    @JSFunction
    public void on(String eventName, Function callback) {
        Event event = Event.valueOf(eventName.toUpperCase());
        CoercibleFunction listener = CoercibleFunction.makeCoercible(this.getParentScope(), callback);
        this.listeners.subscribe(event, listener::call);
    }

    @Override
    public void beginImpl() {
        WebSocketFactory factory = new WebSocketFactory();

        factory.setConnectionTimeout(this.options.connectionTimeout);
        factory.setSocketTimeout(this.options.socketTimeout);

        try {
            this.socket = factory.createSocket(this.url);

            this.options.handshakeHeaders.forEach(this.socket::addHeader);

            this.socket.addListener(new Listener());

            this.socket.connectAsynchronously();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void terminateImpl() {
        if (this.socket.getState() == WebSocketState.OPEN) {
            this.socket.disconnect();
        }
    }

    public class Listener extends WebSocketAdapter {

        @Override
        public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
            WebsocketHost.this.listeners.invoke(Event.CONNECTED, l -> l.call(
                    JavaUtilCoercer.Map.INSTANCE.coerceValue(headers, WebsocketHost.this.getParentScope())
            ));
        }

        @Override
        public void onTextMessage(WebSocket websocket, String text) {
            WebsocketHost.this.listeners.invoke(Event.MESSAGE, l -> l.call(
                    text
            ));
        }

        @Override
        public void onTextMessage(WebSocket websocket, byte[] data) {
            WebsocketHost.this.listeners.invoke(Event.MESSAGE_BUFFER, l -> l.call(
                    new BufferHost(data)
            ));
        }

        @Override
        public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
            int closeCode = serverCloseFrame.getCloseCode();
            String closeReason = serverCloseFrame.getCloseReason();
            WebsocketHost.this.listeners.invoke(Event.DISCONNECTED, l -> l.call(
                    closeCode, closeReason, closedByServer
            ));
        }

        @Override
        public void onError(WebSocket websocket, WebSocketException cause) {
            WebsocketHost.this.listeners.invoke(Event.ERROR, l -> l.call(
                    cause.getClass().getSimpleName(), cause.getMessage()
            ));
        }

    }

    public static class Options {

        public int connectionTimeout = 0;
        public int socketTimeout = 0;
        public Map<String, String> handshakeHeaders = new HashMap<>();

    }

    public enum Event {
        CONNECTED,
        MESSAGE,
        MESSAGE_BUFFER,
        DISCONNECTED,
        ERROR
    }

}
