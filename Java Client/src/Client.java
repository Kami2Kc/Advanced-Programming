import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements AutoCloseable
{
    final int portNumber = 8000;

    private final Scanner reader;
    private final PrintWriter writer;

    private  int traderID;

    public Client() throws Exception
    {
        Socket socket = new Socket("localhost" , portNumber);

        reader = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);

        this.traderID = Integer.parseInt(reader.nextLine());

        String line = reader.nextLine();

        if (line.trim().compareToIgnoreCase("success") != 0)
        {
            throw new Exception(line);
        }
    }

    public int whatIsID()
    {
        return this.traderID;
    }

    public int[] getListOfTradersOnline()
    {
        writer.println("TRADERS");

        String line = reader.nextLine();
        int numberOfTradersOnline = Integer.parseInt(line);

        int[] listOfTraders = new int[numberOfTradersOnline];

        for (int i = 0; i < numberOfTradersOnline; i++)
        {
            line = reader.nextLine();
            listOfTraders[i] = Integer.parseInt(line);
        }

        return listOfTraders;
    }

    public int whoHasStock()
    {
        writer.println("WHO");

        String line = reader.nextLine();

        return Integer.parseInt(line);
    }

    public void exchangeStock(int traderID) throws Exception
    {
        writer.println("EXCHANGE " + this.traderID + " " + traderID);

        String line = reader.nextLine();

        if (line.trim().compareToIgnoreCase("success") != 0)
        {
            throw new Exception(line);
        }
    }

    public boolean checkStock()
    {
        writer.println("INVENTORY " + traderID);

        String line = reader.nextLine();

        return Boolean.parseBoolean(line);
    }

    @Override
    public void close()
    {
        reader.close();
        writer.close();
    }
}