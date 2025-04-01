package net.programmer.igoodie.streamspawn.integration;

import net.programmer.igoodie.goodies.registry.Registrable;
import net.programmer.igoodie.tsl.runtime.event.TSLEvent;
import org.mozilla.javascript.Script;

import java.util.ArrayList;
import java.util.List;

public class Integration implements Registrable<String> {

    protected final IntegrationLoader loader;
    protected IntegrationManifest manifest;
    protected List<TSLEvent> loadedEvents;
    protected Script script;

    protected State state = State.NOT_LOADED;

    protected Runnable startCb;
    protected Runnable stopCb;

    protected Integration(IntegrationLoader loader) {
        this.loader = loader;
        this.loadedEvents = new ArrayList<>();
    }

    @Override
    public String getId() {
        return this.manifest.id;
    }

    public State getState() {
        return state;
    }

    public IntegrationLoader getLoader() {
        return loader;
    }

    public IntegrationManifest getManifest() {
        return this.manifest;
    }

    public List<TSLEvent> getLoadedEvents() {
        return loadedEvents;
    }

    public Script getScript() {
        return this.script;
    }

    public void start() {
        this.startCb.run();
        this.state = State.STARTED;
    }

    public void stop() {
        this.stopCb.run();
        this.state = State.STOPPED;
    }

    public enum State {
        NOT_LOADED,
        MANIFEST_EVALUATED,
        SCRIPT_EVALUATED,
        READY,
        STARTED,
        STOPPED,
        ERROR
    }

}
