import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.*;
import javax.swing.*;

public class ServerProgram extends JFrame
{
    private final static int portNumber = 8000;
    private final StockMarket market = new StockMarket(this);

    JPanel panel;

    JTextArea display;
    JTextArea listOfTraders;

    JScrollPane scroll1;
    JScrollPane scroll2;

    private ServerProgram()
    {
        panel = new JPanel();

        display = new JTextArea(30,30);
        display.setEditable(false);

        listOfTraders = new JTextArea(30,30);
        listOfTraders.setEditable(false);

        scroll1 = new JScrollPane(display);
        scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        scroll2 = new JScrollPane(listOfTraders);
        scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(panel, BorderLayout.CENTER);
        panel.add(scroll1);
        panel.add(scroll2);

        display.append("Awaiting connections ...");
        listOfTraders.append("Traders Online :");
    }

    public static void main(String[] args)
    {
        ServerProgram frame = new ServerProgram();
        frame.setSize(700, 530);
        frame.setTitle("Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);

        frame.runServer();
    }

    private void runServer()
    {
        ServerSocket serverSocket = null;

        try
        {
            serverSocket = new ServerSocket(portNumber);

            while (true)
            {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket, market, this)).start();
            }

        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}