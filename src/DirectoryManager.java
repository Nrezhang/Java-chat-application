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
        path = "app-data/" + folderName + "/" + fileName;
        File newFile = new File(path);
        try {
            if (newFile.createNewFile()) {
                System.out.println("File created: " + newFile.getAbsolutePath());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
            e.printStackTrace();
        }

        return newFile;
        
    }

    //TODO
    public String getFileContent(String filename) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    //TODO
    public boolean setFileContent(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
