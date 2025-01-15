package net.programmer.igoodie.streamspawn.javascript.spawnjs;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.base.RuntimeAPI;
import net.programmer.igoodie.streamspawn.javascript.commonjs.CjsAPI;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.globals.ConsoleAPI;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.globals.TimerAPI;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.network.NetworkAPI;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.commonjs.module.ModuleScope;

import java.io.File;
import java.util.Objects;

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
        try {
            // TODO: Standardize this, perhaps to Config folder? OR make it a config
            File modulesDir = new File(Objects.requireNonNull(CjsAPI.class.getClassLoader()
                    .getResource("assets/streamspawn/spawnjs")).toURI()).getCanonicalFile();
            return new ModuleScope(globalScope, modulesDir.toURI(), modulesDir.toURI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
