package net.programmer.igoodie.javascript.base;

import org.mozilla.javascript.ScriptableObject;

public abstract class HostObject extends ScriptableObject {

    public abstract void begin();

    public abstract void terminate();

}
