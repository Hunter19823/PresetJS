package pie.ilikepiefoo.presetjs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import pie.ilikepiefoo.presetjs.preset.Presets;

public class PresetJSKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("Presets", Presets.class);
    }
}
