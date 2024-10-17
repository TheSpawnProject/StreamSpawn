package net.programmer.igoodie.network.packet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.network.format.NbtGoodieFormat;

import java.util.function.Supplier;

public class ServerboundEventPacket extends PacketDefinition {

    public final String eventName;
    public final GoodieObject eventArgs;

    public ServerboundEventPacket(FriendlyByteBuf buffer) {
        this.eventName = buffer.readUtf();
        this.eventArgs = NbtGoodieFormat.INSTANCE.writeToGoodie(buffer.readNbt());
    }

    public ServerboundEventPacket(String eventName, GoodieObject eventArgs) {
        this.eventName = eventName;
        this.eventArgs = eventArgs;
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.eventName);
        buffer.writeNbt(NbtGoodieFormat.INSTANCE.readFromGoodie(this.eventArgs));
    }

    @Override
    public void handle(Supplier<NetworkManager.PacketContext> contextSupplier) {
        NetworkManager.PacketContext context = contextSupplier.get();

        context.queue(() -> {
            System.out.println("Received " + this.eventName);
            System.out.println(" With args " + this.eventArgs);
        });
    }

}
