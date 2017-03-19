package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.HFCore;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.player.tracking.StackSold;
import joshie.harvest.player.tracking.TrackingRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.Set;

@Packet(Packet.Side.CLIENT)
@SuppressWarnings("unused")
public class PacketRenderSold extends PenguinPacket {
    private Set<StackSold> sold;

    public PacketRenderSold() { }
    public PacketRenderSold(Set<StackSold> sold) {
        this.sold = sold;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("Data", NBTHelper.writeCollection(sold));
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        NBTTagCompound tag = ByteBufUtils.readTag(buf);
        sold = NBTHelper.readHashSet(StackSold.class, tag.getTagList("Data", 10));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (HFCore.DISPLAY_SHIPPED_LIST) MinecraftForge.EVENT_BUS.register(new TrackingRenderer(sold));
    }
}