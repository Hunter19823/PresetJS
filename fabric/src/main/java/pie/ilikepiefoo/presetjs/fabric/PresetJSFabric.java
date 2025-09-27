package pie.ilikepiefoo.presetjs.fabric;

import pie.ilikepiefoo.presetjs.PresetJS;
import net.fabricmc.api.ModInitializer;

public final class PresetJSFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        PresetJS.init();
    }

}
