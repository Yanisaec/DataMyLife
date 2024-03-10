import javax.swing.*;

import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AnalyzeExerciseWindow extends JFrame {

    private String[][] weightLiftedPerExercise;
    private Float weightLifted = -1.0f;
    private JLabel weightLiftedLabel;

    private String[][] maxWeightPerExercise;
    private Float maxWeight = -1.0f;
    private JLabel maxWeightLabel;

    private String[][] bestTotalWeightPerExercise;
    private Float bestTotal = -1.0f;
    private JLabel bestTotalLabel;

    private String[][] bestTotalWeightPerOneSetExercise;
    private Float bestTotalSet = -1.0f;
    private JLabel bestTotalSetLabel;

    public AnalyzeExerciseWindow(String exercise) {
        setTitle(exercise + " Analysis");
        setSize(600, 200);
        // setIconImage(new ImageIcon("C:/Users/boite/Desktop/DataMyLife/Icons/function.png").getImage());
        setLocationRelativeTo(null); // Center the window on the screen

        weightLiftedPerExercise = CSVManagerWorkout.getweightLiftedPerExercise();
        maxWeightPerExercise = CSVManagerWorkout.getmaxWeightPerExercise();
        bestTotalWeightPerExercise = CSVManagerWorkout.getbestTotalWeightPerExercise();
        bestTotalWeightPerOneSetExercise = CSVManagerWorkout.getbestTotalWeightPerOneSetExercise();

        for (String[] s : weightLiftedPerExercise) {
            if (s[0].equals(exercise)) {
                weightLifted = Float.parseFloat(s[1]);
            }
        }
        for (String[] s : maxWeightPerExercise) {
            if (s[0].equals(exercise)) {
                maxWeight = Float.parseFloat(s[1]);
            }
        }
        for (String[] s : bestTotalWeightPerExercise) {
            if (s[0].equals(exercise)) {
                bestTotal = Float.parseFloat(s[1]);
            }
        }
        for (String[] s : bestTotalWeightPerOneSetExercise) {
            if (s[0].equals(exercise)) {
                bestTotalSet = Float.parseFloat(s[1]);
            }
        }

        weightLiftedLabel = new JLabel("The total amount of weight lifted on " + exercise + " is " + weightLifted + " kg");

        maxWeightLabel = new JLabel("The maximum wieght lifted on " + exercise + " during one set is " + maxWeight + " kg");

        bestTotalLabel = new JLabel("The best total amount of weight lifted on " + exercise + " in a single workout is " + bestTotal + " kg");

        bestTotalSetLabel = new JLabel("The best total amount of weight lifted on " + exercise + " in a single set is " + bestTotalSet + " kg");

        // SET EVERYTHING IN THE WINDOW //

        Box verticalBox = Box.createVerticalBox();
        
        verticalBox.add(weightLiftedLabel);
        verticalBox.add(maxWeightLabel);
        verticalBox.add(bestTotalLabel);
        verticalBox.add(bestTotalSetLabel);

        add(verticalBox);
        setVisible(true);
    }

}
