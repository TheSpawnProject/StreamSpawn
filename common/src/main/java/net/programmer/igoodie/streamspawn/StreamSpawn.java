package net.programmer.igoodie.streamspawn;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.resources.ResourceLocation;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.streamspawn.init.ModIntegrations;
import net.programmer.igoodie.streamspawn.init.ModNetwork;
import net.programmer.igoodie.streamspawn.init.ModPaths;
import net.programmer.igoodie.streamspawn.init.ModTSL;
import net.programmer.igoodie.streamspawn.network.packet.TriggerEventC2SPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class StreamSpawn {

    public static final String MOD_ID = "streamspawn";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void init() {
        LifecycleEvent.SETUP.register(() -> {
            ModPaths.initialize();
            ModNetwork.initialize();
            ModTSL.initializePlatform();
            ModIntegrations.initialize();
            ModTSL.loadRulesets();
        });

        ClientLifecycleEvent.CLIENT_STARTED.register(instance -> {});

        LifecycleEvent.SERVER_STARTING.register(server -> {});

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> {
            String eventName = "Testing";
            GoodieObject eventArgs = new GoodieObject();
            eventArgs.put("actor", "iGoodie");
            ModNetwork.CHANNEL.sendToServer(new TriggerEventC2SPacket(eventName, eventArgs));
        });
    }

}
