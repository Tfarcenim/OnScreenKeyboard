package tfar.onscreenkeyboard.client;

import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.network.chat.Component;
import tfar.onscreenkeyboard.BasicKeyboardScreen;
import tfar.onscreenkeyboard.mixin.SignEditScreenMixin;

public abstract class TextFieldHelperKeyboardScreen<S extends Screen> extends BasicKeyboardScreen<S> {

    protected TextFieldHelper helper;
    protected final int line;

    protected TextFieldHelperKeyboardScreen(Component $$0, S parent, int line) {
        super($$0, parent);
        this.line = line;
    }

    protected TextFieldHelper find() {
        if (parent instanceof SignEditScreen) {
            return ((SignEditScreenMixin)parent).getSignField();
        }
        return null;
    }

    @Override
    protected void initEditBox(int xPoint, int yPoint) {
        helper = find();
        super.initEditBox(xPoint, yPoint);
    }

    @Override
    protected void extraDetails() {
        name.setMaxLength(16);
    }
}
