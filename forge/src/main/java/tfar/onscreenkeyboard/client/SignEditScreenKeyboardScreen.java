package tfar.onscreenkeyboard.client;

import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ForgeHooksClient;
import tfar.onscreenkeyboard.mixin.SignEditScreenMixin;

public class SignEditScreenKeyboardScreen extends TextFieldHelperKeyboardScreen<SignEditScreen> {

    public SignEditScreenKeyboardScreen(Component $$0, SignEditScreen parent, int line) {
        super($$0, parent,line);
    }

    @Override
    protected void extraDetails() {
        super.extraDetails();
        name.setValue(((SignEditScreenMixin)parent).getMessages()[line]);
    }

    @Override
    public boolean charTyped(char pCodePoint, int pModifiers) {
        return this.parent.charTyped(pCodePoint,pModifiers);
    }

    @Override
    protected void onEscape() {
        ForgeHooksClient.clearGuiLayers(minecraft);
    }

    @Override
    protected void onChange(String s) {

    }
}
