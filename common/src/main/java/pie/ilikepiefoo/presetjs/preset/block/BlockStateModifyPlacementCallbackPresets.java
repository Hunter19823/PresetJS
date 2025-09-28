package pie.ilikepiefoo.presetjs.preset.block;

import dev.latvian.mods.kubejs.block.callbacks.BlockStateModifyPlacementCallbackJS;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.material.Fluids;

public class BlockStateModifyPlacementCallbackPresets {

    /**
     * Represents the implementation within {@link FaceAttachedHorizontalDirectionalBlock#getStateForPlacement(BlockPlaceContext)}
     * @param callback The callback instance
     */
    public void faceAttachedHorizontalDirectionalBlock(BlockStateModifyPlacementCallbackJS callback) {
        for(Direction direction : callback.getNearestLookingDirections()) {
            var currentFace = callback.getValue(FaceAttachedHorizontalDirectionalBlock.FACE);
            var currentFacing = callback.getValue(HorizontalDirectionalBlock.FACING);
            if (direction.getAxis() == Direction.Axis.Y) {
                callback.setValue(FaceAttachedHorizontalDirectionalBlock.FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR).setValue(HorizontalDirectionalBlock.FACING, callback.getHorizontalDirection());
            } else {
                callback.setValue(FaceAttachedHorizontalDirectionalBlock.FACE, AttachFace.WALL).setValue(HorizontalDirectionalBlock.FACING, direction.getOpposite());
            }

            if (callback.getState().canSurvive(callback.getLevel(), callback.getClickedPos())) {
                return;
            } else {
                // Revert changes if it can't survive
                callback.setValue(FaceAttachedHorizontalDirectionalBlock.FACE, currentFace).setValue(HorizontalDirectionalBlock.FACING, currentFacing);
            }
        }
    }

    /**
     * Represents the implementation within {@link AbstractFurnaceBlock#getStateForPlacement(BlockPlaceContext)}
     * @param callback The callback instance
     */
    public void furnace(BlockStateModifyPlacementCallbackJS callback) {
        callback.setValue(AbstractFurnaceBlock.FACING, callback.getHorizontalDirection().getOpposite());
    }

    /**
     * Represents the implementation within {@link AmethystClusterBlock#getStateForPlacement(BlockPlaceContext)}
     * @param callback The callback instance
     */
    public void amethystCluster(BlockStateModifyPlacementCallbackJS callback) {
        callback
            .setValue(AmethystClusterBlock.WATERLOGGED, callback.getLevel().getFluidState(callback.getClickedPos()).getType() == Fluids.WATER)
            .setValue(AmethystClusterBlock.FACING, callback.getClickedFace());
    }


}
