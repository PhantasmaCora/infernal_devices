package petra.infernal_devices;

import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class PlatformFeature extends Feature<DefaultFeatureConfig> {

	private static final Vec3i[] SUBPOSES = { new Vec3i(0,0,0), new Vec3i(8,0,0), new Vec3i(0,0,8), new Vec3i(8,0,8) };
	
	public PlatformFeature(Codec<DefaultFeatureConfig> configCodec) {
		super(configCodec);
	}

	@Override
	public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
		StructureWorldAccess w = context.getWorld();
		ChunkPos cpos = new ChunkPos(context.getOrigin());
		Random r = context.getRandom();
		
		for(int y = 31; y < 114; y+=4) {
			for (Vec3i p : SUBPOSES) {
				BlockPos bpos = cpos.getBlockPos(p.getX(), y, p.getZ());
				boolean b = false;
				if (y == 31) {
					b = !w.isAir(bpos.up(4));
				} else {
					b = !w.isAir(bpos);
				}
				
				subGenerate(w, bpos, b );
				
				wallGenerate(w, bpos, r);
			}
		}
		
		return false;
	}

	private void subGenerate(StructureWorldAccess w, BlockPos bpos, boolean b) {
		for(int x = 0; x < 8; x++) {
			for (int z = 0; z < 8; z++) {
				for (int y = 0; y < 4; y++) {
					BlockPos p = bpos.add(x, y, z);
					w.setBlockState(p, b && y == 0 ? InfernalDevices.GRILL.getDefaultState() : (p.getY() <= 31 ? Blocks.LAVA.getDefaultState() : Blocks.AIR.getDefaultState()), 0);
				}
			}
		}
	}
	
	private void wallGenerate(StructureWorldAccess w, BlockPos bpos, Random r) {
		if (w.isAir(bpos)) {
			
		} else if (w.isAir(bpos.up(4))) {
			if (w.isAir(bpos.west(8))) {
				placeRail(w, bpos, new Vec3i(0,0,1));
			}
			if (w.isAir(bpos.north(8))) {
				placeRail(w, bpos, new Vec3i(1,0,0));
			}
			if (w.isAir(bpos.east(8))) {
				placeRail(w, bpos.east(7), new Vec3i(0,0,1));
			}
			if (w.isAir(bpos.south(8))) {
				placeRail(w, bpos.south(7), new Vec3i(1,0,0));
			}
		} else {
			if (!w.isAir(bpos.west(8)) && w.isAir(bpos.west(8).up(4)) && r.nextDouble() < 0.5) {
				placeDoorWall(w, bpos, new Vec3i(0,0,1));
			} else if (w.isAir(bpos.west(8)) || w.isAir(bpos.west(8).up(4))) {
				placeWall(w, bpos, new Vec3i(0,0,1));
			}
			if (!w.isAir(bpos.north(8)) && w.isAir(bpos.north(8).up(4)) && r.nextDouble() < 0.5) {
				placeDoorWall(w, bpos, new Vec3i(1,0,0));
			} else if (w.isAir(bpos.north(8)) || w.isAir(bpos.north(8).up(4))) {
				placeWall(w, bpos, new Vec3i(1,0,0));
			}
			if (!w.isAir(bpos.east(8))&& w.isAir(bpos.east(8).up(4)) && r.nextDouble() < 0.5) {
				placeWall(w, bpos.east(7), new Vec3i(0,0,1));
			} else if (w.isAir(bpos.east(8)) || w.isAir(bpos.east(8).up(4))) {
				placeWall(w, bpos.east(7), new Vec3i(0,0,1));
			}
			if (!w.isAir(bpos.south(8)) && w.isAir(bpos.south(8).up(4)) && r.nextDouble() < 0.5) {
				placeWall(w, bpos.south(7), new Vec3i(1,0,0));
			} else if (w.isAir(bpos.south(8)) || w.isAir(bpos.south(8).up(4))) {
				placeWall(w, bpos.south(7), new Vec3i(1,0,0));
			}
		}
	}

	private void placeWall(StructureWorldAccess w, BlockPos bpos, Vec3i vec) {
		BlockPos.Mutable m = bpos.mutableCopy();
		
		for (int i = 0; i < 8; i++) {
			w.setBlockState(m, (i == 0 || i == 7) ? InfernalDevices.CORE.getDefaultState() : InfernalDevices.PLATING.getDefaultState(), 0);
			w.setBlockState(m.up(1), (i % 3 == 2) ? InfernalDevices.ARMOR_SMOOTH.getDefaultState() : InfernalDevices.ARMOR_BLOCK.getDefaultState(), 0);
			w.setBlockState(m.up(2), (i % 3 == 2) ? Blocks.BLACK_STAINED_GLASS.getDefaultState() : InfernalDevices.ARMOR_BLOCK.getDefaultState(), 0);
			w.setBlockState(m.up(3), (i % 3 == 2) ? Blocks.BLACK_STAINED_GLASS.getDefaultState() : InfernalDevices.ARMOR_BLOCK.getDefaultState(), 0);
			m.move(vec);
		}
		
	}
	
	private void placeDoorWall(StructureWorldAccess w, BlockPos bpos, Vec3i vec) {
		BlockPos.Mutable m = bpos.mutableCopy();
		
		for (int i = 0; i < 8; i++) {
			w.setBlockState(m, (i == 0 || i == 7) ? InfernalDevices.CORE.getDefaultState() : InfernalDevices.PLATING.getDefaultState(), 0);
			w.setBlockState(m.up(1), (i == 3 || i == 4) ?  Blocks.AIR.getDefaultState(): InfernalDevices.ARMOR_BLOCK.getDefaultState(), 0);
			w.setBlockState(m.up(2), (i == 3 || i == 4) ?  Blocks.AIR.getDefaultState(): InfernalDevices.ARMOR_BLOCK.getDefaultState(), 0);
			w.setBlockState(m.up(3), (i == 2 || i == 5) ? InfernalDevices.ARMOR_SMOOTH.getDefaultState() : InfernalDevices.ARMOR_BLOCK.getDefaultState(), 0);
			m.move(vec);
		}
		
	}
	
	private void placeRail(StructureWorldAccess w, BlockPos bpos, Vec3i vec) {
		BlockPos.Mutable m = bpos.mutableCopy();
		
		for (int i = 0; i < 8; i++) {
			w.setBlockState(m, (i == 0 || i == 7) ? InfernalDevices.CORE.getDefaultState() : InfernalDevices.PLATING.getDefaultState(), 0);
			w.setBlockState(m.up(1), (i % 3 == 2) ? Blocks.AIR.getDefaultState() : InfernalDevices.ARMOR_BLOCK.getDefaultState(), 0);
			m.move(vec);
		}
		
	}



	

}
