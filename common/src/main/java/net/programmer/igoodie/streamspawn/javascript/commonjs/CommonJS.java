package net.programmer.igoodie.streamspawn.javascript.commonjs;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider;

import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class CommonJS {

    protected Map<String, IntrinsicModule> intrinsicModules = new ConcurrentHashMap<>();

    public CommonJS() {}

    public void registerIntrinsicModule(IntrinsicModule module) {
        this.intrinsicModules.put(module.moduleId(), module);
    }

    public Scriptable createTopLevelScope() {
        Context cx = JavascriptEngine.CONTEXT.get();

        Scriptable scope = JavascriptEngine.createTopLevelScope();

        CjsScriptProvider scriptProvider = new CjsScriptProvider(this,
                new UrlModuleSourceProvider(getBuiltInModulesDir(), null));

        CjsRequire require = new CjsRequire(this, cx, scope, scriptProvider,
                null, null, true);

        require.install(scope);

        return scope;
    }

    protected List<URI> getBuiltInModulesDir() {
        try {
            // TODO: Make those URLs configurable, as they depend on the env and perhaps cwd
            File modulesDir = new File(Objects.requireNonNull(CommonJS.class.getClassLoader()
                    .getResource("assets/streamspawn/spawnjs")).toURI()).getCanonicalFile();

            return Collections.singletonList(modulesDir.toURI());

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
