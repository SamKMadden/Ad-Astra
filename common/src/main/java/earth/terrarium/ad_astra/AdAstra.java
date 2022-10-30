package earth.terrarium.ad_astra;

import earth.terrarium.ad_astra.config.AdAstraConfig;
import earth.terrarium.ad_astra.data.Planet;
import earth.terrarium.ad_astra.data.PlanetData;
import earth.terrarium.ad_astra.entities.mobs.*;
import earth.terrarium.ad_astra.mixin.BlockEntityTypeAccessor;
import earth.terrarium.ad_astra.networking.NetworkHandling;
import earth.terrarium.ad_astra.registry.*;
import earth.terrarium.ad_astra.util.ModResourceLocation;
import earth.terrarium.ad_astra.util.PlatformUtils;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class AdAstra {
    public static final String MOD_ID = "ad_astra";
    public static final Logger LOGGER = LoggerFactory.getLogger("Ad Astra");
    public static AdAstraConfig CONFIG;

    public static Set<Planet> planets = new HashSet<>();
    public static Set<ResourceKey<Level>> adAstraWorlds = new HashSet<>();
    public static Set<ResourceKey<Level>> orbitWorlds = new HashSet<>();
    public static Set<ResourceKey<Level>> planetWorlds = new HashSet<>();
    public static Set<ResourceKey<Level>> levelsWithOxygen = new HashSet<>();

    public static void init() {
        // Register config
        AutoConfig.register(AdAstraConfig.class, Toml4jConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(AdAstraConfig.class).getConfig();

        // Registry
        ModFluids.register();
        ModEntityTypes.register();
        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
        ModRecipes.register();
        ModScreenHandlers.register();
        ModCommands.register();
        ModSoundEvents.register();
        ModParticleTypes.register();
        ModPaintings.register();

        // Worldgen
        ModFeatures.register();
        ModStructures.register();

        // Packets
        NetworkHandling.register();

        ModCriteria.register();
    }

    public static void onRegisterReloadListeners(BiConsumer<ResourceLocation, PreparableReloadListener> registry) {
        registry.accept(new ModResourceLocation("planet_data"), new PlanetData());
    }

    public static void postInit() {
        PlatformUtils.registerStrippedLog(ModBlocks.GLACIAN_LOG.get(), ModBlocks.STRIPPED_GLACIAN_LOG.get());

        // Add custom signs to the sign block entity registry
        BlockEntityTypeAccessor signRegistry = ((BlockEntityTypeAccessor) BlockEntityType.SIGN);
        Set<Block> signBlocks = new HashSet<>(signRegistry.getValidBlocks());
        signBlocks.add(ModBlocks.GLACIAN_SIGN.get());
        signBlocks.add(ModBlocks.GLACIAN_WALL_SIGN.get());
        signRegistry.setValidBlocks(signBlocks);

        // Add custom chests to the chest block entity registry
        BlockEntityTypeAccessor chestRegistry = ((BlockEntityTypeAccessor) BlockEntityType.CHEST);
        Set<Block> chestBlocks = new HashSet<>(chestRegistry.getValidBlocks());
        chestBlocks.add(ModBlocks.AERONOS_CHEST.get());
        chestBlocks.add(ModBlocks.STROPHAR_CHEST.get());
        chestRegistry.setValidBlocks(chestBlocks);

        SpawnPlacements.register(ModEntityTypes.LUNARIAN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LunarianEntity::checkMobSpawnRules);
        SpawnPlacements.register(ModEntityTypes.CORRUPTED_LUNARIAN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CorruptedLunarianEntity::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.STAR_CRAWLER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, StarCrawlerEntity::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.MARTIAN_RAPTOR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MartianRaptorEntity::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.PYGRO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PygroEntity::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.ZOMBIFIED_PYGRO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ZombifiedPygroEntity::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.PYGRO_BRUTE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, PygroBruteEntity::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.MOGLER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MoglerEntity::checkMobSpawnRules);
        SpawnPlacements.register(ModEntityTypes.ZOMBIFIED_MOGLER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ZombifiedMoglerEntity::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.LUNARIAN_WANDERING_TRADER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, LunarianEntity::checkMobSpawnRules);
        SpawnPlacements.register(ModEntityTypes.SULFUR_CREEPER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, SulfurCreeperEntity::checkMonsterSpawnRules);
        SpawnPlacements.register(ModEntityTypes.GLACIAN_RAM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GlacianRamEntity::checkMobSpawnRules);
    }
}