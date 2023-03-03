using System;
using System.IO;
using System.Net.Sockets;

public class Client : IDisposable
{
    int portNumber = 8000;

    private StreamReader reader;
    private StreamWriter writer;

    private  int traderID;

    public Client()
    {
        TcpClient tcpClient = new TcpClient("localhost" , portNumber);
        NetworkStream stream = tcpClient.GetStream();

        reader = new StreamReader(stream);
        writer = new StreamWriter(stream);

        this.traderID = int.Parse(reader.ReadLine());

        string line = reader.ReadLine();

        if (line != "SUCCESS")
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
        writer.WriteLine("TRADERS");
        writer.Flush();

        string line = reader.ReadLine();
        int numberOfTradersOnline = int.Parse(line);

        int[] listOfTraders = new int[numberOfTradersOnline];

        for (int i = 0; i < numberOfTradersOnline; i++)
        {
            line = reader.ReadLine();
            listOfTraders[i] = int.Parse(line);
        }

        return listOfTraders;
    }

    public int whoHasStock()
    {
        writer.WriteLine("WHO");
        writer.Flush();

        string line = reader.ReadLine();

        return int.Parse(line);
    }

    public void exchangeStock(int traderID)
    {
        writer.WriteLine("EXCHANGE " + this.traderID + " " + traderID);
        writer.Flush();

        string line = reader.ReadLine();

        if (line != "SUCCESS")
        {
            throw new Exception(line);
        }
    }

    public bool checkStock()
    {
        writer.WriteLine("INVENTORY " + traderID);
        writer.Flush();

        string line = reader.ReadLine();

        return bool.Parse(line);
    }

    public void Dispose()
    {
        reader.Close();
        writer.Close();
    }
}