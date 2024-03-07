import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.awt.event.FocusAdapter;
// import java.awt.event.FocusEvent;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Eating extends JFrame {  
    private JButton seeIngredients;
  
    private JButton newIngredient;
    private JTextField newChoiceTextField;
    private JLabel gradeLabel;
    private JTextField newChoiceGradeField;
    private JLabel newChoice;

    private JTextField dateBar;
    private JLabel dateLabel;

    private AutoSuggestionBox ingredientBox;
    private AutoSuggestionBox mealBox;
    private JButton addToFile;
    
    private JScrollPane lastDayScrollPane;
    private JTextArea lastDays;

    private JLabel deleteDateLabel;
    private JTextField deleteDateTextField;
    private JButton deleteDateButton;

    private JButton moreInfoButton;

    private JButton menuButton;
    

    public Eating() {
        setTitle("Eating");
        setSize(650, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("C:/Users/boite/Desktop/DataMyLife/Icons/chicken.jpg").getImage());
        setLocationRelativeTo(null); // Center the window on the screen

        gradeLabel = new JLabel("Ingredient grading");

        deleteDateLabel = new JLabel("Date to delete from the file");
        deleteDateTextField = new JTextField();
        deleteDateTextField.setColumns(8);

        deleteDateButton = new JButton("Delete");
        deleteDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateToDelete = deleteDateTextField.getText();
                CSVManagerEating.deleteLinesWithDate(dateToDelete);
                deleteDateTextField.setText("");
                updateLastDays();
            }
        });

        moreInfoButton = new JButton("Analyze eating habits");
        moreInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMoreInfoWindow();
            }
        });

        newChoice = new JLabel("Ingredient to add");

        dateLabel = new JLabel("Date (daymonthyear)");

        seeIngredients = new JButton("See Ingredients");
        seeIngredients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new IngredientsWindow();
            }
        });

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String formattedDate = currentDate.format(formatter);

        // Create a JTextField with default text
        dateBar = new JTextField();
        dateBar.setColumns(6);
        dateBar.setText(formattedDate);

        // Create a JTextField for showing the last ten days
        lastDays = new JTextArea(5,100);
        lastDays.setEditable(false);
        lastDays.setLineWrap(false);
        lastDays.setWrapStyleWord(true);

        // Create a JScrollPane and add the JTextArea to it
        lastDayScrollPane = new JScrollPane(lastDays);
        lastDayScrollPane.setPreferredSize(new Dimension(600, 100));

        updateLastDays();

        // Create a text field for new choices
        newChoiceTextField = new JTextField();
        newChoiceTextField.setColumns(20);
        newChoiceGradeField = new JTextField();
        newChoiceGradeField.setColumns(1);
        
        // Create a button to add to the eating file
        addToFile = new JButton("Add to the file");
        addToFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addToFile();
                    ingredientBox.setText("");
                } catch (CSVManagerEating.CustomException ex) {
                    JOptionPane.showMessageDialog(addToFile, ex);
                }
            }
        });

        // Create a button to add new ingredient
        newIngredient = new JButton("Add Ingredient");
        newIngredient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewChoice();
                newChoiceTextField.setText("");
                newChoiceGradeField.setText("");
            }
        });

        // Create an instance of AutoSuggestionBox
        ingredientBox = new AutoSuggestionBox("Ingredients");
        mealBox = new AutoSuggestionBox("Meals");

        // Create a "Menu" button
        menuButton = new JButton("Menu");
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMenuWindow();
            }
        });

        // SET EVERYTHING IN THE WINDOW //

        Box mainBox = Box.createVerticalBox();

        JPanel datePanel = new JPanel(new FlowLayout());
        datePanel.add(dateLabel);
        datePanel.add(dateBar);

        JPanel newIngredPanel = new JPanel(new FlowLayout());
        newIngredPanel.add(newChoice);
        newIngredPanel.add(newChoiceTextField);
        newIngredPanel.add(gradeLabel);
        newIngredPanel.add(newChoiceGradeField);
        
        JPanel mealAndIngredientPanel = new JPanel(new FlowLayout());
        mealAndIngredientPanel.add(ingredientBox);
        mealAndIngredientPanel.add(mealBox);   
        // subPanel4.add(addToFile, BorderLayout.SOUTH); 
        
        JPanel deleteDatePanel = new JPanel(new FlowLayout());
        deleteDatePanel.add(deleteDateLabel);
        deleteDatePanel.add(deleteDateTextField);
        deleteDatePanel.add(deleteDateButton);

        Box horizontalBoxAddToFile = Box.createHorizontalBox();
        horizontalBoxAddToFile.add(Box.createHorizontalGlue());
        horizontalBoxAddToFile.add(addToFile);
        horizontalBoxAddToFile.add(Box.createHorizontalGlue());

        Box horizontalBoxMenuButton = Box.createHorizontalBox();
        horizontalBoxMenuButton.add(Box.createHorizontalGlue());
        horizontalBoxMenuButton.add(menuButton);
        horizontalBoxMenuButton.add(Box.createHorizontalGlue());

        Box horizontalBoxAddIngredientButton = Box.createHorizontalBox();
        horizontalBoxAddIngredientButton.add(Box.createHorizontalGlue());
        horizontalBoxAddIngredientButton.add(newIngredient);
        horizontalBoxAddIngredientButton.add(Box.createHorizontalGlue());

        Box horizontalBoxSeeIngredientsButton = Box.createHorizontalBox();
        horizontalBoxSeeIngredientsButton.add(Box.createHorizontalGlue());
        horizontalBoxSeeIngredientsButton.add(seeIngredients);
        horizontalBoxSeeIngredientsButton.add(Box.createHorizontalGlue());

        Box horizontalMoreInfoButton = Box.createHorizontalBox();
        horizontalMoreInfoButton.add(Box.createHorizontalGlue());
        horizontalMoreInfoButton.add(moreInfoButton);
        horizontalMoreInfoButton.add(Box.createHorizontalGlue());

        JPanel lastDaysPanel = new JPanel(new FlowLayout());
        lastDaysPanel.add(lastDayScrollPane);

        // Add components to the verticalBox
        mainBox.add(horizontalBoxSeeIngredientsButton);
        mainBox.add(newIngredPanel);
        mainBox.add(horizontalBoxAddIngredientButton);
        mainBox.add(Box.createVerticalStrut(20)); // Add some vertical spacing
        mainBox.add(datePanel);
        mainBox.add(mealAndIngredientPanel);
        mainBox.add(horizontalBoxAddToFile);
        mainBox.add(Box.createVerticalStrut(10)); // Add some vertical spacing
        mainBox.add(lastDaysPanel);
        mainBox.add(deleteDatePanel);
        mainBox.add(Box.createVerticalStrut(25)); // Add some vertical spacing
        mainBox.add(horizontalMoreInfoButton);
        mainBox.add(Box.createVerticalStrut(25)); // Add some vertical spacing
        mainBox.add(horizontalBoxMenuButton);
        
        add(mainBox, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    private void openMenuWindow() {
        new Menu();
        dispose(); // Close the current Workout window
    }

    private void openMoreInfoWindow() {
        new AnalyzeEatingHabits();
    }

    private void addNewChoice() {
        String newChoice = newChoiceTextField.getText();
        String grade = newChoiceGradeField.getText();
        if (!newChoice.isEmpty()) {
            CSVManagerEating.addChoice(newChoice, grade);
            // Optionally, update the AutoSuggestionBox with the new choices
            ingredientBox.loadIngredients();
        } else {
            // Handle the case where the input is empty
            JOptionPane.showMessageDialog(this, "Please enter a choice.");
        }
    }

    private void addToFile() throws CSVManagerEating.CustomException {
        String meal = mealBox.getText();
        String ingredient = ingredientBox.getText();
        String date = dateBar.getText();
        if (!date.equals("") && !ingredient.equals("") && !meal.equals("")) {
            CSVManagerEating.addToFile(date, meal, ingredient);
            // Optionally, update the AutoSuggestionBox with the new choices
            ingredientBox.loadIngredients();
            updateLastDays();
        } else {
            // Handle the case where the input is empty
            JOptionPane.showMessageDialog(this, "Please fill all informations.");
        }
    }

    private void updateLastDays() {
        List<String[]> lastDayss = CSVManagerEating.getMealsAndIngredients();
        lastDays.setText("");
        String fullText = "";
        for (int i = 0; i < lastDayss.size(); i++) {
            String dateBadFormat = lastDayss.get(i)[0].toString();
            DateTimeFormatter originalFormatter = DateTimeFormatter.ofPattern("ddMMyyyy");
            LocalDate datess = LocalDate.parse(dateBadFormat, originalFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String formattedString = datess.format(outputFormatter);
            String day = "Date : " + formattedString + " || Matin : " + lastDayss.get(i)[1].toString().replaceAll("\\[|\\]","") + " || Midi : " + lastDayss.get(i)[2].toString().replaceAll("\\[|\\]","") + " || Soir : " + lastDayss.get(i)[3].toString().replaceAll("\\[|\\]","") + " || Snacks : " + lastDayss.get(i)[4].toString().replaceAll("\\[|\\]","");
            fullText = fullText + day + "\n"; 
        }
        lastDays.setText(fullText);
    }
}
