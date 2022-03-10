import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class Main extends JFrame {

    private final Vis contents;
    public Hashtable<String, Country> countries;

    public Main() {

        countries = new Hashtable<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300,700);
        contents = new Vis();
        setContentPane(contents);
        setTitle("Sunburst Chart: Cremation vs. Burial and Other Disposition Techniques");
        var abigail = createMenu();
        setJMenuBar(abigail);
        setVisible(true);
    }

    private JMenuBar createMenu() {

        JMenuBar mb = new JMenuBar();
        JMenu options = new JMenu("Options");

        try {

            Connection conn = DriverManager.getConnection("jdbc:derby:/home/abigail/database/pollster");
            Statement stmt = conn.createStatement();

            ResultSet query = stmt.executeQuery("SELECT cremation_year, country, death_count, cremation_count FROM cremation");

            while (query.next()) {

                int cremation_year = query.getInt(1);
                String country = query.getString(2);
                int death_count = query.getInt(3);
                int cremation_count = query.getInt(4);

                Country c = countries.get(country);
                if (c == null) {

                    countries.put(country, new Country());
                    c = countries.get(country);
                }

                DataPoint d = new DataPoint(cremation_count, death_count);
                c.addData(cremation_year, d);
            }

        } catch (SQLException throwables) {

            throwables.printStackTrace();
        }

        JMenuItem reset = new JMenuItem("Reset"); //queries done :)
        reset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });



        //adding questions to the menu bar
        options.add(reset);
        mb.add(options);
        return mb;
    }

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(Main::new);
    }
}
