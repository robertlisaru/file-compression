package ro.ulbsibiu.ccsd.laboratory.robert.ui;

import org.apache.commons.io.FilenameUtils;
import ro.ulbsibiu.ccsd.laboratory.robert.algorithm.huffmanstatic.HuffmanStaticDecoder;
import ro.ulbsibiu.ccsd.laboratory.robert.algorithm.huffmanstatic.HuffmanStaticEncoder;
import ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lz77.LZ77Decoder;
import ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lz77.LZ77Encoder;
import ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lz77.Token;
import ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lzw.decoder.LZWDecoder;
import ro.ulbsibiu.ccsd.laboratory.robert.algorithm.lzw.encoder.LZWEncoder;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitReader;
import ro.ulbsibiu.ccsd.laboratory.robert.bitio.BitWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
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
import java.util.Vector;

public class MainFrame extends JFrame {
    private MyMenuBar menuBar = new MyMenuBar();
    private StatusBar statusBar = new StatusBar();
    private Toolbar toolbar = new Toolbar();
    private File inputFile;
    private BitWriter bitWriter;
    private BitReader bitReader;
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private HuffmanPanel huffmanPanel = new HuffmanPanel();
    private LZ77Panel lz77Panel = new LZ77Panel();
    private LZWPanel lzwPanel = new LZWPanel();

    public MainFrame() {
        //region Window properties
        setTitle("CCSD Laboratory");
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null); //screen center
        try {
            setIconImage(ImageIO.read(getClass().getResourceAsStream("/lock.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setJMenuBar(menuBar);
        //endregion

        mainPanel.add(huffmanPanel, "1");
        mainPanel.add(lz77Panel, "2");
        mainPanel.add(lzwPanel, "3");

        cardLayout.show(mainPanel, "3");
        setLayout(new BorderLayout());
        add(statusBar, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
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

        public StatusBar() {
            setLayout(new GridBagLayout());
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
        JComboBox comboBox = new JComboBox(new String[]{"Huffman", "LZ77", "LZW"});
        JFileChooser fileChooser;

        public Toolbar() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setBackground(Color.lightGray);

            openFileButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                    if (fileChooser.showDialog(MainFrame.this, "OK")
                            == JFileChooser.APPROVE_OPTION) {
                        inputFile = fileChooser.getSelectedFile();
                        statusBar.rightStatus.setText("File: " + inputFile.getName());
                        huffmanPanel.southPanel.huffmanEncodeButton.setEnabled(true);
                        lz77Panel.southPanel.lz77encodeButton.setEnabled(true);
                        lzwPanel.southPanel.encodeButton.setEnabled(true);
                        if (FilenameUtils.getExtension(inputFile.getName()).equals("hs")) {
                            huffmanPanel.southPanel.huffmanDecodeButton.setEnabled(true);
                        }
                        if (FilenameUtils.getExtension(inputFile.getName()).equals("lz77")) {
                            lz77Panel.southPanel.lz77decodeButton.setEnabled(true);
                        }
                        if (FilenameUtils.getExtension(inputFile.getName()).equals("lzw")) {
                            lzwPanel.southPanel.decodeButton.setEnabled(true);
                        }
                    }
                }
            });

            comboBox.setSelectedIndex(2);
            comboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    switch (comboBox.getSelectedIndex()) {
                        case 0:
                            cardLayout.show(mainPanel, "1");
                            break;
                        case 1:
                            cardLayout.show(mainPanel, "2");
                            break;
                        case 2:
                            cardLayout.show(mainPanel, "3");
                            break;
                    }
                }
            });

            setBorder(new MatteBorder(0, 0, 1, 0, Color.black));

