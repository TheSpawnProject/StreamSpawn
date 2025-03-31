package net.programmer.igoodie.streamspawn.integration.base;

import net.programmer.igoodie.goodies.registry.Registrable;
import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.base.ScopeInstallable;
import net.programmer.igoodie.streamspawn.javascript.service.ScriptService;
import org.mozilla.javascript.*;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Integration implements Registrable<String>, ScopeInstallable {

    protected final IntegrationManifest manifest;

    protected Script script;

    protected Runnable startCb;
    protected Runnable stopCb;

    @Deprecated
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

    public void downloadScript(String url) throws IOException {
        InputStream inputStream = new URL(url).openStream();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String source = reader.lines().collect(Collectors.joining("\n"));
            this.loadScript(source);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load script", e);
        }
    }

    public void loadScript(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("File does not exist: " + file.getAbsolutePath());
        }

        this.loadScript(Files.readString(file.toPath(), StandardCharsets.UTF_8));
    }

    public void loadScript(String source) {
        Context cx = JavascriptEngine.CONTEXT.get();
        source = source.replaceFirst("^[\"']use strict[\"'];?", "//'use strict';");
        this.script = cx.compileString(source, "<anonymous>", 1, null);
    }

    @Override
    public void install(Scriptable scope) {
        ScriptableObject.defineProperty(scope, "integration", this.manifest, ScriptableObject.CONST);
        ScriptableObject.defineProperty(scope, "exports", new NativeObject(), ScriptableObject.PERMANENT);
        ScriptableObject.defineProperty(scope, "module", new NativeObject(), ScriptableObject.PERMANENT);
        JavascriptEngine.eval(scope, "const integrationConfig = { token: 'TODO:DUMMY_TOKEN' };");
    }

    public void load(Scriptable scope) {
        if (this.script == null) {
            throw new IllegalStateException("Script is not loaded yet.");
        }

        Context cx = JavascriptEngine.CONTEXT.get();

        this.script.exec(cx, scope);

        if (ScriptableObject.getProperty(scope, "exports") instanceof ScriptableObject exports) {
            if (exports.get("default") instanceof NativeObject defaultExports) {
                Function start = (Function) defaultExports.get("start");
                Function stop = (Function) defaultExports.get("stop");
                this.startCb = () -> start.call(cx, scope, exports, new Object[0]);
                this.stopCb = () -> stop.call(cx, scope, exports, new Object[0]);
                return;
            }
        }

        throw new IllegalStateException("Script did not define an integration.");
    }

    public void start() {
        if (this.startCb == null) {
            throw new IllegalStateException("Script is not executed yet.");
        }

        this.startCb.run();
    }

    public void stop() {
        if (this.stopCb == null) {
            throw new IllegalStateException("Script is not executed yet.");
        }

        this.stopCb.run();
    }

}
