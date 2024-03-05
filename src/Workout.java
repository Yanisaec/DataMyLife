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

    private AutoSuggestionBox workoutTypeSuggestionBox;
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

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String formattedDate = currentDate.format(formatter);

        // Create a JTextField with default text
        dateBar = new JTextField();
        dateBar.setColumns(6);
        dateBar.setText(formattedDate);
        dateLabel = new JLabel("Date (daymonthyear)");

        workoutTypeSuggestionBox = new AutoSuggestionBox("Workout");
        exerciseTypeSuggestionBox = new AutoSuggestionBox("Exercise");

        weightLabel = new JLabel("Weight");
        weightField = new JTextField();
        weightField.setColumns(3);

        repsLabel = new JLabel("Repetitions");
        repsField = new JTextField();
        repsField.setColumns(10);

        restLabel = new JLabel("Rest");
        restField = new JTextField();
        restField.setColumns(3);

        addToWorkouts = new JButton("Add to the Workouts file");
        addToWorkouts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addToWorkouts();
                    exerciseTypeSuggestionBox.setText("");
                    weightField.setText("");
                    repsField.setText("");
                    restField.setText("");
                    // updateLastWorkouts();
                } catch (CSVManagerWorkout.CustomException ex) {
                    JOptionPane.showMessageDialog(addToWorkouts, ex);
                }   
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

        JPanel newWorkoutJPanel = new JPanel(new FlowLayout());
        newWorkoutJPanel.add(newWorkoutLabel);
        newWorkoutJPanel.add(addWorkoutField);
        newWorkoutJPanel.add(addWorkout);

        JPanel newExerciseJPanel = new JPanel(new FlowLayout());
        newExerciseJPanel.add(newExerciseLabel);
        newExerciseJPanel.add(addExerciseField);
        newExerciseJPanel.add(addExercise);

        JPanel datePanel = new JPanel(new FlowLayout());
        datePanel.add(dateLabel);
        datePanel.add(dateBar);

        JPanel workoutAndExercisePanel = new JPanel(new FlowLayout());
        workoutAndExercisePanel.add(workoutTypeSuggestionBox);
        workoutAndExercisePanel.add(exerciseTypeSuggestionBox); 

        JPanel weightRepsRestPanel = new JPanel(new FlowLayout());
        weightRepsRestPanel.add(weightLabel);
        weightRepsRestPanel.add(weightField);
        weightRepsRestPanel.add(repsLabel);
        weightRepsRestPanel.add(repsField);
        weightRepsRestPanel.add(restLabel);
        weightRepsRestPanel.add(restField);

        Box horizontalBoxaddToWorkouts = Box.createHorizontalBox();
        horizontalBoxaddToWorkouts.add(Box.createHorizontalGlue());
        horizontalBoxaddToWorkouts.add(addToWorkouts);
        horizontalBoxaddToWorkouts.add(Box.createHorizontalGlue());


        Box horizontalBoxMenuButton = Box.createHorizontalBox();
        horizontalBoxMenuButton.add(Box.createHorizontalGlue());
        horizontalBoxMenuButton.add(menuButton);
        horizontalBoxMenuButton.add(Box.createHorizontalGlue());

        Box mainBox = Box.createVerticalBox();
        mainBox.add(newWorkoutJPanel);
        mainBox.add(newExerciseJPanel);
        mainBox.add(datePanel);
        mainBox.add(workoutAndExercisePanel);
        mainBox.add(weightRepsRestPanel);
        mainBox.add(horizontalBoxaddToWorkouts);
        mainBox.add(horizontalBoxMenuButton);

        add(mainBox);
        pack();
        setVisible(true);
    }

    private void openMenuWindow() {
        new Menu();
        dispose(); // Close the current Workout window
    }

    private void addToWorkouts() throws CSVManagerWorkout.CustomException {
        String workout = workoutTypeSuggestionBox.getText();
        String weight = weightField.getText();
        String exercise = exerciseTypeSuggestionBox.getText();
        String reps = repsField.getText();
        String rest = restField.getText();
        String date = dateBar.getText();
        if (!date.equals("") && !workout.equals("") && !weight.equals("") && !exercise.equals("") && !reps.equals("") && !rest.equals("")) {
            CSVManagerWorkout.addToWorkouts(date, workout, exercise, weight, reps, rest);
            // Optionally, update the AutoSuggestionBox with the new choices
            // sourceNameSuggestionBox.loadSources();
        } else {
            // Handle the case where the input is empty
            JOptionPane.showMessageDialog(this, "Please fill all informations.");
        }
    }
}
