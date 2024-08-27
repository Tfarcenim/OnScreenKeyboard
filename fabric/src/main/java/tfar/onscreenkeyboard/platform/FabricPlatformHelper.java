package tfar.onscreenkeyboard.platform;

import net.minecraft.network.FriendlyByteBuf;
import tfar.onscreenkeyboard.network.C2SModPacket;
import tfar.onscreenkeyboard.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.util.function.Function;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public MLConfig getConfig() {
        return null;
    }

    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {

    }

    @Override
    public void sendToServer(C2SModPacket msg) {

    }
}
