package net.programmer.igoodie.streamspawn.javascript.spawnjs;

import net.programmer.igoodie.streamspawn.javascript.base.ScopeInstallable;
import net.programmer.igoodie.streamspawn.javascript.commonjs.CommonJS;
import net.programmer.igoodie.streamspawn.javascript.commonjs.ModuleInstallable;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.globals.ConsoleAPI;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.globals.TimerAPI;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.globals.TslAPI;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.CoreModule;
import net.programmer.igoodie.streamspawn.javascript.spawnjs.modules.NetworkModule;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class SpawnJS implements ScopeInstallable, ModuleInstallable {

    @Override
    public void install(Scriptable scope) {
        ScriptableObject.defineProperty(scope, "__version", "0.0.1", ScriptableObject.CONST);

        new TslAPI().install(scope);
        new ConsoleAPI().install(scope);
        new TimerAPI().install(scope);
    }

    @Override
    public void installModules(CommonJS commonjs) {
        commonjs.registerIntrinsicModule(new CoreModule());
        commonjs.registerIntrinsicModule(new NetworkModule());
    }

}
