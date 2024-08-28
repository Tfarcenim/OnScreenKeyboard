package tfar.onscreenkeyboard;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import tfar.onscreenkeyboard.network.C2SButtonPacket;

public class NameTagRenameScreen extends Screen {

    protected EditBox name;
    ItemStack stack;


    protected NameTagRenameScreen(Component $$0,ItemStack stack) {
        super($$0);
        this.stack = stack;
    }

    @Override
    protected void init() {
        super.init();
        initEditBox(-152,0);
        KeyboardHelper.addKeyboardDirectly(this,-152,0, this::addRenderableWidget,name);
    }

    protected void initEditBox(int xPoint, int yPoint) {
        int x = width / 2 + xPoint;
        int y = height/2 + yPoint;
        this.name = new EditBox(this.font, x +100, y - 18, 120, 12, new TranslatableComponent("container.repair"));
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setMaxLength(35);
        this.name.setValue(stack.getHoverName().getString());
        addRenderableWidget(name);

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
        if (pKeyCode == GLFW.GLFW_KEY_ENTER) {
            C2SButtonPacket.send(name.getValue());
            minecraft.setScreen(null);
            return true;
        } else if (pKeyCode == GLFW.GLFW_KEY_ESCAPE) {
            minecraft.setScreen(null);
            return true;
        } else {
            return this.name.keyPressed(pKeyCode, pScanCode, pModifiers) || this.name.canConsumeInput() || super.keyPressed(pKeyCode, pScanCode, pModifiers);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
