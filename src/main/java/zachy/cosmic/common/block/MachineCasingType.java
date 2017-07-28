package zachy.cosmic.common.block;

import net.minecraft.util.IStringSerializable;

public enum MachineCasingType implements IStringSerializable {
    BASIC,
    INTERMEDIATE,
    ADVANCED;

    @Override
    public String getName() {
        return name().toLowerCase();
    }
}