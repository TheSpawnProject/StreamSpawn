package net.programmer.igoodie.streamspawn.integration.base;

import net.programmer.igoodie.goodies.registry.Registrable;
import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.service.ScriptService;
import org.mozilla.javascript.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Integration implements Registrable<String> {

    protected final IntegrationManifest manifest;
    protected Script script;

    public final List<ScriptService> services = new ArrayList<>();

    public Integration(IntegrationManifest manifest) {
        this.manifest = manifest;
    }

    @Override
    public String getId() {
        return this.manifest.id;
    }

    public IntegrationManifest getManifest() {
        return this.manifest;
    }

    public Script getScript() {
        return this.script;
    }

    public ScriptableObject createScope(Scriptable parentScope) {
        ScriptableObject integrationScope = JavascriptEngine.createObject(parentScope);
        integrationScope.defineProperty("integration", Context.javaToJS(this.manifest, integrationScope), ScriptableObject.CONST);
        integrationScope.defineProperty("exports", new NativeObject(), ScriptableObject.PERMANENT);
        integrationScope.defineProperty("module", new NativeObject(), ScriptableObject.PERMANENT);
        JavascriptEngine.eval(integrationScope, "const integrationConfig = { token: 'TODO:DUMMY_TOKEN' };");
        return integrationScope;
    }

    public void downloadScript(String url) throws IOException {
        InputStream inputStream = new URL(url).openStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String source = reader.lines().collect(Collectors.joining("\n"));
            this.loadScript(source);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load script", e);
        }
    }

    public void loadScript(String source) {
        Context cx = JavascriptEngine.CONTEXT.get();
        source = source.replaceFirst("^[\"']use strict[\"'];?", "//'use strict';");
        this.script = cx.compileString(source, "<anonymous>", 1, null);
    }

}
