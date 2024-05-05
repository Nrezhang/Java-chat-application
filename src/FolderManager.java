import java.io.*;
import java.util.Objects;

/**
 * Class responsible for all directory/file management tasks that
 * interact with the file system.
 */
public class FolderManager {

    private String path;

    public FolderManager() {}

    public File[] returnDirectories() {
        path = "app-data";
        File directory = new File(path);

        File[] directories = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        return (directories != null) ? directories : new File[0];
    }


    public String makeDirectory(String name) {
        path = "app-data/" + name;
        File newDirectory = new File(path);
        if (!newDirectory.exists()) {
            boolean created = newDirectory.mkdir();
            if (created) {
                return "Folder created successfully.";
            } else {
                return "Failed to create folder.";
            }
        } else {
            return "Folder already exists.";
        }
    }

    public boolean deleteDirectory(String name) {
        path = "app-data/" + name;
        File directory = new File(path);
        if (directory.listFiles() != null) {
            for (File f : Objects.requireNonNull(directory.listFiles())) {
                f.delete();
            }
        }
        return directory.delete();
    }
    public boolean renameDirectory(String oldName, String newName) {
        String oldPath = "app-data/" + oldName;
        String newPath = "app-data/" + newName;
        File oldDirectory = new File(oldPath);
        File newDirectory = new File(newPath);
        if (oldDirectory.exists() && !newDirectory.exists()) {
            return oldDirectory.renameTo(newDirectory);
        }
        return false;
    }

}
