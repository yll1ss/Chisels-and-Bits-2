package nl.dgoossens.chiselsandbits2.common.chiseledblock.voxel;

import net.minecraft.block.Blocks;
import net.minecraftforge.fml.loading.FMLEnvironment;
import nl.dgoossens.chiselsandbits2.api.render.IStateRef;
import nl.dgoossens.chiselsandbits2.common.utils.BitUtil;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;

public final class VoxelBlobStateReference implements IStateRef {
    private static Map<VoxelBlobStateInstance, WeakReference<VoxelBlobStateInstance>> serverRefs = Collections.synchronizedMap(new WeakHashMap<>());
    private static Map<VoxelBlobStateInstance, WeakReference<VoxelBlobStateInstance>> clientRefs = Collections.synchronizedMap(new WeakHashMap<>());

    private static byte[] airBlob;
    private final VoxelBlobStateInstance data;

    public VoxelBlobStateReference() {
        //Build the default voxel state reference
        this(findDefaultBytes());
    }

    public VoxelBlobStateReference(final VoxelBlob blob) {
        this(blob.write(VoxelVersions.getDefault()));
        data.blob = new SoftReference<>(new VoxelBlob(blob));
    }

    public VoxelBlobStateReference(final int stateId) {
        this(findBytesFor(stateId));
    }

    public VoxelBlobStateReference(final byte[] v) {
        data = findReference(v);
    }

    private static Map<VoxelBlobStateInstance, WeakReference<VoxelBlobStateInstance>> getReferences() {
        return FMLEnvironment.dist.isClient() ? clientRefs : serverRefs;
    }

    private static VoxelBlobStateInstance lookupReference(final VoxelBlobStateInstance inst) {
        return Optional.ofNullable(getReferences().get(inst)).map(Reference::get).orElse(null);
    }

    private static byte[] findBytesFor(final int stateId) {
        if (stateId == 0) {
            if (airBlob == null) {
                final VoxelBlob vb = new VoxelBlob();
                airBlob = vb.write(VoxelVersions.getDefault());
            }
            return airBlob;
        }

        final VoxelBlob vb = new VoxelBlob();
        vb.fill(stateId);
        return vb.write(VoxelVersions.getDefault());
    }

    private static byte[] findDefaultBytes() {
        final VoxelBlob vb = new VoxelBlob();
        int b = BitUtil.getBlockId(Blocks.YELLOW_WOOL.getDefaultState());
        for(int y = 1; y <= 12; y++)
            for(int x = 2; x <= 13; x++)
                for(int z = 2; z <= 13; z++) {
                    vb.set(x, y, z, b);
                }
        return vb.write(VoxelVersions.getDefault());
    }

    private static void addReference(final VoxelBlobStateInstance inst) {
        getReferences().put(inst, new WeakReference<>(inst));
    }

    private static VoxelBlobStateInstance findReference(final byte[] v) {
        final VoxelBlobStateInstance t = new VoxelBlobStateInstance(v);
        VoxelBlobStateInstance ref = lookupReference(t);
        if (ref == null) {
            ref = t;
            addReference(t);
        }
        return ref;
    }

    public VoxelBlobStateInstance getInstance() {
        return data;
    }

    public byte[] getByteArray() {
        return data.voxelBytes;
    }

    @Override
    public VoxelBlob getVoxelBlob() {
        return data.getBlob();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof VoxelBlobStateReference)) return false;
        return data.equals(((VoxelBlobStateReference) obj).data);
    }

    @Override
    public int hashCode() {
        return data.hash;
    }

    public int getFormat() {
        return data.getFormat();
    }
}
