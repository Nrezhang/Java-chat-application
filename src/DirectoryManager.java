import java.io.*;

/**
 * Class responsible for all directory/file management tasks that
 * interact with the file system.
 */
public class DirectoryManager {

    private String path;

    public DirectoryManager() {}

    //This path notation works on windows but not sure if it works on macos/linux
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

    public File[] returnDirectoryContents(String folderName) {
        path = "app-data" + folderName;
        File directory = new File(path);

        File[] files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        return (files != null) ? files : new File[0];
    }

    public File makeDirectory(String name) {
        path = "app-data" + name;
        return new File(path);
    }

    public File makeNewFile(String folderName, String fileName) {
        path = "app-data" + folderName + "/" + fileName;
        return new File(path);
    }

    //TODO
    public String getFileContent(String filename) {
        return "";
    }

    //TODO
    public boolean setFileContent(String filename) {
        return false;
    }
}
