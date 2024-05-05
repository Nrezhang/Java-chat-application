import java.io.File;

/**
 * Specifies the implementation details of classes which interact with the file
 * system
 */
public interface FileSystemManager {
    public File[] getAll();
    public File makeNew(String name);
    public boolean delete(String name);
    public boolean rename(String oldName, String newName);
}