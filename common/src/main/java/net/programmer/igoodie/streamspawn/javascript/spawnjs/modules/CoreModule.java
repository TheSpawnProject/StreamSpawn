package net.programmer.igoodie.streamspawn.javascript.spawnjs.modules;

import net.programmer.igoodie.streamspawn.javascript.classes.ClassDefiner;
import net.programmer.igoodie.streamspawn.javascript.commonjs.IntrinsicModule;
import net.programmer.igoodie.streamspawn.javascript.service.ScriptService;
import org.mozilla.javascript.ScriptableObject;

public class CoreModule extends IntrinsicModule {

    @Override
    public String moduleId() {
        return "spawnjs:core";
    }

    @Override
    public void buildExports(ScriptableObject exports) {
        ClassDefiner.defineClass(exports, ScriptService.class, true, false);
    }

}
