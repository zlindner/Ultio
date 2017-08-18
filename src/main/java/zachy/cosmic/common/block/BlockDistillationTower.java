package zachy.cosmic.common.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zachy.cosmic.client.gui.Guis;
import zachy.cosmic.common.Cosmic;
import zachy.cosmic.common.block.base.BlockMultiblockController;
import zachy.cosmic.common.core.Lib;
import zachy.cosmic.common.tile.TileDistillationTower;

public class BlockDistillationTower extends BlockMultiblockController {

    public BlockDistillationTower() {
        super(Lib.Blocks.DISTILLATION_TOWER);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileDistillationTower();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && !player.isSneaking()) {
            player.openGui(Cosmic.INSTANCE, Guis.DISTILLATION_TOWER, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }
}
