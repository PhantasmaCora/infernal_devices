package petra.infernal_devices;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SludgeBlock extends FluidBlock {

	public SludgeBlock(FlowableFluid fluid, Settings settings) {
		super(fluid, settings);
	}

	public boolean receiveNeighborFluids(World world, BlockPos pos, BlockState state) {
        for (Direction direction : FLOW_DIRECTIONS) {
            BlockPos blockPos = pos.offset(direction.getOpposite());
            if (world.getFluidState(blockPos).isIn(FluidTags.WATER)) {
                world.setBlockState(pos, Blocks.WATER.getDefaultState());
                return false;
            } else if (world.getFluidState(blockPos).isIn(FluidTags.LAVA)) {
            	world.setBlockState(pos, InfernalDevices.TOXIC_MAGMA.getDefaultState());
            	return false;
            }
        }
        return true;
    }
	
	@Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (this.receiveNeighborFluids(world, pos, state)) {
            world.createAndScheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
        }
    }
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (this.receiveNeighborFluids(world, pos, state)) {
            world.createAndScheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
        }
	}
	
    public void onEntityCollision(World world, BlockPos pos, BlockState state, Entity entity) {
		if (entity.isLiving() && entity instanceof LivingEntity) {
            LivingEntity le = (LivingEntity)entity;
            le.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 400));
        }
    }
	
}
