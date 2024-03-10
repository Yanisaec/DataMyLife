import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.io.CSV;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AnalyzeWorkoutHabits extends JFrame {
    private int nbOfWorkouts;
    private JLabel nbWorkoutsLabel;
    private Float totalWeightLifted;
    private JLabel totalWeightLiftedLabel;
    private Float totalRest;
    private JLabel totalRestLabel;

    private String[][] workoutsPerMonths;

    private JTable exercisesOccurencesTable;
    private String[][] exercisesOccurences;

    private static AutoSuggestionBox exerciseTypeSuggestionBox;

    private JButton AnalyzeExerciseButton;


    public AnalyzeWorkoutHabits() {
        CSVManagerWorkout.updateData();

        setTitle("Analyze Workout Habits");
        setSize(600, 600);
        // setIconImage(new ImageIcon("C:/Users/boite/Desktop/DataMyLife/Icons/function.png").getImage());
        setLocationRelativeTo(null); // Center the window on the screen

        nbOfWorkouts = CSVManagerWorkout.getNbOfWorkouts();
        totalWeightLifted = CSVManagerWorkout.getTotalWeight();
        totalRest = CSVManagerWorkout.getTotalRest() / 60;

        workoutsPerMonths = CSVManagerWorkout.getWorkoutsPerMonths();

        exercisesOccurences = CSVManagerWorkout.getExercisesOccurences();

        nbWorkoutsLabel = new JLabel("There are " + Integer.toString(nbOfWorkouts) + " recorded workouts");
        totalWeightLiftedLabel = new JLabel("The total amount of weight lifted is equal to " + Float.toString(totalWeightLifted) + " kg");
        totalRestLabel = new JLabel("Total time rested between sets : " + Float.toString(totalRest) + " minutes");

        ChartPanel chartPanel = createChartPanel();

        // Column names
        String[] exercisesColumns = {"Exercises", "Occurences"};
        DefaultTableModel modelExercises = new DefaultTableModel(exercisesOccurences, exercisesColumns);
        // Create the JTable with the DefaultTableModel
        exercisesOccurencesTable = new JTable(modelExercises);
        // Add the table to a scroll pane
        JScrollPane scrollPaneExercises = new JScrollPane(exercisesOccurencesTable);
        scrollPaneExercises.setPreferredSize(new Dimension(600,200));
        // Add the scroll pane to the frame

        exerciseTypeSuggestionBox = new AutoSuggestionBox("Exercise");

        AnalyzeExerciseButton = new JButton("Analyze workout habits");
        AnalyzeExerciseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AnalyzeExercise();
            }
        });

        // SET EVERYTHING IN THE WINDOW //

        Box verticalBoxForTables = Box.createVerticalBox();
        verticalBoxForTables.add(scrollPaneExercises);

        Box horizontalAnalyzeExerciseButton = Box.createHorizontalBox();
        horizontalAnalyzeExerciseButton.add(Box.createHorizontalGlue());
        horizontalAnalyzeExerciseButton.add(AnalyzeExerciseButton);
        horizontalAnalyzeExerciseButton.add(Box.createHorizontalGlue());

        Box verticalBox = Box.createVerticalBox();
        
        verticalBox.add(nbWorkoutsLabel);
        verticalBox.add(chartPanel);
        verticalBox.add(totalWeightLiftedLabel);
        verticalBox.add(totalRestLabel);
        verticalBox.add(verticalBoxForTables);
        verticalBox.add(exerciseTypeSuggestionBox);
        verticalBox.add(horizontalAnalyzeExerciseButton);

        add(verticalBox);
        setVisible(true);

    }

    private ChartPanel createChartPanel() {
        JFreeChart chart = createChart(createDataset());
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 370));
        return chartPanel;
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < workoutsPerMonths.length; i++) {
            dataset.addValue(Integer.parseInt(workoutsPerMonths[i][1]), "Occurences", workoutsPerMonths[i][0]);
        }
        DefaultCategoryDataset reversedDataset = new DefaultCategoryDataset();

        // Get row and column keys
        Object[] rowKeys = dataset.getRowKeys().toArray();
        Object[] columnKeys = dataset.getColumnKeys().toArray();

        // Iterate through the original dataset in reverse order
        for (int i = rowKeys.length - 1; i >= 0; i--) {
            Comparable rowKey = (Comparable) rowKeys[i];
            for (int j = columnKeys.length - 1; j >= 0; j--) {
                Comparable columnKey = (Comparable) columnKeys[j];

                // Add the data to the reversed dataset
                Number value = dataset.getValue(rowKey, columnKey);
                if (value != null) {
                    reversedDataset.addValue(value, rowKey, columnKey);
                }
            }
        }
        return reversedDataset;
    }

    private JFreeChart createChart(CategoryDataset dataset) {
        return ChartFactory.createBarChart(
                "Exercise Occurrences",
                "Exercise",
                "Occurrences",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    private void AnalyzeExercise() {
        String exerciseToAnalyze = exerciseTypeSuggestionBox.getText();
        if (CSVManagerWorkout.isAnExercise(exerciseToAnalyze)) {
            new AnalyzeExerciseWindow(exerciseToAnalyze);
            exerciseTypeSuggestionBox.setText("");
        } else {
            // Handle the case where the input is empty
            JOptionPane.showMessageDialog(this, "Please enter a valid choice.");
        }
    }

}
