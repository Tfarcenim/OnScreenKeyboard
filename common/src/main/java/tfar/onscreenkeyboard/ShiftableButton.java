package tfar.onscreenkeyboard;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ShiftableButton extends Button {

    public Component secondary;
    boolean shift;

    public ShiftableButton(int $$0, int $$1, int $$2, int $$3, Component $$4, OnPress $$5,Component secondary) {
        this($$0, $$1, $$2, $$3, $$4, $$5,NO_TOOLTIP,secondary);
    }

    public ShiftableButton(int $$0, int $$1, int $$2, int $$3, Component $$4, OnPress $$5, OnTooltip $$6,Component secondary) {
        super($$0, $$1, $$2, $$3, $$4, $$5, $$6);
        this.secondary = secondary;
    }

    public void shift() {
        shift = !shift;
    }

    @Override
    public Component getMessage() {
        return shift ? secondary : super.getMessage();
    }
}
