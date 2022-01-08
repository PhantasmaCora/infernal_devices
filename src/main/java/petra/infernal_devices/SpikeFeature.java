package petra.infernal_devices;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SpikeFeature extends Feature<SpikeFeatureConfig> {
	private Direction[] direc = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
	
	
	public SpikeFeature(Codec<SpikeFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(FeatureContext<SpikeFeatureConfig> context) {
		StructureWorldAccess w  = context.getWorld();
		SpikeFeatureConfig c = context.getConfig();
		Random r = context.getRandom();
		
		int len = c.size().get(r);
		
		BlockPos.Mutable mu = new BlockPos.Mutable().move(context.getOrigin());
		
		for(int i = 0; i < len; i++) {
			placeColumn(w, c, mu, r, c.height().get(r));
			Direction d = direc[r.nextInt(4)];
			
			int s = c.dist().get(r);
			for(int j = 0; j < s; j++) {
				mu.move(d);
				placeWalls(w, c, r, mu);
			}
			mu.move(d);
		}
		
		return false;
	}
	
	private void placeColumn(StructureWorldAccess w, SpikeFeatureConfig c, BlockPos p, Random r, int h) {
		BlockPos bottom = p;
		while (w.isAir(bottom)) {
			bottom = bottom.offset(Direction.DOWN);
		}
		
		for(int i = 0; i < h; i++) {
			w.setBlockState(bottom, c.main().getBlockState(r, bottom), 0);
			bottom = bottom.offset(Direction.UP);
		}
	}
	
	private void placeWalls(StructureWorldAccess w, SpikeFeatureConfig c, Random r, BlockPos p) {
		BlockPos pos = p;
		
		while (hasAdjacent(w, pos) && w.isAir(pos)) {
			if (r.nextFloat() < 0.6) {
				w.setBlockState(pos, c.between().getBlockState(r, pos), 0);
				w.getBlockState(pos).onBlockAdded(w.toServerWorld(), pos, w.getBlockState(pos), false);
			}
			pos = pos.offset(Direction.UP);
		}

	}
	
	private boolean hasAdjacent(StructureWorldAccess w, BlockPos p) {
		for(Direction d : direc) {
			if (!w.isAir(p.offset(d)) ) {
				return true;
			}
		}
		
		return false;
	}

}
