package petra.infernal_devices;

import java.util.Random;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class SteelwoodSaplingGenerator extends SaplingGenerator {

	public SteelwoodSaplingGenerator() {
	}
	
	@Override
	protected ConfiguredFeature<?, ?> getTreeFeature(Random r, boolean b) {
		return InfernalDevices.TREE_STEELWOOD;
	}

}
