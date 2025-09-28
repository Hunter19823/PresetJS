package pie.ilikepiefoo.presetjs.preset.block;

import dev.latvian.mods.kubejs.block.callbacks.BlockStateModifyPlacementCallbackJS;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CalibratedSculkSensorBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DetectorRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.StairsShape;
import pie.ilikepiefoo.presetjs.mixin.BlockStateModifyCallbackJSAccessor;

import static net.minecraft.world.level.block.Block.isExceptionForConnection;
import static net.minecraft.world.level.block.StairBlock.isStairs;

public class BlockStateModifyPlacementCallbackPresets {

    private void setStateToNull(BlockStateModifyPlacementCallbackJS callback) {
        ((BlockStateModifyCallbackJSAccessor) callback).setState(null);
    }

    private void overrideState(BlockStateModifyPlacementCallbackJS callback, BlockState newState) {
        ((BlockStateModifyCallbackJSAccessor) callback).setState(newState);
    }

    /**
     * Represents the implementation within {@link FaceAttachedHorizontalDirectionalBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * @Nullable
     * public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
     *     for(Direction direction : blockPlaceContext.getNearestLookingDirections()) {
     *         BlockState blockState;
     *         if (direction.getAxis() == Axis.Y) {
     *             blockState = (BlockState)((BlockState)this.defaultBlockState().setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)).setValue(FACING, blockPlaceContext.getHorizontalDirection());
     *         } else {
     *             blockState = (BlockState)((BlockState)this.defaultBlockState().setValue(FACE, AttachFace.WALL)).setValue(FACING, direction.getOpposite());
     *         }
     *
     *         if (blockState.canSurvive(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos())) {
     *             return blockState;
     *         }
     *     }
     *
     *     return null;
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void faceAttachedHorizontalDirectionalBlock(BlockStateModifyPlacementCallbackJS callback) {
        for (Direction direction : callback.getNearestLookingDirections()) {
            var blockState = callback.minecraftBlock.defaultBlockState();
            if (direction.getAxis() == Direction.Axis.Y) {
                blockState = blockState
                    .setValue(FaceAttachedHorizontalDirectionalBlock.FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
                    .setValue(HorizontalDirectionalBlock.FACING, callback.getHorizontalDirection());
            } else {
                blockState = blockState
                    .setValue(FaceAttachedHorizontalDirectionalBlock.FACE, AttachFace.WALL)
                    .setValue(HorizontalDirectionalBlock.FACING, direction.getOpposite());
            }

            if (blockState.canSurvive(callback.getLevel(), callback.getClickedPos())) {
                overrideState(callback, blockState);
                return;
            }
        }
        setStateToNull(callback); // If no valid placement found, set state to null
    }

    /**
     * Represents the implementation within {@link AbstractFurnaceBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
     *     return (BlockState)this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void furnace(BlockStateModifyPlacementCallbackJS callback) {
        overrideState(callback, callback.minecraftBlock.defaultBlockState().setValue(AbstractFurnaceBlock.FACING, callback.getHorizontalDirection().getOpposite()));
    }

    /**
     * Represents the implementation within {@link AmethystClusterBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * @Nullable
     * public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
     *     LevelAccessor levelAccessor = blockPlaceContext.getLevel();
     *     BlockPos blockPos = blockPlaceContext.getClickedPos();
     *     return (BlockState)((BlockState)this.defaultBlockState().setValue(WATERLOGGED, levelAccessor.getFluidState(blockPos).getType() == Fluids.WATER)).setValue(FACING, blockPlaceContext.getClickedFace());
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void amethystCluster(BlockStateModifyPlacementCallbackJS callback) {
        overrideState(
            callback,
                callback
                    .minecraftBlock
                    .defaultBlockState()
                    .setValue(AmethystClusterBlock.WATERLOGGED, callback.isInWater())
                    .setValue(AmethystClusterBlock.FACING, callback.getClickedFace())
        );
    }

    /**
     * Represents the implementation within {@link SculkSensorBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * @Nullable
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     BlockPos blockpos = arg.getClickedPos();
     *     FluidState fluidstate = arg.getLevel().getFluidState(blockpos);
     *     return (BlockState)this.defaultBlockState().setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void sculkSensor(BlockStateModifyPlacementCallbackJS callback) {
        overrideState(
            callback,
            callback
                .minecraftBlock
                .defaultBlockState()
                .setValue(SculkSensorBlock.WATERLOGGED, callback.isInWater())
        );
    }

    /**
     * Represents the implementation within {@link CalibratedSculkSensorBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * @Nullable
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     return (BlockState)super.getStateForPlacement(arg).setValue(FACING, arg.getHorizontalDirection());
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void calibratedSculkSensor(BlockStateModifyPlacementCallbackJS callback) {
        sculkSensor(callback); // Call the parent method to set WATERLOGGED
        callback.setValue(CalibratedSculkSensorBlock.FACING, callback.getHorizontalDirection());
    }

    /**
     * Represents the implementation within {@link CampfireBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * @Nullable
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     LevelAccessor levelaccessor = arg.getLevel();
     *     BlockPos blockpos = arg.getClickedPos();
     *     boolean flag = levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER;
     *     return (BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(WATERLOGGED, flag)).setValue(SIGNAL_FIRE, this.isSmokeSource(levelaccessor.getBlockState(blockpos.below())))).setValue(LIT, !flag)).setValue(FACING, arg.getHorizontalDirection());
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void campfire(BlockStateModifyPlacementCallbackJS callback) {
        boolean waterlogged = callback.isInWater();
        boolean signalFire = isSmokeSource(callback.getLevel().getBlockState(callback.getClickedPos().below()));

        overrideState(
            callback,
            callback
                .minecraftBlock
                .defaultBlockState()
                .setValue(CampfireBlock.WATERLOGGED, waterlogged)
                .setValue(CampfireBlock.SIGNAL_FIRE, signalFire)
                .setValue(CampfireBlock.LIT, !waterlogged)
                .setValue(CampfireBlock.FACING, callback.getHorizontalDirection())
        );
    }

    /**
     * Helper method to check if a block is a smoke source (used by campfire)
     *
     * @param blockState The block state to check
     * @return true if the block is a smoke source
     */
    private boolean isSmokeSource(BlockState blockState) {
        return blockState.is(Blocks.HAY_BLOCK);
    }

