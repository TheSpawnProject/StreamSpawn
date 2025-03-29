package net.programmer.igoodie.streamspawn.javascript.spawnjs;

import net.programmer.igoodie.streamspawn.javascript.spawnjs.globals.ConsoleAPI;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;

public class SpawnJSExceptions {

    public static IllegalArgumentException invalidArguments(Scriptable thisObj, Object[] args, Function function) {
        String calleeName = "";

        if (function instanceof FunctionObject functionObj) {
            if (thisObj instanceof FunctionObject classObj) {
                calleeName = classObj.getFunctionName() + "." + functionObj.getFunctionName();
            }
        }

        // TODO: Replace with ScriptRuntime.throwError
        return new IllegalArgumentException("Invalid arguments provided to " + calleeName
                + " -> (" + ConsoleAPI.stringifyAll(", ", args) + ")");
    }

}
