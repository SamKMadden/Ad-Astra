package earth.terrarium.adastra.client;

import com.mojang.blaze3d.platform.InputConstants;
import earth.terrarium.adastra.AdAstra;
import earth.terrarium.adastra.api.ti69.client.Ti69AppApi;
import earth.terrarium.adastra.client.config.ClientConfig;
import earth.terrarium.adastra.client.renderers.blocks.base.CustomGeoBlockRenderer;
import earth.terrarium.adastra.client.renderers.blocks.base.SidedGeoBlockRenderer;
import earth.terrarium.adastra.client.renderers.blocks.machines.SteamGeneratorBlockEntityRenderer;
import earth.terrarium.adastra.client.renderers.blocks.machines.TinkerersWorkbenchBlockEntityRenderer;
import earth.terrarium.adastra.client.renderers.items.armor.AerolyteSpaceSuitRenderer;
import earth.terrarium.adastra.client.renderers.items.base.CustomGeoItemRenderer;
import earth.terrarium.adastra.client.ti69.apps.SensorApp;
import earth.terrarium.adastra.client.ti69.apps.WeatherApp;
import earth.terrarium.adastra.client.utils.ClientData;
import earth.terrarium.adastra.common.constants.ConstantComponents;
import earth.terrarium.adastra.common.items.armor.AerolyteSpaceSuitItem;
import earth.terrarium.adastra.common.networking.NetworkHandler;
import earth.terrarium.adastra.common.networking.messages.ServerboundSyncKeybindPacket;
import earth.terrarium.adastra.common.registry.ModBlockEntityTypes;
import earth.terrarium.adastra.common.registry.ModBlocks;
import earth.terrarium.adastra.common.registry.ModEntityTypes;
import earth.terrarium.adastra.common.registry.ModItems;
import earth.terrarium.adastra.common.utils.KeybindManager;
import earth.terrarium.botarium.client.ClientHooks;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AdAstraClient {
    private static final Map<Item, BlockEntityWithoutLevelRenderer> ITEM_RENDERERS = new HashMap<>();
    private static final Map<Item, Supplier<GeoArmorRenderer<?>>> ARMOR_RENDERERS = new HashMap<>();

    public static final KeyMapping KEY_TOGGLE_SUIT_FLIGHT = new KeyMapping(
        ConstantComponents.TOGGLE_SUIT_FLIGHT_KEY.getString(),
        InputConstants.KEY_V,
        ConstantComponents.AD_ASTRA_CATEGORY.getString());

    public static void init() {
        AdAstra.CONFIGURATOR.registerConfig(ClientConfig.class);
        registerBlockRenderTypes();
        registerBlockEntityRenderers();
        registerEntityRenderers();
        registerItemRenderers();
        registerArmorRenderers();
        registerTi69Apps();
    }

    private static void registerBlockRenderTypes() {
        ClientHooks.setRenderLayer(ModBlocks.BATTERY.get(), RenderType.cutout());
        ClientHooks.setRenderLayer(ModBlocks.ETRIONIC_BLAST_FURNACE.get(), RenderType.cutout());
    }

    private static void registerBlockEntityRenderers() {
        ClientHooks.registerBlockEntityRenderers(ModBlockEntityTypes.OXYGEN_DISTRIBUTOR.get(), context -> new SidedGeoBlockRenderer<>(ModBlocks.OXYGEN_DISTRIBUTOR));
        ClientHooks.registerBlockEntityRenderers(ModBlockEntityTypes.SOLAR_PANEL.get(), context -> new CustomGeoBlockRenderer<>(new DefaultedBlockGeoModel<>(new ResourceLocation(AdAstra.MOD_ID, "solar_panel"))));
        ClientHooks.registerBlockEntityRenderers(ModBlockEntityTypes.HYDRAULIC_PRESS.get(), context -> new CustomGeoBlockRenderer<>(ModBlocks.HYDRAULIC_PRESS));
        ClientHooks.registerBlockEntityRenderers(ModBlockEntityTypes.OIL_REFINERY.get(), context -> new CustomGeoBlockRenderer<>(ModBlocks.OIL_REFINERY));
        ClientHooks.registerBlockEntityRenderers(ModBlockEntityTypes.STEAM_GENERATOR.get(), context -> new SteamGeneratorBlockEntityRenderer(ModBlocks.STEAM_GENERATOR));
        ClientHooks.registerBlockEntityRenderers(ModBlockEntityTypes.ENERGY_CONTROLLER.get(), context -> new SidedGeoBlockRenderer<>(ModBlocks.ENERGY_CONTROLLER));
        ClientHooks.registerBlockEntityRenderers(ModBlockEntityTypes.ENERGY_RELAY.get(), context -> new SidedGeoBlockRenderer<>(ModBlocks.ENERGY_RELAY));
        ClientHooks.registerBlockEntityRenderers(ModBlockEntityTypes.VESNIUM_COIL.get(), context -> new CustomGeoBlockRenderer<>(ModBlocks.VESNIUM_COIL));
        ClientHooks.registerBlockEntityRenderers(ModBlockEntityTypes.TINKERERS_WORKBENCH.get(), context -> new TinkerersWorkbenchBlockEntityRenderer(ModBlocks.TINKERERS_WORKBENCH));
        ClientHooks.registerBlockEntityRenderers(ModBlockEntityTypes.RECYCLER.get(), context -> new CustomGeoBlockRenderer<>(ModBlocks.RECYCLER));
        ClientHooks.registerBlockEntityRenderers(ModBlockEntityTypes.GRAVITY_NORMALIZER.get(), context -> new SidedGeoBlockRenderer<>(ModBlocks.GRAVITY_NORMALIZER));
    }

    public static void registerEntityRenderers() {
        ClientHooks.registerEntityRenderer(ModEntityTypes.AIR_VORTEX, NoopRenderer::new);
    }

    private static void registerItemRenderers() {
        ITEM_RENDERERS.put(ModBlocks.OXYGEN_DISTRIBUTOR.get().asItem(), new CustomGeoItemRenderer(ModBlocks.OXYGEN_DISTRIBUTOR));
        ITEM_RENDERERS.put(ModBlocks.ETRIONIC_SOLAR_PANEL.get().asItem(), new CustomGeoItemRenderer(new DefaultedBlockGeoModel<>(new ResourceLocation(AdAstra.MOD_ID, "solar_panel"))));
        ITEM_RENDERERS.put(ModBlocks.VESNIUM_SOLAR_PANEL.get().asItem(), new CustomGeoItemRenderer(new DefaultedBlockGeoModel<>(new ResourceLocation(AdAstra.MOD_ID, "solar_panel"))));
        ITEM_RENDERERS.put(ModBlocks.HYDRAULIC_PRESS.get().asItem(), new CustomGeoItemRenderer(ModBlocks.HYDRAULIC_PRESS));
        ITEM_RENDERERS.put(ModBlocks.OIL_REFINERY.get().asItem(), new CustomGeoItemRenderer(ModBlocks.OIL_REFINERY));
        ITEM_RENDERERS.put(ModBlocks.STEAM_GENERATOR.get().asItem(), new SteamGeneratorBlockEntityRenderer.ItemRender(ModBlocks.STEAM_GENERATOR));
        ITEM_RENDERERS.put(ModBlocks.ENERGY_CONTROLLER.get().asItem(), new CustomGeoItemRenderer(ModBlocks.ENERGY_CONTROLLER));
        ITEM_RENDERERS.put(ModBlocks.ENERGY_RELAY.get().asItem(), new CustomGeoItemRenderer(ModBlocks.ENERGY_RELAY));
        ITEM_RENDERERS.put(ModBlocks.VESNIUM_COIL.get().asItem(), new CustomGeoItemRenderer(ModBlocks.VESNIUM_COIL));
        ITEM_RENDERERS.put(ModBlocks.TINKERERS_WORKBENCH.get().asItem(), new CustomGeoItemRenderer(ModBlocks.TINKERERS_WORKBENCH));
        ITEM_RENDERERS.put(ModBlocks.RECYCLER.get().asItem(), new CustomGeoItemRenderer(ModBlocks.RECYCLER));
        ITEM_RENDERERS.put(ModBlocks.GRAVITY_NORMALIZER.get().asItem(), new CustomGeoItemRenderer(ModBlocks.GRAVITY_NORMALIZER));
    }

    public static void registerArmorRenderers() {
        ARMOR_RENDERERS.put(ModItems.AEROLYTE_SPACE_HELMET.get(), AerolyteSpaceSuitRenderer::new);
        ARMOR_RENDERERS.put(ModItems.AEROLYTE_SPACE_SUIT.get(), AerolyteSpaceSuitRenderer::new);
        ARMOR_RENDERERS.put(ModItems.AEROLYTE_SPACE_PANTS.get(), AerolyteSpaceSuitRenderer::new);
        ARMOR_RENDERERS.put(ModItems.AEROLYTE_SPACE_BOOTS.get(), AerolyteSpaceSuitRenderer::new);
    }

    public static void registerTi69Apps() {
        Ti69AppApi.API.register(SensorApp.ID, new SensorApp());
        Ti69AppApi.API.register(WeatherApp.ID, new WeatherApp());
    }

    public static GeoArmorRenderer<?> getArmorRenderer(ItemLike item) {
        return ARMOR_RENDERERS.get(item.asItem()).get();
    }

    public static BlockEntityWithoutLevelRenderer getItemRenderer(ItemLike item) {
        return ITEM_RENDERERS.get(item.asItem());
    }

    public static void clientTick(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        if (player == null) return;
        Options options = minecraft.options;

        if (KEY_TOGGLE_SUIT_FLIGHT.consumeClick() && player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof AerolyteSpaceSuitItem) {
            player.displayClientMessage(ClientData.suitFlightEnabled ? ConstantComponents.SUIT_FLIGHT_DISABLED : ConstantComponents.SUIT_FLIGHT_ENABLED, true);
            ClientData.suitFlightEnabled = !ClientData.suitFlightEnabled;
        }

        var keybinds = new KeybindManager(
            options.keyJump.isDown(),
            options.keySprint.isDown(),
            options.keyUp.isDown(),
            options.keyLeft.isDown(),
            options.keyDown.isDown(),
            options.keyRight.isDown(),
            ClientData.suitFlightEnabled
        );
        KeybindManager.set(player, keybinds);
        NetworkHandler.CHANNEL.sendToServer(new ServerboundSyncKeybindPacket(keybinds));
    }
}