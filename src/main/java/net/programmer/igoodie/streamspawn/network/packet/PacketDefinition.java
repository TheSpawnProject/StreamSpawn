package net.programmer.igoodie.streamspawn.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class PacketDefinition<P> {

    public abstract Class<P> getPayloadClass();

    public abstract void encode(P payload, FriendlyByteBuf buffer);

    public abstract P decode(FriendlyByteBuf buffer);

    public abstract void handle(final P payload, Supplier<NetworkEvent.Context> ctxSupplier);

}
