package petra.infernal_devices;

import com.mojang.serialization.Codec;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class PillarFeature extends Feature<SingleStateFeatureConfig> {
	public PillarFeature(Codec<SingleStateFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(FeatureContext<SingleStateFeatureConfig> context) {
		StructureWorldAccess w  = context.getWorld();

		BlockPos.Mutable mu = new BlockPos.Mutable(context.getOrigin().getX(), 24, context.getOrigin().getZ());
    	
		while(mu.getY() < 127) {
			w.setBlockState(mu, context.getConfig().state, 0);
			mu.move(0,1,0);
		}
		
		return false;
	}
}
