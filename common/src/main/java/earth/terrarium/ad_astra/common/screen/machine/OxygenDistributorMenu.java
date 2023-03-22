package earth.terrarium.ad_astra.common.screen.machine;

import earth.terrarium.ad_astra.common.block.machine.entity.OxygenDistributorBlockEntity;
import earth.terrarium.ad_astra.common.registry.ModMenus;
import earth.terrarium.ad_astra.common.screen.AbstractModContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.Slot;

public class OxygenDistributorMenu extends AbstractModContainerMenu<OxygenDistributorBlockEntity> {

    public OxygenDistributorMenu(int id, Inventory inv, OxygenDistributorBlockEntity entity) {
        super(ModMenus.OXYGEN_DISTRIBUTOR.get(), id, inv, entity);
    }

    public OxygenDistributorMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, getTileFromBuf(inv.player.level, buf, OxygenDistributorBlockEntity.class));
    }

    @Override
    protected int getContainerInputEnd() {
        return 1;
    }

    @Override
    protected int getInventoryStart() {
        return 2;
    }

    @Override
    public int getPlayerInvXOffset() {
        return 0;
    }

    @Override
    public int getPlayerInvYOffset() {
        return 60;
    }

    @Override
    protected void addMenuSlots() {
        addSlot(new Slot(entity, 0, 30, 18));
        addSlot(new FurnaceResultSlot(player, entity, 1, 30, 36));
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}