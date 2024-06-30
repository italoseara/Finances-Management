package dev.components;

import javax.swing.JButton;

public class Button extends JButton {
    public Button(int x, int y, int width, int height, String text){
        setBounds(x, y, width, height);
        setText(text);
    }
}
