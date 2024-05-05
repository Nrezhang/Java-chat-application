import java.io.*;

/**
 * Handles all interactions with files in a given subdirectory of
 * the app-data directory
 */
public class FileManager implements FileSystemManager{

    private String folderName;

    public FileManager(String folderName) {
        this.folderName = folderName;
    }

    @Override
    public File[] getAll() {
        String path = "app-data/" + folderName;
        File directory = new File(path);
        File[] files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        return (files != null) ? files : new File[0];
    }

    @Override
    public File makeNew(String name) {
        String path = "app-data/" + folderName + "/" + name;
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

    @Override
    public boolean delete(String name) {
        String filePath = "app-data/" + folderName + "/" + name;
        File fileToDelete = new File(filePath);
        try{
            fileToDelete.delete();
            return true;
        }
        catch(SecurityException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean rename(String oldName, String newName) {
        String oldPath = "app-data/" + folderName + "/" + oldName;
        String newPath = "app-data/" + folderName + "/" + newName;
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        if (oldFile.exists() && !newFile.exists()) {
            return oldFile.renameTo(newFile);
        }
        return false;
    }

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
