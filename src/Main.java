import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Hashtable;

public class Main extends JFrame {

    private final Vis contents;
    private Hashtable<String, Country> countries;

    public Main() {

        countries = new Hashtable<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,800);

        //vis
        contents = new Vis();

        //slider
        JSlider j = new JSlider(1885, 2019, 2000);
        j.setLabelTable(j.createStandardLabels(10));
        j.setPaintLabels(true);
        j.setPaintTicks(true);

        j.addChangeListener(new ChangeListener()  {

            public void stateChanged(ChangeEvent e) {

                contents.setCurrentYear(j.getValue());
                contents.repaint();
            }
        });

        //panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(contents, BorderLayout.CENTER);
        panel.add(j, BorderLayout.SOUTH);


        setContentPane(panel);
        setTitle("Sunburst Chart: Cremation vs. Burial and Other Disposition Techniques");
        var abigail = createMenu();
        setJMenuBar(abigail);
        setVisible(true);

    }

    private JMenuBar createMenu() {

        JMenuBar mb = new JMenuBar();
        JMenu options = new JMenu("Options");
        JMenu colors = new JMenu("Color Scheme");
        JMenu about = new JMenu("About");

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

            conn.close();
            contents.setCountries(countries);

        } catch (SQLException throwables) {

            throwables.printStackTrace();
        }

        JMenuItem reset = new JMenuItem("Reset"); //queries done :)
        reset.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                contents.reset();
            }
        });

        JMenuItem pinkAndPurple = new JMenuItem("Angie"); //queries done :)
        pinkAndPurple.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                contents.changeColorScheme(Vis.ColorScheme.PINKANDPURPLE);
            }
        });

        JMenuItem coralAndBlue = new JMenuItem("Ryan"); //queries done :)
        coralAndBlue.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                contents.changeColorScheme(Vis.ColorScheme.CORALANDBLUE);
            }
        });

        JMenuItem blueAndYellow = new JMenuItem("Ikea"); //queries done :)
        blueAndYellow.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                contents.changeColorScheme(Vis.ColorScheme.BLUEANDYELLOW);
            }
        });

        JMenuItem aboutLink = new JMenuItem("About"); //queries done :)
        aboutLink.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(null,
                        "Statistics:\n" +
                                "https://www.cremation.org.uk/\n" +
                                "Papers:\n" +
                                "Stasko, Catrambone, et al. 2000\n" +
                                "Stasko and Zhang, 2000\n" +
                                "Other:\n" +
                                "https://www.cc.gatech.edu/gvu/ii/sunburst/"
                );
            }
        });

        //adding questions to the menu bar
        options.add(reset);
        mb.add(options);

        colors.add(pinkAndPurple);
        colors.add(coralAndBlue);
        colors.add(blueAndYellow);
        mb.add(colors);

        about.add(aboutLink);
        mb.add(about);

        return mb;
    }

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(Main::new);
    }
}
