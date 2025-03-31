package net.programmer.igoodie.streamspawn.javascript.streamspawn.modules;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.commonjs.IntrinsicModule;
import org.mozilla.javascript.Scriptable;

public class IntegrationsModule extends IntrinsicModule {

    @Override
    public String moduleId() {
        return "streamspawn:integrations";
    }

    @Override
    public void buildExports(Scriptable exports) {
        JavascriptEngine.eval(exports, "const defineIntegration = x => x");
    }

}
