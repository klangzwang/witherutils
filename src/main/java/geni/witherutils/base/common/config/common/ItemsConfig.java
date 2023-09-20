package geni.witherutils.base.common.config.common;

import net.minecraftforge.common.ForgeConfigSpec;

public class ItemsConfig {

	public static ForgeConfigSpec.ConfigValue<Integer> WANDENERGY;
	public static ForgeConfigSpec.ConfigValue<Integer> WANDENERGYUSE;
	public static ForgeConfigSpec.ConfigValue<Integer> WANDPORTDELAY;
	public static ForgeConfigSpec.ConfigValue<Integer> SHIELDENERGY;
	public static ForgeConfigSpec.ConfigValue<Integer> SHIELDENERGYUSE;
	public static ForgeConfigSpec.ConfigValue<Integer> SWORDENERGY;
	public static ForgeConfigSpec.ConfigValue<Integer> SWORDENERGYUSE;
	public static ForgeConfigSpec.ConfigValue<Boolean> SPAWNWITHERWORMS;
	public static ForgeConfigSpec.ConfigValue<Boolean> ANVILFASTHITCOUNTER;
	public static ForgeConfigSpec.ConfigValue<Double> ANVILFOODEXHAUSTION;
	public static ForgeConfigSpec.ConfigValue<Boolean> ANVILCOOLDOWN;
	
    public ItemsConfig(ForgeConfigSpec.Builder builder)
    {
    	builder.push("items");
    	
	    	builder.push("wand");
	    	
				builder.push("wandEnergyCapacity");
		        WANDENERGY = builder.comment("The Energy Amount the Wand can hold.")
		        		.defineInRange("wandEnergyCapacity", 50000, 1, 99999);
		        builder.pop();
		        
				builder.push("wandEnergyUse");
		        WANDENERGYUSE = builder.comment("The Energy Amount the Wand is using when Teleport.")
		        		.defineInRange("wandEnergyUse", 550, 0, 99999);
		        builder.pop();
		        
		        builder.push("wandPortDelay");
		        WANDPORTDELAY = builder.comment("Minimum number of ticks between porting. Values of 10 or less allow a limited sort of flight.")
		        		.defineInRange("wandPortDelay", 10, 10, 255);
		        builder.pop();
		        
		    builder.pop();
	
	    	builder.push("shield");
	    	
				builder.push("shieldEnergyCapacity");
				SHIELDENERGY = builder.comment("The Energy Amount the Powered Shield can hold multiplied by Soul Bank Level.")
		        		.defineInRange("shieldEnergyCapacity", 12000, 1, 99999);
		        builder.pop();
		
				builder.push("shieldEnergyUse");
				SHIELDENERGYUSE = builder.comment("The Energy Amount the Powered Shield is consume while Blocking.")
		        		.defineInRange("shieldEnergyUse", 20, 0, 99999);
		        builder.pop();
	
		    builder.pop();
		        
	    	builder.push("sword");
		    
				builder.push("swordEnergyCapacity");
				SWORDENERGY = builder.comment("The Energy Amount the Powered Sword can hold multiplied by Soul Bank Level.")
		        		.defineInRange("swordEnergyCapacity", 24000, 1, 99999);
		        builder.pop();
		
				builder.push("swordEnergyUse");
				SWORDENERGYUSE = builder.comment("The Energy Amount the Powered Sword is consume when perform a Dash.")
		        		.defineInRange("swordEnergyUse", 750, 0, 99999);
		        builder.pop();
	
		    builder.pop();

	    	builder.push("worm");
		    
				builder.push("witherWorms");
				SPAWNWITHERWORMS = builder.comment("Spawn Wither Worms.")
		        		.define("witherWorms", true);
		        builder.pop();
	
		    builder.pop();
		    
	    	builder.push("anvil");
		    
				builder.push("fasthitcounter");
				ANVILFASTHITCOUNTER = builder.comment("All Recipes for the Anvil needs only one Hit.")
		        		.define("fasthitcounter", false);
		        builder.pop();

				builder.push("anvilfoodexhaustion");
				ANVILFOODEXHAUSTION = builder.comment("How many Food Exhaustion a Hammer Hit can do.")
		        		.defineInRange("anvilfoodexhaustion", 0.4, 0.0, 1.0);
		        builder.pop();
		        
				builder.push("hammercooldown");
				ANVILCOOLDOWN = builder.comment("Enables/Disables the Cooldown after hitting the Anvil with the Hammer.")
		        		.define("hammercooldown", true);
		        builder.pop();
		        
		    builder.pop();
		    
        builder.pop();
    }
}
