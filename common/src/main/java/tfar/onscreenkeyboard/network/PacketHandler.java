package tfar.onscreenkeyboard.network;

import net.minecraft.resources.ResourceLocation;
import tfar.onscreenkeyboard.OnScreenKeyboard;
import tfar.onscreenkeyboard.platform.Services;

import java.util.Locale;

public class PacketHandler {

    public static void registerPackets() {

        Services.PLATFORM.registerServerPacket(C2SButtonPacket.class, C2SButtonPacket::new);

        ///////server to client

    }

    public static ResourceLocation packet(Class<?> clazz) {
        return OnScreenKeyboard.id(clazz.getName().toLowerCase(Locale.ROOT));
    }

}
