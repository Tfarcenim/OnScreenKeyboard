package tfar.onscreenkeyboard.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;

import java.util.Objects;
import java.util.stream.IntStream;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.Material;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;
import tfar.onscreenkeyboard.KeyboardHelper;

@OnlyIn(Dist.CLIENT)
public class CustomSignEditScreen extends Screen {
   /** Reference to the sign object. */
   private final SignBlockEntity sign;
   /** Counts the number of screen updates. */
   private int frame;
   /** The index of the line that is being edited. */
   public int line;
   public TextFieldHelper signField;
   private WoodType woodType;
   private SignRenderer.SignModel signModel;
   private final String[] messages;

   public CustomSignEditScreen(SignBlockEntity pSign, boolean pIsTextFilteringEnabled) {
      super(new TranslatableComponent("sign.edit"));
      this.messages = IntStream.range(0, 4).mapToObj((p_169818_) -> {
         return pSign.getMessage(p_169818_, pIsTextFilteringEnabled);
      }).map(Component::getString).toArray((p_169814_) -> {
         return new String[p_169814_];
      });
      this.sign = pSign;
   }

   protected void init() {
      this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
      int w = 150;
      this.addRenderableWidget(new Button(this.width / 2 - w/2, this.height / 2, w, 20, CommonComponents.GUI_DONE, (p_169820_) -> {
         this.onDone();
      }));
      this.sign.setEditable(false);
      this.signField = new TextFieldHelper(() -> {
         return this.messages[this.line];
      }, (p_169824_) -> {
         this.messages[this.line] = p_169824_;
         this.sign.setMessage(this.line, new TextComponent(p_169824_));
      }, TextFieldHelper.createClipboardGetter(this.minecraft), TextFieldHelper.createClipboardSetter(this.minecraft), (p_169822_) -> {
         return this.minecraft.font.width(p_169822_) <= 90;
      });
      BlockState blockstate = this.sign.getBlockState();
      this.woodType = SignRenderer.getWoodType(blockstate.getBlock());
      this.signModel = SignRenderer.createSignModel(this.minecraft.getEntityModels(), this.woodType);

      KeyboardHelper.addKeyboardDirectly(this,-152,20, this::addRenderableWidget,signField);

   }

   public void removed() {
      this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
      ClientPacketListener clientpacketlistener = this.minecraft.getConnection();
      if (clientpacketlistener != null) {
         clientpacketlistener.send(new ServerboundSignUpdatePacket(this.sign.getBlockPos(), this.messages[0], this.messages[1], this.messages[2], this.messages[3]));
      }

      this.sign.setEditable(true);
   }

   public void tick() {
      ++this.frame;
      if (!this.sign.getType().isValid(this.sign.getBlockState())) {
         this.onDone();
      }

   }

   private void onDone() {
      this.sign.setChanged();
      this.minecraft.setScreen(null);
   }

   public boolean charTyped(char pCodePoint, int pModifiers) {
      this.signField.charTyped(pCodePoint);
      return true;
   }

   public void onClose() {
      this.onDone();
   }

