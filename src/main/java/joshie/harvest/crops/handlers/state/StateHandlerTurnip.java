package joshie.harvest.crops.handlers.state;

import joshie.harvest.api.crops.StateHandlerDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class StateHandlerTurnip extends StateHandlerDefault {
    public StateHandlerTurnip() {
        super(3);
    }

    @Override
    public IBlockState getState(IBlockAccess world, BlockPos pos, PlantSection section, int stage, boolean withered) {
        if (stage <= 2) return getState(1);
        else if (stage <= 4) return getState(2);
        else return getState(3);
    }
}