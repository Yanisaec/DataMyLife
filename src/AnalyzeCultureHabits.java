import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.io.CSV;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AnalyzeCultureHabits extends JFrame {
    private JTable mediaOccurencesTable;
    // private String[] list_of_media_types;
    private String[][] occurencesOfMedia;

    private JTable mediaGradesTable;
    private String[][] mediaGrades;

    public AnalyzeCultureHabits() {
        setTitle("Analyze Culture Habits");
        setSize(600, 600);
        setIconImage(new ImageIcon("C:/Users/boite/Desktop/DataMyLife/Icons/function.png").getImage());
        setLocationRelativeTo(null); // Center the window on the screen

        // list_of_media_types = CSVManagerCulture.getAllElements("Media");
        occurencesOfMedia = CSVManagerCulture.getOccurencesOfMedia();
        mediaGrades = CSVManagerCulture.getMediaAndGrades();
        Arrays.sort(mediaGrades, Comparator.comparingInt(arr -> Integer.parseInt(arr[1])));
        Collections.reverse(Arrays.asList(mediaGrades));

        String[] columnsNames = {"Medium", "Occurences of the Medium"};
        DefaultTableModel modelMedia = new DefaultTableModel(occurencesOfMedia, columnsNames);
        // Create the JTable with the DefaultTableModel
        mediaOccurencesTable = new JTable(modelMedia);
        // Add the table to a scroll pane
        JScrollPane scrollPaneMediaOccurences = new JScrollPane(mediaOccurencesTable);
        scrollPaneMediaOccurences.setPreferredSize(new Dimension(600,40));
        // Add the scroll pane to the frame

        String[] columnsNamesGrade = {"Medium", "Grade"};
        DefaultTableModel modelMediaGrades = new DefaultTableModel(mediaGrades, columnsNamesGrade);
        // Create the JTable with the DefaultTableModel
        mediaGradesTable = new JTable(modelMediaGrades);
        // Add the table to a scroll pane
        JScrollPane scrollPaneMediaGrades = new JScrollPane(mediaGradesTable);
        scrollPaneMediaGrades.setPreferredSize(new Dimension(600,200));
        // Add the scroll pane to the frame

        // SET EVERYTHING IN THE WINDOW //

        Box verticalBoxForTables = Box.createVerticalBox();
        verticalBoxForTables.add(scrollPaneMediaOccurences);
        verticalBoxForTables.add(scrollPaneMediaGrades);

        add(verticalBoxForTables);

        setVisible(true);
    }
}
