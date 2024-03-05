import javax.swing.*;

import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Culture extends JFrame {
    private JButton addMedium;
    private JTextField addMediumField;
    private JLabel newMedium;

    private JTextField dateBar;
    private JLabel dateLabel;

    private JLabel mediumNameLabel;
    private JTextField mediumNameTextField;
    private AutoSuggestionBox mediumTypeSuggestionBox;
    private AutoSuggestionBox sourceNameSuggestionBox;

    private JButton addToCulture;

    private JScrollPane lastMediaScrollPane;
    private JTextArea lastMediaTextArea;

    private JLabel deleteDateLabel;
    private JTextField deleteDateTextField;
    private JButton deleteDateButton;

    private JButton moreInfoButton;

    private JButton menuButton;

    public Culture() {
        setTitle("Workout");
        setSize(650, 650);
        setIconImage(new ImageIcon("C:/Users/boite/Desktop/DataMyLife/Icons/book.jpg").getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen

        newMedium = new JLabel("Medium");
        addMediumField = new JTextField();
        addMediumField.setColumns(20);
        addMedium = new JButton("Add medium");
        addMedium.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newMediumString= addMediumField.getText();
                CSVManagerCulture.addMedium(newMediumString);
                addMediumField.setText("");
            }
        });

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String formattedDate = currentDate.format(formatter);

        // Create a JTextField with default text
        dateBar = new JTextField();
        dateBar.setColumns(6);
        dateBar.setText(formattedDate);
        dateLabel = new JLabel("Date (daymonthyear)");

        mediumNameLabel = new JLabel("Medium name");
        mediumNameTextField = new JTextField();
        mediumNameTextField.setColumns(20);
        mediumTypeSuggestionBox = new AutoSuggestionBox("Media");
        sourceNameSuggestionBox = new AutoSuggestionBox("Source");

        addToCulture = new JButton("Add to the Culture file");
        addToCulture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addToCulture();
                    mediumTypeSuggestionBox.setText("");
                    sourceNameSuggestionBox.setText("");
                    mediumNameTextField.setText("");
                    updateLastMedia();
                } catch (CSVManagerCulture.CustomException ex) {
                    JOptionPane.showMessageDialog(addToCulture, ex);
                }   
            }
        });

        // Create a JTextField for showing the last ten days
        lastMediaTextArea = new JTextArea(5,100);
        lastMediaTextArea.setEditable(false);
        lastMediaTextArea.setLineWrap(false);
        lastMediaTextArea.setWrapStyleWord(true);

        // Create a JScrollPane and add the JTextArea to it
        lastMediaScrollPane = new JScrollPane(lastMediaTextArea);
        lastMediaScrollPane.setPreferredSize(new Dimension(500, 100));

        updateLastMedia();

        deleteDateLabel = new JLabel("Date to delete from the file");
        deleteDateTextField = new JTextField();
        deleteDateTextField.setColumns(8);

        deleteDateButton = new JButton("Delete");
        deleteDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateToDelete = deleteDateTextField.getText();
                CSVManagerCulture.deleteLinesWithDate(dateToDelete);
                deleteDateTextField.setText("");
                updateLastMedia();
            }
        });

        moreInfoButton = new JButton("Analyze culture habits");
        moreInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMoreInfoWindow();
            }
        });

        // Create a "Menu" button
        menuButton = new JButton("Menu");
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMenuWindow();
            }
        });

        // SET EVERYTHING IN THE WINDOW //

        JPanel newMediumJPanel = new JPanel(new FlowLayout());
        newMediumJPanel.add(newMedium);
        newMediumJPanel.add(addMediumField);
        newMediumJPanel.add(addMedium);

        JPanel datePanel = new JPanel(new FlowLayout());
        datePanel.add(dateLabel);
        datePanel.add(dateBar);

        JPanel mediumName = new JPanel(new FlowLayout());
        mediumName.add(mediumNameLabel);
        mediumName.add(mediumNameTextField);

        JPanel mediumAndSourcePanel = new JPanel(new FlowLayout());
        mediumAndSourcePanel.add(mediumTypeSuggestionBox);
        mediumAndSourcePanel.add(sourceNameSuggestionBox); 

        Box horizontalBoxAddToCulture = Box.createHorizontalBox();
        horizontalBoxAddToCulture.add(Box.createHorizontalGlue());
        horizontalBoxAddToCulture.add(addToCulture);
        horizontalBoxAddToCulture.add(Box.createHorizontalGlue());

        JPanel lastMediaPanel = new JPanel(new FlowLayout());
        lastMediaPanel.add(lastMediaScrollPane);

        JPanel deleteDatePanel = new JPanel(new FlowLayout());
        deleteDatePanel.add(deleteDateLabel);
        deleteDatePanel.add(deleteDateTextField);
        deleteDatePanel.add(deleteDateButton);

        Box horizontalMoreInfoButton = Box.createHorizontalBox();
        horizontalMoreInfoButton.add(Box.createHorizontalGlue());
        horizontalMoreInfoButton.add(moreInfoButton);
        horizontalMoreInfoButton.add(Box.createHorizontalGlue());

        Box horizontalBoxMenuButton = Box.createHorizontalBox();
        horizontalBoxMenuButton.add(Box.createHorizontalGlue());
        horizontalBoxMenuButton.add(menuButton);
        horizontalBoxMenuButton.add(Box.createHorizontalGlue());
        
        Box mainBox = Box.createVerticalBox();
        mainBox.add(newMediumJPanel);
        mainBox.add(datePanel);
        mainBox.add(mediumName);
        mainBox.add(mediumAndSourcePanel);
        mainBox.add(horizontalBoxAddToCulture);
        mainBox.add(lastMediaPanel);
        mainBox.add(deleteDatePanel);
        mainBox.add(horizontalMoreInfoButton);
        mainBox.add(Box.createVerticalStrut(25)); // Add some vertical spacing
        mainBox.add(horizontalBoxMenuButton);

        add(mainBox);
        pack();
        setVisible(true);
    }

    private void openMoreInfoWindow() {
        new AnalyzeCultureHabits();
    }

    private void openMenuWindow() {
        new Menu();
        dispose(); // Close the current Workout window
    }

    private void addToCulture() throws CSVManagerCulture.CustomException {
        String medium = mediumTypeSuggestionBox.getText();
        String source = sourceNameSuggestionBox.getText();
        String name = mediumNameTextField.getText();
        String date = dateBar.getText();
        if (!date.equals("") && !source.equals("") && !name.equals("") && !medium.equals("")) {
            CSVManagerCulture.addToCulture(date, medium, name, source);
            // Optionally, update the AutoSuggestionBox with the new choices
            sourceNameSuggestionBox.loadSources();
            // updateLast10Days();
        } else {
            // Handle the case where the input is empty
            JOptionPane.showMessageDialog(this, "Please fill all informations.");
        }
    }

    private void updateLastMedia() {
        List<String[]> lastMedia = CSVManagerCulture.getLastMedia();
        lastMediaTextArea.setText("");
        String fullText = "";
        for (int i = 0; i < lastMedia.size(); i++) {
            String dateBadFormat = lastMedia.get(i)[0].toString();
            DateTimeFormatter originalFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
            LocalDate datess = LocalDate.parse(dateBadFormat, originalFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedString = datess.format(outputFormatter);
            String day = "Date : " + formattedString + " || Name : " + lastMedia.get(i)[2].toString().replaceAll("\\[|\\]","") + " || Source : " + lastMedia.get(i)[3].toString().replaceAll("\\[|\\]","") + " || Medium : " + lastMedia.get(i)[1].toString().replaceAll("\\[|\\]","");
            fullText = fullText + day + "\n"; 
        }
        lastMediaTextArea.setText(fullText);
    }
}

