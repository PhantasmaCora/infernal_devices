package petra.infernal_devices;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record SpikeFeatureConfig(IntProvider size, IntProvider dist, IntProvider height, BlockStateProvider main, BlockStateProvider between) implements FeatureConfig {
	public static final Codec<SpikeFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		IntProvider.VALUE_CODEC.fieldOf("size").forGetter(SpikeFeatureConfig::size),
		IntProvider.VALUE_CODEC.fieldOf("dist").forGetter(SpikeFeatureConfig::dist),
		IntProvider.VALUE_CODEC.fieldOf("height").forGetter(SpikeFeatureConfig::height),
		BlockStateProvider.TYPE_CODEC.fieldOf("main").forGetter(SpikeFeatureConfig::main),
		BlockStateProvider.TYPE_CODEC.fieldOf("between").forGetter(SpikeFeatureConfig::between)
	).apply(instance, instance.stable(SpikeFeatureConfig::new)));
}
