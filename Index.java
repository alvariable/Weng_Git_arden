import java.io.*;
import java.util.HashMap;
import java.util.Set;

public class Index {
    private String indexFilePath = "Index";
    private String objectsFolderPath = "objects";
    HashMap<String, String> gitMap = new HashMap<String, String>();

    public Index() throws IOException {
        File folder = new File(objectsFolderPath);
        if (!folder.exists())
            folder.mkdirs();
        File file = new File(indexFilePath);
        if (!file.exists())
            file.createNewFile();
        writeIndex();
    }

    public void add(String fileName) throws IOException {
        // creates blob from fileName
        Blob blob = new Blob(fileName);
        String hash = blob.getHash();
        if (gitMap.containsKey(fileName)) {
            replace(fileName, hash);
        } else {
            try (PrintWriter pw = new PrintWriter(new FileWriter(indexFilePath, true))) {
                if (gitMap.isEmpty())
                    pw.print(fileName + " : " + hash);
                else
                    pw.print("\n" + hash + " : " + fileName);
            }
            gitMap.put(fileName, hash);
        }
    }

    public void replace(String fileName, String hash) throws IOException {
        if (!hash.equals(gitMap.get(fileName))) {
            gitMap.remove(fileName);
            gitMap.put(fileName, hash);
            writeIndex();
        }
    }

    public void remove(String fileName) throws IOException {
        gitMap.remove(fileName);
        writeIndex();
    }

    public void writeIndex() throws IOException {

        FileOutputStream fos = new FileOutputStream(indexFilePath);
        File file = new File(indexFilePath);
        PrintWriter pw = new PrintWriter(file);
        Set<String> fileSet = gitMap.keySet();
        Boolean isFirst = true;
        for (String k : fileSet) {
            if (isFirst)
                isFirst = false;
            else
                pw.print("\n");
            pw.print(gitMap.get(k) + " : " + k);
        }
        pw.close();
        fos.close();
    }
}