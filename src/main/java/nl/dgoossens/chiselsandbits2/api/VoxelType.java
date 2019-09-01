package nl.dgoossens.chiselsandbits2.api;

import nl.dgoossens.chiselsandbits2.common.chiseledblock.voxel.VoxelBlob;

import java.util.stream.Stream;

public enum VoxelType {
    SOLID_BLOCKSTATE(0b11),
    TRANSLUCENT(0b00),
    COLOURED(0b10),
    FLUIDSTATE(0b01),
    AIR(0b0),
    ;

    private int bt;
    VoxelType(int b) { bt=b; }

    /**
     * Returns true if this voxel type can be collided with.
     */
    public boolean isSolid() {
        return this==SOLID_BLOCKSTATE||this==TRANSLUCENT||this==COLOURED;
    }

    /**
     * Returns true if this voxel type is a fluid and can be swum in.
     */
    public boolean isFluid() {
        return this==FLUIDSTATE;
    }

    /**
     * Get the voxel type that corresponds to this bit.
     */
    public static VoxelType getType(int bit) {
        if(bit== VoxelBlob.AIR_BIT) return AIR;
        return Stream.of(values()).parallel().filter(f -> bit << 30 == f.bt).findAny().orElse(VoxelType.AIR);
    }

    /**
     * Whether or not the other voxeltype can be seen through this
     * voxel type. (used for culling tests)
     */
    public boolean shouldShow(final VoxelType other) {
        return this != AIR && this != other;
    }
}