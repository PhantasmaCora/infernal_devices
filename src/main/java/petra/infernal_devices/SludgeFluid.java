package petra.infernal_devices;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;

public abstract class SludgeFluid extends TutorialFluid {
    @Override
    public Fluid getStill() {
    	return InfernalDevices.STILL_SLUDGE;
    }
     
    @Override
    public Fluid getFlowing() {
    	return InfernalDevices.FLOWING_SLUDGE;
    }
     
    @Override
    public Item getBucketItem() {
    	return InfernalDevices.SLUDGE_BUCKET;
    }
     
    @Override
    protected BlockState toBlockState(FluidState fluidState) {
    	return InfernalDevices.SLUDGE.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }
     
    public static class Flowing extends SludgeFluid {
    	@Override
    	protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
    		super.appendProperties(builder);
    		builder.add(LEVEL);
    	}
     
    	@Override
    	public int getLevel(FluidState fluidState) {
    		return fluidState.get(LEVEL);
    	}
     
    	@Override
    	public boolean isStill(FluidState fluidState) {
    		return false;
    	}
    }
     
    public static class Still extends SludgeFluid {
    	@Override
    	public int getLevel(FluidState fluidState) {
    		return 8;
    	}
    
    	@Override
    	public boolean isStill(FluidState fluidState) {
    		return true;
    	}
    }
}
