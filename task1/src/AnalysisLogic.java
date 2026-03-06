import java.io.*;

public class AnalysisLogic {
    private final File file;
    private final String charset;
    private String word = "";
    private int totalLines = 0;
    private int totalCharacters = 0;
    private int nonEmptyLines = 0;
    private int wordOccurrences = 0;

    public AnalysisLogic(String pathName, String charsetName, String wordToFind){
        file = new File(pathName);
        charset = charsetName;
        word = wordToFind;
        if (!file.exists()){
            System.out.print("Файл не найден.");
            return;
        }
        if (!file.isFile()){
            System.out.print("Указанный путь является директорией.");
            return;
        }
        if (!file.canRead()){
            System.out.print("Файл не доступен к чтению.");
            return;
        }
    }

    public void analyzeFile(){
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splittedLine = line.split(" ");
                if (!word.isEmpty()){
                    for (String wordInLine : splittedLine){
                        String cleaned = wordInLine.replaceAll("^\\p{Punct}+|\\p{Punct}+$", "");
                        if (cleaned.equalsIgnoreCase(word)) wordOccurrences++;
                    }
                }

                totalLines++;
                totalCharacters += line.length();
                if (!line.isBlank()) {
                    nonEmptyLines++;
                }
            }
        } catch (UnsupportedEncodingException e) {
            System.err.println("Ошибка: указанная кодировка не поддерживается: " + charset);
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода при чтении файла: " + e.getMessage());
        }
    }

    public int getTotalLines(){
        return totalLines;
    }

    public int getTotalCharacters(){
        return totalCharacters;
    }

    public int getNonEmptyLines(){
        return nonEmptyLines;
    }

    public int getWordOccurrences(){
        return wordOccurrences;
    }
}