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

public class KeyboardScreen extends Screen {

    private final EditBox original;
    private EditBox name;

    protected KeyboardScreen(Component $$0,EditBox original) {
        super($$0);
        this.original = original;
    }

    @Override
    protected void init() {
        super.init();
        initEditBox();

        char[][] chars = new char[][]{
                new char[]{'1','2','3','4','5','6','7','8','9','0'},
                new char[]{'q','w','e','r','t','y','u','i','o','p'},
                new char[]{'a','s','d','f','g','h','j','k','l','-'},
                new char[]{'z','x','c','v','b','n','m','_','@','.'}
        };

        int offset = -109;

        for (int row = 0; row < chars.length; row++) {
            char[] charRow = chars[row];
            for (int column = 0; column < charRow.length;column++) {
                char cha = charRow[column];
                addRenderableWidget(new Button(width / 2 +offset + column * 22, height / 2 - 45 + row * 22, 20, 20, new TextComponent(String.valueOf(cha)), button -> name.charTyped(cha, 0)));
            }
        }



        addRenderableWidget(new Button(width / 2 + offset , height / 2 - 45 + chars.length * 22, 80, 20, new TextComponent("Backspace"), button -> {
            name.deleteChars(-1);
        }));

        addRenderableWidget(new Button(width / 2 + offset + 138 , height / 2 - 45 + chars.length * 22, 80, 20, new TextComponent("Space"), button -> {
            name.charTyped(' ',0);
        }));


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
