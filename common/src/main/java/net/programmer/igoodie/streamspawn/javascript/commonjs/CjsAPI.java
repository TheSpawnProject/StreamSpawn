package net.programmer.igoodie.streamspawn.javascript.commonjs;

import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.base.ScopeInstallable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.commonjs.module.ModuleScriptProvider;
import org.mozilla.javascript.commonjs.module.provider.ModuleSourceProvider;
import org.mozilla.javascript.commonjs.module.provider.UrlModuleSourceProvider;

import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class CjsAPI implements ScopeInstallable {

    protected Map<String, IntrinsicModule> intrinsicModules = new ConcurrentHashMap<>();

    public CjsAPI() {}

    public CjsAPI withIntrinsicModule(IntrinsicModule intrinsicModule) {
        this.intrinsicModules.put(intrinsicModule.moduleName(), intrinsicModule);
        return this;
    }

    @Override
    public void install(ScriptableObject scope) {
        Context cx = JavascriptEngine.CONTEXT.get();

        ModuleSourceProvider sourceProvider = new UrlModuleSourceProvider(
                getBuiltInModulesDir(),
                null
        );

        ModuleScriptProvider scriptProvider = new CjsScriptProvider(this, sourceProvider);

        CjsRequire require = new CjsRequire(this, cx, scope, scriptProvider,
                null, null, true);

        require.install(scope);
    }

    protected List<URI> getBuiltInModulesDir() {
        try {
            File modulesDir = new File(Objects.requireNonNull(CjsAPI.class.getClassLoader()
                    .getResource("assets/streamspawn/spawnjs")).toURI()).getCanonicalFile();

            return Collections.singletonList(modulesDir.toURI());

        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
