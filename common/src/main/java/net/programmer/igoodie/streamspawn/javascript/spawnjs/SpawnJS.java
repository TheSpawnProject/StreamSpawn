package net.programmer.igoodie.streamspawn.javascript.spawnjs;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.base.RuntimeAPI;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.core.ConsoleAPI;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.core.TimerAPI;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.network.NetworkAPI;
import org.mozilla.javascript.ScriptableObject;

public class SpawnJS extends RuntimeAPI {

    @Override
    public void init(ScriptableObject scope) {
        scope.defineProperty("__version", "0.0.1", ScriptableObject.CONST);
        new ConsoleAPI().init(scope);
        new TimerAPI().init(scope);
        new NetworkAPI().init(scope);
    }

    public static ScriptableObject createGlobals() {
        ScriptableObject globalScope = JavascriptEngine.createScope();
        new SpawnJS().init(globalScope);
        return globalScope;
    }

}
