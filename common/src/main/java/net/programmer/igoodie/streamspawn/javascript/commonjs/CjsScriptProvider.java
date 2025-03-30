package net.programmer.igoodie.streamspawn.javascript.commonjs;

import org.mozilla.javascript.commonjs.module.provider.ModuleSourceProvider;
import org.mozilla.javascript.commonjs.module.provider.StrongCachingModuleScriptProvider;

public class CjsScriptProvider extends StrongCachingModuleScriptProvider {

    protected final CommonJS cjs;

    public CjsScriptProvider(CommonJS cjs, ModuleSourceProvider moduleSourceProvider) {
        super(moduleSourceProvider);
        this.cjs = cjs;
    }

}
