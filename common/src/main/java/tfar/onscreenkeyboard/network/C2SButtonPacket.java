package tfar.onscreenkeyboard.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import tfar.onscreenkeyboard.platform.Services;

public class C2SButtonPacket implements C2SModPacket {

    private final String name;

    public C2SButtonPacket(String name) {
        this.name = name;
    }

    public C2SButtonPacket(FriendlyByteBuf buf) {
        name = buf.readUtf();
    }

    public static void send(String name) {
        Services.PLATFORM.sendToServer(new C2SButtonPacket(name));
    }

    public void handleServer(ServerPlayer player) {
        player.getMainHandItem().setHoverName(new TextComponent(name));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(name);
    }

}
