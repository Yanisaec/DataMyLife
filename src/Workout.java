import javax.swing.*;

import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Workout extends JFrame {
    private JButton addWorkout;
    private JTextField addWorkoutField;
    private JLabel newWorkoutLabel;

    private JButton addExercise;
    private JTextField addExerciseField;
    private JLabel newExerciseLabel;

    private JTextField dateBar;
    private JLabel dateLabel;

    private AutoSuggestionBox workoutTypSuggestionBox;
    private AutoSuggestionBox exerciseTypeSuggestionBox;

    private JTextField weightField;
    private JLabel weightLabel;
    private JTextField repsField;
    private JLabel repsLabel;
    private JTextField restField;
    private JLabel restLabel;

    private JButton addToWorkouts;

    private JScrollPane lastWorkoutsScrollPane;
    private JTextArea lastWorkoutsTextArea;

    private JLabel deleteDateLabel;
    private JTextField deleteDateTextField;
    private JButton deleteDateButton;

    private JButton moreInfoButton;

    private JButton menuButton;

    public Workout() {
        setTitle("Workout");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen

        newWorkoutLabel = new JLabel("Workout type");
        addWorkoutField = new JTextField();
        addWorkoutField.setColumns(20);
        addWorkout = new JButton("Add workout");
        addWorkout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newWorkoutString= addWorkoutField.getText();
                // CSVManagerWorkout.addWorkout(newWorkoutString);
                addWorkoutField.setText("");
            }
        });

        newExerciseLabel = new JLabel("Exercise name");
        addExerciseField = new JTextField();
        addExerciseField.setColumns(20);
        addExercise = new JButton("Add Exercise");
        addExercise.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newExerciseString= addExerciseField.getText();
                // CSVManagerWorkout.addExercise(newExerciseString);
                addExerciseField.setText("");
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

        Box horizontalBoxMenuButton = Box.createHorizontalBox();
        horizontalBoxMenuButton.add(Box.createHorizontalGlue());
        horizontalBoxMenuButton.add(menuButton);
        horizontalBoxMenuButton.add(Box.createHorizontalGlue());

        Box mainBox = Box.createVerticalBox();
        mainBox.add(horizontalBoxMenuButton);

        add(mainBox);
        pack();
        setVisible(true);
    }

    private void openMenuWindow() {
        new Menu();
        dispose(); // Close the current Workout window
    }
}
