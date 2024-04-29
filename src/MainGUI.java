import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainGUI extends JFrame {

    private JPanel mainPanel;
    private JPanel btnPanel;
    private JButton openFolderBtn;
    private JButton newFolderBtn;
    private JButton backBtn;
    private JButton selectBtn;
    private ImageIcon imageIcon;
    private JLabel subtitle;
    // Component to display existing folders
    private JList<File> directoryList;
    // Component to map list to Files
    private DefaultListModel<File> listModel;

    public MainGUI () {
        this.setSize(500, 500);
        this.setTitle("Notes App");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        openFolderBtn = new JButton("Open Folder");
        newFolderBtn = new JButton("New Folder");
        backBtn = new JButton("←");
        selectBtn = new JButton("Select Folder");
        imageIcon = new ImageIcon("notes-logo.png");
        subtitle = new JLabel("", SwingConstants.CENTER);
        subtitle.setFont(new Font("Sans Serif", Font.BOLD, 20));

        this.setIconImage(imageIcon.getImage());

        mainPanel = new JPanel();
        btnPanel = new JPanel(new FlowLayout());
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        selectionScreen();

        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
    }

    /**
     * Populates JPanel mainPanel with components and layout for selection
     * between create new folder and open folder.
     */
    private void selectionScreen() {
        mainPanel.removeAll();
        btnPanel.removeAll();
        subtitle.setText("Welcome to notes app");

        openFolderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFolder();
            }
        });

        newFolderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newFolder();
            }
        });

        btnPanel.add(openFolderBtn);
        btnPanel.add(newFolderBtn);

        mainPanel.add(subtitle, BorderLayout.NORTH);
        mainPanel.add(btnPanel, BorderLayout.CENTER);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * Populates JPanel mainPanel with components and layout to allow user
     * to select an existing folder in app-data to open.
     */
    private void openFolder() {
        mainPanel.removeAll();
        btnPanel.removeAll();
        subtitle.setText("Open a folder");

        listModel = new DefaultListModel<>();
        directoryList = new JList<>(listModel);
        directoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DirectoryManager directoryManager = new DirectoryManager();
        File[] directories = directoryManager.returnDirectories();

        for (File directory : directories) {
            listModel.addElement(directory);
        }

        selectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File selectedDirectory = directoryList.getSelectedValue();
                EditNoteGUI editNoteGUI = new EditNoteGUI(selectedDirectory.getName());
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectionScreen();
            }
        });

        btnPanel.add(backBtn);
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
        System.out.println("new folder");
    }
}