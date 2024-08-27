package tfar.onscreenkeyboard;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class SignKeyboardScreen extends Screen {

    private final EditBox original;
    private EditBox name;

    List<ShiftableButton> shiftableButtons = new ArrayList<>();

    protected SignKeyboardScreen(Component $$0, EditBox original) {
        super($$0);
        this.original = original;
    }

    @Override
    protected void init() {
        super.init();
        initEditBox();
        shiftableButtons.clear();

        int xPoint = -150;

        char[][] row1Chars = new char[][]{
                new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0','-','='},
                new char[]{'!', '@', '#', '$', '%', '^', '&', '*', '(', ')','_','+'},
        };
        makeShiftableRow(xPoint,-44,row1Chars);


        char[][] row2Chars = new char[][]{
                new char[]{'q','w','e','r','t','y','u','i','o','p','[',']','\\','`'},
                new char[]{'Q','W','E','R','T','Y','U','I','O','P','{','}','|','~'}
        };
        makeShiftableRow(xPoint,-22,row2Chars);


        char[][] row3Chars = new char[][]{
                new char[]{'a','s','d','f','g','h','j','k','l',';','\'','/'},
                new char[]{'A','S','D','F','G','H','J','K','L',':','\"','?'}
        };
        makeShiftableRow(xPoint,0,row3Chars);


        char[][] row4Chars = new char[][]{
                new char[]{'z','x','c','v','b','n','m',',','.'},
                new char[]{'Z','X','C','V','B','N','M','<','>',}

        };
        makeShiftableRow(xPoint + 44,22,row4Chars);


        addRenderableWidget(new Button(width / 2 + xPoint + 264, height / 2 - 44 , 42, 20, new TextComponent("Back"), button -> {
            name.deleteChars(-1);
        }));

        addRenderableWidget(new Button(width / 2 + xPoint + 242 , height / 2 +22, 64, 20, new TextComponent("Space"), button -> {
            name.charTyped(' ',0);
        }));

        addRenderableWidget(new Button(width / 2 + xPoint + 264 , height / 2, 42, 20, new TextComponent("Enter"), button -> {
            this.minecraft.popGuiLayer();
        }));

        addRenderableWidget(new Button(width / 2 + xPoint  , height / 2 +22, 42, 20, new TextComponent("Shift"), button -> {
            for (ShiftableButton shiftableButton : shiftableButtons) {
                shiftableButton.shift();
            }
        }));

        }

        protected void makeShiftableRow(int xStart,int yStart,char[][] chars) {
            for (int i = 0 ; i < chars[0].length;i++) {
                char char0 = chars[0][i];
                char char1 = chars[1][i];
                ShiftableButton shiftableButton = new ShiftableButton(width / 2 +xStart + i * 22, height / 2 +yStart, 20, 20, new TextComponent(String.valueOf(char0)), button -> {
                    if (((ShiftableButton)button).shift) {
                        name.charTyped(char1, 0);
                    } else {
                        name.charTyped(char0, 0);
                    }
                },new TextComponent(String.valueOf(char1)));
                shiftableButtons.add(shiftableButton);
                addRenderableWidget(shiftableButton);
            }
        }

    protected void initEditBox() {
        int x = width / 2;
        int y = height/2;
        this.name = new EditBox(this.font, x - 50, y - 65, 120, 12, new TranslatableComponent("container.repair"));
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);

        this.name.setMaxLength(50);
        name.setValue(original.getValue());
        name.setResponder(this::onChange);
        setInitialFocus(name);
    }

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        RenderSystem.disableBlend();
        this.renderFg(pPoseStack, pMouseX, pMouseY, pPartialTick);
     //   this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    public void renderFg(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.name.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == GLFW.GLFW_KEY_ESCAPE) {
            original.setEditable(true);
            this.minecraft.popGuiLayer();
        }

        return this.name.keyPressed(pKeyCode, pScanCode, pModifiers) || this.name.canConsumeInput() || super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    void onChange(String s) {
        original.setValue(s);
    }

    @Override
    public void onClose() {
        super.onClose();
        original.setEditable(true);
    }
}
