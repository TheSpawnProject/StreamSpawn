package net.programmer.igoodie.streamspawn.javascript.base;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public abstract class HostObject extends ScriptableObject {

    protected static <S extends Scriptable> S bindToScope(S obj, Scriptable scope) {
        obj.setParentScope(scope);
        obj.setPrototype(getClassPrototype(scope, obj.getClassName()));
        return obj;
    }

}
