package geni.witherutils.base.common.sets;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class SlideElement extends AbstractElement {

	private boolean powered = false;
	
    private float maxProgress = 20.0f;
    private float slideProgress;
    private float prevSlideProgress;
	
	@Override
	void clientTick(Level level, BlockPos pos)
	{
		powered = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above());
		
        prevSlideProgress = slideProgress;
        if(powered)
        {
            if(slideProgress < Math.max(0, maxProgress))
            {
                slideProgress += 5.0f;
            }
        }
        else if(slideProgress > 0)
        {
            slideProgress -= 5.0f;
        }
	}

	@Override
	void serverTick(ServerLevel level, BlockPos pos)
	{
	}
	
    public float getSlideProgress(float partialTicks)
    {
        float partialSlideProgress = prevSlideProgress + (slideProgress - prevSlideProgress) * partialTicks;
        float normalProgress = partialSlideProgress / (float) maxProgress;
        return 0.815F * (1.0F - ((float) Math.sin(Math.toRadians(90.0 + 180.0 * normalProgress)) / 2.0F + 0.5F));
    }
}
