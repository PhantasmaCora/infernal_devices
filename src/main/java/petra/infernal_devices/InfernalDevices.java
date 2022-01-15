package petra.infernal_devices;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableSet;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.PillarBlock;
import net.minecraft.client.sound.MusicType;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biome.Precipitation;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.decorator.HeightRangePlacementModifier;
import net.minecraft.world.gen.decorator.RandomOffsetPlacementModifier;
import net.minecraft.world.gen.decorator.SquarePlacementModifier;
import net.minecraft.world.gen.decorator.BiomePlacementModifier;
import net.minecraft.world.gen.decorator.CountMultilayerPlacementModifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DeltaFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.SingleStateFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.RandomSpreadFoliagePlacer;
import net.minecraft.world.gen.feature.SpringFeatureConfig;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.StraightTrunkPlacer;
import ru.bclib.api.biomes.BCLBiomeBuilder;
import ru.bclib.api.biomes.BiomeAPI;
import ru.bclib.api.features.BCLFeatureBuilder;
import ru.bclib.world.biomes.BCLBiome;
import ru.bclib.world.features.BCLFeature;

public class InfernalDevices implements ModInitializer {
	public static final String MODID = "infernal_devices";
	
	// ########### BLOCKS ############
	private static final FabricBlockSettings SCRAP = FabricBlockSettings.of(Material.METAL).requiresTool().strength(0.8f, 0.5f);
	public static final Block SCRAP_METAL_BLOCK = new Block(SCRAP);
	
