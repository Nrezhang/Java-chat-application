import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainGUI extends JFrame {

    private JPanel mainPanel;
    private JPanel btnPanel;
    private JPanel searchPanel;
    private JButton newFolderBtn;
    private JButton backBtn;
    private JButton renameBtn;
    private JButton selectBtn;
    private JButton deleteBtn;
    private JButton submitBtn;
    private ImageIcon imageIcon;
    private JLabel subtitle;
    private JTextField nameField;
    private JTextField searchField;
    private JList<String> directoryList;
    private DefaultListModel<String> listModel;

    public MainGUI () {
        this.setSize(500, 500);
        this.setTitle("Notes");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        newFolderBtn = new JButton("New Folder");
        backBtn = new JButton("←");
        selectBtn = new JButton("Select Folder");
        deleteBtn = new JButton("Delete Folder");
        submitBtn = new JButton("Create Folder");
        renameBtn = new JButton("Rename Folder");
        nameField = new JTextField();
        searchField = new JTextField();
        imageIcon = new ImageIcon("notes-logo.png");
        subtitle = new JLabel("", SwingConstants.CENTER);
        subtitle.setFont(new Font("Sans Serif", Font.BOLD, 20));

        this.setIconImage(imageIcon.getImage());

        mainPanel = new JPanel();
        btnPanel = new JPanel(new FlowLayout());
        searchPanel = new JPanel(new FlowLayout());
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        this.openFolder();

        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
        this.initializeActionListeners();
    }

    /**
     * Initializes ActionListeners used in MainGUI in a single, atomic function
     * to avoid adding duplicate ActionListeners
     */
    private void initializeActionListeners() {
        newFolderBtn.addActionListener(e -> newFolder());
        backBtn.addActionListener(e -> openFolder());

        selectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (directoryList.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel, "Please select a folder.",
                            "Notice", JOptionPane.WARNING_MESSAGE);
                } else {
                    String selectedDirectory = directoryList.getSelectedValue();
                    EditNoteGUI editNoteGUI = new EditNoteGUI(selectedDirectory);
                }
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = directoryList.getSelectedIndex();
                if (selectedIndex != - 1) {
                    String selectedDirectory = directoryList.getSelectedValue();
                    listModel.remove(selectedIndex);
                    DirectoryManager directoryManager = new DirectoryManager();
                    directoryManager.deleteDirectory(selectedDirectory);
                }
            }
        });

        renameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (directoryList.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(mainPanel, "Please select a folder.",
                            "Notice", JOptionPane.WARNING_MESSAGE);
                } else {
                    String newName = JOptionPane.showInputDialog("New Name:");
                    String selectedDirectory = directoryList.getSelectedValue();
                    DirectoryManager directoryManager = new DirectoryManager();
                    if (directoryManager.renameDirectory(selectedDirectory, newName)) {
                        listModel.set(directoryList.getSelectedIndex(), newName);
                    } else {
                        JOptionPane.showMessageDialog(mainPanel, "Folder rename failed.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DirectoryManager directoryManager = new DirectoryManager();
                String directoryName = nameField.getText();
                if (directoryName.isEmpty()) {
                    JOptionPane.showMessageDialog(submitBtn, "Folder name cannot be empty.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!directoryName.matches("[a-zA-Z0-9_ ]+")) {
                    JOptionPane.showMessageDialog(submitBtn, "Folder name must be alphanumeric.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else if (directoryName.length() > 255) {
                    JOptionPane.showMessageDialog(submitBtn, "Folder name cannot exceed 255 characters.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println(directoryManager.makeDirectory(directoryName));
                    openFolder();
                }
            }
        });
    }


    /**
     * Populates JPanel mainPanel with components and layout to allow user
     * to select an existing folder in app-data to open.
     */
    private void openFolder() {
        mainPanel.removeAll();
        btnPanel.removeAll();
        subtitle.setText("Note folders");

        listModel = new DefaultListModel<>();
        directoryList = new JList<>(listModel);
        directoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DirectoryManager directoryManager = new DirectoryManager();
        File[] directories = directoryManager.returnDirectories();

        for (File directory : directories) {
            String folderName = directory.getName().replace("app-data/", "");
            listModel.addElement(folderName);
        }
        searchField.setColumns(30);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }

            void filter() {
                String filter = searchField.getText();
                if (filter.isEmpty()) {
                    updateList(directories);
                } else {
                    updateList(directories, filter);
                }
            }

            void updateList(File[] directories, String filter) {
                directories = directoryManager.returnDirectories();
                listModel.clear();
                for (File directory : directories) {
                    String folderName = directory.getName().replace("app-data/", "");
                    if (folderName.toLowerCase().contains(filter.toLowerCase())) {
                        listModel.addElement(folderName);
                    }
                }
            }

            void updateList(File[] directories) {
                directories = directoryManager.returnDirectories();
                listModel.clear();
                for (File directory : directories) {
                    String folderName = directory.getName().replace("app-data/", "");
                    listModel.addElement(folderName);
                }
            }
        });

        searchPanel.add(new JLabel("Search"));
        searchPanel.add(searchField);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(subtitle);
        topPanel.add(searchPanel);

        btnPanel.add(deleteBtn);
        btnPanel.add(newFolderBtn);
        btnPanel.add(renameBtn);
        btnPanel.add(selectBtn);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(directoryList), BorderLayout.CENTER);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Populates JPanel mainPanel with components and layout to allow user
     * to create a new folder with a new name under app-data.
     */
    private void newFolder() {
        mainPanel.removeAll();
        btnPanel.removeAll();

        subtitle.setText("Create new folder");
        submitBtn.setVerifyInputWhenFocusTarget(true);
        nameField.setColumns(20);


        btnPanel.add(backBtn);
        btnPanel.add(nameField);
        btnPanel.add(submitBtn);
        mainPanel.add(subtitle, BorderLayout.NORTH);
        mainPanel.add(btnPanel, BorderLayout.CENTER);

        mainPanel.revalidate();
        mainPanel.repaint();
    }
}