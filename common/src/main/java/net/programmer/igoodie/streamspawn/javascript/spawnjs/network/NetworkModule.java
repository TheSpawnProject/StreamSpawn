package net.programmer.igoodie.streamspawn.javascript.spawnjs.network;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.commonjs.IntrinsicModule;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.network.hosts.BufferHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.network.hosts.SocketIOHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.network.hosts.TcpConnectionHost;
import org.mozilla.javascript.ScriptableObject;

public class NetworkModule extends IntrinsicModule {

    @Override
    public String moduleName() {
        return "spawnjs:network";
    }

    @Override
    public void buildExports(ScriptableObject exports) {
        JavascriptEngine.defineClass(exports, BufferHost.class);
        JavascriptEngine.defineClass(exports, TcpConnectionHost.class);
        JavascriptEngine.defineClass(exports, SocketIOHost.class);
    }

}

