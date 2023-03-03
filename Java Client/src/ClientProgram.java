import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.concurrent.TimeUnit; //Can I even use this ?

public class ClientProgram extends JFrame
{
    JPanel panel;

    JTextArea display;
    JTextArea displayTraders;

    JTextField input;

    JLabel inputLabel;

    JScrollPane scroll1;
    JScrollPane scroll2;

    ClientProgram()
    {
        panel = new JPanel();

        input = new JTextField(10);
        input.setEditable(false);

        inputLabel = new JLabel("Input :");

        display = new JTextArea(30,30);
        display.setEditable(false);

        displayTraders = new JTextArea(30,30);
        displayTraders.setEditable(false);

        scroll1 = new JScrollPane(display);
        scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        scroll2 = new JScrollPane(displayTraders);
        scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(panel);

        panel.add(scroll1);
        panel.add(scroll2);
        panel.add(inputLabel);
        panel.add(input);

        setSize(700, 560);
        setTitle("Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args)
    {
        new ClientProgram().run();
    }

    public void run()
    {
        try (Client client = new Client())
        {
            input.addActionListener(new inputHandler(this, client, 1));

            while (true)
            {
                TimeUnit.SECONDS.sleep(1);
                display.setText(null);
                displayTraders.setText(null);
                display.append("=============================\nLogged in as Trader : " + client.whatIsID());

                int[] tradersOnline = client.getListOfTradersOnline();

                displayTraders.append("Traders online : ");

                for (int ID : tradersOnline)
                {
                    displayTraders.append("\nTrader : " + ID);
                }

                if (client.checkStock())
                {
                    input.setEditable(true);
                    display.append("\nYou currently have the stock.\n\nChoose who to give stock to from list of traders : ");
                }
                else
                {
                    display.append("\nWho has stock : ");
                    input.setEditable(false);
                    switch (client.whoHasStock())
                    {
                        case 0:
                            display.append("\nMarket");
                            break;

                        default:
                            display.append("\nTrader : " + client.whoHasStock());
                            break;
                    }
                }
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}

class inputHandler implements ActionListener
{
    private ClientProgram clientP;
    private Client client;
    private int action;

    inputHandler(ClientProgram clientP, Client client, int action)
    {
        this.clientP = clientP;
        this.client = client;
        this.action = action;
    }

    public void actionPerformed(ActionEvent e)
    {
        if (this.action == 1)
        {
            try
            {
                client.exchangeStock(Integer.parseInt(clientP.input.getText()));
                clientP.input.setText(null);
            } catch (Exception ex)
            {
                clientP.display.append("\n\nInvalid input!");
            }
        }
    }
}