import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class creates GUI to provide an interface for the user to create, edit, and save files in a given subdirectory
 * of app-data
 */
public class EditNoteGUI extends JFrame {

    private String folderName;
    private String fileName;
    private DirectoryManager directoryManager;

    public EditNoteGUI(String directoryPath) {
        folderName = directoryPath;
        setTitle("Notes App / " + folderName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        directoryManager = new DirectoryManager();

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem addItem = new JMenuItem("New");
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(addItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewFile();
            }
        });

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
    private void createNewFile(){
        String fileName = JOptionPane.showInputDialog("Enter file name:");
        if (fileName != null && !fileName.isEmpty()) {
            directoryManager.makeNewFile(folderName, fileName);
        }
    }
    //TODO remove
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditNoteGUI("test"));
    }
}
