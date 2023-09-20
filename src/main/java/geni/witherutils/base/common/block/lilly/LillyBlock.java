package geni.witherutils.base.common.block.lilly;

import java.util.List;

import geni.witherutils.base.client.ClientTooltipHandler;
import geni.witherutils.base.common.init.WUTBlocks;
import geni.witherutils.base.common.init.WUTItems;
import geni.witherutils.core.common.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.PlantType;

@SuppressWarnings("deprecation")
public class LillyBlock extends CropBlock {

	public static final PlantType ender = PlantType.get("ender");
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]
	{
		Block.box(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D),
		Block.box(3.0D, 0.0D, 3.0D, 13.0D, 4.0D, 13.0D),
		Block.box(3.0D, 0.0D, 3.0D, 13.0D, 6.0D, 13.0D),
		Block.box(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D),
		Block.box(1.0D, 0.0D, 1.0D, 15.0D, 10.0D, 15.0D),
		Block.box(1.0D, 0.0D, 1.0D, 15.0D, 12.0D, 15.0D),
		Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D),
		Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D)
	};
	
	public LillyBlock(BlockBehaviour.Properties props)
	{
		super(props);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
	}
	
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(AGE);
	}
	
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		super.onPlace(oldState, level, pos, oldState, isMoving);
		level.scheduleTick(pos, this, 1);
	}
	
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand)
	{
		super.tick(state, level, pos, rand);
		level.scheduleTick(pos, this, 60);
		
		if (level.getRawBrightness(pos, 0) > 11)
		{
			int i = state.getValue(AGE);
			boolean flag = i == 7;
			if(i > 4)
			{
				int j = 1 + level.random.nextInt(2);
				ItemStackUtil.drop(level, pos, new ItemStack(WUTItems.ENDERPSHARD.get(), j + (flag ? 1 : 0)));
			}
			else
			{
				level.playSound((Player) null, pos, SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.4F + 0.8F);
			}
			level.setBlock(pos, Blocks.FIRE.defaultBlockState(), 11);
		}
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		if (!level.isAreaLoaded(pos, 1))
			return;
		
		if (level.getRawBrightness(pos, 0) < 11)
		{
			int i = this.getAge(state);
			if (i < this.getMaxAge())
			{
				float f = getGrowthSpeed(this, level, pos);
				
				if (level.getRawBrightness(pos, 0) < 4)
				{
					f = getGrowthSpeed(this, level, pos) * 2;
				}
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / f) + 1) == 0))
				{
					level.setBlock(pos, this.getStateForAge(i + 1), 2);
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
				}
			}
		}
	}
	
    @Override
    public void appendHoverText(ItemStack stack, BlockGetter world, List<Component> list, TooltipFlag flag)
    {
    	ClientTooltipHandler.Tooltip.addInformation(stack, world, list, flag, true);
    }
    
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand)
	{
		if(rand.nextInt(5) != 0)
			return;
		int ddx = rand.nextInt(2) * 2 - 1;
		int ddz = rand.nextInt(2) * 2 - 1;
		double dx = rand.nextFloat() * 1.0F * ddx;
		double dy = (rand.nextFloat() - 0.5D) * 0.125D;
		double dz = rand.nextFloat() * 1.0F * ddz;
		double x = pos.getX() + 0.5D + 0.25D * ddx;
		double y = pos.getY() + rand.nextFloat();
		double z = pos.getZ() + 0.5D + 0.25D * ddz;
		level.addParticle(ParticleTypes.PORTAL, x, y, z, dx, dy, dz);
	}
	
	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context)
	{
		return state.getBlock() instanceof BaseFireBlock;
	}
	
	/*
	 * 
	 * PLANT
	 * 
	 */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
    {
        int i = state.getValue(AGE);
        boolean flag = i == 7;
        
        if (i > 4)
        {
        	int j = 1 + level.random.nextInt(2);
            popResource(level, pos, new ItemStack(WUTItems.ENDERPSHARD.get(), j + (flag ? 1 : 0)));
            if(level.random.nextFloat() < 0.05f)
            	popResource(level, pos, new ItemStack(WUTBlocks.LILLY.get(), 1));
            level.playSound((Player)null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            BlockState blockstate = state.setValue(AGE, Integer.valueOf(1));
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        else
        {
            return super.use(state, level, pos, player, hand, hitResult);
        }
    }
    
    @Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity)
    {
		if (entity instanceof LivingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE)
		{
			entity.makeStuckInBlock(state, new Vec3((double) 0.8F, 0.75D, (double) 0.8F));
			if (!level.isClientSide && state.getValue(AGE) > 0 && (entity.xOld != entity.getX() || entity.zOld != entity.getZ()))
			{
				double d0 = Math.abs(entity.getX() - entity.xOld);
				double d1 = Math.abs(entity.getZ() - entity.zOld);
				if (d0 >= (double) 0.003F || d1 >= (double) 0.003F)
				{
					entity.hurt(level.damageSources().sweetBerryBush(), 1.0F);
				}
			}
		}
	}
    
    @Override
    public ItemStack getCloneItemStack(BlockGetter p_57256_, BlockPos p_57257_, BlockState p_57258_)
    {
        return new ItemStack(Items.ENDER_PEARL);
    }
    
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
    	return this.mayPlaceOn(level.getBlockState(pos.below()), level, pos);
    }
    
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
    {
        return state.getMapColor(level, pos) == MapColor.COLOR_BLACK ||
        	   state.getMapColor(level, pos) == MapColor.COLOR_GRAY ||
        	   state.getMapColor(level, pos) == MapColor.CRIMSON_NYLIUM ||
        	   state.getMapColor(level, pos) == MapColor.DEEPSLATE ||
        	   state.getMapColor(level, pos) == MapColor.STONE ||
               state.getMapColor(level, pos) == MapColor.WARPED_NYLIUM;
    }
    
	public boolean shouldCatchFire(Level level, BlockPos pos)
	{
		return level.canSeeSkyFromBelowWater(pos) && level.getDayTime() < 13000 || level.getDayTime() > 23000;
	}
    
	@Override
	public BlockState getPlant(BlockGetter level, BlockPos pos)
	{
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() != this) return defaultBlockState();
		return state;
	}
	
	@Override
	public PlantType getPlantType(BlockGetter world, BlockPos pos)
	{
		return ender;
	}
	
	@Override
	public boolean isValidBonemealTarget(LevelReader p_256559_, BlockPos p_50898_, BlockState state, boolean p_50900_)
	{
		return false;
//		return state.getValue(AGE) < 7;
	}

	@Override
	public boolean isBonemealSuccess(Level p_220878_, RandomSource p_220879_, BlockPos p_220880_, BlockState p_220881_)
	{
		return false;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state)
	{
//		int i = Math.min(7, state.getValue(AGE) + 1);
//		level.setBlock(pos, state.setValue(AGE, Integer.valueOf(i)), 2);
	}
}
