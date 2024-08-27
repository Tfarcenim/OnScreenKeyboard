package tfar.onscreenkeyboard.network;

import net.minecraft.network.FriendlyByteBuf;

public interface IModPacket {
    void write(FriendlyByteBuf to);

}
