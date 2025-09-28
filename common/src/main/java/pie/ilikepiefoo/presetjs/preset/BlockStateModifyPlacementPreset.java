package pie.ilikepiefoo.presetjs.preset;

import dev.latvian.mods.kubejs.block.callbacks.BlockStateModifyPlacementCallbackJS;

@FunctionalInterface
public interface BlockStateModifyPlacementPreset {

    public void onBlockStateModifyPlacementCallbackJS(BlockStateModifyPlacementCallbackJS callbackJS);
}
