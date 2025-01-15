package net.programmer.igoodie.streamspawn.javascript.spawnjs;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.base.RuntimeAPI;
import net.programmer.igoodie.streamspawn.javascript.commonjs.CjsAPI;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.core.ConsoleAPI;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.core.TimerAPI;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.network.NetworkAPI;
import org.mozilla.javascript.ScriptableObject;

public class SpawnJS implements RuntimeAPI {

    @Override
    public void install(ScriptableObject scope) {
        CjsAPI cjs = new CjsAPI()
                .withBuildInAPI("spawnjs:network", new NetworkAPI());

        cjs.install(scope);

        scope.defineProperty("__version", "0.0.1", ScriptableObject.CONST);
        new ConsoleAPI().install(scope);
        new TimerAPI().install(scope);
    }

    public static ScriptableObject createGlobal() {
        ScriptableObject globalScope = JavascriptEngine.createScope();
        new SpawnJS().install(globalScope);
        return globalScope;
    }

}
