package petra.infernal_devices;

import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SimpleBlockSurfaceFeature extends Feature<SimpleBlockFeatureConfig> {

	public SimpleBlockSurfaceFeature(Codec<SimpleBlockFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(FeatureContext<SimpleBlockFeatureConfig> context) {
		BlockPos.Mutable mu = new BlockPos.Mutable(context.getOrigin().getX(), context.getOrigin().getY(), context.getOrigin().getZ());
		StructureWorldAccess world = context.getWorld();
		
		while (world.isAir(mu) && mu.getY() > 3 ) {
			mu.move(Direction.DOWN);
		}
		
		world.setBlockState(mu, context.getConfig().toPlace().getBlockState(context.getRandom(), mu), 0);
		
		return false;
	}

}