	private static final FabricBlockSettings DPC = FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).requiresTool().strength(1.2f, 4.0f);
	public static final Block DEAD_POWER_CORE = new DeadPowerCoreBlock(DPC);
	
	private static final FabricBlockSettings LPC = FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).requiresTool().strength(1.5f, 0.8f);
	public static final Block POWER_CORE = new MyHorizontalFacingBlock(LPC);
	
	private static final FabricBlockSettings DECAYED_NETHROTEK = FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).requiresTool().strength(1.0f, 3.0f);
	public static final Block DECAYED_PLATING = new Block(DECAYED_NETHROTEK);
	public static final Block DECAYED_TUBING = new PillarBlock(DECAYED_NETHROTEK);
	
	private static final FabricBlockSettings NETHROTEK = FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).requiresTool().strength(2.2f, 15.0f);
	public static final Block PLATING = new Block(NETHROTEK);
	public static final Block TUBING = new PillarBlock(NETHROTEK);
	public static final Block GRILL = new Block(DECAYED_NETHROTEK);
	public static final Block REINFORCEMENT = new Block(NETHROTEK);
	public static final Block CORE = new Block(NETHROTEK);
	public static final Block GEARBOX = new Block(NETHROTEK);
	public static final Block FRAME = new GlassBlock(NETHROTEK.nonOpaque());
	
	private static final FabricBlockSettings ARMOR = FabricBlockSettings.of(Material.METAL).sounds(BlockSoundGroup.METAL).requiresTool().strength(3.0f, 80.0f);
	public static final Block ARMOR_BLOCK = new PillarBlock(ARMOR);
	public static final Block ARMOR_SMOOTH = new Block(ARMOR);
	
	private static final FabricBlockSettings EMBER = FabricBlockSettings.of(Material.STONE).requiresTool().strength(2.0f, 6.0f);
	public static final Block EMBER_STONE = new PillarBlock(EMBER);
	public static final Block SMOOTH_EMBER_STONE = new Block(EMBER);
	public static final Block EMBER_PANEL = new PillarBlock(EMBER);
	
	public static final Block TOXIC_MAGMA = new ToxicMagmaBlock(FabricBlockSettings.copyOf(Blocks.MAGMA_BLOCK).mapColor(MapColor.GREEN));
	
	public static final FlowableFluid STILL_SLUDGE = new SludgeFluid.Still();
	public static final FlowableFluid FLOWING_SLUDGE = new SludgeFluid.Flowing();
	public static final Block SLUDGE = new SludgeBlock(STILL_SLUDGE, FabricBlockSettings.copy(Blocks.LAVA).luminance( state -> 3 ));
	public static final Item SLUDGE_BUCKET = new BucketItem(STILL_SLUDGE, new FabricItemSettings().group(ItemGroup.MISC).recipeRemainder(Items.BUCKET).maxCount(1));
	
	private static final FabricBlockSettings STEELWOOD = FabricBlockSettings.copyOf(Blocks.OAK_PLANKS).mapColor(MapColor.IRON_GRAY).strength(2.6f, 2.6f);
	
	public static final Block STEELWOOD_LOG = new PillarBlock(STEELWOOD);
	public static final Block STEELWOOD_SAPLING = new MySaplingBlock(new SteelwoodSaplingGenerator(), FabricBlockSettings.copyOf(Blocks.OAK_SAPLING));
	
	static HashMap<String, Block> basic_blocks = new HashMap<String, Block>();
	
	// ########### FEATURES ############
	private static final Feature<SingleStateFeatureConfig> POWERCORE = new PowerCoreFeature(SingleStateFeatureConfig.CODEC);
	
	public static final BCLFeature POWERCORE_DEAD_CONF = BCLFeatureBuilder.start(new Identifier(MODID, "powercore_dead"), POWERCORE)
			.decoration(GenerationStep.Feature.UNDERGROUND_STRUCTURES)
			.countMax(4)
			.onlyInBiome()
			.modifier(RandomOffsetPlacementModifier.of(UniformIntProvider.create(-8, 7), ConstantIntProvider.create(0)))
			.build(new SingleStateFeatureConfig(DEAD_POWER_CORE.getDefaultState()));
	
	private static final Feature<DecayedPlateFeatureConfig> PLATE = new DecayedPlateFeature(DecayedPlateFeatureConfig.CODEC);
	
	public static final BCLFeature PLATE_CONF = BCLFeatureBuilder.start(new Identifier(MODID, "decayed_plate"), PLATE)
			.decoration(GenerationStep.Feature.UNDERGROUND_STRUCTURES)
			.countMax(4)
			.onlyInBiome()
			.modifier(RandomOffsetPlacementModifier.of(UniformIntProvider.create(-8, 7), ConstantIntProvider.create(0)))
			.build( new DecayedPlateFeatureConfig( UniformIntProvider.create(-85, 85), UniformIntProvider.create(-90, 89), UniformIntProvider.create(3, 6), UniformIntProvider.create(3, 6), SimpleBlockStateProvider.of(DECAYED_PLATING.getDefaultState()) ) );
	
	public static final BCLFeature LARGE_PLATE = BCLFeatureBuilder.start(new Identifier(MODID, "large_decayed_plate"), PLATE)
			.decoration(GenerationStep.Feature.UNDERGROUND_STRUCTURES)
			.countMax(5)
			.onlyInBiome()
			.modifier(RandomOffsetPlacementModifier.of(UniformIntProvider.create(-8, 7), ConstantIntProvider.create(0)))
			.build( new DecayedPlateFeatureConfig( UniformIntProvider.create(75, 90), UniformIntProvider.create(-180, 179), UniformIntProvider.create(8, 20), UniformIntProvider.create(8, 24), SimpleBlockStateProvider.of(DECAYED_PLATING.getDefaultState()) ) );
	
	public static final BCLFeature SLUDGE_DELTA = BCLFeatureBuilder.start(new Identifier(MODID, "sludge_delta"), Feature.DELTA_FEATURE)
			.decoration(GenerationStep.Feature.SURFACE_STRUCTURES)
			.countLayers(30)
			.onlyInBiome()
			.modifier(HeightRangePlacementModifier.uniform(YOffset.aboveBottom(33), YOffset.belowTop(0)))
			.build( new DeltaFeatureConfig( SLUDGE.getDefaultState(), TOXIC_MAGMA.getDefaultState(), UniformIntProvider.create(8, 12), UniformIntProvider.create(0, 3) ) );

	public static final BCLFeature SLUDGE_SPRING = BCLFeatureBuilder.start(new Identifier(MODID, "sludge_spring"), Feature.SPRING_FEATURE)
			.countMax(8)
			.onlyInBiome()
			.modifier(SquarePlacementModifier.of())
			.modifier(PlacedFeatures.FOUR_ABOVE_AND_BELOW_RANGE)
			.build(new SpringFeatureConfig(STILL_SLUDGE.getDefaultState(), false, 4, 1, ImmutableSet.of(Blocks.NETHERRACK, SCRAP_METAL_BLOCK)));
	
	private static final BCLFeature TOXIC_BLOCK = BCLFeatureBuilder.start(new Identifier(MODID, "toxic_block"), new SimpleBlockSurfaceFeature(SimpleBlockFeatureConfig.CODEC))
			.build( new SimpleBlockFeatureConfig( SimpleBlockStateProvider.of(TOXIC_MAGMA) ));
	
	public static final BCLFeature TOXIC_PATCH = BCLFeatureBuilder.start(new Identifier(MODID, "toxic_patch"), Feature.RANDOM_PATCH)
			.decoration(GenerationStep.Feature.LOCAL_MODIFICATIONS)
			.countLayers(8)
			.onlyInBiome()
			.build( new RandomPatchFeatureConfig(3, 4, 0, () -> TOXIC_BLOCK.getPlacedFeature() ) );
	
	private static final Feature<SpikeFeatureConfig> SPIKE_FEATURE = new SpikeFeature(SpikeFeatureConfig.CODEC);
	
	public static final BCLFeature TUBING_SPIKES = BCLFeatureBuilder.start(new Identifier(MODID, "tubing_spikes"), SPIKE_FEATURE)
			.decoration(GenerationStep.Feature.SURFACE_STRUCTURES)
			.countLayers(10)
			.onlyInBiome()
			.build( new SpikeFeatureConfig(UniformIntProvider.create(3, 5), UniformIntProvider.create(1, 2), UniformIntProvider.create(4,7), SimpleBlockStateProvider.of(DECAYED_TUBING), SimpleBlockStateProvider.of(Blocks.IRON_BARS)) );
	
	private static final Feature<SingleStateFeatureConfig> PILLAR = new PillarFeature(SingleStateFeatureConfig.CODEC);
	
	public static final BCLFeature PILLAR_CONF = BCLFeatureBuilder.start(new Identifier(MODID, "factory_pillar"), PILLAR)
			.decoration(GenerationStep.Feature.VEGETAL_DECORATION)
			.count(1)
			.onlyInBiome()
			.build(new SingleStateFeatureConfig(EMBER_STONE.getDefaultState()));
	
	private static final Feature<DefaultFeatureConfig> PLATFORM = new PlatformFeature(DefaultFeatureConfig.CODEC);
	public static final BCLFeature PLATFORM_CONF = BCLFeatureBuilder.start(new Identifier(MODID, "factory_platform"), PLATFORM)
			.decoration(GenerationStep.Feature.RAW_GENERATION)
			.count(1)
			.onlyInBiome()
			.build(new DefaultFeatureConfig());
	
	public static final ConfiguredFeature<?, ?> TREE_STEELWOOD = Feature.TREE
		// Configure the feature using the builder
		.configure(new TreeFeatureConfig.Builder(
		SimpleBlockStateProvider.of(STEELWOOD_LOG.getDefaultState()), // Trunk block provider
		new StraightTrunkPlacer(8, 3, 0), // places a straight trunk
		SimpleBlockStateProvider.of(STEELWOOD_LOG.getDefaultState()), // Foliage block provider
		new RandomSpreadFoliagePlacer(ConstantIntProvider.create(5), ConstantIntProvider.create(0), ConstantIntProvider.create(2), 10), // places leaves as a blob (radius, offset from trunk, height)
		new TwoLayersFeatureSize(1, 0, 1) // The width of the tree at different layers; used to see how tall the tree can be without clipping into blocks
	) .ignoreVines()
	.build());
			  
	public static final PlacedFeature TREE_STEELWOOD_PLACED = TREE_STEELWOOD.withPlacement(
		CountMultilayerPlacementModifier.of(5),
		BiomePlacementModifier.of()
	);
	
	
	//private static final StructureFeature<StructurePoolFeatureConfig> FACTORY_ONE = new FactoryOneFeature(StructurePoolFeatureConfig.CODEC, true, (object) -> true );
	//public static final ConfiguredStructureFeature<?,?> FACTORY_ONE_CONFIGURED = FACTORY_ONE.configure( new StructurePoolFeatureConfig( () -> BuiltinRegistries.STRUCTURE_POOL.get(new Identifier(MODID, "bases")), 5 ) );
	
	// ########### BIOMES ############
	public static final BCLBiome BCL_SCRAPHEAP = makeScrapheap();
	public static final BCLBiome BCL_PLATEHEAP = makePlateheap();
	public static final BCLBiome BCL_SLUDGEPIT = makeSludgePit();
	//public static final BCLBiome BCL_THICKET = makeIronThicket();
	
	public static final BCLBiome BCL_FACTORY = makeFactory();
	
	public static final BCLBiome BCL_STEELWOODS = makeSteelwoods();
	

	
	@Override
	public void onInitialize() {
		// list blocks
		basic_blocks.put("scrap_metal_block", SCRAP_METAL_BLOCK);
		basic_blocks.put("dead_power_core", DEAD_POWER_CORE);
		basic_blocks.put("decayed_plating", DECAYED_PLATING);
		basic_blocks.put("decayed_tubing", DECAYED_TUBING);
		basic_blocks.put("toxic_magma", TOXIC_MAGMA);
		basic_blocks.put("plating", PLATING);
		basic_blocks.put("tubing", TUBING);
		basic_blocks.put("grill", GRILL);
		basic_blocks.put("core", CORE);
		basic_blocks.put("gearbox", GEARBOX);
		basic_blocks.put("frame", FRAME);
		basic_blocks.put("power_core", POWER_CORE);
		basic_blocks.put("reinforcement", REINFORCEMENT);
		basic_blocks.put("ember_stone", EMBER_STONE);
		basic_blocks.put("ember_smooth", SMOOTH_EMBER_STONE);
		basic_blocks.put("ember_panel", EMBER_PANEL);
		basic_blocks.put("armor_block", ARMOR_BLOCK);
		basic_blocks.put("armor_smooth", ARMOR_SMOOTH);
		basic_blocks.put("steelwood_log", STEELWOOD_LOG);
    	
    	// Actually do the registration
    	Set<Entry<String, Block>> es = basic_blocks.entrySet();
    	Iterator<Entry<String, Block>> iter = es.iterator();
    	while (iter.hasNext()) {
    		Entry<String, Block> e = iter.next();
    		Registry.register(Registry.BLOCK, new Identifier(MODID, e.getKey()), e.getValue());
    		Registry.register(Registry.ITEM, new Identifier(MODID, e.getKey()), new BlockItem(e.getValue(), new FabricItemSettings().group(ItemGroup.BUILDING_BLOCKS)));
    	}
    	
    	Registry.register(Registry.FLUID, new Identifier(MODID, "toxic_sludge"), STILL_SLUDGE);
    	Registry.register(Registry.FLUID, new Identifier(MODID, "flowing_toxic_sludge"), FLOWING_SLUDGE);
    	Registry.register(Registry.ITEM, new Identifier(MODID, "toxic_sludge_bucket"), SLUDGE_BUCKET);
    	
    	Registry.register(Registry.BLOCK, new Identifier(MODID, "toxic_sludge"), SLUDGE);
    	
    	// Register biomes
    	BiomeAPI.registerNetherBiome(BCL_SCRAPHEAP);
    	BiomeAPI.registerSubBiome(BCL_SCRAPHEAP, BCL_PLATEHEAP);
    	BiomeAPI.registerSubBiome(BCL_SCRAPHEAP, BCL_SLUDGEPIT);
    	//BiomeAPI.registerSubBiome(BCL_SCRAPHEAP, BCL_THICKET);
    	
    	BiomeAPI.registerNetherBiome(BCL_FACTORY);
    	BiomeAPI.registerNetherBiome(BCL_STEELWOODS);

    	// Modify biomes
    	RegistryKey<Biome> sh = RegistryKey.of(Registry.BIOME_KEY, new Identifier(MODID, "scrap_heap"));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey( sh ), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("minecraft", "spring_open")));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey( sh ), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("minecraft", "spring_closed")));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey( sh ), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("minecraft", "glowstone")));
		
		RegistryKey<Biome> ph = RegistryKey.of(Registry.BIOME_KEY, new Identifier(MODID, "scrap_heap_plates"));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey( ph ), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("minecraft", "spring_open")));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey( ph ), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("minecraft", "spring_closed")));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey( ph ), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("minecraft", "glowstone")));
		
		RegistryKey<Biome> sp = RegistryKey.of(Registry.BIOME_KEY, new Identifier(MODID, "sludge_pit"));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey( sp ), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("minecraft", "spring_open")));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey( sp ), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("minecraft", "spring_closed")));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey( sp ), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("minecraft", "glowstone")));
		
		//RegistryKey<Biome> it = RegistryKey.of(Registry.BIOME_KEY, new Identifier(MODID, "iron_thicket"));
		//BiomeModifications.addFeature(BiomeSelectors.includeByKey( it ), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("minecraft", "spring_open")));
		//BiomeModifications.addFeature(BiomeSelectors.includeByKey( it ), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("minecraft", "spring_closed")));
		//BiomeModifications.addFeature(BiomeSelectors.includeByKey( it ), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("minecraft", "glowstone")));
		
		RegistryKey<Biome> sw = RegistryKey.of(Registry.BIOME_KEY, new Identifier(MODID, "steelwood_forest"));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey( sw ), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("minecraft", "spring_open")));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey( sw ), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("minecraft", "spring_closed")));
		BiomeModifications.addFeature(BiomeSelectors.includeByKey( sw ), GenerationStep.Feature.UNDERGROUND_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, new Identifier("minecraft", "glowstone")));

	}
	
	private static BCLBiome makeScrapheap() {
		return BCLBiomeBuilder.start(new Identifier(MODID, "scrap_heap"))
			.precipitation(Precipitation.NONE)
			.category(Category.NETHER)
			.temperature(2.0f)
			.wetness(0.0f)
			.spawn(EntityType.STRIDER, 60, 1, 2)
			.spawn(EntityType.ENDERMAN, 1, 4, 4)
			.spawn(EntityType.GHAST, 20, 2, 4)
			.skyColor(255, 64, 2)
			.fogColor(134, 96, 29)
			.genChance(1.0f)
			.waterAndFogColor(255, 64, 2)
			.plantsColor(184, 196, 64)
			.music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST))
			.loop(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
			.mood(SoundEvents.AMBIENT_NETHER_WASTES_MOOD)
			.additions(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS)
			.netherDefaultOres()
			.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.NETHER_CAVE)
			.surface(SCRAP_METAL_BLOCK, SCRAP_METAL_BLOCK, 2)
			.feature(POWERCORE_DEAD_CONF)
			.feature(PLATE_CONF)
			.feature(SLUDGE_SPRING)
			.build();
	}
	
	private static BCLBiome makePlateheap() {
		return BCLBiomeBuilder.start(new Identifier(MODID, "scrap_heap_plates"))
			.precipitation(Precipitation.NONE)
			.category(Category.NETHER)
			.temperature(2.0f)
			.wetness(0.0f)
			.spawn(EntityType.STRIDER, 60, 1, 2)
			.spawn(EntityType.ENDERMAN, 1, 4, 4)
			.spawn(EntityType.GHAST, 20, 2, 4)
			.skyColor(255, 64, 2)
			.fogColor(134, 96, 29)
			.genChance(0.6f)
			.waterAndFogColor(255, 64, 2)
			.plantsColor(184, 196, 64)
			.music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST))
			.loop(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
			.mood(SoundEvents.AMBIENT_NETHER_WASTES_MOOD)
			.additions(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS)
			.netherDefaultOres()
			.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.NETHER_CAVE)
			.surface(SCRAP_METAL_BLOCK, SCRAP_METAL_BLOCK, 2)
			.feature(LARGE_PLATE)
			.feature(SLUDGE_SPRING)
			.build();
	}
	
	private static BCLBiome makeSludgePit() {
		return BCLBiomeBuilder.start(new Identifier(MODID, "sludge_pit"))
			.precipitation(Precipitation.NONE)
			.category(Category.NETHER)
			.temperature(2.0f)
			.wetness(0.3f)
			.spawn(EntityType.GHAST, 20, 2, 4)
			.skyColor(255, 64, 2)
			.fogColor(134, 134, 29)
			.genChance(0.6f)
			.waterAndFogColor(255, 64, 2)
			.plantsColor(184, 196, 64)
			.music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST))
			.loop(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
			.mood(SoundEvents.AMBIENT_NETHER_WASTES_MOOD)
			.additions(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS)
			.netherDefaultOres()
			.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.NETHER_CAVE)
			.surface(SCRAP_METAL_BLOCK, SCRAP_METAL_BLOCK, 2)
			.feature(POWERCORE_DEAD_CONF)
			.feature(SLUDGE_DELTA)
			.feature(TOXIC_PATCH)
			.feature(SLUDGE_SPRING)
			.build();
	}
	
	private static BCLBiome makeIronThicket() {
		return BCLBiomeBuilder.start(new Identifier(MODID, "iron_thicket"))
			.precipitation(Precipitation.NONE)
			.category(Category.NETHER)
			.temperature(2.0f)
			.wetness(0.0f)
			.spawn(EntityType.STRIDER, 60, 1, 2)
			.spawn(EntityType.ENDERMAN, 1, 4, 4)
			.spawn(EntityType.GHAST, 20, 2, 4)
			.skyColor(255, 64, 2)
			.fogColor(134, 96, 29)
			.genChance(1.0f)
			.waterAndFogColor(255, 64, 2)
			.plantsColor(184, 196, 64)
			.music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST))
			.loop(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
			.mood(SoundEvents.AMBIENT_NETHER_WASTES_MOOD)
			.additions(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS)
			.netherDefaultOres()
			.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.NETHER_CAVE)
			.surface(SCRAP_METAL_BLOCK, SCRAP_METAL_BLOCK, 2)
			.feature(POWERCORE_DEAD_CONF)
			.feature(TUBING_SPIKES)
			.feature(SLUDGE_SPRING)
			.build();
	}
	
	private static BCLBiome makeFactory() {
		return BCLBiomeBuilder.start(new Identifier(MODID, "nether_factory"))
			.precipitation(Precipitation.NONE)
			.category(Category.NETHER)
			.temperature(2.0f)
			.wetness(0.0f)
			.spawn(EntityType.STRIDER, 60, 1, 2)
			.spawn(EntityType.ENDERMAN, 1, 4, 4)
			.spawn(EntityType.GHAST, 20, 2, 4)
			.skyColor(255, 64, 2)
			.fogColor(134, 96, 29)
			.genChance(1.0f)
			.waterAndFogColor(255, 64, 2)
			.plantsColor(184, 196, 64)
			.music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST))
			.loop(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
			.mood(SoundEvents.AMBIENT_NETHER_WASTES_MOOD)
			.additions(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS)
			.netherDefaultOres()
			.surface(EMBER_STONE, EMBER_STONE, 2)
			.feature(PLATFORM_CONF)
			//.structure(FACTORY_ONE_CONFIGURED)
			.build();
	}
	
	private static BCLBiome makeSteelwoods() {
		return BCLBiomeBuilder.start(new Identifier(MODID, "steelwood_forest"))
			.precipitation(Precipitation.NONE)
			.category(Category.NETHER)
			.temperature(2.0f)
			.wetness(0.0f)
			.spawn(EntityType.STRIDER, 60, 1, 2)
			.spawn(EntityType.GHAST, 20, 2, 4)
			.skyColor(255, 64, 2)
			.fogColor(134, 96, 29)
			.genChance(1.0f)
			.waterAndFogColor(255, 64, 2)
			.plantsColor(184, 196, 64)
			.music(MusicType.createIngameMusic(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST))
			.loop(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP)
			.mood(SoundEvents.AMBIENT_NETHER_WASTES_MOOD)
			.additions(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS)
			.netherDefaultOres()
			.carver(GenerationStep.Carver.AIR, ConfiguredCarvers.NETHER_CAVE)
			.surface(Blocks.NETHERRACK)
			.feature(GenerationStep.Feature.SURFACE_STRUCTURES, TREE_STEELWOOD_PLACED)
			.build();
	}

}
