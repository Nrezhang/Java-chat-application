import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
/**
 * Class creates GUI to provide an interface for the user to create, edit, and save files in a given subdirectory
 * of app-data
 */
public class EditNoteGUI extends JFrame {

    private String folderName;
    private String fileName;
    private File[] files;
    private JTextArea textArea;
    private DefaultTableModel tableModel;
    private DirectoryManager directoryManager;
    private String selectedFileName;

    public EditNoteGUI(String directoryPath) {
        folderName = directoryPath;
        setTitle("Notes App / " + folderName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        directoryManager = new DirectoryManager();
        setIconImage(new ImageIcon("notes-logo.png").getImage());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem addItem = new JMenuItem("New");
        JMenuItem deleteItem = new JMenuItem("Delete");

        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(deleteItem);
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
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFileName != null) {
                    int option = JOptionPane.showConfirmDialog(EditNoteGUI.this, "Are you sure you want to delete" + selectedFileName + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        boolean deleted = directoryManager.deleteFile(folderName, selectedFileName);
                        if (deleted) {
                            setFiles();
                            displayFileContent(""); // Clear the text area
                            setTitle("Notes App / " + folderName); // Reset window title
                        } else {
                            JOptionPane.showMessageDialog(EditNoteGUI.this, "Failed to delete file.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        });
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToFile(selectedFileName ,textArea.getText());
            }
        });
        

        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{folderName});
        setFiles();
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);



        textArea = new JTextArea();
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                selectedFileName = (String) tableModel.getValueAt(selectedRow, 0);
                displayFileContent(selectedFileName);
            }
        });

        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScrollPane, textAreaScrollPane);
        splitPane.setDividerLocation(150);
        add(splitPane);

        setSize(800, 600);
        setVisible(true);
    }

    private void displayFileContent(String fileName) {
        if (fileName.isEmpty()) {
            textArea.setText("");
        } else {
            String filePath = "app-data/" + folderName + "/" + fileName;
            String content = directoryManager.getFileContent(filePath);
            textArea.setText(content);
            setTitle("Notes App / " + folderName + "/ " + selectedFileName);
        }
    }

    private void saveToFile(String fileName, String content) {
        String filePath = "app-data/" + folderName + "/" + fileName;
        directoryManager.setFileContent(filePath, content);
    }


    private void setFiles(){
        files = directoryManager.returnDirectoryContents(folderName);
        String[][] data = new String[files.length][1];
        for (int i = 0; i < files.length; i++) {
            data[i][0] = files[i].getName(); // Extract file name from File object
        }
        if (tableModel != null) {
            tableModel.setDataVector(data, new String[]{folderName});
        }
    }
   
    private void createNewFile(){
        String fileName = JOptionPane.showInputDialog("Enter file name:");
        if (fileName != null && !fileName.isEmpty()) {
            directoryManager.makeNewFile(folderName, fileName);
            setFiles();
            selectedFileName = fileName;
            displayFileContent(selectedFileName);
        }
    }

    
    //TODO remove
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EditNoteGUI("test"));
    }
}
