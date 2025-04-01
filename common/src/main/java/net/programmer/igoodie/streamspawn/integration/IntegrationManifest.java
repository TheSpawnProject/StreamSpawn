package net.programmer.igoodie.streamspawn.integration;

import java.io.File;

public class IntegrationManifest {

    public final String id;
    public final String name;
    public final String author;
    public final String version;

    protected IntegrationManifest(String id, String name, String author, String version) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.version = version;
    }

    public static IntegrationManifest of(String id, String name, String author, String version) {
        return new IntegrationManifest(id, name, author, version);
    }

    public static IntegrationManifest fromFile(File file) {
        // TODO: Read a JSON file and deserialize
        return null;
    }

}
