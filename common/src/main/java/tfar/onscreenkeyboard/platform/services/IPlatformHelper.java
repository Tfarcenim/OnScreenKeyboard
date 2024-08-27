package tfar.onscreenkeyboard.platform.services;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import tfar.onscreenkeyboard.network.C2SModPacket;
import tfar.onscreenkeyboard.platform.MLConfig;

import java.util.function.Function;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    MLConfig getConfig();


    <MSG extends C2SModPacket> void registerServerPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf,MSG> reader);



    void sendToServer(C2SModPacket msg);

}