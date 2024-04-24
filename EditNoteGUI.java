import javax.swing.*;
import java.awt.*;

public class EditNoteGUI extends JFrame {

    public EditNoteGUI() {
        setTitle("Notes App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);


        String[] columns = {"My Notes"};
        String[][] data = {{"Note 1"}, {"Note 2"}, {"Note 3"}};
        JTable table = new JTable(data, columns);
        JScrollPane tableScrollPane = new JScrollPane(table);

        JTextArea textArea = new JTextArea();
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScrollPane, textAreaScrollPane);
        splitPane.setDividerLocation(150);

        add(splitPane);

        setSize(800, 600);
    }
}
