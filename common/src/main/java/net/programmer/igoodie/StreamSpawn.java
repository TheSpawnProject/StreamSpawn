package net.programmer.igoodie;

import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import net.minecraft.resources.ResourceLocation;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.network.ModNetwork;
import net.programmer.igoodie.network.packet.ServerboundEventPacket;

public final class StreamSpawn {

    public static final String MOD_ID = "streamspawn";

    public static ResourceLocation id(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public static void init() {
        LifecycleEvent.SETUP.register(() -> {
            ModNetwork.initialize();
        });

        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(player -> {
            String eventName = "Testing";
            GoodieObject eventArgs = new GoodieObject();
            eventArgs.put("actor", "iGoodie");
            ModNetwork.CHANNEL.sendToServer(new ServerboundEventPacket(eventName, eventArgs));
        });
    }

}
