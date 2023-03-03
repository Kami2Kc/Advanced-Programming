using System;

public class ClientProgram
{
    static void Main(string[] args)
    {
        try
        {
            using (Client client = new Client())
            {
                while (true)
                {
                    Console.WriteLine($"Logged in as Trader : {client.whatIsID()}");

                    int[] tradersOnline = client.getListOfTradersOnline();

                    Console.WriteLine("Traders online : ");

                    foreach (int i in tradersOnline)
                    {
                        Console.WriteLine($"Trader : {i}");
                    }

                    if (client.checkStock())
                    {
                        Console.WriteLine("You currently have the stock.\nChoose who to give stock to from list of traders");
                        try
                        {
                            client.exchangeStock(int.Parse(Console.ReadLine()));
                        }
                        catch (Exception e)
                        {
                            Console.WriteLine(e);
                        }
                        
                    }else
                    {  
                        Console.WriteLine("Who has stock :");

                        switch (client.whoHasStock())
                        {
                            case 0:
                                Console.WriteLine("Market");
                                break;

                            default:
                                Console.WriteLine($"Trader :  {client.whoHasStock()}");
                                break;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            Console.WriteLine(e.Message);
        }
    }
}