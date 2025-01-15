package net.programmer.igoodie.streamspawn.javascript.commonjs;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.base.RuntimeAPI;
import org.mozilla.javascript.*;
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

        if (cjs.apiLookup.containsKey(id)) {
            return cjs.loadedApis.computeIfAbsent(id, moduleId -> {
                RuntimeAPI runtimeAPI = cjs.apiLookup.get(moduleId);
                ScriptableObject moduleScope = JavascriptEngine.createScope();
                runtimeAPI.install(moduleScope);
                return moduleScope;
            });
        }

        return super.call(cx, scope, thisObj, args);
    }

}
