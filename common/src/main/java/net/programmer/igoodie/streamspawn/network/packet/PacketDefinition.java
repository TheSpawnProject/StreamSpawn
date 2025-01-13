package net.programmer.igoodie.streamspawn.network.packet;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Supplier;

public abstract class PacketDefinition {

    public abstract void encode(FriendlyByteBuf buffer);

    public abstract void handle(Supplier<NetworkManager.PacketContext> contextSupplier);

}