    /**
     * Represents the implementation within {@link BarrelBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     return (BlockState)this.defaultBlockState().setValue(FACING, arg.getNearestLookingDirection().getOpposite());
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void barrel(BlockStateModifyPlacementCallbackJS callback) {
        overrideState(
            callback,
            callback
                .minecraftBlock
                .defaultBlockState()
                .setValue(BarrelBlock.FACING, callback.getNearestLookingDirection().getOpposite())
        );
    }

    /**
     * Represents the implementation within {@link DispenserBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     return (BlockState)this.defaultBlockState().setValue(FACING, arg.getNearestLookingDirection().getOpposite());
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void dispenser(BlockStateModifyPlacementCallbackJS callback) {
        overrideState(
            callback,
            callback
                .minecraftBlock
                .defaultBlockState()
                .setValue(DispenserBlock.FACING, callback.getNearestLookingDirection().getOpposite())
        );
    }

    /**
     * Represents the implementation within {@link BaseRailBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     FluidState fluidstate = arg.getLevel().getFluidState(arg.getClickedPos());
     *     boolean flag = fluidstate.getType() == Fluids.WATER;
     *     BlockState blockstate = super.defaultBlockState();
     *     Direction direction = arg.getHorizontalDirection();
     *     boolean flag1 = direction == Direction.EAST || direction == Direction.WEST;
     *     return (BlockState)((BlockState)blockstate.setValue(this.getShapeProperty(), flag1 ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH)).setValue(WATERLOGGED, flag);
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void baseRail(BlockStateModifyPlacementCallbackJS callback, EnumProperty<RailShape> shapeProperty) {
        boolean waterlogged = callback.isInWater();
        var direction = callback.getHorizontalDirection();
        boolean flag1 = direction == Direction.EAST || direction == Direction.WEST;

        overrideState(
            callback,
            callback
                .minecraftBlock
                .defaultBlockState()
                .setValue(shapeProperty, flag1 ? RailShape.EAST_WEST : RailShape.NORTH_SOUTH)
                .setValue(BaseRailBlock.WATERLOGGED, waterlogged)
        );
    }

    /**
     * Represents the implementation within {@link DetectorRailBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code inherited from {@link BaseRailBlock#getStateForPlacement(BlockPlaceContext)}
     *
     * @param callback The callback instance
     */
    public void detectorRail(BlockStateModifyPlacementCallbackJS callback) {
        baseRail(callback, DetectorRailBlock.SHAPE);
    }



    /**
     * Represents the implementation within {@link PoweredRailBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code inherited from {@link BaseRailBlock#getStateForPlacement(BlockPlaceContext)}
     *
     * @param callback The callback instance
     */
    public void poweredRail(BlockStateModifyPlacementCallbackJS callback) {
        baseRail(callback, PoweredRailBlock.SHAPE);
    }

