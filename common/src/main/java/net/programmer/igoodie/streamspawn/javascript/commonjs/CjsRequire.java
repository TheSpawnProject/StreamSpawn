package net.programmer.igoodie.streamspawn.javascript.commonjs;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.Require;

public class CjsRequire extends Require {

    protected final CjsAPI cjs;

    public CjsRequire(CjsAPI cjs, Context cx, Scriptable nativeScope, ModuleScriptProvider moduleScriptProvider, Script preExec, Script postExec, boolean sandboxed) {
        super(cx, nativeScope, moduleScriptProvider, preExec, postExec, sandboxed);
        this.cjs = cjs;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        if (args == null || args.length < 1) {
            throw ScriptRuntime.throwError(cx, scope, "require() needs one argument");
        }

        String id = (String) Context.jsToJava(args[0], String.class);

        IntrinsicModule intrinsicModule = cjs.intrinsicModules.get(id);

        if (intrinsicModule != null) {
            if (!intrinsicModule.isLoaded()) {
                intrinsicModule.load();
            }
            return intrinsicModule.moduleScope;
        }

        return super.call(cx, scope, thisObj, args);
    }

}
