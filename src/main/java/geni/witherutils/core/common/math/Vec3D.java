package geni.witherutils.core.common.math;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

import static net.minecraft.core.Direction.Axis.X;
import static net.minecraft.core.Direction.Axis.Y;

public class Vec3D {

    public double x;
    public double y;
    public double z;

    public Vec3D() {
    }

    public Vec3D(Entity entity) {
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
    }

    public Vec3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3D(Vec3D vec3I) {
        this.x = vec3I.x;
        this.y = vec3I.y;
        this.z = vec3I.z;
    }

    public Vec3D(Vector3 vector3) {
        this.x = vector3.x;
        this.y = vector3.y;
        this.z = vector3.z;
    }

    public Vec3D(BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    public Vec3D set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vec3D set(Vec3D vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        return this;
    }

    public Vec3D set(BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        return this;
    }

    public Vec3D add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vec3D add(Vec3D vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    public Vec3D add(BlockPos pos) {
        this.x += pos.getX();
        this.y += pos.getY();
        this.z += pos.getZ();
        return this;
    }

    public Vec3D subtract(BlockPos pos) {
        this.x -= pos.getX();
        this.y -= pos.getY();
        this.z -= pos.getZ();
        return this;
    }

    public Vec3D subtract(Vec3D vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }

    public Vec3D subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vec3D multiply(Vec3D vec) {
        this.x *= vec.x;
        this.y *= vec.y;
        this.z *= vec.z;
        return this;
    }

    public Vec3D multiply(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vec3D copy() {
        return new Vec3D(this);
    }

    public BlockPos getPos()
    {
        return new BlockPos((int) x, (int) y, (int) z);
    }

    public Vector3 toVector3() { return new Vector3(x, y, z); }

    @Override
    public String toString() {
        return String.format("Vec3D: [x: %s, y: %s, z: %s]", x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Vec3D other = (Vec3D) obj;

        if (x == other.x && y == other.y && z == other.z) return true;

        return false;
    }

    @Override
    public int hashCode() {
        return ((int) y + (int) z * 31) * 31 + (int) x;
    }

    public Vec3D offset(Direction direction, double offsetDistance) {
        this.x += direction.getStepX() * offsetDistance;
        this.y += direction.getStepY() * offsetDistance;
        this.z += direction.getStepZ() * offsetDistance;
        return this;
    }

    public Vec3D offset(Vec3D direction, double offsetDistance) {
        this.x += direction.x * offsetDistance;
        this.y += direction.y * offsetDistance;
        this.z += direction.z * offsetDistance;
        return this;
    }

    public Vec3D radialOffset(Direction.Axis axis, double sin, double cos, double offsetAmount) {
        x += ((axis == X ? 0 : axis == Y ? sin : sin) * offsetAmount);
        y += ((axis == X ? sin : axis == Y ? 0 : cos) * offsetAmount);
        z += ((axis == X ? cos : axis == Y ? cos : 0) * offsetAmount);
        return this;
    }

    public static Vec3D getDirectionVec(Vec3D vecFrom, Vec3D vecTo) {
        double distance = getDistance(vecFrom, vecTo);
        if (distance == 0) {
            distance = 0.1;
        }
        Vec3D offset = vecTo.copy();
        offset.subtract(vecFrom);
        return new Vec3D(offset.x / distance, offset.y / distance, offset.z / distance);
    }

    public static Vector3 getDirectionVec(Vector3 vecFrom, Vector3 vecTo) {
        double distance = getDistance(vecFrom.x, vecFrom.y, vecFrom.z, vecTo.x, vecTo.y, vecTo.z);
        if (distance == 0) {
            distance = 0.1;
        }
        Vector3 offset = vecTo.copy();
        offset.subtract(vecFrom);
        return new Vector3(offset.x / distance, offset.y / distance, offset.z / distance);
    }

    public static Vec3D getCenter(BlockPos pos) {
        return new Vec3D(pos).add(0.5, 0.5, 0.5);
    }

    public static Vec3D getCenter(BlockEntity tile) {
        return getCenter(tile.getBlockPos());
    }

    public double distXZ(Vec3D vec3D) {
        return getDistance(x, z, vec3D.x, vec3D.z);
    }

    public double distance(Vec3D vec3D) {
        return getDistance(this, vec3D);
    }

    public double distance(Entity entity) {
        return getDistance(this, new Vec3D(entity));
    }

    public double distanceSq(Vec3D v) {
        return getDistanceSq(x, y, z, v.x, v.y, v.z);
    }

    public int floorX() {
        return Mth.floor(x);
    }

    public int floorY() {
        return Mth.floor(y);
    }

    public int floorZ() {
        return Mth.floor(z);
    }
    
    public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return Math.sqrt((dx * dx + dy * dy + dz * dz));
    }

    @Deprecated
    public static double getDistanceAtoB(double x1, double y1, double z1, double x2, double y2, double z2) {
        return getDistance(x1, y1, z1, x2, y2, z2);
    }

    @Deprecated
    public static double getDistanceAtoB(Vec3D pos1, Vec3D pos2) {
        return getDistance(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }

    public static double getDistance(Vec3D pos1, Vec3D pos2) {
        return getDistance(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }
    
    @Deprecated
    public static double getDistanceAtoB(double x1, double z1, double x2, double z2) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return Math.sqrt((dx * dx + dz * dz));
    }

    public static double getDistance(double x1, double z1, double x2, double z2) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return Math.sqrt((dx * dx + dz * dz));
    }

    public static double getDistanceSq(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return dx * dx + dy * dy + dz * dz;
    }

    public static double getDistanceSq(double x1, double z1, double x2, double z2) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return dx * dx + dz * dz;
    }
}
