package petra.infernal_devices;

import java.util.ArrayList;
import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

public class DecayedPlateFeature extends Feature<DecayedPlateFeatureConfig> {

	public DecayedPlateFeature(Codec<DecayedPlateFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(FeatureContext<DecayedPlateFeatureConfig> context) {
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
    		DecayedPlateFeatureConfig config = context.getConfig();
    		Random r = context.getRandom();
    		Quaternion q = Quaternion.fromEulerXyzDegrees(new Vec3f(config.rotate_x().get(r), config.rotate_y().get(r), 0));
    		
    		int s_x = config.size_x().get(r);
    		int s_y = config.size_y().get(r);
    		ArrayList<Vec3f> points = new ArrayList<Vec3f>();
    		for (int x = 0; x < s_x; x++) {
    			for (int y = 0; y < s_y; y++) {
    				points.add(new Vec3f(x, y, 0));
    			}
    		}
    		
    		StructureWorldAccess world = context.getWorld();
    		int ypos = heights.get(r.nextInt(heights.size()));
    		BlockPos origin = new BlockPos(context.getOrigin().getX(), ypos, context.getOrigin().getZ());
    		
    		for (Vec3f p : points) {
    			p.rotate(q);
    			BlockPos pos = origin.east(Math.round(p.getX())).south(Math.round(p.getZ())).up(Math.round(p.getY()));
    			world.setBlockState(pos, config.state().getBlockState(r, pos), 0);
    		}
    		
    		return true;
    	}
    	else {
    		return false;
    	}
	}

}