   public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
      if (pKeyCode == GLFW.GLFW_KEY_UP) {
         this.line = this.line - 1 & 3;
         this.signField.setCursorToEnd();
         return true;
      } else if (pKeyCode != 264 && pKeyCode != 257 && pKeyCode != 335) {
         return this.signField.keyPressed(pKeyCode) || super.keyPressed(pKeyCode, pScanCode, pModifiers);
      } else {
         this.line = this.line + 1 & 3;
         this.signField.setCursorToEnd();
         return true;
      }
   }

   public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
      Lighting.setupForFlatItems();
      //this.renderBackground(pPoseStack);
      renderDirtBackground(0);
      drawCenteredString(pPoseStack, this.font, this.title, this.width / 2, 40, 16777215);
      pPoseStack.pushPose();
      pPoseStack.translate((double)(this.width / 2), 0.0, 50.0);
      float $$4 = 93.75F;
      pPoseStack.scale(93.75F, -93.75F, 93.75F);
      pPoseStack.translate(0.0, -1.3125, 0.0);
      BlockState $$5 = this.sign.getBlockState();
      boolean $$6 = false;//$$5.getBlock() instanceof StandingSignBlock;
      if (!$$6) {
         pPoseStack.translate(0.0, -0.3125, 0.0);
      }

      pPoseStack.translate(0.0, 0.3125, 0.0);

      boolean $$7 = this.frame / 6 % 2 == 0;
      float $$8 = 0.6666667F;
      pPoseStack.pushPose();
      pPoseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
      MultiBufferSource.BufferSource $$9 = this.minecraft.renderBuffers().bufferSource();
      Material $$10 = Sheets.getSignMaterial(this.woodType);
      SignRenderer.SignModel var10002 = this.signModel;
      Objects.requireNonNull(var10002);
      VertexConsumer $$11 = $$10.buffer($$9, var10002::renderType);
      this.signModel.stick.visible = $$6;
      this.signModel.root.render(pPoseStack, $$11, 15728880, OverlayTexture.NO_OVERLAY);
      pPoseStack.popPose();
      float $$12 = 0.010416667F;
      pPoseStack.translate(0.0, 0.3333333432674408, 0.046666666865348816);
      pPoseStack.scale(0.010416667F, -0.010416667F, 0.010416667F);
      int $$13 = this.sign.getColor().getTextColor();
      int $$14 = this.signField.getCursorPos();
      int $$15 = this.signField.getSelectionPos();
      int $$16 = this.line * 10 - this.messages.length * 5;
      Matrix4f $$17 = pPoseStack.last().pose();

      int $$18;
      String $$24;
      int $$26;
      int $$27;
      for($$18 = 0; $$18 < this.messages.length; ++$$18) {
         $$24 = this.messages[$$18];
         if ($$24 != null) {
            if (this.font.isBidirectional()) {
               $$24 = this.font.bidirectionalShaping($$24);
            }

            float $$20 = (float)(-this.minecraft.font.width($$24) / 2);
            this.minecraft.font.drawInBatch($$24, $$20, (float)($$18 * 10 - this.messages.length * 5), $$13, false, $$17, $$9, false, 0, 15728880, false);
            if ($$18 == this.line && $$14 >= 0 && $$7) {
               $$26 = this.minecraft.font.width($$24.substring(0, Math.max(Math.min($$14, $$24.length()), 0)));
               $$27 = $$26 - this.minecraft.font.width($$24) / 2;
               if ($$14 >= $$24.length()) {
                  this.minecraft.font.drawInBatch("_", (float)$$27, (float)$$16, $$13, false, $$17, $$9, false, 0, 15728880, false);
               }
            }
         }
      }

      $$9.endBatch();

      for($$18 = 0; $$18 < this.messages.length; ++$$18) {
         $$24 = this.messages[$$18];
         if ($$24 != null && $$18 == this.line && $$14 >= 0) {
            int $$25 = this.minecraft.font.width($$24.substring(0, Math.max(Math.min($$14, $$24.length()), 0)));
            $$26 = $$25 - this.minecraft.font.width($$24) / 2;
            if ($$7 && $$14 < $$24.length()) {
               int var31 = $$16 - 1;
               int var10003 = $$26 + 1;
               Objects.requireNonNull(this.minecraft.font);
               fill(pPoseStack, $$26, var31, var10003, $$16 + 9, -16777216 | $$13);
            }

            if ($$15 != $$14) {
               $$27 = Math.min($$14, $$15);
               int $$28 = Math.max($$14, $$15);
               int $$29 = this.minecraft.font.width($$24.substring(0, $$27)) - this.minecraft.font.width($$24) / 2;
               int $$30 = this.minecraft.font.width($$24.substring(0, $$28)) - this.minecraft.font.width($$24) / 2;
               int $$31 = Math.min($$29, $$30);
               int $$32 = Math.max($$29, $$30);
               Tesselator $$33 = Tesselator.getInstance();
               BufferBuilder $$34 = $$33.getBuilder();
               RenderSystem.setShader(GameRenderer::getPositionColorShader);
               RenderSystem.disableTexture();
               RenderSystem.enableColorLogicOp();
               RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
               $$34.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
               float var32 = (float)$$31;
               Objects.requireNonNull(this.minecraft.font);
               $$34.vertex($$17, var32, (float)($$16 + 9), 0.0F).color(0, 0, 255, 255).endVertex();
               var32 = (float)$$32;
               Objects.requireNonNull(this.minecraft.font);
               $$34.vertex($$17, var32, (float)($$16 + 9), 0.0F).color(0, 0, 255, 255).endVertex();
               $$34.vertex($$17, (float)$$32, (float)$$16, 0.0F).color(0, 0, 255, 255).endVertex();
               $$34.vertex($$17, (float)$$31, (float)$$16, 0.0F).color(0, 0, 255, 255).endVertex();
               $$34.end();
               BufferUploader.end($$34);
               RenderSystem.disableColorLogicOp();
               RenderSystem.enableTexture();
            }
         }
      }

      pPoseStack.popPose();
      Lighting.setupFor3DItems();
      super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
   }
}