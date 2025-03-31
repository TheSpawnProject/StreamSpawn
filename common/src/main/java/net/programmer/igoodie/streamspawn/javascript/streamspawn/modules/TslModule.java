package net.programmer.igoodie.streamspawn.javascript.streamspawn.modules;

import net.programmer.igoodie.streamspawn.javascript.commonjs.IntrinsicModule;
import net.programmer.igoodie.streamspawn.javascript.streamspawn.modules.tsl.CreateEmitterFn;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class TslModule extends IntrinsicModule {

    @Override
    public String moduleId() {
        return "streamspawn:tsl";
    }

    @Override
    public void buildExports(Scriptable exports) {
        ScriptableObject.defineProperty(exports, "createEmitter", new CreateEmitterFn(), ScriptableObject.CONST);
    }

}