    /**
     * Represents the implementation within {@link RailBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code inherited from {@link BaseRailBlock#getStateForPlacement(BlockPlaceContext)}
     *
     * @param callback The callback instance
     */
    public void rail(BlockStateModifyPlacementCallbackJS callback) {
        baseRail(callback, RailBlock.SHAPE);
    }


    /**
     * Represents the implementation within {@link ChestBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     ChestType chesttype = ChestType.SINGLE;
     *     Direction direction = arg.getHorizontalDirection().getOpposite();
     *     FluidState fluidstate = arg.getLevel().getFluidState(arg.getClickedPos());
     *     boolean flag = arg.isSecondaryUseActive();
     *     Direction direction1 = arg.getClickedFace();
     *     if (direction1.getAxis().isHorizontal() && flag) {
     *         Direction direction2 = this.candidatePartnerFacing(arg, direction1.getOpposite());
     *         if (direction2 != null && direction2.getAxis() != direction1.getAxis()) {
     *             direction = direction2;
     *             chesttype = direction2.getCounterClockWise() == direction1.getOpposite() ? ChestType.RIGHT : ChestType.LEFT;
     *         }
     *     }
     *     if (chesttype == ChestType.SINGLE && !flag) {
     *         if (direction == this.candidatePartnerFacing(arg, direction.getClockWise())) {
     *             chesttype = ChestType.LEFT;
     *         } else if (direction == this.candidatePartnerFacing(arg, direction.getCounterClockWise())) {
     *             chesttype = ChestType.RIGHT;
     *         }
     *     }
     *     return (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, direction)).setValue(TYPE, chesttype)).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void chest(BlockStateModifyPlacementCallbackJS callback) {
        ChestType chestType = ChestType.SINGLE;
        Direction direction = callback.getHorizontalDirection().getOpposite();
        boolean isSecondaryUse = callback.isSecondaryUseActive();
        Direction clickedFace = callback.getClickedFace();

        if (clickedFace.getAxis().isHorizontal() && isSecondaryUse) {
            Direction partnerFacing = candidatePartnerFacing(callback, clickedFace.getOpposite());
            if (partnerFacing != null && partnerFacing.getAxis() != clickedFace.getAxis()) {
                direction = partnerFacing;
                chestType = partnerFacing.getCounterClockWise() == clickedFace.getOpposite() ? ChestType.RIGHT : ChestType.LEFT;
            }
        }

        if (chestType == ChestType.SINGLE && !isSecondaryUse) {
            if (direction == candidatePartnerFacing(callback, direction.getClockWise())) {
                chestType = ChestType.LEFT;
            } else if (direction == candidatePartnerFacing(callback, direction.getCounterClockWise())) {
                chestType = ChestType.RIGHT;
            }
        }

        overrideState(
            callback,
            callback
                .minecraftBlock
                .defaultBlockState()
                .setValue(ChestBlock.FACING, direction)
                .setValue(ChestBlock.TYPE, chestType)
                .setValue(ChestBlock.WATERLOGGED, callback.isInWater())
        );
    }

    /**
     * Helper method to find candidate partner facing for chest placement
     *
     * @param callback  The callback instance
     * @param direction The direction to check
     * @return The candidate partner facing direction
     */
    private Direction candidatePartnerFacing(BlockStateModifyPlacementCallbackJS callback, Direction direction) {
        BlockState blockState = callback.getLevel().getBlockState(callback.getClickedPos().relative(direction));
        return blockState.is(callback.getState().getBlock()) && blockState.getValue(ChestBlock.TYPE) == ChestType.SINGLE ?
            blockState.getValue(ChestBlock.FACING) : null;
    }

    /**
     * Represents the implementation within {@link DoorBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * @Nullable
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     BlockPos blockPos = arg.getClickedPos();
     *     Level level = arg.getLevel();
     *     if (blockPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockPos.above()).canBeReplaced(arg)) {
     *         boolean bl = level.hasNeighborSignal(blockPos) || level.hasNeighborSignal(blockPos.above());
     *         return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, arg.getHorizontalDirection())).setValue(HINGE, this.getHinge(arg))).setValue(POWERED, bl)).setValue(OPEN, bl)).setValue(HALF, DoubleBlockHalf.LOWER);
     *     } else {
     *         return null;
     *     }
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void door(BlockStateModifyPlacementCallbackJS callback) {
        if (callback.getClickedPos().getY() < callback.getLevel().getMaxBuildHeight() - 1 &&
            callback.getLevel().getBlockState(callback.getClickedPos().above()).canBeReplaced(callback.context)) {

            boolean powered = callback.getLevel().hasNeighborSignal(callback.getClickedPos()) ||
                callback.getLevel().hasNeighborSignal(callback.getClickedPos().above());

            overrideState(
                callback,
                callback
                    .minecraftBlock
                    .defaultBlockState()
                    .setValue(DoorBlock.FACING, callback.getHorizontalDirection())
                    .setValue(DoorBlock.HINGE, getHinge(callback))
                    .setValue(DoorBlock.POWERED, powered)
                    .setValue(DoorBlock.OPEN, powered)
                    .setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER)
            );
        } else {
            setStateToNull(callback); 
        }
    }

    /**
     * Helper method to determine door hinge side
     *
     * @param callback The callback instance
     * @return The hinge side
     */
    private DoorHingeSide getHinge(BlockStateModifyPlacementCallbackJS callback) {
        // This is a simplified version of the complex hinge determination logic
        // The full implementation would need access to the block getter and click location
        return DoorHingeSide.LEFT; // Simplified for now
    }

