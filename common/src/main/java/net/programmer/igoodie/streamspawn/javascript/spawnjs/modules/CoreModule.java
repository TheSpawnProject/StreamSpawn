package net.programmer.igoodie.streamspawn.javascript.spawnjs.modules;

import net.programmer.igoodie.streamspawn.javascript.commonjs.IntrinsicModule;
import org.mozilla.javascript.Scriptable;

public class CoreModule extends IntrinsicModule {

    @Override
    public String moduleId() {
        return "spawnjs:core";
    }

    @Override
    public void buildExports(Scriptable exports) {
        // TODO: export version variable
//        ClassDefiner.defineClass(exports, ScriptService.class, true, false);
    }

}
