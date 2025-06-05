package tfar.onscreenkeyboard.mixin;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Shadow;
import tfar.onscreenkeyboard.OnScreenKeyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
abstract class AnvilScreenMixin extends AbstractContainerScreen<AnvilMenu> {
    @Shadow private EditBox name;

    public AnvilScreenMixin(AnvilMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }


    @Inject(method = "slotChanged",at = @At("RETURN"))
    private void modifyName(AbstractContainerMenu pContainerToSend, int pSlotInd, ItemStack pStack, CallbackInfo ci) {
        ItemStack stack1 = getMenu().slots.get(1).getItem();
        if (!stack1.isEmpty()) {
            this.name.setValue(stack1.getHoverName().getString());
            this.name.setEditable(false);
            this.setFocused(this.name);
        }
    }
}