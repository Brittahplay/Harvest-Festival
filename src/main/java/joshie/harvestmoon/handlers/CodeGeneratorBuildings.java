package joshie.harvestmoon.handlers;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import joshie.harvestmoon.buildings.placeable.PlaceableHelper;
import joshie.lib.util.IFaceable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;

public class CodeGeneratorBuildings {

    private World world;
    private int x1, y1, z1, x2, y2, z2;

    public CodeGeneratorBuildings(World world, int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd) {
        this.world = world;
        this.x1 = xStart < xEnd ? xStart : xEnd;
        this.x2 = xStart < xEnd ? xEnd : xStart;
        this.y1 = yStart < yEnd ? yStart : yEnd;
        this.y2 = yStart < yEnd ? yEnd : yStart;
        this.z1 = zStart < zEnd ? zStart : zEnd;
        this.z2 = zStart < zEnd ? zEnd : zStart;
    }

    public ArrayList<String> getArea(boolean air) {
        ArrayList<String> ret = new ArrayList();
        for (int y = 0; y <= y2 - y1; y++) {
            for (int x = 0; x <= x2 - x1; x++) {
                for (int z = 0; z <= z2 - z1; z++) {
                    Block block = world.getBlock(x1 + x, y1 + y, z1 + z);
                    if (!block.isAir(world, x1 + x, y1 + y, z1 + z) || air) {
                        String print = Block.blockRegistry.getNameForObject(block);
                        int meta = world.getBlockMetadata(x1 + x, y1 + y, z1 + z);
                        ret.add(x + "," + y + "," + z + "," + print + "," + meta);
                    }
                }
            }
        }

        return ret;
    }

    public ArrayList<Entity> getEntities(Class clazz, int x, int y, int z) {
        return (ArrayList<Entity>) world.getEntitiesWithinAABB(clazz, Blocks.stone.getCollisionBoundingBoxFromPool(world, x, y, z));
    }

    public void getCode(boolean air) {
        if (!world.isRemote) {
            ArrayList<String> ret = new ArrayList();
            StringBuilder entities = new StringBuilder();
            Set all = new HashSet();
            boolean hasAFrame = false;
            int i = 0;
            for (int y = 0; y <= y2 - y1; y++) {
                for (int x = 0; x <= x2 - x1; x++) {
                    for (int z = 0; z <= z2 - z1; z++) {
                        Set<Entity> entityList = new HashSet();
                        entityList.addAll(getEntities(EntityPainting.class, x1 + x, y1 + y, z1 + z));
                        entityList.addAll(getEntities(EntityItemFrame.class, x1 + x, y1 + y, z1 + z));

                        Block block = world.getBlock(x1 + x, y1 + y, z1 + z);
                        if (!block.isAir(world, x1 + x, y1 + y, z1 + z) || air || entityList.size() > 0) {
                            int meta = world.getBlockMetadata(x1 + x, y1 + y, z1 + z);
                            TileEntity tile = world.getTileEntity(x1 + x, y1 + y, z1 + z);
                            if (tile instanceof IFaceable) {
                                ret.add(PlaceableHelper.getPlaceableIFaceableString((IFaceable) tile, block, meta, x, y, z));
                            } else if (tile instanceof TileEntitySign) {
                                String[] text = ((TileEntitySign) tile).signText;
                                if (block == Blocks.standing_sign) {
                                    ret.add(PlaceableHelper.getFloorSignString(text, block, meta, x, y, z));
                                } else ret.add(PlaceableHelper.getWallSignString(text, block, meta, x, y, z));
                            } else if (block == Blocks.standing_sign) {

                            } else {
                                ret.add(PlaceableHelper.getPlaceableBlockString(block, meta, x, y, z));
                            }

                            //Entities
                            if (entityList.size() > 0) {
                                int l = 0;
                                for (Entity e : entityList) {
                                    if (!all.contains(e)) {
                                        ret.add(PlaceableHelper.getPlaceableEntityString(e, x, y, z));
                                        all.add(e);
                                    }

                                    l++;
                                }
                            }

                            i++;
                        }
                    }
                }
            }

            ArrayList<String> build = new ArrayList();
            build.add("list = new ArrayList();");
            build.addAll(ret);

            try {
                PrintWriter writer = new PrintWriter("building-generator.log", "UTF-8");
                for (String s : build) {
                    writer.println(s);
                }

                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
