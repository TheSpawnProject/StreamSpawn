package net.programmer.igoodie.streamspawn.javascript.spawnjs.network;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.classes.ClassDefiner;
import net.programmer.igoodie.streamspawn.javascript.commonjs.IntrinsicModule;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.network.hosts.BufferScriptHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.network.hosts.SocketIOScriptHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.network.hosts.TcpConnectionScriptHost;
import org.mozilla.javascript.ScriptableObject;

public class NetworkModule extends IntrinsicModule {

    @Override
    public String moduleName() {
        return "spawnjs:network";
    }

    @Override
    public void buildExports(ScriptableObject exports) {
        ClassDefiner.defineClass(exports, BufferScriptHost.class, true, false);
        JavascriptEngine.defineClass(exports, TcpConnectionScriptHost.class);
        JavascriptEngine.defineClass(exports, SocketIOScriptHost.class);
    }

}

