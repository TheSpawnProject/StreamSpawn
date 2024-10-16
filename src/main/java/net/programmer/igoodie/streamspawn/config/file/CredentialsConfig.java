package net.programmer.igoodie.streamspawn.config.file;

import net.programmer.igoodie.goodies.configuration.JsonConfiGoodie;
import net.programmer.igoodie.goodies.serialization.annotation.Goodie;
import net.programmer.igoodie.streamspawn.config.base.ModConfig;

import java.util.List;

public class CredentialsConfig extends JsonConfiGoodie implements ModConfig {

    @Goodie
    List<CredentialInfo> credentials;

    public static class CredentialInfo {

        @Goodie
        String platform;

        @Goodie
        String token;

        @Goodie
        String name;

    }

    @Override
    public String getName() {
        return "credentials.json";
    }

}
