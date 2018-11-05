package ro.ulbsibiu.ccsd.laboratory.robert.ui;

import org.apache.commons.io.FilenameUtils;
import org.jfree.data.statistics.HistogramDataset;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic.HuffmanStaticDecoder;
import ro.ulbsibiu.ccsd.laboratory.robert.encoding.huffmanstatic.HuffmanStaticEncoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainFrame extends JFrame {
    private MyMenuBar menuBar = new MyMenuBar();
    private StatusBar statusBar = new StatusBar(new GridBagLayout());
    private Toolbar toolbar = new Toolbar(new GridBagLayout());
    private File inputFile;
    private BitWriter bitWriter;
    private BitReader bitReader;
    private HuffmanStaticEncoder encoder;
    private HuffmanStaticDecoder decoder;

    public MainFrame() {
        //region Window properties
        setTitle("RDVD: Redundanta, Decriptarea si Vulnerabilitatea Datelor");
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null); //screen center
        try {
            setIconImage(ImageIO.read(getClass().getResourceAsStream("/lock.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setJMenuBar(menuBar);
        //endregion

        setLayout(new BorderLayout());
        add(statusBar, BorderLayout.SOUTH);
        add(toolbar, BorderLayout.NORTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
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
        MainFrame mainFrame = new MainFrame();
    }

    private class MyMenuBar extends JMenuBar {
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

    private class StatusBar extends JPanel {
        private JLabel leftStatus = new JLabel("Ready");
        private JLabel rightStatus = new JLabel("No open file");

        public StatusBar(LayoutManager layout) {
            super(layout);
            setBorder(new EtchedBorder(EtchedBorder.RAISED));
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(2, 2, 2, 2);
            c.anchor = GridBagConstraints.WEST;
            c.weightx = 0.5;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            add(leftStatus, c);
            c.gridx = 1;
            c.anchor = GridBagConstraints.EAST;
            c.weightx = 0.5;
            add(rightStatus, c);
        }
    }

    private class Toolbar extends JPanel {
        JButton openFileButton = new JButton("Open File");
        JButton encodeButton = new JButton("Encode");
        JButton decodeButton = new JButton("Decode");
        JFileChooser fileChooser;

        public Toolbar(LayoutManager layout) {
            super(layout);
            setBackground(Color.lightGray);

            openFileButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                    if (fileChooser.showDialog(MainFrame.this, "OK")
                            == JFileChooser.APPROVE_OPTION) {
                        inputFile = fileChooser.getSelectedFile();
                        statusBar.rightStatus.setText("File: " + inputFile.getName());
                        encodeButton.setEnabled(true);
                        if (FilenameUtils.getExtension(inputFile.getName()).equals("hs")) {
                            decodeButton.setEnabled(true);
                        }
                    }
                }
            });

            encodeButton.setEnabled(false);
            encodeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    File outputFile = new File(FilenameUtils
                            .removeExtension(inputFile.getAbsolutePath()) + ".hs");
                    OutputStream outputStream = null;
                    InputStream inputStream = null;
                    try {
                        outputFile.createNewFile();
                        outputStream = new FileOutputStream(outputFile);
                        inputStream = new BufferedInputStream(new FileInputStream(inputFile));
                        bitWriter = new BitWriter(new BufferedOutputStream(outputStream));
                        encoder = new HuffmanStaticEncoder(bitWriter);
                        long time0 = System.currentTimeMillis();
                        encoder.computeHeader(inputStream);
                        encoder.computeDictionary();
                        encoder.writeHeader();
                        inputStream = new BufferedInputStream(new FileInputStream(inputFile));
                        encoder.encodeAndWrite(inputStream);
                        encoder.flush();
                        long timePassed = System.currentTimeMillis() - time0;
                        statusBar.leftStatus.setText("Encoded in " + (timePassed / 1000.0) + " seconds.");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } finally {
                        try {
                            outputStream.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            inputStream.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });
            decodeButton.setEnabled(false);
            decodeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    File outputFile = new File(FilenameUtils.removeExtension(inputFile.getAbsolutePath()) + ".hsd");
                    OutputStream outputStream = null;
                    InputStream inputStream = null;
                    try {
                        outputFile.createNewFile();
                        outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
                        inputStream = new BufferedInputStream(new FileInputStream(inputFile));
                        bitReader = new BitReader(inputStream);
                        decoder = new HuffmanStaticDecoder(bitReader);
                        long time0 = System.currentTimeMillis();
                        decoder.readHeader();
                        decoder.computeDictionary();
                        decoder.decodeAndWrite(outputStream);
                        long timePassed = System.currentTimeMillis() - time0;
                        statusBar.leftStatus.setText("Decoded in " + (timePassed / 1000.0) + " seconds.");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } finally {
                        try {
                            outputStream.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        try {
                            inputStream.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });

            GridBagConstraints c = new GridBagConstraints();
            setBorder(new MatteBorder(0, 0, 1, 0, Color.black));
            c.insets = new Insets(2, 2, 2, 2);
            c.anchor = GridBagConstraints.WEST;
            c.weightx = 0.2;
            c.weighty = 1;
            c.gridx = 0;
            c.gridy = 0;
            add(openFileButton, c);
            c.anchor = GridBagConstraints.EAST;
            c.gridx = 2;
            add(decodeButton, c);
            c.gridx = 1;
            add(encodeButton, c);
        }
    }

    private class HuffmanPanel extends JPanel {
        HistogramDataset histogramDataset = new HistogramDataset();

        public HuffmanPanel(LayoutManager layout) {
            super(layout);
        }
    }
}
