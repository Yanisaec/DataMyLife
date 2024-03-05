import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.io.CSV;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AnalyzeEatingHabits extends JFrame {

    private int[] grades = {1,2,3,4,5};
    private int[] occurencesOfGrades;

    private float averageGrade;
    private JLabel averageGradeLabel;
    private float empiricalVarianceGrade;
    private JLabel varianceLabel;

    private JTable ingredientsTable;
    private String[][] occurencesOfIngredients;

    private JTable averageGradeMealsTable;
    private String[][] averageGradeMeals;

    private JTable averageGradeDaysTable;
    private String[][] averageGradeDays;

    public AnalyzeEatingHabits() {
        averageGradeDays = CSVManagerEating.getAverageDailyGrade();
        averageGradeMeals = CSVManagerEating.getAverageMealGrade();
        occurencesOfIngredients = CSVManagerEating.getOccurencesOfIngredients();
        occurencesOfGrades = CSVManagerEating.getOccurencesOfGrades();
        averageGrade = CSVManagerEating.averageGrade(occurencesOfGrades);
        empiricalVarianceGrade = CSVManagerEating.empiricalVarianceGrade(occurencesOfGrades, averageGrade);

        setTitle("Analyze Eating Habits");
        setSize(600, 600);
        setIconImage(new ImageIcon("C:/Users/boite/Desktop/DataMyLife/Icons/function.png").getImage());
        setLocationRelativeTo(null); // Center the window on the screen

        // Create a bar chart panel and add it to the frame
        ChartPanel chartPanel = createChartPanel();
        // chartPanel.setPreferredSize(new Dimension(560, 250));

        averageGradeLabel = new JLabel("The average grade of the ingredients you eat is " + Float.toString(averageGrade) + "/5");
        varianceLabel = new JLabel("The variance of the grade of the ingredients you eat is " + Float.toString(empiricalVarianceGrade));

        // Column names
        String[] ingredientsColumnNames = {"Ingredient", "Occurences"};
        DefaultTableModel modelIngredients = new DefaultTableModel(occurencesOfIngredients, ingredientsColumnNames);
        // Create the JTable with the DefaultTableModel
        ingredientsTable = new JTable(modelIngredients);
        // Add the table to a scroll pane
        JScrollPane scrollPaneIngredients = new JScrollPane(ingredientsTable);
        scrollPaneIngredients.setPreferredSize(new Dimension(600,200));
        // Add the scroll pane to the frame
        
        // Column names
        String[] mealsColumnNames = {"Meal", "Average grade"};
        DefaultTableModel modelMeals = new DefaultTableModel(averageGradeMeals, mealsColumnNames);
        // Create the JTable with the DefaultTableModel
        averageGradeMealsTable = new JTable(modelMeals);
        // Add the table to a scroll pane
        JScrollPane scrollPaneMeals = new JScrollPane(averageGradeMealsTable);
        scrollPaneMeals.setPreferredSize(new Dimension(600,120));
        // Add the scroll pane to the frame

        // Column names
        String[] daysColumnNames = {"Day", "Average grade"};
        DefaultTableModel modelDays = new DefaultTableModel(averageGradeDays, daysColumnNames);
        // Create the JTable with the DefaultTableModel
        averageGradeDaysTable = new JTable(modelDays);
        // Add the table to a scroll pane
        JScrollPane scrollPaneDays = new JScrollPane(averageGradeDaysTable);
        scrollPaneDays.setPreferredSize(new Dimension(600,200));
        // Add the scroll pane to the frame

        // SET EVERYTHING IN THE WINDOW //

        Box verticalBoxForTables = Box.createVerticalBox();
        verticalBoxForTables.add(scrollPaneMeals);
        verticalBoxForTables.add(scrollPaneIngredients);
        verticalBoxForTables.add(scrollPaneDays);

        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(chartPanel);
        verticalBox.add(averageGradeLabel);
        verticalBox.add(varianceLabel);
        verticalBox.add(verticalBoxForTables);

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

        for (int i = 0; i < grades.length; i++) {
            dataset.addValue(occurencesOfGrades[i], "Occurences", String.valueOf(grades[i]));
        }

        return dataset;
    }

    private JFreeChart createChart(CategoryDataset dataset) {
        return ChartFactory.createBarChart(
                "Grades Occurrences",
                "Grades",
                "Occurrences",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }
}
