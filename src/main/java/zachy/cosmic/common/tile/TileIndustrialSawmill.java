package zachy.cosmic.common.tile;

import elucent.albedo.lighting.Light;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.Optional;
import zachy.cosmic.common.Cosmic;
import zachy.cosmic.common.core.util.MultiBlockUtils;
import zachy.cosmic.common.tile.base.TileMultiBlockBase;

import javax.annotation.Nullable;

public class TileIndustrialSawmill extends TileMultiBlockBase implements IFluidHandler {

    private FluidTank tank = new FluidTank(16000);

    private final int INPUT_SLOTS[] = {0};
    private final int OUTPUT_SLOTS[] = {1, 2, 3};

    public TileIndustrialSawmill() {
        setValid(false);
        setWorking(false);
        setProgress(0);

        inventory = NonNullList.withSize(3, ItemStack.EMPTY);
    }

    @Override
    protected boolean verifyStructure() {
        BlockPos start = pos.offset(EnumFacing.DOWN);

        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                BlockPos check = start.add(x, 0, z);

                if (x == 0 && z == 0) {
                    if (!MultiBlockUtils.isIntermediateCasing(world, check)) {
                        return false;
                    }
                } else if (!MultiBlockUtils.isStandardCasing(world, check)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public double getMaxInput() {
        return 32;
    }

    @Override
    public double getMaxStored() {
        return 3200;
    }

    @Override
    public int getSinkTier() {
        return 1;
    }

    @Override
    public void onInventoryChanged() {

    }

    @Override
    public void update() {
        super.update();

        if (world.isRemote) {
            return;
        }

        if (!isValid()) {
            return;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        tank.setFluid(FluidStack.loadFluidStackFromNBT(tag));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (tank.getFluid() != null) {
            tank.getFluid().writeToNBT(tag);
        }

        return super.writeToNBT(tag);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.UP) {
            return INPUT_SLOTS;
        } else if (side == EnumFacing.DOWN) {
            return new int[0];
        } else {
            return OUTPUT_SLOTS;
        }
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
        return index == INPUT_SLOTS[0];
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == OUTPUT_SLOTS[0] || index == OUTPUT_SLOTS[1] || index == OUTPUT_SLOTS[2];
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return tank.getTankProperties();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return tank.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return tank.drain(resource, doDrain);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    @Optional.Method(modid = "albedo")
    @Override
    public Light provideLight() {
        if (Cosmic.INSTANCE.config.enableColouredLights) {
            if (isValid()) {
                return Light.builder().pos(pos).color(0, 1, 0).radius(2).build();
            }

            return Light.builder().pos(pos).color(1, 0, 0).radius(2).build();
        }

        return null;
    }
}
