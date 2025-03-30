package net.programmer.igoodie.streamspawn.javascript.coercer;

import net.programmer.igoodie.goodies.registry.Registrable;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public abstract class Coercer<T, N extends ScriptableObject> implements Registrable<Class<?>> {

    public abstract N coerceValue(T value, Scriptable scope);

}