            add(openFileButton);
            add(new JLabel("Algorithm: "));
            add(comboBox);
        }
    }

    private class HuffmanPanel extends JPanel {
        private HuffmanStaticEncoder encoder;
        private HuffmanStaticDecoder decoder;
        private SouthPanel southPanel = new SouthPanel();

        public HuffmanPanel() {
            setLayout(new BorderLayout());
            add(southPanel, BorderLayout.SOUTH);
        }

        private class SouthPanel extends JPanel {
            private JButton huffmanEncodeButton = new JButton("Encode");
            private JButton huffmanDecodeButton = new JButton("Decode");

            public SouthPanel() {
                huffmanEncodeButton.setEnabled(false);
                huffmanEncodeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        statusBar.leftStatus.setText("Encoding. Please wait...");
                        EventQueue.invokeLater(new Thread(() -> {
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
                        }));
                    }
                });
                huffmanDecodeButton.setEnabled(false);
                huffmanDecodeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        statusBar.leftStatus.setText("Decoding. Please wait...");
                        EventQueue.invokeLater(new Thread(() -> {
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
                        }));
                    }
                });

                setLayout(new FlowLayout());
                add(huffmanEncodeButton);
                add(huffmanDecodeButton);
            }
        }
    }

    private class LZ77Panel extends JPanel {
        private SouthPanel southPanel = new SouthPanel();
        private WestPanel westPanel = new WestPanel();
        private EastPanel eastPanel = new EastPanel();
        private LZ77Encoder encoder;
        private LZ77Decoder decoder;

        public LZ77Panel() {
            setLayout(new BorderLayout());
            add(southPanel, BorderLayout.SOUTH);
            add(westPanel, BorderLayout.WEST);
            add(eastPanel, BorderLayout.EAST);
        }

        private class EastPanel extends JPanel {
            private JScrollPane tokensScrollPane;
            private JTable tokensTable;
            private DefaultTableModel tokensTableModel;
            private String[] columnNames = new String[]{"Offset", "Length", "Byte", "ASCII"};

            public EastPanel() {
                setLayout(new BorderLayout());
                tokensTableModel = new DefaultTableModel() {
                    @Override
                    public int getColumnCount() {
                        return 4;
                    }

                    @Override
                    public String getColumnName(int index) {
                        return columnNames[index];
                    }

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                tokensTable = new JTable(tokensTableModel);
                tokensTable.setFocusable(false);
                tokensScrollPane = new JScrollPane(tokensTable);
                tokensScrollPane.setPreferredSize(new Dimension(200, 285));
                add(tokensScrollPane, BorderLayout.CENTER);
            }
        }

        private class SouthPanel extends JPanel {
            private JButton lz77encodeButton = new JButton("Encode");
            private JButton lz77decodeButton = new JButton("Decode");

            public SouthPanel() {
                lz77encodeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        statusBar.leftStatus.setText("Encoding. Please wait...");
                        eastPanel.tokensTableModel.getDataVector().removeAllElements();
                        EventQueue.invokeLater(
                                new Thread(() -> {
                                    File outputFile = new File(inputFile.getAbsolutePath() + ".o" +
                                            (westPanel.offsetComboBox.getSelectedIndex() + 3) +
                                            "l" + (westPanel.lengthComboBox.getSelectedIndex() + 2) + ".lz77");
                                    OutputStream outputStream = null;
                                    InputStream inputStream = null;
                                    try {
                                        outputFile.createNewFile();
                                        outputStream = new FileOutputStream(outputFile);
                                        inputStream = new BufferedInputStream(new FileInputStream(inputFile));
                                        encoder = new LZ77Encoder();
                                        long time0 = System.currentTimeMillis();
                                        if (westPanel.showTokens.isSelected()) {
                                            encoder.prepareStepByStepEncoding(
                                                    westPanel.offsetComboBox.getSelectedIndex() + 3
                                                    , westPanel.lengthComboBox.getSelectedIndex() + 2
                                                    , inputStream, outputStream);
                                            while (encoder.slidingWindowHasMoreBytes()) {
                                                Token token = encoder.nextToken();
                                                Vector row = new Vector();
                                                row.add(token.getOffset());
                                                row.add(token.getLength());
                                                row.add(token.getSymbol());
                                                row.add((char) token.getSymbol());
                                                eastPanel.tokensTableModel.addRow(row);
                                            }
                                            encoder.flush();
                                        } else {
                                            encoder.encode(westPanel.offsetComboBox.getSelectedIndex() + 3
                                                    , westPanel.lengthComboBox.getSelectedIndex() + 2
                                                    , inputStream, outputStream);
                                        }
                                        outputStream.flush();
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
                                }));
                    }
                });
                lz77decodeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        statusBar.leftStatus.setText("Decoding. Please wait...");
                        EventQueue.invokeLater(new Thread(() -> {
                            File outputFile = new File(FilenameUtils.removeExtension(inputFile.getAbsolutePath())
                                    + ".lz77d");
                            OutputStream outputStream = null;
                            InputStream inputStream = null;
                            try {
                                outputFile.createNewFile();
                                outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
                                inputStream = new BufferedInputStream(new FileInputStream(inputFile));
                                decoder = new LZ77Decoder();
                                long time0 = System.currentTimeMillis();
                                statusBar.leftStatus.setText("Decoding. Please wait...");
                                decoder.decode(inputStream, outputStream);
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
                        }));
                    }
                });
                lz77encodeButton.setEnabled(false);
                lz77decodeButton.setEnabled(false);
                setLayout(new FlowLayout());
                add(lz77encodeButton);
                add(lz77decodeButton);
            }
        }

        private class WestPanel extends JPanel {
            private JComboBox offsetComboBox =
                    new JComboBox(new String[]{"3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"});
            private JComboBox lengthComboBox =
                    new JComboBox(new String[]{"2", "3", "4", "5", "6", "7"});
            private JCheckBox showTokens = new JCheckBox("Show generated tokens");

            public WestPanel() {
                showTokens.setSelected(true);
                showTokens.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (showTokens.isSelected()) {
                            eastPanel.setVisible(true);
                        } else {
                            eastPanel.setVisible(false);
                        }
                    }
                });

                setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
                add(Box.createVerticalGlue());
                JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row1.add(new JLabel("Offset size: "));
                row1.add(offsetComboBox);
                row1.add(new JLabel("bits"));
                row1.setAlignmentX(Component.LEFT_ALIGNMENT);
                row1.setAlignmentY(Component.TOP_ALIGNMENT);
                add(row1);
                add(Box.createVerticalGlue());
                JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row2.add(new JLabel("Length size: "));
                row2.add(lengthComboBox);
                row2.add(new JLabel("bits"));
                row2.setAlignmentX(Component.LEFT_ALIGNMENT);
                row2.setAlignmentY(Component.TOP_ALIGNMENT);
                add(row2);
                add(Box.createVerticalGlue());
                showTokens.setAlignmentX(Component.LEFT_ALIGNMENT);
                showTokens.setAlignmentY(Component.TOP_ALIGNMENT);
                add(showTokens);
                add(new Box.Filler(new Dimension(0, 500), new Dimension(0, 1500),
                        new Dimension(200, 1500)));
            }
        }
    }

    private class LZWPanel extends JPanel {
        private SouthPanel southPanel = new SouthPanel();
        private WestPanel westPanel = new WestPanel();
        private EastPanel eastPanel = new EastPanel();
        private LZWEncoder encoder;
        private LZWDecoder decoder;

        public LZWPanel() {
            setLayout(new BorderLayout());
            add(southPanel, BorderLayout.SOUTH);
            add(westPanel, BorderLayout.WEST);
            add(eastPanel, BorderLayout.EAST);
        }

        private class SouthPanel extends JPanel {
            private JButton encodeButton = new JButton("Encode");
            private JButton decodeButton = new JButton("Decode");

            public SouthPanel() {
                encodeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        statusBar.leftStatus.setText("Encoding. Please wait...");
                        EventQueue.invokeLater(
                                new Thread(() -> {
                                    File outputFile = new File(inputFile.getAbsolutePath() + ".o" +
                                            (westPanel.dictionaryStrategy.getSelectedItem()) +
                                            (westPanel.dictionarySize.getSelectedIndex() + 9) + ".lzw");
                                    OutputStream outputStream = null;
                                    InputStream inputStream = null;
                                    try {
                                        outputFile.createNewFile();
                                        outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
                                        inputStream = new BufferedInputStream(new FileInputStream(inputFile));
                                        encoder = new LZWEncoder(westPanel.dictionaryStrategy.getSelectedIndex(),
                                                westPanel.dictionarySize.getSelectedIndex() + 9,
                                                inputStream, outputStream);
                                        long time0 = System.currentTimeMillis();
                                        encoder.encode();
                                        outputStream.flush();
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
                                }));
                    }
                });
                decodeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        statusBar.leftStatus.setText("Decoding. Please wait...");
                        EventQueue.invokeLater(new Thread(() -> {
                            File outputFile = new File(FilenameUtils.removeExtension(inputFile.getAbsolutePath())
                                    + ".lzwd");
                            OutputStream outputStream = null;
                            InputStream inputStream = null;
                            try {
                                outputFile.createNewFile();
                                outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
                                inputStream = new BufferedInputStream(new FileInputStream(inputFile));
                                decoder = new LZWDecoder(inputStream, outputStream);
                                long time0 = System.currentTimeMillis();
                                statusBar.leftStatus.setText("Decoding. Please wait...");
                                decoder.decode();
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
                        }));
                    }
                });
                encodeButton.setEnabled(false);
                decodeButton.setEnabled(false);
                setLayout(new FlowLayout());
                add(encodeButton);
                add(decodeButton);
            }
        }

        private class WestPanel extends JPanel {
            private JComboBox dictionarySize =
                    new JComboBox(new String[]{"9", "10", "11", "12", "13", "14", "15"});
            private JComboBox dictionaryStrategy =
                    new JComboBox(new String[]{"Freeze", "Empty"});
            private JCheckBox showGeneratedIndexes = new JCheckBox("Show generated indexes");

            public WestPanel() {
                showGeneratedIndexes.setSelected(true);
                showGeneratedIndexes.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (showGeneratedIndexes.isSelected()) {
                            eastPanel.setVisible(true);
                        } else {
                            eastPanel.setVisible(false);
                        }
                    }
                });

                setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
                add(Box.createVerticalGlue());
                JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row1.add(new JLabel("Dictionary size: "));
                row1.add(dictionarySize);
                row1.add(new JLabel("bits"));
                row1.setAlignmentX(Component.LEFT_ALIGNMENT);
                row1.setAlignmentY(Component.TOP_ALIGNMENT);
                add(row1);
                add(Box.createVerticalGlue());
                JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row2.add(new JLabel("Dictionary strategy: "));
                row2.add(dictionaryStrategy);
                row2.setAlignmentX(Component.LEFT_ALIGNMENT);
                row2.setAlignmentY(Component.TOP_ALIGNMENT);
                add(row2);
                add(Box.createVerticalGlue());
                showGeneratedIndexes.setAlignmentX(Component.LEFT_ALIGNMENT);
                showGeneratedIndexes.setAlignmentY(Component.TOP_ALIGNMENT);
                add(showGeneratedIndexes);
                add(new Box.Filler(new Dimension(0, 500), new Dimension(0, 1500),
                        new Dimension(200, 1500)));
            }
        }

        private class EastPanel extends JPanel {
            private JScrollPane indexesScrollPane;
            private JTable indexesTable;
            private DefaultTableModel indexesTableModel;
            private String[] columnNames = new String[]{"Index"};

            public EastPanel() {
                setLayout(new BorderLayout());
                indexesTableModel = new DefaultTableModel() {
                    @Override
                    public int getColumnCount() {
                        return columnNames.length;
                    }

                    @Override
                    public String getColumnName(int index) {
                        return columnNames[index];
                    }

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                indexesTable = new JTable(indexesTableModel);
                indexesTable.setFocusable(false);
                indexesScrollPane = new JScrollPane(indexesTable);
                indexesScrollPane.setPreferredSize(new Dimension(50, 285));
                add(indexesScrollPane, BorderLayout.CENTER);
            }
        }
    }
}
