package net.programmer.igoodie.streamspawn.network.packet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;
import net.programmer.igoodie.goodies.runtime.GoodieObject;
import net.programmer.igoodie.streamspawn.network.format.NbtGoodieFormat;
import net.programmer.igoodie.tsl.runtime.executor.TSLAsyncExecutor;

import java.util.function.Supplier;

public class TriggerEventC2SPacket extends PacketDefinition {

    public final String eventName;
    public final GoodieObject eventArgs;

    public TriggerEventC2SPacket(FriendlyByteBuf buffer) {
        this.eventName = buffer.readUtf();
        this.eventArgs = NbtGoodieFormat.INSTANCE.writeToGoodie(buffer.readNbt());
    }

    public TriggerEventC2SPacket(String eventName, GoodieObject eventArgs) {
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
            // TODO: Properly implement
            new TSLAsyncExecutor("").executeTaskAsync(() -> {
                System.out.println("Received " + this.eventName);
                System.out.println(" With args " + this.eventArgs);
                return null;
            });
        });
    }

}
