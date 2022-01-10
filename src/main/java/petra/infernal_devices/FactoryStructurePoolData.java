package petra.infernal_devices;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.util.Identifier;

public class FactoryStructurePoolData {
	public static void init() {
	}
	
	static {
		StructurePools.register(new StructurePool(new Identifier(InfernalDevices.MODID, "4xceiling"), new Identifier("empty"), ImmutableList.of(
			Pair.of(StructurePoolElement.ofSingle(new Identifier(InfernalDevices.MODID, "4xroof").toString() ), 1),
			Pair.of(StructurePoolElement.ofSingle(new Identifier(InfernalDevices.MODID, "4xcupola").toString() ), 1),
			Pair.of(StructurePoolElement.ofSingle(new Identifier(InfernalDevices.MODID, "4xfloor").toString() ), 1),
			Pair.of(StructurePoolElement.ofSingle(new Identifier(InfernalDevices.MODID, "4xopen").toString() ), 1),
			Pair.of(StructurePoolElement.ofSingle(new Identifier(InfernalDevices.MODID, "4xclosed").toString() ), 1)
		), StructurePool.Projection.RIGID));
		
		StructurePools.register(new StructurePool(new Identifier(InfernalDevices.MODID, "bases"), new Identifier("empty"), ImmutableList.of(
				Pair.of(StructurePoolElement.ofSingle(new Identifier(InfernalDevices.MODID, "8xbase").toString() ), 1)
			), StructurePool.Projection.RIGID));
	}


}
