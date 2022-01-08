package petra.infernal_devices;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record DecayedPlateFeatureConfig(IntProvider rotate_x, IntProvider rotate_y, IntProvider size_x, IntProvider size_y, BlockStateProvider state) implements FeatureConfig {
	public static final Codec<DecayedPlateFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		IntProvider.VALUE_CODEC.fieldOf("rotate_x").forGetter(DecayedPlateFeatureConfig::rotate_x),
		IntProvider.VALUE_CODEC.fieldOf("rotate_y").forGetter(DecayedPlateFeatureConfig::rotate_y),
		IntProvider.VALUE_CODEC.fieldOf("size_x").forGetter(DecayedPlateFeatureConfig::size_x),
		IntProvider.VALUE_CODEC.fieldOf("size_y").forGetter(DecayedPlateFeatureConfig::size_y),
		BlockStateProvider.TYPE_CODEC.fieldOf("state").forGetter(DecayedPlateFeatureConfig::state)
	).apply(instance, instance.stable(DecayedPlateFeatureConfig::new)));
}
