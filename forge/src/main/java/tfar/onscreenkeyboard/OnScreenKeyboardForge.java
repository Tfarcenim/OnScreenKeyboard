package tfar.onscreenkeyboard;

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
        
    }
}