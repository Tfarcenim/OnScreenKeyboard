package tfar.onscreenkeyboard.platform;

import net.minecraft.client.gui.screens.Screen;

import java.util.Set;

public class TomlConfig implements MLConfig{
    @Override
    public Set<Class<? extends Screen>> allowed() {
        return Set.of();
    }
}
