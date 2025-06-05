package tfar.onscreenkeyboard;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(OnScreenKeyboard.MOD_ID)
public class OnScreenKeyboardForge {
    
    public OnScreenKeyboardForge() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        if (FMLEnvironment.dist.isClient()) {
            ModClientForge.init(bus);
        }
    
        // Use Forge to bootstrap the Common mod.
        OnScreenKeyboard.init();
        MinecraftForge.EVENT_BUS.addListener(this::anvil);
    }

    private void anvil(AnvilUpdateEvent event) {
        ItemStack right = event.getRight();
        ItemStack left = event.getLeft();
        ItemStack leftCopy = left.copy();
        if (right.is(Items.NAME_TAG)) {
            event.setOutput(leftCopy.setHoverName(right.getHoverName()));
        }
        event.setCost(1);
    }

}