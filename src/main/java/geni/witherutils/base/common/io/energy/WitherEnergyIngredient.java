package geni.witherutils.base.common.io.energy;

import com.google.gson.JsonObject;

public class WitherEnergyIngredient {

    private static final String KEY_ENERGY = "energy";
    private static final int TICKS_DEFAULT = 60;
    private static final int RFPT_DEFAULT = 80;
    private int rfPertick;
    private int ticks;

    public WitherEnergyIngredient(int rf, int ticks) {
        setRf(rf);
        setTicks(ticks);
    }

    public WitherEnergyIngredient(final JsonObject recipeJson) {
        parseData(recipeJson);
    }

    private void parseData(final JsonObject recipeJson)
    {
        if (!recipeJson.has(KEY_ENERGY))
        {
            setRf(RFPT_DEFAULT);
            setTicks(TICKS_DEFAULT);
        }
        else if (recipeJson.get(KEY_ENERGY).isJsonObject())
        {
            JsonObject energyJson = recipeJson.get(KEY_ENERGY).getAsJsonObject();
            setRf(energyJson.get("rfpertick").getAsInt());
            setTicks(energyJson.get("ticks").getAsInt());
        }
        else
        {
            setRf(recipeJson.get(KEY_ENERGY).getAsInt() / TICKS_DEFAULT);
            setTicks(TICKS_DEFAULT);
        }
    }

    private void setTicks(int ticks)
    {
        this.ticks = Math.max(1, ticks);
    }
    private void setRf(int rf)
    {
        this.rfPertick = Math.max(0, rf);
    }
    public int getEnergyTotal()
    {
        return rfPertick * ticks;
    }
    public int getTicks()
    {
        return ticks;
    }
    public int getRfPertick()
    {
        return this.rfPertick;
    }
    @Override
    public String toString()
    {
        return "EnergyIngredient [rfPertick=" + rfPertick + ", ticks=" + ticks + ", seconds =" + (ticks / 20) + "]";
    }
}