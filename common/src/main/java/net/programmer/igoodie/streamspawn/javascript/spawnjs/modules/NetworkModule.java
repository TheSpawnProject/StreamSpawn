package net.programmer.igoodie.streamspawn.javascript.spawnjs.modules;

import com.neovisionaries.ws.client.WebSocketState;
import net.programmer.igoodie.streamspawn.javascript.classes.ClassDefiner;
import net.programmer.igoodie.streamspawn.javascript.commonjs.IntrinsicModule;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network.BufferHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network.SocketIOHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network.TcpSocket;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network.WebsocketHost;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Scriptable;

public class NetworkModule extends IntrinsicModule {

    @Override
    public String moduleId() {
        return "spawnjs:network";
    }

    @Override
    public void buildExports(Scriptable exports) {
        ClassDefiner.defineClass(exports, BufferHost.class, true, false);
        ClassDefiner.defineClass(exports, TcpSocket.class, true, true);
        ClassDefiner.defineClass(exports, SocketIOHost.class, true, true);

        BaseFunction websocket = ClassDefiner.defineClass(exports, WebsocketHost.class, false, true).getRight();
        ClassDefiner.defineEnum(websocket, "State", WebSocketState.class);
        websocket.sealObject();
    }

}

