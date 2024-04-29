import javax.swing.*;
import java.awt.*;

/**
 * Class creates GUI to provide an interface for the user to create, edit, and save files in a given subdirectory
 * of app-data
 */
public class EditNoteGUI extends JFrame {

    private String folderName;
    private String fileName;

    public EditNoteGUI(String directoryPath) {
        folderName = directoryPath;
        setTitle("Notes App / " + folderName);
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


        String[] columns = {folderName};
        String[][] data = {{"Note 1"}, {"Note 2"}, {"Note 3"}};
        JTable table = new JTable(data, columns);
        JScrollPane tableScrollPane = new JScrollPane(table);

        JTextArea textArea = new JTextArea();
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScrollPane, textAreaScrollPane);
        splitPane.setDividerLocation(150);

        add(splitPane);

        setSize(800, 600);
        setVisible(true);
    }

    //for testing
    //TODO remove
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditNoteGUI("test"));
    }
}
