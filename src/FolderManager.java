import java.io.File;
import java.io.FileFilter;
import java.util.Objects;

/**
 * Handles all interactions with subdirectories of the app-data
 * directory
 */
public class FolderManager implements FileSystemManager {

    public FolderManager() {}

    @Override
    public File[] getAll() {
        String path = "app-data";
        File directory = new File(path);

        File[] directories = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        return (directories != null) ? directories : new File[0];
    }

    @Override
    public File makeNew(String name) {
        String path = "app-data/" + name;
        File newDirectory = new File(path);
        if (!newDirectory.exists()) {
            boolean created = newDirectory.mkdir();
            if (created) {
                return newDirectory;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(String name) {
        String path = "app-data/" + name;
        File directory = new File(path);
        if (directory.listFiles() != null) {
            for (File f : Objects.requireNonNull(directory.listFiles())) {
                f.delete();
            }
        }
        return directory.delete();
    }

    @Override
    public boolean rename(String oldName, String newName) {
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
