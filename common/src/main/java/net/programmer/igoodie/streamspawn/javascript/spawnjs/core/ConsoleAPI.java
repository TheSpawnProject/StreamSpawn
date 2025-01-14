package net.programmer.igoodie.streamspawn.javascript.spawnjs.core;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.base.RuntimeAPI;
import org.mozilla.javascript.*;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ConsoleAPI extends RuntimeAPI {

    @Override
    public void init(ScriptableObject scope) {
        ScriptableObject consoleObj = JavascriptEngine.createScope(scope);
        scope.defineProperty("console", consoleObj, ScriptableObject.CONST);

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

    public static String stringify(Object obj) {
        if (obj instanceof NativeObject nativeObject) {
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

        if (obj instanceof NativeArray nativeArray) {
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

        if (obj instanceof Undefined)
            return "undefined";

        if (obj == null)
            return "null";

        return obj.toString();
    }

}
