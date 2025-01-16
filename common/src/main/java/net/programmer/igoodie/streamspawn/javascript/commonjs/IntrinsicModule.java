package net.programmer.igoodie.streamspawn.javascript.commonjs;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.commonjs.module.ModuleScope;

import java.io.File;
import java.util.Objects;

public abstract class IntrinsicModule {

    protected ModuleScope moduleScope;

    public abstract String moduleName();

    public abstract void buildExports(ScriptableObject exports);

    public boolean isLoaded() {
        return this.moduleScope != null;
    }

    public void load() {
        ScriptableObject scope = JavascriptEngine.createObject();
        this.buildExports(scope);

        try {
            // TODO: Standardize this, perhaps to Config folder? OR make it a config
            File modulesDir = new File(Objects.requireNonNull(CjsAPI.class.getClassLoader()
                    .getResource("assets/streamspawn/spawnjs")).toURI()).getCanonicalFile();
            this.moduleScope = new ModuleScope(scope, modulesDir.toURI(), modulesDir.toURI());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}