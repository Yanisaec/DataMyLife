import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class AutoSuggestionBox extends JPanel{
    private final JTextField tf;
    private final JComboBox combo = new JComboBox();
    private final Vector<String> vector = new Vector<String>();
    private String[] choiceList;
    private JButton clearButton;

    public AutoSuggestionBox(String Type) {
        super(new BorderLayout());
        combo.setEditable(true);
        tf = (JTextField) combo.getEditor().getEditorComponent();
        tf.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
            EventQueue.invokeLater(new Runnable() {
            public void run() {
                String text = tf.getText();
                        if(text.length()==0) {
                    combo.hidePopup();
                    setModel(new DefaultComboBoxModel(vector), "");     
                } else {
                    DefaultComboBoxModel m = getSuggestedModel(vector, text);
                    if(m.getSize()==0 || hide_flag) {
                        combo.hidePopup();
                        hide_flag = false;
                    } else {
                        setModel(m, text);
                        combo.showPopup();
                    }
                }
            }
        });
            }
            public void keyPressed(KeyEvent e) {
            String text = tf.getText();
            int code = e.getKeyCode();
            if(code==KeyEvent.VK_ENTER) {
                if(!vector.contains(text)) {
                    setModel(getSuggestedModel(vector, text), text);
                }
            hide_flag = true; 
        }else if(code==KeyEvent.VK_ESCAPE) {
            hide_flag = true; 
        }else if(code==KeyEvent.VK_RIGHT) {
                for(int i=0;i<vector.size();i++) {
                    String str = vector.elementAt(i);
                    if(str.contains(text)) {
                        combo.setSelectedIndex(-1);
                        tf.setText(str);
                        return;
                    }
                }
        }
    }
    });
    
    clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tf.setText("");
            }
        });
        
        if (Type.equals("Ingredients")) {
            choiceList = CSVManagerEating.getAllElements("Ingredients");
        } else if (Type.equals("Meals")) {
            choiceList = CSVManagerEating.getAllElements("Meals");
        } else if (Type.equals("Source")) {
            choiceList = CSVManagerCulture.getAllElements("Source");
        } else if (Type.equals("Media")) {
            choiceList = CSVManagerCulture.getAllElements("Media");
        } else if (Type.equals("Workout")) {
            choiceList = CSVManagerWorkout.getAllElements("Workout");
        } else if (Type.equals("Exercise")) {
            choiceList = CSVManagerWorkout.getAllElements("Exercise");
        }
        
        for(int i=0;i<choiceList.length;i++){
            vector.addElement(choiceList[i]);
        }

        setModel(new DefaultComboBoxModel(vector), "");

        JPanel subPanel = new JPanel(new BorderLayout());
        if (Type.equals("Ingredients")) {
            subPanel.setBorder(BorderFactory.createTitledBorder("Ingredient"));
        } else if (Type.equals("Meals")){
            subPanel.setBorder(BorderFactory.createTitledBorder("Meal"));
        } else if (Type.equals("Media")){
            subPanel.setBorder(BorderFactory.createTitledBorder("Medium Type"));
        } else if (Type.equals("Source")){
            subPanel.setBorder(BorderFactory.createTitledBorder("Source"));
        } else if (Type.equals("Workout")){
            subPanel.setBorder(BorderFactory.createTitledBorder("Workout"));
        } else if (Type.equals("Exercise")){
            subPanel.setBorder(BorderFactory.createTitledBorder("Exercise"));
        } 
        subPanel.add(combo, BorderLayout.NORTH);
        // subPanel.add(clearButton, BorderLayout.CENTER); // Add clear button to the panel
        JPanel sub = new JPanel(new BorderLayout());
        sub.add(subPanel, BorderLayout.NORTH);
        add(sub);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        setPreferredSize(new Dimension(300, 50));
    }
    private boolean hide_flag = false;
       private void setModel(DefaultComboBoxModel mdl, String str) {
        combo.setModel(mdl);
        combo.setSelectedIndex(-1);
        tf.setText(str);  
    }

    private static DefaultComboBoxModel getSuggestedModel(java.util.List<String> list, String text) {
        DefaultComboBoxModel m = new DefaultComboBoxModel();
        for(String s: list) {
            if(s.contains(text)) m.addElement(s);
        }
        return m;
    }

    public void setText(String str) {
        tf.setText(str);
    }

    public String getText() {
        return tf.getText();
    }

    public void loadIngredients() {
        vector.clear();
        choiceList = CSVManagerEating.getAllElements("Ingredients");
        for(int i=0;i<choiceList.length;i++){
            vector.addElement(choiceList[i]);
        }
    }

    public void loadSources() {
        vector.clear();
        choiceList = CSVManagerCulture.getAllElements("Source");
        for(int i=0;i<choiceList.length;i++){
            vector.addElement(choiceList[i]);
        }
    }

    public void loadWorkouts() {
        vector.clear();
        choiceList = CSVManagerWorkout.getAllElements("Workout");
        for(int i=0;i<choiceList.length;i++){
            vector.addElement(choiceList[i]);
        }
    }

    public void loadExercises() {
        vector.clear();
        choiceList = CSVManagerWorkout.getAllElements("Exercise");
        for(int i=0;i<choiceList.length;i++){
            vector.addElement(choiceList[i]);
        }
    }
}
