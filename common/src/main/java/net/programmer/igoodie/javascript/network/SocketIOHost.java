package net.programmer.igoodie.javascript.network;

import io.socket.client.Socket;
import net.programmer.igoodie.javascript.base.HostObject;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.annotations.JSConstructor;
import org.mozilla.javascript.annotations.JSGetter;
import org.mozilla.javascript.annotations.JSSetter;

import java.net.URI;

public class SocketIOHost extends HostObject implements SocketHost {

    protected URI url;
    protected Socket socket;
    public Function prepareOptions;

    @Override
    public String getClassName() {
        return "SocketIO";
    }

    public SocketIOHost() {}

    @JSConstructor
    public SocketIOHost(String url) {
        this.url = URI.create(url);
    }

    @JSGetter
    public Function getPrepareOptions() {
        return this.prepareOptions;
    }

    @JSSetter
    public void setPrepareOptions(Function func) {
        this.prepareOptions = func;
    }

}
