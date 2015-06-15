package joshie.harvest.core.helpers;

import joshie.harvest.api.shops.IPurchaseable;
import net.minecraft.entity.player.EntityPlayer;

public class ShopHelper {
    /** This should only be ever called server ide **/
    public static boolean purchase(EntityPlayer player, IPurchaseable purchaseable, long cost) {
        long player_gold = PlayerHelper.getGold(player);
        if (player_gold - cost >= 0) {
            PlayerHelper.adjustGold(player, -cost);
            return purchaseable.onPurchased(player);
        }
        
        return false;
    }
}
