package example;

// ты ему список ссылок в текстовом файле, а он скачивает эти файлы и кладет в указанную папку на локальном диске.
// Должен уметь качать несколько файлов одновременно (в несколько потоков, например, 3 потока) и
// выдерживать указанное ограничение на скорость загрузки, например, 500 килобайт в секунду. Всё.

// Требования:
// 1. на вход тестовый файл со списком ссылок
// 2. на вход папку на локальном диске куда скачивать
// 3. скачивание идёт в 3 потока
// 4. скорость загрузки 500 килобайт

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
//    private static String stringUrl = "https://i.pinimg.com/originals/ec/65/6f/ec656fd5b72d8f1d8839018cc545e99a.jpg";
//    private static String stringUrl = "https://filesamples.com/samples/document/txt/sample3.txt";
//    private static String directoryPath = "D:\\My_documents\\Java\\Projects\\DownloadFilesUtility\\downloadFiles";

    private static final int THREAD_COUNT = 3;

    public static void main(String[] args) {
//        downloadOneFile(1); // 0,1,2
        CheckedInputArgs checker = new CheckedInputArgs(args);
        if (!checker.check()) return;
        List<String> downloadNames = new CreatedDownloadFilesName(checker.getInputHttps()).createNamesFromInput();
        System.out.println("============");
        downloadNames.forEach(System.out::println);
        System.out.println("============");

        // Вот тут экзекьютор с фиксированными потоками и будем писать
        Download.setDirectoryPath(checker.getDirectoryPath());
        try (ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT)) {
            for (int i = 0; i < downloadNames.size(); i++) {
                executorService.submit(new Download(checker.getInputHttps().get(i) ,downloadNames.get(i)));
            }
        }
        System.out.println("Программа завершена");

    }

//    private static void downloadOneFile(int case0) {
//        DownloadOneFile case1 = new DownloadOneFile( stringUrl, directoryPath);
//        switch (case0) {
//            case 0 -> case1.SimpleDownloadByStream();
//            case 1 -> case1.SimpleDownloadByHttpConnection();
//            case 2 -> case1.SimpleDownloadByNetHttp();
//        }
//    }


    private static void checkDirectory(Path directoryPath) {
        if (Files.exists(directoryPath) && Files.isDirectory(directoryPath)) {
            System.out.println("Directory exists: " + directoryPath);

            // Check permissions (read, write, execute)
            if (Files.isReadable(directoryPath)) {
                System.out.println("Directory is readable.");
            }
            if (Files.isWritable(directoryPath)) {
                System.out.println("Directory is writable.");
            }
            if (Files.isExecutable(directoryPath)) {
                System.out.println("Directory is executable.");
            }
        }
    }

    static void errorPrint(String message) {
        System.out.print("\nОШИБКА: " + message);
    }
}
