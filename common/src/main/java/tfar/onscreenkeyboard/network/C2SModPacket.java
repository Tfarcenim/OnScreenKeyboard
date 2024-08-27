package tfar.onscreenkeyboard.network;

import net.minecraft.server.level.ServerPlayer;

public interface C2SModPacket extends IModPacket {

    void handleServer(ServerPlayer player);

}
