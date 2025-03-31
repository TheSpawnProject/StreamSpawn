package net.programmer.igoodie.streamspawn.javascript.commonjs;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public abstract class IntrinsicModule {

    protected Scriptable scope;

    public abstract String moduleId();

    public abstract void buildExports(Scriptable exports);

    public Scriptable require(Scriptable scope) {
        if (this.scope == null) {
            this.scope = JavascriptEngine.forkScope(ScriptableObject.getTopLevelScope(scope));
            this.buildExports(this.scope);
        }

        return this.scope;
    }

}