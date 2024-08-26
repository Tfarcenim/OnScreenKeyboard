package tfar.onscreenkeyboard;

import net.minecraftforge.fml.common.Mod;

@Mod(OnScreenKeyboard.MOD_ID)
public class OnScreenKeyboardForge {
    
    public OnScreenKeyboardForge() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        OnScreenKeyboard.LOG.info("Hello Forge world!");
        OnScreenKeyboard.init();
        
    }
}