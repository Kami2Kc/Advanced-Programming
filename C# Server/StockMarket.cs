using System;
using System.Collections.Generic;

public class StockMarketClass
{
    private List<Trader> traders = new List<Trader>();

    public bool marketHasStock = true;

    public int createNewTrader()
    {
        int newTraderID;

        if (traders.Count == 0)
        {
            newTraderID = 1;
        } else
            {
            newTraderID = traders[traders.Count - 1].getTraderID() + 1;
        }

        Trader trader = new Trader(newTraderID, false);
        traders.Add(trader);

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

    public bool checkStock(int traderID) 
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
                Console.WriteLine($"Market gave stock to Trader : {firstOnline.getTraderID()}");
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
        foreach (Trader i in traders)
        {
            if (i.isOnline())
            {
                return i;
            }
        }

        return null;
    }

    public int[] getListOfTradersOnline()
    {
        List<int> list = new List<int>();

        foreach (Trader i in traders)
        {
            if (i.isOnline())
            {
                list.Add(i.getTraderID());
            }
        }

        return list.ToArray();
    }

    public Trader findTrader(int traderID)
    {
        foreach (Trader i in traders)
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

        foreach (Trader i in traders)
        {
            if (i.getStock())
            {
                return i.getTraderID();
            }
        }

        return 0;
    }

    public void exchangeStock(int traderID1, int traderID2)
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
                        Console.WriteLine($"Stock was given back to Trader : {trader1.getTraderID()} as Trader : {trader2.getTraderID()} has went offline.");
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