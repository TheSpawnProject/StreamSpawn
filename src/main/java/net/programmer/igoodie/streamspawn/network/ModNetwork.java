package net.programmer.igoodie.streamspawn.network;

import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.programmer.igoodie.streamspawn.StreamSpawn;
import net.programmer.igoodie.streamspawn.network.packet.PacketDefinition;
import net.programmer.igoodie.streamspawn.network.packet.SboundEventPacket;

public final class ModNetwork {

    public static final String PROTOCOL_VERSION = "0.0.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(StreamSpawn.id("network"))
            .clientAcceptedVersions(version -> version.equals(PROTOCOL_VERSION))
            .serverAcceptedVersions(version -> version.equals(PROTOCOL_VERSION))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void initialize() {
        register(new SboundEventPacket());
    }

    /* ----------------------- */

    private static int index = 0;

    private static <P, T extends PacketDefinition<P>> void register(T packetDefinition) {
        CHANNEL.registerMessage(index++,
                packetDefinition.getPayloadClass(),
                packetDefinition::encode,
                packetDefinition::decode,
                packetDefinition::handle
        );
    }

}
