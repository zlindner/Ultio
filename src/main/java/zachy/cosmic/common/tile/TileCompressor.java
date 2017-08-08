package zachy.cosmic.common.tile;

import elucent.albedo.lighting.Light;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Optional;
import zachy.cosmic.api.recipe.compressor.ICompressorRecipe;
import zachy.cosmic.apiimpl.API;
import zachy.cosmic.common.Cosmic;
import zachy.cosmic.common.core.util.MultiBlockUtils;
import zachy.cosmic.common.core.util.WorldUtils;
import zachy.cosmic.common.tile.base.TileMultiBlockBase;

public class TileCompressor extends TileMultiBlockBase {

    private ICompressorRecipe recipe;

    private final int INPUT_SLOTS[] = {0, 1};
    private final int OUTPUT_SLOTS[] = {2, 3};

    //TODO fix shift clicking not updating recipe?
    public TileCompressor() {
        setValid(false);
        setWorking(false);
        setProgress(0);

        inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    }

    @Override
    protected boolean verifyStructure() {
        BlockPos start = pos.offset(EnumFacing.DOWN).offset(EnumFacing.DOWN).offset(EnumFacing.DOWN);

        for (int y = 0; y < 3; y++) {
            for (int x = -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    BlockPos check = start.add(x, y, z);

                    if (x == 0 && y == 1 && z == 0) {
                        if (!MultiBlockUtils.isAir(world, check)) {
                            return false;
                        }
                    } else if (y == 0 || y == 2) {
                        if (x != 0 && z != 0) {
                            if (!MultiBlockUtils.isStandardCasing(world, check)) {
                                return false;
                            }
                        } else {
                            if (!MultiBlockUtils.isIntermediateCasing(world, check)) {
                                return false;
                            }
                        }
                    } else {
                        if (!MultiBlockUtils.isIntermediateCasing(world, check)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public int getDuration() {
        return recipe != null ? recipe.getDuration() : 0;
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
        ICompressorRecipe _recipe = API.instance().getCompressorRegistry().getRecipe(this);

        if (_recipe != recipe) {
            setProgress(0);
        }

        recipe = _recipe;

        markDirty();

        WorldUtils.updateBlock(world, pos);
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

        if (getEnergy() < 0) {
            return;
        }

        if (isWorking()) {
            if (recipe == null) {
                setWorking(false);
            } else if ((getStackInSlot(2).isEmpty() && getStackInSlot(3).isEmpty()
                    || (API.instance().getComparer().isEqualNoQuantity(recipe.getOutput(0), getStackInSlot(2))
                    && getStackInSlot(2).getCount() + recipe.getOutput(0).getCount() <= getStackInSlot(2).getMaxStackSize())
                    || (API.instance().getComparer().isEqualNoQuantity(recipe.getOutput(1), getStackInSlot(3))
                    && getStackInSlot(3).getCount() + recipe.getOutput(1).getCount() <= getStackInSlot(3).getMaxStackSize()))) {

                if (getEnergy() >= recipe.getEnergy()) {
                    drainEnergy(recipe.getEnergy());
                } else {
                    setProgress(0);

                    return;
                }

                setProgress(getProgress() + 1);

                if (getProgress() >= recipe.getDuration()) {
                    for (int i = 0; i < OUTPUT_SLOTS.length; i++) {
                        ItemStack outputSlot = getStackInSlot(OUTPUT_SLOTS[i]);

                        if (outputSlot.isEmpty()) {
                            setInventorySlotContents(OUTPUT_SLOTS[i], recipe.getOutput(i).copy());
                        } else {
                            outputSlot.grow(recipe.getOutput(i).getCount());

                            markDirty();

                            WorldUtils.updateBlock(world, pos);
                        }
                    }

                    for (int i = 0; i < INPUT_SLOTS.length; i++) {
                        ItemStack inputSlot = getStackInSlot(i);

                        if (!inputSlot.isEmpty()) {
                            inputSlot.shrink(recipe.getInput(i).get(0).getCount());
                        }
                    }

                    recipe = API.instance().getCompressorRegistry().getRecipe(this);

                    setProgress(0);
                }

                markDirty();

                WorldUtils.updateBlock(world, pos);
            }
        } else if (recipe != null) {
            setWorking(true);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        recipe = API.instance().getCompressorRegistry().getRecipe(this);
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
        return index == INPUT_SLOTS[0] || index == INPUT_SLOTS[1];
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == OUTPUT_SLOTS[0] || index == OUTPUT_SLOTS[1];
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
