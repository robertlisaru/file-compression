package ro.ulbsibiu.ccsd.laboratory.robert.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUIForm {
    private JButton buttonMsg;
    private JPanel panelMain;
    private JPanel innerPanel;

    public GUIForm() {
        buttonMsg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(panelMain, "Hello world!");
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        JFrame guiFrame = new JFrame("RDVD: Redundanta, Decriptarea si Vulnerabilitatea Datelor");
        guiFrame.setMinimumSize(new Dimension(600, 400));
        guiFrame.setLocationRelativeTo(null); //screen center
        try {
            guiFrame.setIconImage(ImageIO.read(GUIForm.class.getResourceAsStream("/lock.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        guiFrame.setContentPane(new GUIForm().panelMain);

        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.pack();
        guiFrame.setVisible(true);

    }

    private static class MyMenuBar extends JMenuBar {
        private JMenu fileMenu = new JMenu("File");
        private JMenuItem exit = new JMenuItem("Exit");

        public MyMenuBar() {
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            fileMenu.add(exit);
            add(fileMenu);
        }
    }
}
