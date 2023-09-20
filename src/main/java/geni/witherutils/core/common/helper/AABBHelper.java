package geni.witherutils.core.common.helper;

import org.joml.Vector3i;

public final class AABBHelper {

    public final Vector3i start;
    public final Vector3i end;

    public AABBHelper() {
        start = new Vector3i();
        end = new Vector3i();
    }

    public AABBHelper(Vector3i pos)
    {
        start = clone(pos);
        end = clone(pos);
    }
    
    public Vector3i clone(Vector3i pos)
    {
        return new Vector3i(pos.x, pos.y, pos.z);
    }
    
    public AABBHelper(Vector3i a, Vector3i b)
    {
        start = new Vector3i();
        end = new Vector3i();

        start.x = Math.min(a.x, b.x);
        start.y = Math.min(a.y, b.y);
        start.z = Math.min(a.z, b.z);
        end.x = Math.max(a.x, b.x);
        end.y = Math.max(a.y, b.y);
        end.z = Math.max(a.z, b.z);
    }

    public AABBHelper(net.minecraft.world.phys.AABB bb)
    {
        start = new Vector3i();
        end = new Vector3i();

        start.x = (int) bb.minX;
        start.y = (int) bb.minY;
        start.z = (int) bb.minZ;
        end.x = (int) Math.ceil(bb.maxX);
        end.y = (int) Math.ceil(bb.maxY);
        end.z = (int) Math.ceil(bb.maxZ);
    }

    public AABBHelper expand(Vector3i vec) {
        if(vec.x > end.x)
            end.x = vec.x;
        else if(vec.x < start.x)
            start.x = vec.x;

        if(vec.y > end.y)
            end.y = vec.y;
        else if(vec.y < start.y)
            start.y = vec.y;

        if(vec.z > end.z)
            end.z = vec.z;
        else if(vec.z < start.z)
            start.z = vec.z;

        return this;
    }

    public AABBHelper move(Vector3i start) {
        end.sub(this.start).add(start);
        this.start.set(start);
        return this;
    }

    public net.minecraft.world.phys.AABB toMc() {
        return new net.minecraft.world.phys.AABB(start.x, start.y, start.z, end.x, end.y, end.z);
    }

}