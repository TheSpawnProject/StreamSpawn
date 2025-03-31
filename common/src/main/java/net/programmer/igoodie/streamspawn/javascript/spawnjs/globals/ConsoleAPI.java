package net.programmer.igoodie.streamspawn.javascript.spawnjs.globals;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.base.ScopeInstallable;
import org.mozilla.javascript.*;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ConsoleAPI implements ScopeInstallable {

    @Override
    public void install(Scriptable scope) {
        Context cx = JavascriptEngine.CONTEXT.get();
        ScriptableObject consoleObj = (ScriptableObject) cx.newObject(scope);
        ScriptableObject.defineProperty(scope, "console", consoleObj, ScriptableObject.CONST);
        consoleObj.defineProperty("log", new Log(), ScriptableObject.CONST);
    }

    public static class Log extends BaseFunction {

        @Override
        public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            String msg = Arrays.stream(args)
                    .map(ConsoleAPI::stringify)
                    .collect(Collectors.joining(" "));

            System.out.println(msg);

            return Undefined.instance;
        }

    }

    public static String stringify(Object nativeValue) {
        if (nativeValue instanceof NativeObject nativeObject) {
            StringBuilder sb = new StringBuilder("{ ");

            Object[] ids = nativeObject.getIds();
            for (int i = 0; i < ids.length; i++) {
                Object id = ids[i];
                if (i != 0) sb.append(", ");
                Object value = nativeObject.get(id);
                sb.append(id).append(": ");
                if (value instanceof String) sb.append('"');
                sb.append(stringify(value));
                if (value instanceof String) sb.append('"');
            }

            sb.append(" }");
            return sb.toString();
        }

        if (nativeValue instanceof NativeArray nativeArray) {
            StringBuilder sb = new StringBuilder("[");

            for (int i = 0; i < nativeArray.size(); i++) {
                if (i != 0) sb.append(", ");
                Object value = nativeArray.get(i);
                if (value instanceof String) sb.append('"');
                sb.append(stringify(value));
                if (value instanceof String) sb.append('"');
            }

            sb.append("]");
            return sb.toString();
        }

        if (nativeValue instanceof Undefined)
            return "undefined";

        if (nativeValue == null)
            return "null";

        return nativeValue.toString();
    }

    public static String stringifyAll(String delimeter, Object... nativeValues) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < nativeValues.length; i++) {
            Object value = nativeValues[i];
            sb.append(stringify(value));
            if (i != nativeValues.length - 1) sb.append(delimeter);
        }

        return sb.toString();
    }

}
