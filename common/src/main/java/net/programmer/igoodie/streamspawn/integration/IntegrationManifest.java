package net.programmer.igoodie.streamspawn.integration;

import java.io.File;

public class IntegrationManifest {

    public final String id;
    public final String name;
    public final String version;

    protected IntegrationManifest(String id, String name, String version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }

    public static IntegrationManifest of(String id, String name, String version) {
        return new IntegrationManifest(id, name, version);
    }

    public static IntegrationManifest fromFile(File file) {
        // TODO: Read a JSON file and deserialize
        return null;
    }

}
