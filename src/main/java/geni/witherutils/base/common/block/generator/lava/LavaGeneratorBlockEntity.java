package geni.witherutils.base.common.block.generator.lava;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import geni.witherutils.api.io.energy.EnergyIOMode;
import geni.witherutils.base.common.base.WitherMachineEnergyGenBlockEntity;
import geni.witherutils.base.common.config.common.generator.LavaGeneratorConfig;
import geni.witherutils.base.common.init.WUTBlockEntityTypes;
import geni.witherutils.base.common.init.WUTSounds;
import geni.witherutils.base.common.io.item.MachineInventoryLayout;
import geni.witherutils.base.common.io.item.SingleSlotAccess;
import geni.witherutils.core.common.network.NetworkDataSlot;
import geni.witherutils.core.common.util.FacingUtil;
import geni.witherutils.core.common.util.SoundUtil;
import geni.witherutils.core.common.util.StackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MagmaBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class LavaGeneratorBlockEntity extends WitherMachineEnergyGenBlockEntity implements MenuProvider {

	public static final SingleSlotAccess INPUT = new SingleSlotAccess();
    public static final SingleSlotAccess OUTPUT = new SingleSlotAccess();
    
    public static final Supplier<Integer> CAPACITY = () -> LavaGeneratorConfig.MAXENERGY.get();
    public static final Supplier<Integer> USAGE = () -> LavaGeneratorConfig.SENDPERTICK.get();
    
    private int disappearTime;
    private int generationRate;
    private double efficiencyRate = LavaGeneratorConfig.EFFECIENCYBASE.get();
    
    public LavaGeneratorBlockEntity(BlockPos worldPosition, BlockState blockState)
    {
		super(EnergyIOMode.OUTPUT, CAPACITY, USAGE, WUTBlockEntityTypes.LAVA_GENERATOR.get(), worldPosition, blockState);
		addDataSlot(NetworkDataSlot.INT.create(this::getGenerationRate, p -> generationRate = p));
		addDataSlot(NetworkDataSlot.FLOAT.create(this::getEfficiencyRate, p -> efficiencyRate = p));
    }

    @Override
    public void serverTick()
    {
        super.serverTick();

        if(getEnergyHandler(null).getEnergyStored() > 0)
        {
        	moveEnergy(Direction.UP, 1000);
        	if(!getInventory().getStackInSlot(1).isEmpty() && getInventory().getStackInSlot(2).isEmpty())
        		handleChargingItem(getInventory());
        }
	}

    @Override
    public void clientTick()
    {
    	super.clientTick();

    	List<BlockPos> bpLava = new ArrayList<>();
    	for(Direction facing : FacingUtil.FACES_AROUND_Y)
    	{
    		BlockState state = level.getBlockState(worldPosition.relative(facing));
    		if(!state.isAir() && state.getBlock() == Blocks.LAVA)
    		{
    			bpLava.add(worldPosition);
    		}
    	}
    	
    	setLitProperty(bpLava.size() > 0);
    	
		if(bpLava.size() > 0)
		{
			if(level.random.nextFloat() < 0.01F)
			{
    			level.addParticle(ParticleTypes.LAVA,
    				bpLava.get(level.random.nextInt(bpLava.size())).getX() - 0.5D,
    				bpLava.get(level.random.nextInt(bpLava.size())).getY(),
    				bpLava.get(level.random.nextInt(bpLava.size())).getZ() - 0.5D,
    				0, 0.1, 0);
    			level.addParticle(ParticleTypes.SMOKE,
        			bpLava.get(level.random.nextInt(bpLava.size())).getX() - 0.5D,
        			bpLava.get(level.random.nextInt(bpLava.size())).getY(),
        			bpLava.get(level.random.nextInt(bpLava.size())).getZ() - 0.5D,
        			0, 0.1, 0);
			}
		}
    }

    private void handleChargingItem(IItemHandler handler)
    {
    	IEnergyStorage storage = this.getInventory().getStackInSlot(1).getCapability(Capabilities.EnergyStorage.ITEM, null);
    	int received = storage.receiveEnergy(getGenerationRate(), false);
    	getEnergyHandler(null).extractEnergy(received, false);

    	if(storage.getEnergyStored() == storage.getMaxEnergyStored())
    	{
    		this.getInventory().setStackInSlot(2, this.getInventory().getStackInSlot(1).copy());
    		this.getInventory().setStackInSlot(1, StackUtil.shrink(this.getInventory().getStackInSlot(1), 1));
    	}
    }
    
    public static long receiveEnergy(ItemStack stack, long maxReceive)
    {
    	return stack.getCapability(Capabilities.EnergyStorage.ITEM).receiveEnergy(unsignedClampToInt(maxReceive), false);
    }

    public static int unsignedClampToInt(long l)
    {
        return l > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)l;
    }
    
    @Override
    public MachineInventoryLayout getInventoryLayout()
    {
        return MachineInventoryLayout.builder()
                .inputSlot((slot, stack) -> stack.getCapability(Capabilities.EnergyStorage.ITEM) != null &&
                		stack.getCapability(Capabilities.EnergyStorage.ITEM).getEnergyStored() <
                		stack.getCapability(Capabilities.EnergyStorage.ITEM).getMaxEnergyStored()).slotAccess(INPUT)
                .setStackLimit(1)
                .outputSlot().slotAccess(OUTPUT)
                .build();
    }
    
    @Override
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit)
    {
        if(!world.isClientSide)
        {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if(tileEntity instanceof MenuProvider)
            {
	            player.openMenu((MenuProvider) tileEntity, tileEntity.getBlockPos());
                SoundUtil.playSoundFromServer((ServerPlayer) player, WUTSounds.BUCKET.get(), 0.4f, 1.0f);
            }
            else
            {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity)
    {
        return new LavaGeneratorContainer(this, playerInventory, i);
    }
    
    @Override
    public boolean isGenerating()
    {
    	return getGenerationRate() > 0;
    }
    
    @Override
    public int getGenerationRate()
    {
        int v = 0;
    	ArrayList<BlockPos> blocksAround = new ArrayList<>();
        for (Direction facing : FacingUtil.FACES_AROUND_Y)
        {
        	BlockPos pos = worldPosition.relative(facing);
            BlockState state = this.level.getBlockState(pos);
            Block block = state.getBlock();
            
            if (block != null && this.level.getBlockState(pos).getMapColor(level, pos) == MapColor.FIRE || this.level.getBlockState(pos).getBlock() instanceof MagmaBlock)
            {
                blocksAround.add(pos);
            }

            if (blocksAround.size() > 0)
            {
                final double value = 0;
                v += (value + blocksAround.size());

                this.disappearTime++;
                if (this.disappearTime >= LavaGeneratorConfig.DISSAPEARTIME.get())
                {
                    this.disappearTime = 0;

                    if (this.level.random.nextInt(200) == 0)
                    {
                        BlockPos randomSide = blocksAround.get(this.level.random.nextInt(blocksAround.size()));
                        this.level.setBlock(randomSide, Blocks.AIR.defaultBlockState(), 1);
                    }
                }
            }
        }
        
        if(generationRate != 0)
        	generationRate = v/2;
        else
        	generationRate = v;
        
        return generationRate;
    }

	@Override
	public boolean hasEfficiencyRate()
	{
		return LavaGeneratorConfig.HASEFFECIENCY.get();
	}

	@Override
	public float getEfficiencyRate()
	{
		if (level == null || getGenerationRate() == 0)
			return 0.0f;

		final long t = level.getGameTime();
		final float v = ((int)t & 0xFF) / 256.0f;
		long k = t >> 8;
        k += 3 * 31L;
        final long a = k * k * 42317861L + k * 11L;
        final long b = a + (2L * k + 1L) * 42317861L + 11L;
        final float ai = (int)(a & 0xFFL) / 256.0f;
        final float bi = (int)(b & 0xFFL) / 256.0f;
        final float v2 = ai + (bi - ai) * v;

        return (float) efficiencyRate + v2 * 2.0f;
	}
}
