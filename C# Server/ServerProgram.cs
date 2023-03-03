using System;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Threading;

public class ServerProgram
{
    private const int portNumber = 8000;
    private static readonly StockMarketClass market = new StockMarketClass();

    public static void Main(string[] args)
    {
        runServer();
    }

    private static void runServer()
    {
        TcpListener listener = new TcpListener(IPAddress.Loopback, portNumber);
        listener.Start();
        Console.WriteLine("Waiting for incoming connections...");
        while (true)
        {
            TcpClient TcpClient = listener.AcceptTcpClient();
            new Thread(HandleIncomingConnection).Start(TcpClient);
        }
    }

    private static void HandleIncomingConnection(object param)
    {
        TcpClient tcpClient = (TcpClient) param;
        using (Stream stream = tcpClient.GetStream())
        {
            StreamWriter writer = new StreamWriter(stream);
            StreamReader reader = new StreamReader(stream);

            int traderID = 0;

            try
            {
                traderID = market.createNewTrader();
                writer.WriteLine(traderID);
                writer.Flush();

                Console.WriteLine($"New connection; Trader ID {traderID}");

                writer.WriteLine("SUCCESS");
                market.goOnline(traderID);
                printListOfTraders();
                writer.Flush();

                if (market.whoHasStock() == 0)
                {
                    market.giveStockToAnyOnline();
                }

                while (true)
                {
                    string line = reader.ReadLine();
                    string[] substring = line.Split(" ");
                    switch (substring[0])
                    {
                        case "TRADERS" :
                            int[] listOfTraders = market.getListOfTradersOnline();
                            writer.WriteLine(listOfTraders.Length);
                            foreach (int i in listOfTraders)
                            {
                                writer.WriteLine(i);
                            }
                            writer.Flush();
                            break;

                        case "WHO" :
                            int whoHasStock = market.whoHasStock();
                            writer.WriteLine(whoHasStock);
                            writer.Flush();
                            break;

                        case "EXCHANGE" :
                            int fromTrader = int.Parse(substring[1]);
                            int toTrader = int.Parse(substring[2]);

                            if (market.findTrader(toTrader) != null)
                            {
                                if (market.findTrader(toTrader).isOnline())
                                {
                                    market.exchangeStock(fromTrader, toTrader);
                                    Console.WriteLine($"Trader : {fromTrader} has given the stock to Trader : {toTrader}");
                                }else
                                {
                                    Console.WriteLine($"Stock was given back to Trader : {fromTrader} as Trader : {toTrader} is offline.");
                                }
                            }else
                            {
                                Console.WriteLine($"Trader : {toTrader} does not exist!");
                            }

                            writer.WriteLine("SUCCESS");
                            writer.Flush();
                            break;

                        case "INVENTORY" :
                            writer.WriteLine(market.checkStock(int.Parse(substring[1])));
                            writer.Flush();
                            break;

                        default :
                            throw new Exception($"Unknown command : {substring[0]}");
                    }
                }
            }
            catch (Exception e)
            {
                try
                {
                    writer.WriteLine("ERROR " + e.Message);
                    writer.Flush();
                    tcpClient.Close();
                }
                catch
                    {
                        Console.WriteLine("Failed to send error message.");
                    }
            }
            finally
                {
                    market.goOffline(traderID);
                    Console.WriteLine($"Customer {traderID} disconnected.");

                    if (market.findTrader(traderID).getStock())
                    {
                        market.giveStockToMarket(traderID);
                        Console.WriteLine("Stock was given back to the market.");
                        market.giveStockToAnyOnline();
                    }

                    printListOfTraders();
                }
        }
    }

    public static void printListOfTraders()
    {
        int[] listOfTraders = market.getListOfTradersOnline();

        Console.WriteLine("Traders Online : ");

        for (int i = 0; i < listOfTraders.Length; i++)
        {
            Console.WriteLine($"Trader : {listOfTraders[i]}");
        }
        Console.WriteLine("");
    }
}