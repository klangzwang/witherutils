package geni.witherutils.api.soulbank;

public enum SoulBankModifier {
    
    ENERGY_CAPACITY,
    ENERGY_USE,
    ENERGY_TRANSFER;

    public final String id;

    SoulBankModifier()
    {
        this.id = new String("soulbank." + name().toLowerCase());
    }
}
