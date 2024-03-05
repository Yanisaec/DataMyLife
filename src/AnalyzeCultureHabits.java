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

public class AnalyzeCultureHabits extends JFrame {
    private JTable mediaOccurencesTable;
    private String[] list_of_media_types;
    private String[][] occurencesOfMedia;

    public AnalyzeCultureHabits() {
        setTitle("Analyze Culture Habits");
        setSize(600, 200);
        setIconImage(new ImageIcon("C:/Users/boite/Desktop/DataMyLife/Icons/function.png").getImage());
        setLocationRelativeTo(null); // Center the window on the screen

        list_of_media_types = CSVManagerCulture.getAllElements("Media");
        occurencesOfMedia = CSVManagerCulture.getOccurencesOfMedia();

        String[] columnsNames = {"Medium", "Occurences of the Medium"};
        DefaultTableModel modelMedia = new DefaultTableModel(occurencesOfMedia, columnsNames);
        // Create the JTable with the DefaultTableModel
        mediaOccurencesTable = new JTable(modelMedia);
        // Add the table to a scroll pane
        JScrollPane scrollPaneMedia = new JScrollPane(mediaOccurencesTable);
        scrollPaneMedia.setPreferredSize(new Dimension(600,200));
        // Add the scroll pane to the frame

        // SET EVERYTHING IN THE WINDOW //

        Box verticalBoxForTables = Box.createVerticalBox();
        verticalBoxForTables.add(scrollPaneMedia);

        add(verticalBoxForTables);

        setVisible(true);
    }
}
