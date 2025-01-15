package net.programmer.igoodie.streamspawn.javascript.base;

import net.programmer.igoodie.streamspawn.javascript.spawnjs.globals.ConsoleAPI;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.FunctionObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public abstract class HostObject extends ScriptableObject {

    protected static <S extends Scriptable> S bindToScope(S obj, Scriptable scope) {
        obj.setParentScope(scope);
        obj.setPrototype(getClassPrototype(scope, obj.getClassName()));
        return obj;
    }

    protected static IllegalArgumentException createInvalidArgumentsException(Scriptable thisObj, Object[] args, Function function) {
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
