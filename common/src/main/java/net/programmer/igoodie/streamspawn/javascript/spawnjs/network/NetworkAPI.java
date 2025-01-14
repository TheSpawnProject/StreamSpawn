package net.programmer.igoodie.streamspawn.javascript.spawnjs.network;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.base.RuntimeAPI;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.network.hosts.BufferHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.network.hosts.SocketIOHost;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.network.hosts.TcpClientHost;
import org.mozilla.javascript.ScriptableObject;

public class NetworkAPI extends RuntimeAPI {

    @Override
    public void init(ScriptableObject scope) {
        JavascriptEngine.defineClass(scope, BufferHost.class);

        ScriptableObject networkNs = JavascriptEngine.createScope(scope);
        scope.defineProperty("Network", networkNs, ScriptableObject.CONST);

        JavascriptEngine.defineClass(networkNs, SocketIOHost.class);
        JavascriptEngine.defineClass(networkNs, TcpClientHost.class);
    }

}
