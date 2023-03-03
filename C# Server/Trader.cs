public class Trader
{
    private int traderID;
    private bool hasStock;
    private bool online;

    public Trader(int traderID, bool hasStock)
    {
        this.traderID = traderID;
        this.hasStock = hasStock;
    }

    public int getTraderID()
    {
        return traderID;
    }

    public bool getStock()
    {
        return hasStock;
    }

    public void setStock(bool stock)
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

    public bool isOnline()
    {
        return online;
    }
}