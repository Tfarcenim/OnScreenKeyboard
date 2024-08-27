package tfar.onscreenkeyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import tfar.onscreenkeyboard.client.SignEditScreenKeyboardScreen;
import tfar.onscreenkeyboard.mixin.SignEditScreenMixin;
import tfar.onscreenkeyboard.platform.Services;

import java.util.function.Consumer;

public class ModClientForge {


    static void init(IEventBus bus) {
        MinecraftForge.EVENT_BUS.addListener(ModClientForge::screenInit);
        MinecraftForge.EVENT_BUS.addListener(ModClientForge::widgetClick);
        MinecraftForge.EVENT_BUS.addListener(ModClientForge::useNameTag);
    }

    static void useNameTag(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().is(Items.NAME_TAG) && event.getPlayer().level.isClientSide) {
            NameTagRenameScreen nameTagRenameScreen = new NameTagRenameScreen(new TextComponent("Edit NameTag"),event.getItemStack());
            Minecraft.getInstance().setScreen(nameTagRenameScreen);
            Minecraft.getInstance().pushGuiLayer(new EditBoxKeyboardScreen<>(new TextComponent(""),nameTagRenameScreen));
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    static EditBox selected;

    static void screenInit(ScreenEvent.InitScreenEvent.Post event) {
        Screen screen = event.getScreen();
        if (hasKeyboard(screen)  && !isKeyboardVisible()) {
            Consumer<Button> addButton = event::addListener;
            ModClient.addKeyboardButtons(screen, addButton);
            selected = (EditBox) event.getListenersList().stream().filter(guiEventListener -> guiEventListener instanceof EditBox).findFirst().orElse(null);
        }
    }

    static void widgetClick(ScreenEvent.MouseClickedEvent.Pre event) {
        Screen screen = event.getScreen();
        double mouseX = event.getMouseX();
        double mouseY = event.getMouseY();
        if (hasKeyboard(screen) && !isKeyboardVisible()) {
            if (selected != null && selected.isMouseOver(mouseX,mouseY) && selected.isEditable) {
                Minecraft.getInstance().pushGuiLayer(new EditBoxKeyboardScreen<>(new TextComponent(""),screen));
                event.setCanceled(true);
            }
        } else if (screen instanceof SignEditScreen signEditScreen) {
            Button done = (Button) signEditScreen.children().stream().filter(guiEventListener -> guiEventListener instanceof Button).findFirst().orElse(null);
            if (done != null && (mouseY+20) < done.y && mouseX > done.x && mouseX < done.x + done.getWidth()) {
                Minecraft.getInstance().pushGuiLayer(new SignEditScreenKeyboardScreen(new TextComponent(""), signEditScreen, ((SignEditScreenMixin) signEditScreen).getLine()));
            }
        }
    }

    public static boolean isKeyboardVisible() {
        return Minecraft.getInstance().screen instanceof BasicKeyboardScreen<?>;
    }

    public static boolean hasKeyboard(Screen screen) {
        return Services.PLATFORM.getConfig().allowed().contains(screen.getClass());
    }

}
