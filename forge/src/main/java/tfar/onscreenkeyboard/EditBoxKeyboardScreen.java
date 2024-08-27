package tfar.onscreenkeyboard;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class EditBoxKeyboardScreen<T extends Screen> extends BasicKeyboardScreen<T> {

    private EditBox original;

    protected EditBoxKeyboardScreen(Component $$0, T screen) {
        super($$0, screen);
    }

    protected EditBox find() {
        List<? extends GuiEventListener> children = parent.children();
        return (EditBox) children.stream().filter(guiEventListener -> guiEventListener instanceof EditBox).findFirst().orElse(null);
    }

    @Override
    protected void initEditBox(int xPoint, int yPoint) {
        original = find();
        super.initEditBox(xPoint, yPoint);
    }

    @Override
    protected void extraDetails() {
        name.setMaxLength(original.maxLength);
        name.setValue(original.getValue());
    }

    protected void onChange(String s) {
//        original.setValue(s);
    }

    @Override
    public void removed() {
        super.removed();
        original.setEditable(true);
    }
}
