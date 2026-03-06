import java.util.Scanner;

public class FileAnalyzer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (args.length < 2) {
            System.out.println("Укажите путь к файлу и кодировку как аргументы командной строки");
            return;
        }
        String filePath = args[0];
        String charsetName = args[1];

        Boolean isWordSearched;
        String wordToFind;

        System.out.println("Хотите узнать статистику о каком-то конкретном слове?(да/нет)");
        String answer = scanner.nextLine().trim();
        if (answer.equals("да") || answer.equals("Да")){
            isWordSearched = true;
            System.out.print("Введите слово о котором хотите узнать статистику: ");
            wordToFind = scanner.nextLine().trim();
        }
        else{
            isWordSearched = false;
            wordToFind = "";
        }

        AnalysisLogic analysisLogic = new AnalysisLogic(filePath, charsetName, wordToFind);
        analysisLogic.analyzeFile();

        System.out.println("Всего строк: " + analysisLogic.getTotalLines());
        System.out.println("Всего непустых строк: "  + analysisLogic.getNonEmptyLines());
        System.out.println("Всего символов: " + analysisLogic.getTotalCharacters());
        if (isWordSearched) System.out.println("Всего найденных слов " + wordToFind+  ": " + analysisLogic.getWordOccurrences());
    }
}