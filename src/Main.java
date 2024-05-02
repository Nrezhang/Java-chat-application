import javax.swing.*;

class Main {

    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        System.out.println("Notes app running...");
        // Makes UI feel more native
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(() -> {
            MainGUI maingui = new MainGUI();
        });
    }
}