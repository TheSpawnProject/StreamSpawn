package net.programmer.igoodie.streamspawn.javascript.spawnjs.modules;

import net.programmer.igoodie.streamspawn.javascript.classes.ClassDefiner;
import net.programmer.igoodie.streamspawn.javascript.commonjs.IntrinsicModule;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network.BufferHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network.SocketIOHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network.TcpConnectionHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network.WebsocketHost;
import org.java_websocket.enums.ReadyState;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.ScriptableObject;

public class NetworkModule extends IntrinsicModule {

    @Override
    public String moduleName() {
        return "spawnjs:network";
    }

    @Override
    public void buildExports(ScriptableObject exports) {
        ClassDefiner.defineClass(exports, BufferHost.class, true, false);
        ClassDefiner.defineClass(exports, TcpConnectionHost.class, true, true);
        ClassDefiner.defineClass(exports, SocketIOHost.class, true, true);

        BaseFunction websocket = ClassDefiner.defineClass(exports, WebsocketHost.class, true, true);
        ClassDefiner.defineEnum(websocket, ReadyState.class);
    }

}

