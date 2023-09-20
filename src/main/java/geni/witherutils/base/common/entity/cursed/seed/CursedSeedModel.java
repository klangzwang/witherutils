package geni.witherutils.base.common.entity.cursed.seed;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;

public class CursedSeedModel<T extends Entity> extends HierarchicalModel<T> {
	
    public ModelPart root;
    public ModelPart[] tents = new ModelPart[9];

    public CursedSeedModel(ModelPart part)
    {
        this.root = part;
        for(int i = 0; i < this.tents.length; ++i)
        {
           this.tents[i] = part.getChild(createTentacleName(i));
        }
    }
    private static String createTentacleName(int i)
    {
        return "tent" + i;
    }
    
    public static LayerDefinition createBodyLayer()
    {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 1.0F, 0.0F));
        RandomSource randomsource = RandomSource.create(1660L);

        for(int i = 0; i < 9; ++i)
        {
           float f = (((float)(i % 3) - (float)(i / 3 % 2) * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
           float f1 = ((float)(i / 3) / 2.0F * 2.0F - 1.0F) * 5.0F;
           int j = randomsource.nextInt(7) + 8;
           partdefinition.addOrReplaceChild(createTentacleName(i), CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, (float)j, 2.0F), PartPose.offset(f, 24.6F, f1));
        }
        return LayerDefinition.create(meshdefinition, 64, 32);
    }
    
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		for(int i = 0; i < this.tents.length; ++i)
		{
			this.tents[i].xRot = 0.2F * Mth.sin(ageInTicks * 0.3F + (float)i) + 0.4F;
		}
	}
	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer vertex, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_)
	{
	}
	@Override
	public ModelPart root()
	{
		return this.root;
	}
}
