package zachy.cosmic.common.block.base;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import zachy.cosmic.common.core.util.WorldUtils;
import zachy.cosmic.common.tile.base.TileMultiBlockBase;

public class BlockMultiBlockBase extends BlockMachineBase {

    protected static final PropertyBool VALID = PropertyBool.create("valid");

    public BlockMultiBlockBase(String name) {
        super(name);

        setDefaultState(getDefaultState().withProperty(DIRECTION, EnumFacing.NORTH).withProperty(VALID, false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DIRECTION, VALID);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = WorldUtils.getTile(world, pos);

        if (tile instanceof TileMultiBlockBase) {
            return getDefaultState().withProperty(DIRECTION, ((TileMultiBlockBase) tile).getDirection()).withProperty(VALID, ((TileMultiBlockBase) tile).isValid());
        }

        return state;
    }

    @Override
    public boolean isIntermediate() {
        return true;
    }
}