package petra.infernal_devices;

import net.minecraft.block.BlockState;
import net.minecraft.block.MagmaBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ToxicMagmaBlock extends MagmaBlock {

	public ToxicMagmaBlock(Settings settings) {
		super(settings);
	}
	
	@Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (entity.isLiving() && entity instanceof LivingEntity) {
            LivingEntity le = (LivingEntity)entity;
            le.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200));
        }
        super.onSteppedOn(world, pos, state, entity);
    }

}
