package pie.ilikepiefoo.presetjs.block.custom;

import dev.latvian.mods.kubejs.block.KubeJSBlockProperties;
import dev.latvian.mods.kubejs.core.BlockKJS;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import pie.ilikepiefoo.presetjs.mixin.BlockBehaviourAccessor;
import pie.ilikepiefoo.presetjs.util.ConditionalProcedure;

import java.util.Collections;

public class BlockImplementations {

    ConditionalProcedure<BlockKJS, BlockGetName> GET_NAME =
        ConditionalProcedure.of(
            BlockKJS::kjs$getBlockBuilder,
            (builder) -> builder.displayName != null && builder.formattedDisplayName,
            blockBuilder -> () -> Component.literal("").append(blockBuilder.displayName),
            ConditionalProcedure.NullHandleStrategy.ON_NULL_FALLBACK_TO_DEFAULT
        );

    ConditionalProcedure<BlockKJS, BlockCreateBlockStateDefinition> CREATE_BLOCK_STATE_DEFINITION =
        ConditionalProcedure.of(
            (BlockKJS block) -> block instanceof BlockBehaviourAccessor accessor && accessor.presetjs$getProperties() instanceof KubeJSBlockProperties kp ? kp : null,
            properties -> (builder) -> {
                for (var property : properties.blockBuilder.blockStateProperties) {
                    builder.add(property);
                }
                properties.blockBuilder.blockStateProperties = Collections.unmodifiableSet(properties.blockBuilder.blockStateProperties);
            },
            ConditionalProcedure.NullHandleStrategy.ON_NULL_FALLBACK_TO_DEFAULT
        );



    @FunctionalInterface
    public interface BlockGetName {
        public MutableComponent getName();
    }

    @FunctionalInterface
    public interface BlockCreateBlockStateDefinition {
        public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder);
    }

    public class BasicBlockJS extends Block implements BlockKJS {
        public BasicBlockJS(Properties properties) {
            super(properties);
        }

        // Previous Implementation for reference:
//        @Override
//        public MutableComponent getName() {
//            if (blockBuilder.displayName != null && blockBuilder.formattedDisplayName) {
//                return Component.literal("").append(blockBuilder.displayName);
//            }
//
//            return super.getName();
//        }
        @Override
        public @NotNull MutableComponent getName() {
            return GET_NAME.build(this, super::getName).getName();
        }

        // Previous Implementation for reference:
//        @Override
//        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
//            if (properties instanceof KubeJSBlockProperties kp) {
//                for (var property : kp.blockBuilder.blockStateProperties) {
//                    builder.add(property);
//                }
//                kp.blockBuilder.blockStateProperties = Collections.unmodifiableSet(kp.blockBuilder.blockStateProperties);
//            }
//        }
        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            CREATE_BLOCK_STATE_DEFINITION.build(this, super::createBlockStateDefinition).createBlockStateDefinition(builder);
        }
    }
}
