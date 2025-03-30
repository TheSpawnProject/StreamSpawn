package net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network;

import net.programmer.igoodie.streamspawn.javascript.service.ScriptService;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSFunction;
import org.mozilla.javascript.annotations.JSGetter;

import java.net.URI;

public class WebsocketHost extends ScriptService {

    protected URI url;
    protected Socket socket;
    protected Options options;

    // TODO: Integrate, once Wutax releases Interlude

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
        // TODO
    }

    @Override
    public void beginImpl() {
        this.socket = new Socket(this.url);

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

    public static class Socket extends WebSocketClient {

        public Socket(URI serverUri) {
            super(serverUri);
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake) {

        }

        @Override
        public void onMessage(String s) {

        }

        @Override
        public void onClose(int i, String s, boolean b) {

        }

        @Override
        public void onError(Exception e) {

        }

    }

    public static class Options {

        public boolean reuseAddr = false;
        public boolean tcpNoDelay = false;

    }

}
