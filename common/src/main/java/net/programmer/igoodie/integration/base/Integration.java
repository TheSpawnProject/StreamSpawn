package net.programmer.igoodie.integration.base;

import net.programmer.igoodie.javascript.JavascriptEngine;
import net.programmer.igoodie.javascript.base.ServiceObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Integration {

    protected final String name;
    protected final String version;
    protected Script script;

    public final List<ServiceObject> services = new ArrayList<>();

    public Integration(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public Script getScript() {
        return script;
    }

    public ScriptableObject createScope(Scriptable parentScope) {
        ScriptableObject integrationScope = JavascriptEngine.createScope(parentScope);
        JavascriptEngine.eval(integrationScope, "const integrationId = '" + name + "'");
        JavascriptEngine.eval(integrationScope, "const integrationVersion = '" + version + "'");
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
        Context context = JavascriptEngine.CONTEXT.get();
        this.script = context.compileString(source, name + ".integration.js", 1, null);
    }

}
