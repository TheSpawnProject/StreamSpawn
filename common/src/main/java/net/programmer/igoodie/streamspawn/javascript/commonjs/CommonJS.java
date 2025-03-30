package net.programmer.igoodie.streamspawn.javascript.commonjs;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.commonjs.module.ModuleScope;
import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.provider.ModuleSourceProvider;
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
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

    public ModuleScope createTopLevelScope() {
        ScriptableObject scope = JavascriptEngine.createObject();
        Context cx = JavascriptEngine.CONTEXT.get();

        ModuleSourceProvider sourceProvider = new UrlModuleSourceProvider(
                getBuiltInModulesDir(),
                null
        );

        ModuleScriptProvider scriptProvider = new CjsScriptProvider(this, sourceProvider);

        CjsRequire require = new CjsRequire(this, cx, scope, scriptProvider,
                null, null, true);

        require.install(scope);

        try {
            // TODO: Make Base and Source URLs configurable, as they depend on the env and perhaps cwd
            return new ModuleScope(scope,
                    Objects.requireNonNull(CommonJS.class.getClassLoader()
                            .getResource("assets/streamspawn/spawnjs")).toURI(),
                    Objects.requireNonNull(CommonJS.class.getClassLoader()
                            .getResource("assets/streamspawn/spawnjs")).toURI()
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
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
