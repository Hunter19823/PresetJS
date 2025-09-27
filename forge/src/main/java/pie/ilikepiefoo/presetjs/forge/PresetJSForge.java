package pie.ilikepiefoo.presetjs.forge;

import pie.ilikepiefoo.presetjs.PresetJS;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(PresetJS.MOD_ID)
public final class PresetJSForge {

    public PresetJSForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(PresetJS.MOD_ID,
            FMLJavaModLoadingContext
                .get()
                .getModEventBus()
        );

        // Run our common setup.
        PresetJS.init();
    }

}
