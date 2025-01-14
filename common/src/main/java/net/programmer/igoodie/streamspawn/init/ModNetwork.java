package net.programmer.igoodie.streamspawn.init;

import dev.architectury.networking.NetworkChannel;
import net.minecraft.network.FriendlyByteBuf;
import net.programmer.igoodie.streamspawn.StreamSpawn;
import net.programmer.igoodie.streamspawn.network.packet.PacketDefinition;
import net.programmer.igoodie.streamspawn.network.packet.TriggerEventC2SPacket;

import java.lang.reflect.Constructor;

public class ModNetwork {

    public static final NetworkChannel CHANNEL = NetworkChannel.create(StreamSpawn.id("network"));

    public static void initialize() {
        register(TriggerEventC2SPacket.class);
    }

    private static <T extends PacketDefinition> void register(Class<T> packetClass) {
        CHANNEL.register(
                packetClass,
                PacketDefinition::encode,
                buffer -> {
                    try {
                        Constructor<T> constructor = packetClass.getConstructor(FriendlyByteBuf.class);
                        return constructor.newInstance(buffer);
                    } catch (Exception e) {
                        throw new UnsupportedOperationException("Could not decode the packet.", e);
                    }
                },
                PacketDefinition::handle
        );
    }

}
