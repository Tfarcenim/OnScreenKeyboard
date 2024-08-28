package tfar.onscreenkeyboard;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.chars.CharCharPair;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.lwjgl.glfw.GLFW;
import tfar.onscreenkeyboard.mixin.SignEditScreenMixin;

import java.util.ArrayList;
import java.util.List;

public abstract class BasicKeyboardScreen<S extends Screen> extends Screen {

    protected final S parent;
    protected EditBox name;

    List<ShiftableButton> shiftableButtons = new ArrayList<>();

    protected BasicKeyboardScreen(Component $$0, S parent) {
        super($$0);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int xPoint = -152;
        int yPoint = 32;
        initEditBox(xPoint,yPoint);
        shiftableButtons.clear();


        KeyboardLayout keyboardLayout = KeyboardLayout.QWERTY;
        makeShiftableRow(xPoint,yPoint,keyboardLayout.row1());
        makeShiftableRow(xPoint,yPoint+22,keyboardLayout.row2());
        makeShiftableRow(xPoint,yPoint+44,keyboardLayout.row3());
        makeShiftableRow(xPoint + 44,yPoint+66,keyboardLayout.row4());


        addRenderableWidget(new Button(width / 2 + xPoint + 264, height / 2 +yPoint , 42, 20, new TextComponent("Back"), button -> {
            keyPressed(GLFW.GLFW_KEY_BACKSPACE,0,0);
        }));

        addRenderableWidget(new Button(width / 2 + xPoint + 242 , height / 2 +yPoint+66, 64, 20, new TextComponent("Space"), button -> {
            name.charTyped(' ',0);
            parent.charTyped(' ',0);
          //  parent.keyPressed(GLFW.GLFW_KEY_SPACE,0,0);
        }));

        addRenderableWidget(new Button(width / 2 + xPoint + 264 , height / 2 +yPoint +44, 42, 20, new TextComponent("Enter"), button -> {
            parent.keyPressed(GLFW.GLFW_KEY_ENTER,0,0);
            //minecraft.popGuiLayer();
        }));

        addRenderableWidget(new Button(width / 2 + xPoint  , height / 2 + yPoint+66, 42, 20, new TextComponent("Shift"), button -> {
            for (ShiftableButton shiftableButton : shiftableButtons) {
                shiftableButton.shift();
            }
        }));

        addRenderableWidget(new Button(width / 2 + xPoint+110  , height / 2 + yPoint-22, 86, 20, CommonComponents.GUI_DONE, button -> {
            ((SignEditScreenMixin)parent).done();
        }));
        }

        protected void makeShiftableRow(int xStart,int yStart,List<CharCharPair> chars) {
            for (int i = 0 ; i < chars.size();i++) {
                CharCharPair charCharPair = chars.get(i);
                char char0 = charCharPair.firstChar();
                char char1 = charCharPair.secondChar();
                ShiftableButton shiftableButton = new ShiftableButton(width / 2 +xStart + i * 22, height / 2 +yStart, 20, 20, new TextComponent(String.valueOf(char0)), button -> {
                    if (((ShiftableButton)button).shift) {
                        parent.charTyped(char1,0);
                        name.charTyped(char1, 0);
                    } else {
                        parent.charTyped(char0,0);
                        name.charTyped(char0,0);
                    }
                },new TextComponent(String.valueOf(char1)));
                shiftableButtons.add(shiftableButton);
                addRenderableWidget(shiftableButton);
            }
        }

    protected void initEditBox(int xPoint, int yPoint) {
        int x = width / 2 + xPoint;
        int y = height/2 + yPoint;
        this.name = new EditBox(this.font, x +100, y - 18, 120, 12, new TranslatableComponent("container.repair"));
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        name.setResponder(this::onChange);
        extraDetails();
        setInitialFocus(name);
    }

    protected abstract void extraDetails();

    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
    //    this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        RenderSystem.disableBlend();
        this.renderFg(pPoseStack, pMouseX, pMouseY, pPartialTick);
     //   this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    public void renderFg(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
       // this.name.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if (pKeyCode == GLFW.GLFW_KEY_ESCAPE) {
            onEscape();
            return true;
        } else {
            parent.keyPressed(pKeyCode,pScanCode,pModifiers);
            return this.name.keyPressed(pKeyCode, pScanCode, pModifiers) || this.name.canConsumeInput() || super.keyPressed(pKeyCode, pScanCode, pModifiers);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected void onEscape() {
        minecraft.popGuiLayer();
    }

    protected abstract void onChange(String s);

}
