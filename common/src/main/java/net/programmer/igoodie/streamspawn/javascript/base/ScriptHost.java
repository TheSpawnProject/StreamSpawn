package net.programmer.igoodie.streamspawn.javascript.base;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Represents an implementation in Java Realm,
 * which has bindings to the Javascript Realm.
 */
public abstract class ScriptHost extends ScriptableObject {

    protected static <S extends Scriptable> S bindToScope(S obj, Scriptable scope) {
        obj.setParentScope(scope);
        obj.setPrototype(getClassPrototype(scope, obj.getClassName()));
        return obj;
    }

}
