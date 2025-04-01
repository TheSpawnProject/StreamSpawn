package net.programmer.igoodie.streamspawn.integration;

import net.programmer.igoodie.goodies.registry.Registry;
import net.programmer.igoodie.streamspawn.javascript.JavascriptEngine;
import net.programmer.igoodie.streamspawn.javascript.commonjs.CommonJS;
import net.programmer.igoodie.tsl.TSLPlatform;
import net.programmer.igoodie.tsl.runtime.event.TSLEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mozilla.javascript.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class IntegrationLoader {

    protected final Registry<String, Integration> registry;
    protected final TSLPlatform platform;
    protected final Scriptable scope;

    protected final File dir;
    protected State state = State.PRISTINE;
    protected Exception error;
    protected Integration integration;

    public IntegrationLoader(Registry<String, Integration> registry, TSLPlatform platform, Scriptable scope, File dir) {
        this.registry = registry;
        this.platform = platform;
        this.scope = scope;
        this.dir = dir;
    }

    public State getState() {
        return state;
    }

    public Exception getError() {
        return error;
    }

    protected String readIntegrationFile(String relativePath) {
        Path filePath = dir.toPath().resolve(relativePath);
        if (!Files.exists(filePath)) return null;

        try {
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    public Integration load() {
        try {
            this.state = State.LOADING;
            this.integration = new Integration(this);

            this.interpretManifest();
            this.interpretScript();

            this.state = State.DONE;
            this.integration.state = Integration.State.READY;

        } catch (Exception e) {
            this.state = State.FAILED;
            this.error = e;
        }

        return integration;
    }

    protected void interpretManifest() throws Exception {
        String manifestRaw = readIntegrationFile("manifest.json");

        if (manifestRaw == null) {
            throw new IllegalStateException("Missing manifest.json");
        }

        JSONObject manifest = new JSONObject(manifestRaw);

        String id = manifest.getString("id");
        String name = manifest.getString("name");
        String author = manifest.getString("author");
        String version = manifest.getString("version");

        if (this.registry.get(id) != null) {
            throw new IllegalStateException("Plugin with " + id + " was already loaded...");
        }

        this.integration.manifest = new IntegrationManifest(id, name, author, version);

        JSONObject target = manifest.getJSONObject("target");
        String targetTslVersion = target.getString("tsl_version");
        String targetSsVersion = target.getString("streamspawn_version");
        // TODO: Check target version integrity

        JSONObject events = manifest.getJSONObject("events");
        JSONArray eventNames = events.names();

        for (int i = 0; i < eventNames.length(); i++) {
            String eventName = eventNames.getString(i);

            TSLEvent tslEvent = new TSLEvent(eventName);

            JSONObject propertySchemas = events.getJSONObject(eventName);
            JSONArray propertyNames = propertySchemas.names();

            for (int j = 0; j < propertyNames.length(); j++) {
                String propertyName = propertyNames.getString(j);
                JSONObject propertySchema = propertySchemas.getJSONObject(propertyName);
                TSLEvent.Property<?> property = this.interpretProperty(propertyName, propertySchema);
                tslEvent.addPropertyType(property);
            }

            this.platform.registerEvent(tslEvent);
            this.integration.loadedEvents.add(tslEvent);
        }

        this.integration.state = Integration.State.MANIFEST_EVALUATED;
    }

    protected void interpretScript() throws Exception {
        String scriptRaw = readIntegrationFile("integration.js");

        Context cx = JavascriptEngine.CONTEXT.get();
        scriptRaw = scriptRaw.replaceFirst("^[\"']use strict[\"'];?", "//'use strict';");
        this.integration.script = cx.compileString(scriptRaw, dir.getName() + "/integration.js", 1, null);

        Scriptable integrationScope = JavascriptEngine.forkScope(scope);
        CommonJS.installModuleVariables(integrationScope);
        ScriptableObject.defineProperty(integrationScope, "integration", this.integration.manifest, ScriptableObject.CONST);
        JavascriptEngine.eval(integrationScope, "const integrationConfig = { token: 'TODO:DUMMY_TOKEN' };");

        this.integration.script.exec(cx, integrationScope);

        if (ScriptableObject.getProperty(integrationScope, "exports") instanceof ScriptableObject exports) {
            if (exports.get("default") instanceof NativeObject defaultExports) {
                Function start = (Function) defaultExports.get("start");
                Function stop = (Function) defaultExports.get("stop");
                this.integration.startCb = () -> start.call(cx, integrationScope, exports, new Object[0]);
                this.integration.stopCb = () -> stop.call(cx, integrationScope, exports, new Object[0]);
                this.integration.state = Integration.State.SCRIPT_EVALUATED;
                return;
            }
        }

        throw new IllegalStateException("Script did not define an integration.");
    }

    protected TSLEvent.Property<?> interpretProperty(String propertyName, JSONObject schema) throws Exception {
        TSLEvent.Property<?> property = null;

        if (schema.optBoolean("string")) {
            property = TSLEvent.PropertyBuilder.STRING.create(propertyName);
        }

        if (schema.optBoolean("number")) {
            if (property != null)
                throw new IllegalArgumentException("Properties cannot have multiple types. -> " + propertyName);
            property = TSLEvent.PropertyBuilder.DOUBLE.create(propertyName);
        }

        if (schema.optBoolean("boolean")) {
            if (property != null)
                throw new IllegalArgumentException("Properties cannot have multiple types. -> " + propertyName);
            property = TSLEvent.PropertyBuilder.BOOLEAN.create(propertyName);
        }

        if (property == null) {
            throw new IllegalArgumentException("Missing property type, or unknown type is present for '" + propertyName + "'");
        }

        return property;
    }

    public enum State {
        PRISTINE,
        LOADING,
        FAILED,
        DONE
    }

}


