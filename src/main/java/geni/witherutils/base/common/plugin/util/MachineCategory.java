package geni.witherutils.base.common.plugin.util;

import geni.witherutils.api.WitherUtilsRegistry;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;

public abstract class MachineCategory<T> implements IRecipeCategory<T> {

	protected final IDrawableStatic staticFlame;
	protected final IDrawable animatedFlame;

    public MachineCategory(IGuiHelper guiHelper, boolean burnDown)
    {
        staticFlame = guiHelper.createDrawable(WitherUtilsRegistry.loc("textures/jei/flame.png"), 176, 0, 14, 14);
        animatedFlame = guiHelper.createAnimatedDrawable(staticFlame, 300, burnDown ? IDrawableAnimated.StartDirection.TOP : IDrawableAnimated.StartDirection.BOTTOM, burnDown);
    }
}
