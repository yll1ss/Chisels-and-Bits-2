package nl.dgoossens.chiselsandbits2.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nl.dgoossens.chiselsandbits2.api.bit.RestrictionAPI;
import nl.dgoossens.chiselsandbits2.api.block.BitAccess;
import nl.dgoossens.chiselsandbits2.api.item.ItemPropertyAPI;

import java.util.Optional;

/**
 * The Chisels & Bits API is a way for any mod (including C&B2 itself)
 * to interact with C&B tiles and modify them.
 * Much of C&Bs functionality can also be found in the various utility classes
 * in the common.utils package.
 *
 * If you'd like more of C&B to be accessible through the API, like how the API allows adding custom item modes, you can
 * make an issue requesting it. No part of C&B should be hidden from other mods wanting to use it and most enums should
 * be extendable. However, something like VoxelType can't be extended and no PR trying to do so will be merged.
 */
public interface ChiselsAndBitsAPI {
    /**
     * Get an entry point to a given block position in a given world.
     * The BitAccess allows access and easy manipulation of the voxel data of a given
     * chiseled block.
     */
    Optional<BitAccess> getBitAccess(final World world, final BlockPos pos);

    /**
     * Get the registry that manages everything related to C&B items and their properties. Includes things like registering
     * item modes, item mode types or additional buttons for in the radial menu.
     */
    ItemPropertyAPI getItemPropertyRegistry();

    /**
     * Get the API that manages blocked or restricted blocks for rotating or using in chiseled blocks. Here blocks can be
     * specially marked as chiselable regardless of the block shape or properties.
     */
    RestrictionAPI getRestrictions();
}
