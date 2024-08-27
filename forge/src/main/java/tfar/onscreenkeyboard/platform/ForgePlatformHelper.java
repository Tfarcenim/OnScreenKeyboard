package tfar.onscreenkeyboard.platform;

import net.minecraft.network.FriendlyByteBuf;
import tfar.onscreenkeyboard.PacketHandlerForge;
import tfar.onscreenkeyboard.network.C2SModPacket;
import tfar.onscreenkeyboard.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.Function;

public class ForgePlatformHelper implements IPlatformHelper {

    final MLConfig config = new TomlConfig();
    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public MLConfig getConfig() {
        return config;
    }

    int i;


    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerForge.INSTANCE.registerMessage(i++, packetLocation, MSG::write, reader, PacketHandlerForge.wrapC2S());
    }

    @Override
    public void sendToServer(C2SModPacket msg) {
        PacketHandlerForge.sendToServer(msg);
    }
}