    /**
     * Represents the implementation within {@link SlabBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * @Nullable
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     BlockPos blockPos = arg.getClickedPos();
     *     BlockState blockState = arg.getLevel().getBlockState(blockPos);
     *     if (blockState.is(this)) {
     *         return (BlockState)((BlockState)blockState.setValue(TYPE, SlabType.DOUBLE)).setValue(WATERLOGGED, false);
     *     } else {
     *         FluidState fluidState = arg.getLevel().getFluidState(blockPos);
     *         BlockState blockState2 = (BlockState)((BlockState)this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM)).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
     *         Direction direction = arg.getClickedFace();
     *         return direction != Direction.DOWN && (direction == Direction.UP || !(arg.getClickLocation().y - (double)blockPos.getY() > (double)0.5F)) ? blockState2 : (BlockState)blockState2.setValue(TYPE, SlabType.TOP);
     *     }
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void slab(BlockStateModifyPlacementCallbackJS callback) {
        BlockState existingState = callback.getLevel().getBlockState(callback.getClickedPos());

        // Check if we're placing on the same slab type to create a double slab
        if (existingState.is(callback.getState().getBlock())) {
            overrideState(
                callback,
                callback
                    .minecraftBlock
                    .defaultBlockState()
                    .setValue(SlabBlock.TYPE, SlabType.DOUBLE)
                    .setValue(SlabBlock.WATERLOGGED, false)
            );
        } else {
            boolean waterlogged = callback.isInWater();
            Direction clickedFace = callback.getClickedFace();

            SlabType slabType = SlabType.BOTTOM;
            if (clickedFace != Direction.DOWN &&
                (clickedFace == Direction.UP ||
                    !(callback.getClickLocation().y - (double) callback.getClickedPos().getY() > 0.5F))) {
                slabType = SlabType.TOP;
            }

            overrideState(
                callback,
                callback
                    .minecraftBlock
                    .defaultBlockState()
                    .setValue(SlabBlock.TYPE, slabType)
                    .setValue(SlabBlock.WATERLOGGED, waterlogged)
            );
        }
    }

    /**
     * Represents the implementation within {@link ObserverBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     return (BlockState)this.defaultBlockState().setValue(FACING, arg.getNearestLookingDirection().getOpposite().getOpposite());
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void observer(BlockStateModifyPlacementCallbackJS callback) {
        overrideState(
            callback,
            callback
                .minecraftBlock
                .defaultBlockState()
                .setValue(ObserverBlock.FACING, callback.getNearestLookingDirection().getOpposite().getOpposite())
        );
    }

    /**
     * Represents the implementation within {@link StairBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     Direction direction = arg.getClickedFace();
     *     BlockPos blockpos = arg.getClickedPos();
     *     FluidState fluidstate = arg.getLevel().getFluidState(blockpos);
     *     BlockState blockstate = (BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, arg.getHorizontalDirection())).setValue(HALF, direction == Direction.DOWN || direction != Direction.UP && arg.getClickLocation().y - (double)blockpos.getY() > (double)0.5F ? Half.TOP : Half.BOTTOM)).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
     *     return (BlockState)blockstate.setValue(SHAPE, getStairsShape(blockstate, arg.getLevel(), blockpos));
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void stairs(BlockStateModifyPlacementCallbackJS callback) {
        Direction clickedFace = callback.getClickedFace();
        boolean waterlogged = callback.isInWater();

        Half half = Half.BOTTOM;
        if (clickedFace == Direction.DOWN ||
            (clickedFace != Direction.UP &&
                callback.getClickLocation().y - (double) callback.getClickedPos().getY() > 0.5F)) {
            half = Half.TOP;
        }

        overrideState(
            callback,
            callback
                .minecraftBlock
                .defaultBlockState()
                .setValue(StairBlock.FACING, callback.getHorizontalDirection())
                .setValue(StairBlock.HALF, half)
                .setValue(StairBlock.WATERLOGGED, waterlogged)
                .setValue(StairBlock.SHAPE, getStairsShape(callback))
        );

    }

    /**
     * Helper method that mirrors the getStairsShape logic from Minecraft
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * private static StairsShape getStairsShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
     *     Direction direction = (Direction)blockState.getValue(FACING);
     *     BlockState blockState2 = blockGetter.getBlockState(blockPos.relative(direction));
     *     if (isStairs(blockState2) && blockState.getValue(HALF) == blockState2.getValue(HALF)) {
     *         Direction direction2 = (Direction)blockState2.getValue(FACING);
     *         if (direction2.getAxis() != ((Direction)blockState.getValue(FACING)).getAxis() && canTakeShape(blockState, blockGetter, blockPos, direction2.getOpposite())) {
     *             if (direction2 == direction.getCounterClockWise()) {
     *                 return StairsShape.OUTER_LEFT;
     *             }
     *
     *             return StairsShape.OUTER_RIGHT;
     *         }
     *     }
     *
     *     BlockState blockState3 = blockGetter.getBlockState(blockPos.relative(direction.getOpposite()));
     *     if (isStairs(blockState3) && blockState.getValue(HALF) == blockState3.getValue(HALF)) {
     *         Direction direction3 = (Direction)blockState3.getValue(FACING);
     *         if (direction3.getAxis() != ((Direction)blockState.getValue(FACING)).getAxis() && canTakeShape(blockState, blockGetter, blockPos, direction3)) {
     *             if (direction3 == direction.getCounterClockWise()) {
     *                 return StairsShape.INNER_LEFT;
     *             }
     *
     *             return StairsShape.INNER_RIGHT;
     *         }
     *     }
     *
     *     return StairsShape.STRAIGHT;
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     * @return The determined stairs shape
     */
    private static StairsShape getStairsShape(BlockStateModifyPlacementCallbackJS callback) {
        var blockState = callback.getState();
        var blockGetter = callback.getLevel();
        var blockPos = callback.getClickedPos();
        var direction = blockState.getValue(StairBlock.FACING);
        var blockState2 = blockGetter.getBlockState(blockPos.relative(direction));
        if (isStairs(blockState2) && blockState.getValue(StairBlock.HALF) == blockState2.getValue(StairBlock.HALF)) {
            var direction2 = blockState2.getValue(StairBlock.FACING);
            if (direction2.getAxis() != blockState.getValue(StairBlock.FACING).getAxis() && canTakeShape(blockState, blockGetter, blockPos, direction2.getOpposite())) {
                if (direction2 == direction.getCounterClockWise()) {
                    return StairsShape.OUTER_LEFT;
                }

                return StairsShape.OUTER_RIGHT;
            }
        }

        var blockState3 = blockGetter.getBlockState(blockPos.relative(direction.getOpposite()));
        if (isStairs(blockState3) && blockState.getValue(StairBlock.HALF) == blockState3.getValue(StairBlock.HALF)) {
            var direction3 = blockState3.getValue(StairBlock.FACING);
            if (direction3.getAxis() != blockState.getValue(StairBlock.FACING).getAxis() && canTakeShape(blockState, blockGetter, blockPos, direction3)) {
                if (direction3 == direction.getCounterClockWise()) {
                    return StairsShape.INNER_LEFT;
                }

                return StairsShape.INNER_RIGHT;
            }
        }

        return StairsShape.STRAIGHT;
    }

