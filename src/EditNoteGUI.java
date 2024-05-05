import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Creates GUI to provide an interface for the user to create, edit, and save files in a given subdirectory
 * of app-data
 */
public class EditNoteGUI extends JFrame {

    private String folderName;
    private File[] files;
    private JTextArea textArea;
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private FileManager fileManager;
    private String selectedFileName;

    public EditNoteGUI(String directoryPath) {
        folderName = directoryPath;
        setTitle("Notes App / " + folderName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fileManager = new FileManager(folderName);
        setIconImage(new ImageIcon("notes-logo.png").getImage());
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem addItem = new JMenuItem("New");
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem renameItem = new JMenuItem("Rename");

        fileMenu.add(addItem);
        fileMenu.add(saveItem);
        fileMenu.add(renameItem);
        fileMenu.addSeparator();
        fileMenu.add(deleteItem);
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
                        boolean deleted = fileManager.delete(selectedFileName);
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
                saveToFile(selectedFileName, textArea.getText());
            }
        });

        renameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToFile(selectedFileName, textArea.getText());
                String newFileName = JOptionPane.showInputDialog(renameItem, "New name for " + selectedFileName,
                        "Rename File", JOptionPane.QUESTION_MESSAGE);
                changeFileName(selectedFileName, newFileName);
                selectedFileName = newFileName;
                displayFileContent(selectedFileName);
                setFiles();
                setTitle("Notes / " + folderName + " / " + selectedFileName);
            }
        });
        

        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{folderName}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setFiles();
        table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        searchField = new JTextField(15);
        JPanel lhsPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Search"));
        searchPanel.add(searchField);

        lhsPanel.add(searchPanel, BorderLayout.NORTH);
        lhsPanel.add(tableScrollPane, BorderLayout.CENTER);


        textArea = new JTextArea();
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);

        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                selectedFileName = (String) tableModel.getValueAt(selectedRow, 0);
                displayFileContent(selectedFileName);
            }
        });

        table.setGridColor(new Color(83, 83, 82));

        JTableHeader header = table.getTableHeader();
        header.setOpaque(false);
        header.setBackground(new Color(83, 83, 82));
        header.setForeground(new Color(210, 188, 72));

        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, lhsPanel, textAreaScrollPane);
        splitPane.setDividerLocation(300);
        add(splitPane);

        setSize(800, 600);
        setVisible(true);


        File[] files = fileManager.getAll();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }

            void filter() {
                String filter = searchField.getText();
                if (filter.isEmpty()) {
                    updateList(files);
                } else {
                    updateList(files, filter);
                }
            }

            void updateList(File[] files, String filter) {
                files = fileManager.getAll();
                tableModel.setRowCount(0);
                for (File file : files) {
                    String fileName = file.getName().replace("app-data/" + folderName + "/", "");
                    if (fileName.toLowerCase().contains(filter.toLowerCase())) {
                        tableModel.addRow(new Object[]{fileName});
                    }
                }
            }

            void updateList(File[] files) {
                files = fileManager.getAll();
                tableModel.setRowCount(0);
                for (File file : files) {
                    String fileName = file.getName().replace("app-data/" + folderName + "/", "");
                    tableModel.addRow(new Object[]{fileName});
                }
            }
        });
    }

    private void displayFileContent(String fileName) {
        if (fileName.isEmpty()) {
            textArea.setText("");
        } else {
            String filePath = "app-data/" + folderName + "/" + fileName;
            String content = fileManager.getFileContent(filePath);
            textArea.setText(content);
            setTitle("Notes App / " + folderName + " / " + selectedFileName);
        }
    }

    private void saveToFile(String fileName, String content) {
        String filePath = "app-data/" + folderName + "/" + fileName;
        fileManager.setFileContent(filePath, content);
    }

    private void changeFileName(String oldName, String newName) {
        fileManager.rename(oldName, newName);
    }


    private void setFiles() {
        files = fileManager.getAll();
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
            fileManager.makeNew(fileName);
            setFiles();
            selectedFileName = fileName;
            displayFileContent(selectedFileName);
        }
    }
}