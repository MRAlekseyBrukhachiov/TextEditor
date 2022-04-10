package editor;

import org.w3c.dom.Text;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.filechooser.*;

public class TextEditor extends JFrame {

    private JTextArea textArea;
    private JTextField searchField;
    private JButton saveButton;
    private JButton openButton;
    private JButton startSearchButton;
    private JButton prevMatchButton;
    private JButton nextMatchButton;
    private JCheckBox useRegexCheckBox;
    private JFileChooser jfc;

    private ArrayList<Integer> indexes = new ArrayList<>();
    private ArrayList<Integer> lens = new ArrayList<>();
    private int index = 0;

    public TextEditor() {
        super("Text Editor");
        setWindowProperties();
        initComponents();
        setVisible(true);
    }

    private void setWindowProperties() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void initComponents() {
        jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setName("FileChooser");
        this.add(jfc);

        textArea = new JTextArea(17,45);
        searchField = new JTextField(20);

        initButtons();

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setName("TextArea");
        searchField.setName("SearchField");

        setJMenuBar(createMenuBar());
        add(createFilePanel(), BorderLayout.NORTH);
        add(createTextPanel(), BorderLayout.CENTER);
    }

    private void addActionListeners() {
        saveButton.addActionListener(actionEvent -> {
            int returnValue = jfc.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();

                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(textArea.getText());
                } catch (IOException e) {
                    e.getMessage();
                }
            }
        });

        openButton.addActionListener(actionListener -> {
            int returnValue = jfc.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = jfc.getSelectedFile();
                textArea.setText(null);

                try (FileReader reader = new FileReader(file)) {
                    int c;
                    while((c = reader.read()) != -1){
                        textArea.append(Character.toString((char) c));
                    }
                } catch (IOException e) {
                    e.getMessage();
                }
            }
        });

        startSearchButton.addActionListener(actionListener -> {
            new Search(indexes, lens, textArea, searchField, useRegexCheckBox).execute();
            index = 0;
        });

        prevMatchButton.addActionListener(actionListener -> {
            index--;
            if (index < 0) {
                index = indexes.size() - 1;
            }
            setCaret(index);
        });

        nextMatchButton.addActionListener(actionListener -> {
            index++;
            if (index == indexes.size()) {
                index = 0;
            }
            setCaret(index);
        });
    }

    private void setCaret(int index) {
        int length = useRegexCheckBox.isSelected() ? lens.get(index) : searchField.getText().length();
        textArea.setCaretPosition(indexes.get(index) + length);
        textArea.select(indexes.get(index), indexes.get(index) + length);
        textArea.grabFocus();
    }

    private void initButtons() {
        saveButton = new JButton(new ImageIcon("save.png"));
        openButton = new JButton(new ImageIcon("open.png"));
        startSearchButton = new JButton(new ImageIcon("search.png"));
        prevMatchButton = new JButton(new ImageIcon("prev.png"));
        nextMatchButton = new JButton(new ImageIcon("next.png"));
        useRegexCheckBox = new JCheckBox("Use regex");

        saveButton.setName("SaveButton");
        openButton.setName("OpenButton");
        startSearchButton.setName("StartSearchButton");
        prevMatchButton.setName("PreviousMatchButton");
        nextMatchButton.setName("NextMatchButton");
        useRegexCheckBox.setName("UseRegExCheckbox");

        addActionListeners();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem exit = new JMenuItem("Exit");

        JMenu searchMenu = new JMenu("Search");
        JMenuItem startSearch = new JMenuItem("Start search");
        JMenuItem prevSearch = new JMenuItem("Previous search");
        JMenuItem nextMatch = new JMenuItem("Next match");
        JMenuItem useRegex = new JMenuItem("Use regex");

        fileMenu.setName("MenuFile");
        searchMenu.setName("MenuSearch");
        open.setName("MenuOpen");
        save.setName("MenuSave");
        exit.setName("MenuExit");
        startSearch.setName("MenuStartSearch");
        prevSearch.setName("MenuPreviousMatch");
        nextMatch.setName("MenuNextMatch");
        useRegex.setName("MenuUseRegExp");

        open.addActionListener(actionEvent -> openButton.doClick());
        save.addActionListener(actionEvent -> saveButton.doClick());
        exit.addActionListener(actionEvent -> this.dispose());
        startSearch.addActionListener(actionEvent -> startSearchButton.doClick());
        prevSearch.addActionListener(actionEvent -> prevMatchButton.doClick());
        nextMatch.addActionListener(actionEvent -> nextMatchButton.doClick());
        useRegex.addActionListener(actionEvent -> useRegexCheckBox.doClick());

        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        searchMenu.add(startSearch);
        searchMenu.add(prevSearch);
        searchMenu.add(nextMatch);
        searchMenu.add(useRegex);

        menuBar.add(fileMenu);
        menuBar.add(searchMenu);

        return menuBar;
    }

    private JPanel createFilePanel() {
        JPanel filePanel = new JPanel();

        filePanel.add(openButton);
        filePanel.add(saveButton);
        filePanel.add(searchField);
        filePanel.add(startSearchButton);
        filePanel.add(prevMatchButton);
        filePanel.add(nextMatchButton);

        Arrays.stream(filePanel.getComponents()).forEach(e ->
                e.setPreferredSize(new Dimension(25,25))
        );

        filePanel.add(useRegexCheckBox);
        return filePanel;
    }

    private JPanel createTextPanel() {
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setName("ScrollPane");
        JPanel textPanel = new JPanel();
        textPanel.add(scrollPane);
        return textPanel;
    }
}
