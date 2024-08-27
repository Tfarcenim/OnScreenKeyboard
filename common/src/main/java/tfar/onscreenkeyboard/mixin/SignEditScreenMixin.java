package tfar.onscreenkeyboard.mixin;

import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SignEditScreen.class)
public interface SignEditScreenMixin {
    @Accessor
    TextFieldHelper getSignField();
    @Accessor int getLine();
    @Accessor String[] getMessages();
}