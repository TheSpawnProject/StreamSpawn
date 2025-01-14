package net.programmer.igoodie.streamspawn.javascript.spawnjs.core;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.base.RuntimeAPI;
import net.programmer.igoodie.streamspawn.javascript.util.NativeFormat;
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
                    .map(NativeFormat::toString)
                    .collect(Collectors.joining(" "));

            System.out.println(msg);

            return Undefined.instance;
        }

    }

}
