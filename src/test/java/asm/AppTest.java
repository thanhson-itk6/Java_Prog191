package asm;

import static org.junit.Assert.assertTrue;

import asm.frames.ManagerFrame;

import javax.swing.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            new ManagerFrame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
