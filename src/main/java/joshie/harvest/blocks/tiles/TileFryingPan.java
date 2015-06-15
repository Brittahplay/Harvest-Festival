package joshie.harvest.blocks.tiles;

import joshie.harvest.api.cooking.IUtensil;
import joshie.harvest.blocks.BlockCookware;
import joshie.harvest.cooking.Utensil;
import joshie.harvest.init.HFBlocks;
import net.minecraft.block.Block;

public class TileFryingPan extends TileCooking {
    @Override
    public IUtensil getUtensil() {
        return Utensil.FRYING_PAN;
    }

    @Override
    public boolean hasPrerequisites() {
        Block block = worldObj.getBlock(xCoord, yCoord - 1, zCoord);
        int meta = worldObj.getBlockMetadata(xCoord, yCoord - 1, zCoord);
        if (block == HFBlocks.cookware && meta == BlockCookware.OVEN) {
            return true;
        } else return false;
    }
}
