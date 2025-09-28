package pie.ilikepiefoo.presetjs.mixin;

import dev.latvian.mods.kubejs.block.callbacks.BlockStateModifyCallbackJS;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockStateModifyCallbackJS.class)
public interface BlockStateModifyCallbackJSAccessor {
    @Accessor
    void setState(BlockState state);
}
