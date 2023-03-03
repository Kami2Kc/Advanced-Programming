import java.util.ArrayList;
import java.util.List;

public class StockMarket
{
    private final ArrayList<Trader> traders = new ArrayList<>();

    public boolean marketHasStock = true;

    private ServerProgram server;

    StockMarket(ServerProgram server)
    {
        this.server = server;
    }

    public int createNewTrader()
    {
        int newTraderID;

        if (traders.size() == 0)
        {
            newTraderID = 1;
        } else
            {
            newTraderID = traders.get(traders.size() - 1).getTraderID() + 1;
        }

        Trader trader = new Trader(newTraderID, false);
        traders.add(trader);

        return newTraderID;
    }

    public void goOnline(int traderID)
    {
        Trader trader = findTrader(traderID);
        trader.goOnline();
    }

    public void goOffline(int traderID)
    {
        Trader trader = findTrader(traderID);
        trader.goOffline();
    }

    public boolean checkStock(int traderID) throws Exception
    {
        Trader trader = findTrader(traderID);

        if (trader != null)
        {
            return trader.getStock();
        }

        throw new Exception("Trader with ID : " + traderID + " does not exist in the database.");
    }

    public void giveStockToAnyOnline()
    {
        if (marketHasStock)
        {
            Trader firstOnline = getFirstOnline();
            if (firstOnline != null)
            {
                firstOnline.setStock(true);
                marketHasStock = false;
                server.display.append("\nMarket gave stock to Trader : " + firstOnline.getTraderID());
            }
        }
    }

    public void giveStockToMarket(int traderID)
    {
        Trader trader = findTrader(traderID);

        trader.setStock(false);
        marketHasStock = true;
    }

    public Trader getFirstOnline()
    {
        for (Trader i : traders)
        {
            if (i.isOnline())
            {
                return i;
            }
        }

        return null;
    }

    public List<Integer> getListOfTradersOnline()
    {
        List<Integer> list = new ArrayList<>();

        for (Trader i : traders)
        {
            if (i.isOnline())
            {
                list.add(i.getTraderID());
            }
        }

        return list;
    }

    public Trader findTrader(int traderID)
    {
        for (Trader i : traders)
        {
            if (i.getTraderID() == traderID)
            {
                return i;
            }
        }

        return null;
    }

    public int whoHasStock()
    {
        if (marketHasStock)
        {
            return 0;
        }

        for (Trader i : traders)
        {
            if (i.getStock())
            {
                return i.getTraderID();
            }
        }

        return 0;
    }

    public void exchangeStock(int traderID1, int traderID2) throws Exception
    {
        Trader trader1 = findTrader(traderID1);
        Trader trader2 = findTrader(traderID2);

        if (trader1 != null && trader2 != null)
        {
            if (trader1.getStock())
            {
                if (trader2.isOnline())
                {
                    trader1.setStock(false);
                    trader2.setStock(true);
                }
                else
                    {
                        server.display.append("\nStock was given back to Trader : " + trader1.getTraderID() + " as Trader : " + trader2.getTraderID() + " has went offline.");
                }
            }else
                {
                throw new Exception("Trader with ID : " + trader1.getTraderID() + " does not have stock to exchange.");
            }
        }else
            {
            throw new Exception("One or more Traders are missing from this exchange.");
        }
    }
}