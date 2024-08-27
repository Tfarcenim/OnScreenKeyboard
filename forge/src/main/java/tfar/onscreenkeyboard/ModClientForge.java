package tfar.onscreenkeyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.function.Consumer;

public class ModClientForge {

    static final Class<AnvilScreen> screenClass = AnvilScreen.class;

    static void init(IEventBus bus) {
        MinecraftForge.EVENT_BUS.addListener(ModClientForge::screenInit);
        MinecraftForge.EVENT_BUS.addListener(ModClientForge::widgetClick);
    }

    static EditBox selected;

    static void screenInit(ScreenEvent.InitScreenEvent.Post event) {
        Screen screen = event.getScreen();
        if (screenClass == screen.getClass()) {
            Consumer<Button> addButton = event::addListener;
            ModClient.addKeyboardButtons(screen, addButton);
            selected = (EditBox) event.getListenersList().stream().filter(guiEventListener -> guiEventListener instanceof EditBox).findFirst().orElse(null);
        }
    }

    static void widgetClick(ScreenEvent.MouseClickedEvent.Post event) {
        Screen screen = event.getScreen();
        if (screen.getClass() == ModClientForge.screenClass) {
            if (selected != null && selected.isMouseOver(event.getMouseX(),event.getMouseY()) && selected.isEditable) {
                Minecraft.getInstance().pushGuiLayer(new KeyboardScreen(new TextComponent(""),selected));
            }
        }
    }
}
