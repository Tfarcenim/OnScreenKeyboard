package tfar.onscreenkeyboard;

import it.unimi.dsi.fastutil.chars.CharCharPair;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class KeyboardHelper {


    public static void addKeyboardDirectly(Screen screen, int xPoint, int yPoint, Consumer<Button> addButton, EditBox editBox){
        List<ShiftableButton> shiftableButtons = new ArrayList<>();
        addShiftableKeys(screen,xPoint,yPoint,addButton,shiftableButtons,editBox);
        addSpecialKeys(screen,xPoint,yPoint,addButton,shiftableButtons);

        addButton.accept(new Button(screen.width / 2 + xPoint + 264, screen.height / 2 +yPoint , 42, 20, new TextComponent("Back"), button -> {
            screen.keyPressed(GLFW.GLFW_KEY_BACKSPACE,0,0);
        }));

        addButton.accept(new Button(screen.width / 2 + xPoint + 242 , screen.height / 2 +yPoint+66, 64, 20, new TextComponent("Space"), button -> {
            editBox.charTyped(' ',0);
            //  parent.keyPressed(GLFW.GLFW_KEY_SPACE,0,0);
        }));

       addButton.accept(new Button(screen.width / 2 + xPoint + 264 , screen.height / 2 +yPoint +44, 42, 20, new TextComponent("Enter"), button -> {
           screen.keyPressed(GLFW.GLFW_KEY_ENTER,0,0);
            //minecraft.popGuiLayer();
        }));

        addButton.accept(new Button(screen.width / 2 + xPoint  , screen.height / 2 + yPoint+66, 42, 20, new TextComponent("Shift"), button -> {
            for (ShiftableButton shiftableButton : shiftableButtons) {
                shiftableButton.shift();
            }
        }));

    }

    public static void addSpecialKeys(Screen screen, int xPoint, int yPoint, Consumer<Button> consumer, List<ShiftableButton> shiftableButtons) {

    }

    public static void addShiftableKeys(Screen screen, int xPoint, int yPoint, Consumer<Button> consumer, List<ShiftableButton> shiftableButtons,EditBox editBox) {

        KeyboardLayout keyboardLayout = KeyboardLayout.QWERTY;

        makeShiftableRow(screen,xPoint,yPoint,keyboardLayout.row1(),consumer,shiftableButtons, editBox);
        makeShiftableRow(screen,xPoint,yPoint+22,keyboardLayout.row2(),consumer,shiftableButtons, editBox);
        makeShiftableRow(screen,xPoint,yPoint+44,keyboardLayout.row3(),consumer,shiftableButtons, editBox);
        makeShiftableRow(screen,xPoint+44,yPoint+66,keyboardLayout.row4(),consumer,shiftableButtons, editBox);
    }

    protected static void makeShiftableRow(Screen screen, int xStart, int yStart, List<CharCharPair> chars, Consumer<Button> addButton, List<ShiftableButton> shiftableButtons,EditBox editBox) {
        for (int i = 0 ; i < chars.size();i++) {
            CharCharPair charCharPair = chars.get(i);
            ShiftableButton shiftableButton = new ShiftableButton(screen.width / 2 +xStart + i * 22, screen.height / 2 +yStart, 20, 20, new TextComponent(String.valueOf(charCharPair.firstChar())), button -> {
                if (((ShiftableButton)button).shift) {
                    editBox.charTyped(charCharPair.secondChar(),0);
                } else {
                    editBox.charTyped(charCharPair.firstChar(),0);
                }
            },new TextComponent(String.valueOf(charCharPair.secondChar())));
            shiftableButtons.add(shiftableButton);
            addButton.accept(shiftableButton);
        }
    }

}
