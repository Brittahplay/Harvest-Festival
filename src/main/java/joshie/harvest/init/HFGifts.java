package joshie.harvest.init;

import joshie.harvest.init.gifts.HFGiftVanillaBlocks;
import joshie.harvest.init.gifts.HFGiftVanillaItems;

public class HFGifts {
    public static void preInit() {
        HFGiftVanillaItems.init();
        HFGiftVanillaBlocks.init();
    }
}
