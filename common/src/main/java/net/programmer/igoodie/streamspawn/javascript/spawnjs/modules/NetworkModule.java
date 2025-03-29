package net.programmer.igoodie.streamspawn.javascript.spawnjs.modules;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.classes.ClassDefiner;
import net.programmer.igoodie.streamspawn.javascript.commonjs.IntrinsicModule;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network.BufferScriptHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network.SocketIOScriptHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.network.TcpConnectionScriptHost;
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
        ClassDefiner.defineClass(exports, SocketIOScriptHost.class, true, true);
    }

}

