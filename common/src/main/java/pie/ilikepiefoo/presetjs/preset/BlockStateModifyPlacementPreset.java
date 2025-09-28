package pie.ilikepiefoo.presetjs.preset;

import dev.latvian.mods.kubejs.block.callbacks.BlockStateModifyPlacementCallbackJS;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import pie.ilikepiefoo.presetjs.mixin.BlockStateModifyCallbackJSAccessor;

@FunctionalInterface
public interface BlockStateModifyPlacementPreset {

    public void onBlockStateModifyPlacementCallbackJS(BlockStateModifyPlacementCallbackJS callbackJS);


    default void setStateToNull(BlockStateModifyPlacementCallbackJS callback) {
        overrideState(callback, null);
    }

    default void overrideState(BlockStateModifyPlacementCallbackJS callback, BlockState newState) {
        ((BlockStateModifyCallbackJSAccessor) callback).presetJS$setState(newState);
    }

    /**
     * A common pattern for blocks that face opposite the player when placed.
     * @param callback The callback instance
     * @param facingProperty The facing property to set
     */
    default void facingOppositeOfHorizontalDirection(BlockStateModifyPlacementCallbackJS callback, DirectionProperty facingProperty) {
        overrideState(callback, callback.minecraftBlock.defaultBlockState().setValue(facingProperty, callback.getHorizontalDirection().getOpposite()));
    }

    /**
     * A common pattern for blocks that can be waterlogged.
     * @param callback The callback instance
     * @param waterloggedProperty The waterlogged property to set
     */
    default void waterLogged(BlockStateModifyPlacementCallbackJS callback, BooleanProperty waterloggedProperty) {
        overrideState(callback, callback.minecraftBlock.defaultBlockState().setValue(waterloggedProperty, callback.isInWater()));
    }

    /**
     * A common pattern for blocks that face the direction the player clicked when placed.
     * @param callback The callback instance
     * @param facingProperty The facing property to set
     */
    default void clickedFace(BlockStateModifyPlacementCallbackJS callback, DirectionProperty facingProperty) {
        overrideState(callback, callback.minecraftBlock.defaultBlockState().setValue(facingProperty, callback.getClickedFace()));
    }

    /**
     * A common pattern for blocks that face the opposite of the nearest looking direction when placed.
     * @param callback The callback instance
     * @param facingProperty The facing property to set
     */
    default void oppositeNearestLookingDirection(BlockStateModifyPlacementCallbackJS callback, DirectionProperty facingProperty) {
        overrideState(callback, callback.minecraftBlock.defaultBlockState().setValue(facingProperty, callback.getNearestLookingDirection().getOpposite()));
    }

    /**
     * A common pattern for blocks that face the direction the player clicked when placed, but also waterloggable.
     * @param callback The callback instance
     * @param facingProperty The facing property to set
     * @param waterloggedProperty The waterlogged property to set
     */
    default void clickedFaceAndWaterLogged(BlockStateModifyPlacementCallbackJS callback, DirectionProperty facingProperty, BooleanProperty waterloggedProperty) {
        overrideState(
            callback,
            callback
                .minecraftBlock
                .defaultBlockState()
                .setValue(facingProperty, callback.getClickedFace())
                .setValue(waterloggedProperty, callback.isInWater())
        );
    }
}
