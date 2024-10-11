package net.programmer.igoodie.streamspawn.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.streamspawn.network.format.NbtGoodieFormat;

import java.util.function.Supplier;

public class SboundEventPacket extends PacketDefinition<SboundEventPacket.Payload> {

    public static class Payload {
        public String eventName;
        public GoodieObject eventArgs;

        public Payload() {}

        public Payload(String eventName, GoodieObject eventArgs) {
            this.eventName = eventName;
            this.eventArgs = eventArgs;
        }
    }

    @Override
    public Class<Payload> getPayloadClass() {
        return Payload.class;
    }

    @Override
    public void encode(Payload payload, FriendlyByteBuf buffer) {
        buffer.writeUtf(payload.eventName);
        buffer.writeNbt(NbtGoodieFormat.INSTANCE.readFromGoodie(payload.eventArgs));
    }

    @Override
    public Payload decode(FriendlyByteBuf buffer) {
        Payload payload = new Payload();
        payload.eventName = buffer.readUtf();
        payload.eventArgs = NbtGoodieFormat.INSTANCE.writeToGoodie(buffer.readNbt());
        return payload;
    }

    @Override
    public void handle(Payload payload, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context context = ctxSupplier.get();

        context.enqueueWork(() -> {
            System.out.println("Received " + payload.eventName);
            System.out.println(" With args " + payload.eventArgs);
        });

        context.setPacketHandled(true);
    }

}
