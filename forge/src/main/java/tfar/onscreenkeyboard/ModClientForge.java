package tfar.onscreenkeyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import tfar.onscreenkeyboard.client.SignEditScreenKeyboardScreen;
import tfar.onscreenkeyboard.mixin.SignEditScreenMixin;
import tfar.onscreenkeyboard.platform.Services;

import java.util.function.Consumer;

public class ModClientForge {


    static void init(IEventBus bus) {
        MinecraftForge.EVENT_BUS.addListener(ModClientForge::screenInit);
        MinecraftForge.EVENT_BUS.addListener(ModClientForge::widgetClick);
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

    static void widgetClick(ScreenEvent.MouseClickedEvent.Post event) {
        Screen screen = event.getScreen();
        double mouseX = event.getMouseX();
        double mouseY = event.getMouseY();
        if (hasKeyboard(screen) && !isKeyboardVisible()) {
            if (selected != null && selected.isMouseOver(mouseX,mouseY) && selected.isEditable) {
                Minecraft.getInstance().pushGuiLayer(new EditBoxKeyboardScreen<>(new TextComponent(""),screen));
            }
        } else if (screen instanceof SignEditScreen signEditScreen) {
            Button done = (Button) signEditScreen.children().stream().filter(guiEventListener -> guiEventListener instanceof Button).findFirst().orElse(null);
            if (done != null && (mouseY+20) < done.y) {
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
