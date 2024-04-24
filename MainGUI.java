import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame {

    private JButton openFolderBtn;
    private JButton newFolderBtn;

    public MainGUI () {
        this.setSize(400, 400);
        this.setTitle("Notes App");

        openFolderBtn = new JButton("Open Folder");
        newFolderBtn = new JButton("New Folder");
        this.add(openFolderBtn);
        this.add(newFolderBtn);
        this.setLayout(new FlowLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        openFolderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditNoteGUI edit = new EditNoteGUI();
                edit.setVisible(true);
            }
        });

        this.setVisible(true);
        this.setResizable(false);
    }
}
