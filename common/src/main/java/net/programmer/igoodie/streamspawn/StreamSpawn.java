package net.programmer.igoodie.streamspawn;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.resources.ResourceLocation;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.streamspawn.init.ModIntegrations;
import net.programmer.igoodie.streamspawn.init.ModNetwork;
import net.programmer.igoodie.streamspawn.network.packet.TriggerEventC2SPacket;

public final class StreamSpawn {

    public static final String MOD_ID = "streamspawn";

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void init() {
        LifecycleEvent.SETUP.register(() -> {
            ModNetwork.initialize();
        });

        ClientLifecycleEvent.CLIENT_STARTED.register(instance -> {
            ModIntegrations.initialize();
        });

        LifecycleEvent.SERVER_STARTING.register(server -> {
        });

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> {
            String eventName = "Testing";
            GoodieObject eventArgs = new GoodieObject();
            eventArgs.put("actor", "iGoodie");
            ModNetwork.CHANNEL.sendToServer(new TriggerEventC2SPacket(eventName, eventArgs));
        });
    }

}
