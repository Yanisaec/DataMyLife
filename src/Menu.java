import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {
    public Menu() {
        setTitle("Menu");
        setSize(100, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen

        JButton workoutButton = new JButton("Workout");
        JButton eatingButton = new JButton("Eating");
        JButton cultureButton = new JButton("Culture");

        workoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openWorkoutWindow();
            }
        });

        eatingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEatingWindow();
            }
        });

        cultureButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCultureWindow();
            }
        });

        JPanel panel = new JPanel();
        panel.add(workoutButton);
        panel.add(eatingButton);
        panel.add(cultureButton);
        
        add(panel);
        setVisible(true);
    }

    private void openWorkoutWindow() {
        dispose(); // Close the current Menu window
        new Workout();
    }

    private void openEatingWindow() {
        dispose(); // Close the current Menu window
        new Eating();
    }

    private void openCultureWindow() {
        dispose(); // Close the current Menu window
        new Culture();
    }
}
