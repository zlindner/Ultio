package zachy.cosmic.container.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import zachy.cosmic.container.ContainerBase;
import zachy.cosmic.container.slot.SlotOutput;
import zachy.cosmic.tile.multiblock.TileCompressor;

public class ContainerCompressor extends ContainerBase {

    public ContainerCompressor(TileCompressor tile, EntityPlayer player) {
        super(tile, player);

        addSlotToContainer(new Slot(tile, 0, 57, 33));
        addSlotToContainer(new Slot(tile, 1, 57, 51));

        addSlotToContainer(new SlotOutput(tile, 2, 111, 42));
        addSlotToContainer(new SlotOutput(tile, 3, 129, 42));

        addPlayerInventory(8, 100);
    }
}
