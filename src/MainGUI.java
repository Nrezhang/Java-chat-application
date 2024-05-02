import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainGUI extends JFrame {

    private JPanel mainPanel;
    private JPanel btnPanel;
    private JButton newFolderBtn;
    private JButton backBtn;
    private JButton selectBtn;
    private JButton deleteBtn;
    private JButton submitBtn;
    private ImageIcon imageIcon;
    private JLabel subtitle;
    private JTextField nameField;
    // Component to display existing folders
    private JList<String> directoryList;
    // Component to map list to Files
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
        nameField = new JTextField();
        imageIcon = new ImageIcon("notes-logo.png");
        subtitle = new JLabel("", SwingConstants.CENTER);
        subtitle.setFont(new Font("Sans Serif", Font.BOLD, 20));

        this.setIconImage(imageIcon.getImage());

        mainPanel = new JPanel();
        btnPanel = new JPanel(new FlowLayout());
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
                String selectedDirectory = directoryList.getSelectedValue();
                EditNoteGUI editNoteGUI = new EditNoteGUI(selectedDirectory);
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

        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DirectoryManager directoryManager = new DirectoryManager();
                String directoryName = nameField.getText();
                System.out.println(directoryManager.makeDirectory(directoryName));
                openFolder();
            }
        });

        nameField.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                JTextField field = (JTextField) input;
                String text = field.getText();
                if (text.isEmpty()) {
                    field.setToolTipText("folder name cannot be empty");
                    return false;
                }
                if (!text.matches("[a-zA-Z0-9]+")) {
                    field.setToolTipText("folder name must be a-z, A-Z, 0-9");
                    return false;
                }
                return true;
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
        subtitle.setText("Open folders");

        listModel = new DefaultListModel<>();
        directoryList = new JList<>(listModel);
        directoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DirectoryManager directoryManager = new DirectoryManager();
        File[] directories = directoryManager.returnDirectories();

        for (File directory : directories) {
            String folderName = directory.getName().replace("app-data/", "");
            listModel.addElement(folderName);
        }

        btnPanel.add(deleteBtn);
        btnPanel.add(newFolderBtn);
        btnPanel.add(selectBtn);
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
        //TODO add components to mainPanel for newFolder UI
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