package petra.infernal_devices;

import java.util.function.Predicate;

import com.mojang.serialization.Codec;

import net.minecraft.structure.StructureGeneratorFactory;
import net.minecraft.structure.StructureGeneratorFactory.Context;
import net.minecraft.world.gen.feature.JigsawFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import net.minecraft.world.gen.random.AtomicSimpleRandom;
import net.minecraft.world.gen.random.ChunkRandom;

public class FactoryOneFeature extends JigsawFeature {

	public FactoryOneFeature(Codec<StructurePoolFeatureConfig> codec, boolean modifyBoundingBox,
			Predicate<Context<StructurePoolFeatureConfig>> predicate) {
		super(codec, 33, modifyBoundingBox, false, predicate);
		
		FactoryStructurePoolData.init();
	}

}
