package petra.infernal_devices;

import javax.swing.text.html.BlockView;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class DeadPowerCoreBlock extends MyHorizontalFacingBlock {

	public DeadPowerCoreBlock(Settings settings) {
		super(settings);
	}

	  @Override
	  public boolean emitsRedstonePower(BlockState state) {
	      return true;
	  }

	  public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
	      return 7;
	  }
	
}
