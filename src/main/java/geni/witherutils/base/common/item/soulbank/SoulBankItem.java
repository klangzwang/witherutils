package geni.witherutils.base.common.item.soulbank;

import geni.witherutils.base.common.base.WitherItem;

public class SoulBankItem extends WitherItem {

	public SoulBankItem(Properties pProperties)
	{
		super(pProperties.stacksTo(1));
	}
}
