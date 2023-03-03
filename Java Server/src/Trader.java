public class Trader
{
    private final int traderID;
    private boolean hasStock;
    private boolean online;

    public Trader(int traderID, boolean hasStock)
    {
        this.traderID = traderID;
        this.hasStock = hasStock;
    }

    public int getTraderID()
    {
        return traderID;
    }

    public boolean getStock()
    {
        return hasStock;
    }

    public void setStock(boolean stock)
    {
        hasStock = stock;
    }

    public void goOnline()
    {
        online = true;
    }

    public void goOffline()
    {
        online = false;
    }

    public boolean isOnline()
    {
        return online;
    }
}