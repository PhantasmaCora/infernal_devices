package petra.infernal_devices;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

public class InfernalModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		FluidRenderHandlerRegistry.INSTANCE.register(InfernalDevices.STILL_SLUDGE, InfernalDevices.FLOWING_SLUDGE, new SimpleFluidRenderHandler(
				new Identifier("minecraft:block/lava_still"),
				new Identifier("minecraft:block/lava_flow"),
				0x4CC248
		));
 
		//BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), InfernalDevices.STILL_SLUDGE, InfernalDevices.FLOWING_SLUDGE);
 
		//if you want to use custom textures they needs to be registered.
		//In this example this is unnecessary because the vanilla water textures are already registered.
		//To register your custom textures use this method.
		//ClientSpriteRegistryCallback.event(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
		//    registry.register(new Identifier("modid:block/toxic_sludge_still"));
		//    registry.register(new Identifier("modid:block/toxic_sludge_flowing"));
		//});
 
		// ...
	}
}
