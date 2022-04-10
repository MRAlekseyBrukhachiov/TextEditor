package editor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Search extends SwingWorker<ArrayList<Integer>, Object> {

    private ArrayList<Integer> indexes;
    private ArrayList<Integer> lens;
    private final JTextArea textArea;
    private final JTextField searchField;
    private final JCheckBox useRegexCheckBox;
    private final String text;

    public Search(ArrayList<Integer> indexes, ArrayList<Integer> lens,
                  JTextArea textArea, JTextField searchField, JCheckBox useRegexCheckBox) {
        this.indexes = indexes;
        this.lens = lens;
        this.textArea = textArea;
        this.searchField = searchField;
        this.useRegexCheckBox = useRegexCheckBox;
        text = textArea.getText();
        indexes.clear();
        lens.clear();
    }

    @Override
    public ArrayList<Integer> doInBackground() {
        Matcher matcher = Pattern.compile(searchField.getText()).matcher(text);
        while (matcher.find()) {
            indexes.add(matcher.start());
            if (useRegexCheckBox.isSelected()) {
                lens.add(matcher.group().length());
            }
        }
        return indexes;
    }

    @Override
    protected void done() {
        int length = useRegexCheckBox.isSelected() ? lens.get(0) : searchField.getText().length();
        textArea.setCaretPosition(indexes.get(0) + length);
        textArea.select(indexes.get(0), indexes.get(0) + length);
        textArea.grabFocus();
    }
}
