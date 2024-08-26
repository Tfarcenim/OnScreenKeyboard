package tfar.onscreenkeyboard.platform;

import net.minecraft.client.gui.screens.Screen;

import java.util.Set;

public interface MLConfig {
    Set<Class<? extends Screen>> allowed();
}
