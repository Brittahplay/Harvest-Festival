package joshie.harvest.core.util.base;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.generic.IHasMetaItem;
import joshie.harvest.core.util.generic.Text;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public abstract class ItemBlockHFBase extends ItemBlock implements IHasMetaItem {
    public ItemBlockHFBase(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {
        return new CreativeTabs[]{HFTab.FARMING, HFTab.COOKING, HFTab.MINING, HFTab.TOWN};
    }

    @Override
    public int getMetaCount() {
        return ((BlockHFBaseMeta) Block.getBlockFromItem(this)).getEnumFromMeta(new ItemStack(this).getItemDamage()).ordinal(); //TODO
        //return ((IHasMetaBlock) Block.getBlockFromItem(this)).getMetaCount();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String unlocalized = block.getUnlocalizedName().replace("tile.", "").replace("_", ".");
        String name = getName(stack).replaceAll("(.)([A-Z])", "$1$2").toLowerCase();
        return Text.localize(unlocalized + "." + name.replace("_", "."));
    }
}