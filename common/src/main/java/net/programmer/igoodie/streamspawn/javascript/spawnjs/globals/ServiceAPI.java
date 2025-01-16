package net.programmer.igoodie.streamspawn.javascript.spawnjs.globals;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.base.GlobalAPI;
import net.programmer.igoodie.streamspawn.javascript.base.IntrinsicService;
import org.mozilla.javascript.*;

public class ServiceAPI implements GlobalAPI {

    public static final SymbolKey BEGIN_SERVICE = new SymbolKey("ServiceSymbols.beginService");
    public static final SymbolKey TERMINATE_SERVICE = new SymbolKey("ServiceSymbols.terminateService");

    @Override
    public void install(ScriptableObject scope) {
        ScriptableObject serviceSymbols = JavascriptEngine.createObject();

        scope.defineProperty("ServiceSymbols", serviceSymbols, ScriptableObject.CONST | ScriptableObject.READONLY | ScriptableObject.PERMANENT);
        serviceSymbols.defineProperty("beginService", constructSymbol(scope, BEGIN_SERVICE), ScriptableObject.CONST | ScriptableObject.READONLY | ScriptableObject.PERMANENT);
        serviceSymbols.defineProperty("terminateService", constructSymbol(scope, TERMINATE_SERVICE), ScriptableObject.CONST | ScriptableObject.READONLY | ScriptableObject.PERMANENT);
    }

    protected static NativeSymbol constructSymbol(ScriptableObject scope, SymbolKey key) {
        Context cx = JavascriptEngine.CONTEXT.get();
        LambdaConstructor ctor = (LambdaConstructor) scope.get(NativeSymbol.CLASS_NAME);
        return (NativeSymbol) ctor.call(cx, scope, null, new Object[]{Undefined.instance, key});
    }

    public static boolean isService(ScriptableObject obj) {
        if (obj instanceof IntrinsicService) return true;
        Object beginService = obj.get(BEGIN_SERVICE, obj);
        return beginService instanceof Function;
    }

}
