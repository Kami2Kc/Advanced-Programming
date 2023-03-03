import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable
{
    private ServerProgram server;
    private final Socket socket;

    private StockMarket market;

    List<Integer> listOfTraders;

    public ClientHandler(Socket socket, StockMarket market, ServerProgram server)
    {
        this.socket = socket;
        this.market = market;
        this.server = server;
    }

    public void run()
    {
        int traderID = 0;

        try (Scanner scanner = new Scanner(socket.getInputStream()); PrintWriter writer = new PrintWriter(socket.getOutputStream(), true))
        {
            try
            {
                traderID = market.createNewTrader();
                writer.println(traderID);

                server.display.append("\nTrader : " + traderID + " has connected.");

                if (market.findTrader(traderID) == null)
                {
                    throw new Exception("Unknown trader : " + traderID + ".");
                }

                writer.println("SUCCESS");
                market.goOnline(traderID);
                printListOfTraders();

                if (market.whoHasStock() == 0)
                {
                    market.giveStockToAnyOnline();
                }

                while (true)
                {
                    String line = scanner.nextLine();
                    String[] substrings = line.split(" ");

                    switch (substrings[0])
                    {
                        case "TRADERS":
                            listOfTraders = market.getListOfTradersOnline();
                            writer.println(listOfTraders.size());

                            for (Integer i : listOfTraders)
                            {
                                writer.println(i);
                            }
                            break;

                        case "WHO":
                            int whoHasStock = market.whoHasStock();
                            writer.println(whoHasStock);
                            break;

                        case "EXCHANGE":
                            int fromTrader = Integer.parseInt(substrings[1]);
                            int toTrader = Integer.parseInt(substrings[2]);

                            if(market.findTrader(toTrader) != null)
                            {
                                if (market.findTrader(toTrader).isOnline())
                                {
                                    market.exchangeStock(fromTrader, toTrader);
                                    server.display.append("\nTrader : " + fromTrader + " has give the stock to Trader : " + toTrader);
                                } else {
                                    server.display.append("\nStock was given back to Trader : " + fromTrader + " as Trader : " + toTrader + " is offline.");
                                }
                            }
                            else
                                {
                                    server.display.append("\nTrader : " + toTrader + " does not exist!");
                            }

                            writer.println("SUCCESS");
                            break;

                        case "INVENTORY":
                            writer.println(market.checkStock(Integer.parseInt(substrings[1])));
                            break;

                        default :
                            throw new Exception("Unknown command : " + substrings[1]);
                    }
                }
            }catch (Exception e)
            {
                writer.println("ERROR " + e.getMessage());
                socket.close();
            }
        }catch (Exception e)
        {
        }finally
        {
            market.goOffline(traderID);
            server.display.append("\nTrader : " + traderID + " has disconnected.");

            if (market.findTrader(traderID).getStock())
            {
                market.giveStockToMarket(traderID);
                server.display.append("\nStock given back to market. ");
                market.giveStockToAnyOnline();
            }

            printListOfTraders();
        }
    }

    public void printListOfTraders()
    {
        server.listOfTraders.setText(null);
        listOfTraders = market.getListOfTradersOnline();

        server.listOfTraders.append("Traders Online : ");

        for (int i = 0; i < listOfTraders.size(); i++)
        {
            server.listOfTraders.append("\nTrader : " + listOfTraders.get(i));
        }
        server.listOfTraders.append("\n");
    }
}