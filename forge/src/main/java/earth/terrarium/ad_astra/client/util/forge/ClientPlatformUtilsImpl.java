package earth.terrarium.ad_astra.client.util.forge;

import earth.terrarium.ad_astra.client.util.ClientPlatformUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;

public class ClientPlatformUtilsImpl {
    public static BakedModel getModel(ModelManager dispatcher, ResourceLocation id) {
        return dispatcher.getModel(id);
    }

    public static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void registerScreen(MenuType<? extends M> type, ClientPlatformUtils.ScreenConstructor<M, U> factory) {
        MenuScreens.register(type, factory::create);
    }

    public static void registerItemProperty(Item item, ResourceLocation name, ClampedItemPropertyFunction property) {
        ItemProperties.register(item, name, property);
    }
}