    /**
     * Helper method that mirrors the canTakeShape logic from Minecraft
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * private static boolean canTakeShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
     *     BlockState blockState2 = blockGetter.getBlockState(blockPos.relative(direction));
     *     return !isStairs(blockState2) || blockState2.getValue(FACING) != blockState.getValue(FACING) || blockState2.getValue(HALF) != blockState.getValue(HALF);
     * }
     * }
     * </pre>
     *
     * @param blockState The block state to check
     * @param blockGetter The level
     * @param blockPos The position
     * @param direction The direction to check
     * @return true if the block can take the shape
     */
    private static boolean canTakeShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        BlockState blockState2 = blockGetter.getBlockState(blockPos.relative(direction));
        return !isStairs(blockState2) ||
            blockState2.getValue(StairBlock.FACING) != blockState.getValue(StairBlock.FACING) ||
            blockState2.getValue(StairBlock.HALF) != blockState.getValue(StairBlock.HALF);
    }

    /**
     * Represents the implementation within {@link TrapDoorBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     BlockState blockstate = this.defaultBlockState();
     *     FluidState fluidstate = arg.getLevel().getFluidState(arg.getClickedPos());
     *     Direction direction = arg.getClickedFace();
     *     if (!arg.replacingClickedOnBlock() && direction.getAxis().isHorizontal()) {
     *         blockstate = (BlockState)((BlockState)blockstate.setValue(FACING, direction)).setValue(HALF, arg.getClickLocation().y - (double)arg.getClickedPos().getY() > (double)0.5F ? Half.TOP : Half.BOTTOM);
     *     } else {
     *         blockstate = (BlockState)((BlockState)blockstate.setValue(FACING, arg.getHorizontalDirection().getOpposite())).setValue(HALF, direction == Direction.UP ? Half.BOTTOM : Half.TOP);
     *     }
     *     if (arg.getLevel().hasNeighborSignal(arg.getClickedPos())) {
     *         blockstate = (BlockState)((BlockState)blockstate.setValue(OPEN, true)).setValue(POWERED, true);
     *     }
     *     return (BlockState)blockstate.setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void trapdoor(BlockStateModifyPlacementCallbackJS callback) {
        Direction clickedFace = callback.getClickedFace();
        boolean waterlogged = callback.isInWater();
        boolean powered = callback.getLevel().hasNeighborSignal(callback.getClickedPos());

        Direction facing;
        Half half;

        if (!callback.replacingClickedOnBlock() && clickedFace.getAxis().isHorizontal()) {
            facing = clickedFace;
            half = callback.getClickLocation().y - (double) callback.getClickedPos().getY() > 0.5F ? Half.TOP : Half.BOTTOM;
        } else {
            facing = callback.getHorizontalDirection().getOpposite();
            half = clickedFace == Direction.UP ? Half.BOTTOM : Half.TOP;
        }

        overrideState(
            callback,
            callback
                .minecraftBlock
                .defaultBlockState()
                .setValue(TrapDoorBlock.FACING, facing)
                .setValue(TrapDoorBlock.HALF, half)
                .setValue(TrapDoorBlock.OPEN, powered)
                .setValue(TrapDoorBlock.POWERED, powered)
                .setValue(TrapDoorBlock.WATERLOGGED, waterlogged)
        );
    }

    /**
     * Represents the implementation within {@link BedBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     Direction direction = arg.getHorizontalDirection();
     *     BlockPos blockPos = arg.getClickedPos();
     *     BlockPos blockPos2 = blockPos.relative(direction);
     *     Level level = arg.getLevel();
     *     return level.getBlockState(blockPos2).canBeReplaced(arg) && level.getWorldBorder().isWithinBounds(blockPos2) ? (BlockState)this.defaultBlockState().setValue(FACING, direction) : null;
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void bed(BlockStateModifyPlacementCallbackJS callback) {
        Direction direction = callback.getHorizontalDirection();
        if (callback.getLevel().getBlockState(callback.getClickedPos().relative(direction)).canBeReplaced(callback.context) &&
            callback.getLevel().getWorldBorder().isWithinBounds(callback.getClickedPos().relative(direction))) {
            overrideState(
                callback,
                callback
                    .minecraftBlock
                    .defaultBlockState()
                    .setValue(BedBlock.FACING, direction)
            );
        }else {
            setStateToNull(callback);
        }
    }

    /**
     * Represents the implementation within {@link BannerBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     return (BlockState)this.defaultBlockState().setValue(ROTATION, RotationSegment.convertToSegment(arg.getRotation() + 180.0F));
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void banner(BlockStateModifyPlacementCallbackJS callback) {
        overrideState(
            callback,
            callback
                .minecraftBlock
                .defaultBlockState()
                .setValue(BannerBlock.ROTATION, RotationSegment.convertToSegment(callback.getRotation() + 180.0F))
        );
    }

    /**
     * Represents the implementation within {@link LanternBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * @Nullable
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     FluidState fluidState = arg.getLevel().getFluidState(arg.getClickedPos());
     *     for(Direction direction : arg.getNearestLookingDirections()) {
     *         if (direction.getAxis() == Direction.Axis.Y) {
     *             BlockState blockState = (BlockState)this.defaultBlockState().setValue(HANGING, direction == Direction.UP);
     *             if (blockState.canSurvive(arg.getLevel(), arg.getClickedPos())) {
     *                 return (BlockState)blockState.setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
     *             }
     *         }
     *     }
     *     return null;
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void lantern(BlockStateModifyPlacementCallbackJS callback) {
        for (Direction direction : callback.getNearestLookingDirections()) {
            if (direction.getAxis() == Direction.Axis.Y) {
                boolean hanging = direction == Direction.UP;
                boolean waterlogged = callback.isInWater();
                var state = callback
                    .minecraftBlock
                    .defaultBlockState()
                    .setValue(LanternBlock.HANGING, hanging);

                if (state.canSurvive(callback.getLevel(), callback.getClickedPos())) {
                    overrideState(
                        callback,
                        state.setValue(LanternBlock.WATERLOGGED, waterlogged)
                    );
                    return;
                }
            }
        }
        setStateToNull(callback);
    }

    /**
     * Represents the implementation within {@link FenceBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     BlockGetter blockGetter = arg.getLevel();
     *     BlockPos blockPos = arg.getClickedPos();
     *     FluidState fluidState = arg.getLevel().getFluidState(arg.getClickedPos());
     *     BlockPos blockPos2 = blockPos.north();
     *     BlockPos blockPos3 = blockPos.east();
     *     BlockPos blockPos4 = blockPos.south();
     *     BlockPos blockPos5 = blockPos.west();
     *     BlockState blockState = blockGetter.getBlockState(blockPos2);
     *     BlockState blockState2 = blockGetter.getBlockState(blockPos3);
     *     BlockState blockState3 = blockGetter.getBlockState(blockPos4);
     *     BlockState blockState4 = blockGetter.getBlockState(blockPos5);
     *     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)super.getStateForPlacement(arg).setValue(NORTH, this.connectsTo(blockState, blockState.isFaceSturdy(blockGetter, blockPos2, Direction.SOUTH), Direction.SOUTH))).setValue(EAST, this.connectsTo(blockState2, blockState2.isFaceSturdy(blockGetter, blockPos3, Direction.WEST), Direction.WEST))).setValue(SOUTH, this.connectsTo(blockState3, blockState3.isFaceSturdy(blockGetter, blockPos4, Direction.NORTH), Direction.NORTH))).setValue(WEST, this.connectsTo(blockState4, blockState4.isFaceSturdy(blockGetter, blockPos5, Direction.EAST), Direction.EAST))).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void fence(BlockStateModifyPlacementCallbackJS callback) {
        BlockGetter blockGetter = callback.getLevel();
        BlockPos blockPos = callback.getClickedPos();
        boolean waterlogged = callback.isInWater();

        BlockPos northPos = blockPos.north();
        BlockPos eastPos = blockPos.east();
        BlockPos southPos = blockPos.south();
        BlockPos westPos = blockPos.west();

        BlockState northState = blockGetter.getBlockState(northPos);
        BlockState eastState = blockGetter.getBlockState(eastPos);
        BlockState southState = blockGetter.getBlockState(southPos);
        BlockState westState = blockGetter.getBlockState(westPos);

        var state = callback
            .minecraftBlock
            .defaultBlockState()
            .setValue(FenceBlock.NORTH, connectsTo(northState, northState.isFaceSturdy(blockGetter, northPos, Direction.SOUTH), Direction.SOUTH, callback.getState()))
            .setValue(FenceBlock.EAST, connectsTo(eastState, eastState.isFaceSturdy(blockGetter, eastPos, Direction.WEST), Direction.WEST, callback.getState()))
            .setValue(FenceBlock.SOUTH, connectsTo(southState, southState.isFaceSturdy(blockGetter, southPos, Direction.NORTH), Direction.NORTH, callback.getState()))
            .setValue(FenceBlock.WEST, connectsTo(westState, westState.isFaceSturdy(blockGetter, westPos, Direction.EAST), Direction.EAST, callback.getState()))
            .setValue(FenceBlock.WATERLOGGED, waterlogged);

        overrideState(callback, state);
    }

    /**
     * Helper method that mirrors the connectsTo logic from Minecraft
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * public boolean connectsTo(BlockState blockState, boolean bl, Direction direction) {
     *     Block block = blockState.getBlock();
     *     boolean bl2 = this.isSameFence(blockState);
     *     boolean bl3 = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(blockState, direction);
     *     return !isExceptionForConnection(blockState) && bl || bl2 || bl3;
     * }
     * }
     * </pre>
     *
     * @param blockState The block state to check
     * @param isSturdyFace Whether the face is sturdy
     * @param direction The direction to check
     * @param defaultState The default state of the fence being placed
     * @return true if the fence connects to the block
     */
    private boolean connectsTo(BlockState blockState, boolean isSturdyFace, Direction direction, BlockState defaultState) {
        boolean isSameFence = this.isSameFence(blockState, defaultState);
        // Uses tag instead of instanceof to allow for custom fence gates
        boolean isFenceGate = blockState.is(BlockTags.FENCE_GATES) && FenceGateBlock.connectsToDirection(blockState, direction);
        return !isExceptionForConnection(blockState) && isSturdyFace || isSameFence || isFenceGate;
    }

    /**
     * Helper method to check if a block is an exception for fence connection
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * private boolean isSameFence(BlockState blockState) {
     *     return blockState.is(BlockTags.FENCES) && blockState.is(BlockTags.WOODEN_FENCES) == this.defaultBlockState().is(BlockTags.WOODEN_FENCES);
     * }
     * }
     * </pre>
     *
     * @param blockState The block state to check
     * @param defaultState The default state of the fence being placed
     * @return true if the block is the same type of fence
     */
    private boolean isSameFence(BlockState blockState, BlockState defaultState) {
        return blockState.is(BlockTags.FENCES)
            // I assume this is to see if they are the same type of material (wooden or not)
            && blockState.is(BlockTags.WOODEN_FENCES) == defaultState.is(BlockTags.WOODEN_FENCES);
    }

    /**
     * Helper method to check if a block is an exception for fence connection
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * public static boolean isExceptionForConnection(BlockState blockState) {
     *     return blockState.getBlock() instanceof LeavesBlock || blockState.is(Blocks.BARRIER) || blockState.is(Blocks.CARVED_PUMPKIN) || blockState.is(Blocks.JACK_O_LANTERN) || blockState.is(Blocks.MELON) || blockState.is(Blocks.PUMPKIN) || blockState.is(BlockTags.SHULKER_BOXES);
     * }
     * }
     * </pre>
     *
     * @param blockState The block state to check
     * @return true if the block is an exception for fence connection
     */
    private boolean isExceptionForConnection(BlockState blockState) {
        // Uses tag instead of instanceof to allow for custom leaves
        return blockState.is(BlockTags.LEAVES) ||
            blockState.is(Blocks.BARRIER) ||
            blockState.is(Blocks.CARVED_PUMPKIN) ||
            blockState.is(Blocks.JACK_O_LANTERN) ||
            blockState.is(Blocks.MELON) ||
            blockState.is(Blocks.PUMPKIN) ||
            blockState.is(BlockTags.SHULKER_BOXES);
    }

    /**
     * Represents the implementation within {@link FenceGateBlock#getStateForPlacement(BlockPlaceContext)}
     * <br>
     * Code for Reference:
     * <pre>
     * {@code
     * public BlockState getStateForPlacement(BlockPlaceContext arg) {
     *     Level level = arg.getLevel();
     *     BlockPos blockpos = arg.getClickedPos();
     *     boolean flag = level.hasNeighborSignal(blockpos);
     *     Direction direction = arg.getHorizontalDirection();
     *     Direction.Axis direction$axis = direction.getAxis();
     *     boolean flag1 = direction$axis == Direction.Axis.Z && (this.isWall(level.getBlockState(blockpos.west())) || this.isWall(level.getBlockState(blockpos.east()))) || direction$axis == Direction.Axis.X && (this.isWall(level.getBlockState(blockpos.north())) || this.isWall(level.getBlockState(blockpos.south())));
     *     return (BlockState)((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(FACING, direction)).setValue(OPEN, flag)).setValue(POWERED, flag)).setValue(IN_WALL, flag1);
     * }
     * }
     * </pre>
     *
     * @param callback The callback instance
     */
    public void fenceGate(BlockStateModifyPlacementCallbackJS callback) {
        boolean powered = callback.getLevel().hasNeighborSignal(callback.getClickedPos());
        Direction direction = callback.getHorizontalDirection();
        Direction.Axis axis = direction.getAxis();

        boolean inWall = false;
        if (axis == Direction.Axis.Z) {
            inWall = isWall(callback.getLevel().getBlockState(callback.getClickedPos().west())) ||
                isWall(callback.getLevel().getBlockState(callback.getClickedPos().east()));
        } else if (axis == Direction.Axis.X) {
            inWall = isWall(callback.getLevel().getBlockState(callback.getClickedPos().north())) ||
                isWall(callback.getLevel().getBlockState(callback.getClickedPos().south()));
        }

        var result = callback
            .minecraftBlock
            .defaultBlockState()
            .setValue(FenceGateBlock.FACING, direction)
            .setValue(FenceGateBlock.OPEN, powered)
            .setValue(FenceGateBlock.POWERED, powered)
            .setValue(FenceGateBlock.IN_WALL, inWall);

        overrideState(callback, result);
    }

    /**
     * Helper method to check if a block is a wall (used by fence gate)
     *
     * @param blockState The block state to check
     * @return true if the block is a wall
     */
    private boolean isWall(BlockState blockState) {
        return blockState.is(BlockTags.WALLS);
    }


}
