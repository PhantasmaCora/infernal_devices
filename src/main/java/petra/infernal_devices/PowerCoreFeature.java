package petra.infernal_devices;

import java.util.Random;
import java.util.ArrayList;

import com.mojang.serialization.Codec;

import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class PowerCoreFeature extends Feature<SingleStateFeatureConfig> {
	public PowerCoreFeature(Codec<SingleStateFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(FeatureContext<SingleStateFeatureConfig> context) {
		StructureWorldAccess w  = context.getWorld();

		BlockPos.Mutable mu = new BlockPos.Mutable(context.getOrigin().getX(), 33, context.getOrigin().getZ());
    	
		ArrayList<Integer> heights = new ArrayList<Integer>();
		
		boolean prev = false;
		
    	while ( mu.getY() < 122) {
    		mu.move(0, 1, 0);
    		if (w.isAir(mu)) {
    			if (prev) {
    				prev = false;
    				heights.add(mu.getY());
    			}
    		} else {
    			prev = true;
    		}
    	}
    	
    	if (heights.size() > 0) {
    		Random r = context.getRandom();
    		int groundOffset = r.nextBoolean() ? -1 : 0;
    		int height = r.nextInt(2, 5);
    		
    		int ypos = heights.get(r.nextInt(heights.size()));
    		
    		BlockPos origin = new BlockPos(context.getOrigin().getX(), ypos, context.getOrigin().getZ());
    		
    		for (int i = groundOffset; i < groundOffset + height; i++) {
    			BlockPos o = origin.up(i);
    			w.setBlockState(o, context.getConfig().state.with(HorizontalFacingBlock.FACING, Direction.WEST), 0);
    			w.setBlockState(o.north(), context.getConfig().state.with(HorizontalFacingBlock.FACING, Direction.NORTH), 0);
    			w.setBlockState(o.east(), context.getConfig().state.with(HorizontalFacingBlock.FACING, Direction.SOUTH), 0);
    			w.setBlockState(o.north().east(), context.getConfig().state.with(HorizontalFacingBlock.FACING, Direction.EAST), 0);
    		}
    		return true;
    	}
    	else {
    		return false;
    	}
		
	}

}
