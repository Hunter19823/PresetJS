package pie.ilikepiefoo.presetjs.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@FunctionalInterface
@Mixin(BlockBehaviour.class)
public interface BlockBehaviourAccessor {
    @Accessor("properties")
    BlockBehaviour.Properties presetjs$getProperties();
}
