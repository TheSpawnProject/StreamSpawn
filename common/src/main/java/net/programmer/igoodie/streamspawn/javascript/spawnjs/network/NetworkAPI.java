package net.programmer.igoodie.streamspawn.javascript.spawnjs.network;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.base.RuntimeAPI;
import org.mozilla.javascript.ScriptableObject;

public class NetworkAPI extends RuntimeAPI {

    @Override
    public void init(ScriptableObject scope) {
        ScriptableObject nsNetwork = JavascriptEngine.createScope();
        JavascriptEngine.defineClass(nsNetwork, SocketIOHost.class);
        scope.defineProperty("Network", nsNetwork, ScriptableObject.CONST);
    }

}
