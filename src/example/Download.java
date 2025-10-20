package example;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

import static example.Main.errorPrint;

public class Download implements Runnable {
    private static String directoryPath;

    private final String stringHttpUrl;
    private final String downloadName;

    public Download(String stringHttpUrl, String downloadName) {
        this.stringHttpUrl = stringHttpUrl;
        this.downloadName = downloadName;
    }

    public static void setDirectoryPath(String directoryPath0) {
        directoryPath = directoryPath0;

    }

    @Override
    public void run() {
        try (HttpClient client = HttpClient.newHttpClient()){
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(stringHttpUrl))
                    .build();
            HttpResponse<Path> response = client.send(request,
                    HttpResponse.BodyHandlers.ofFile(Path.of(directoryPath+"\\"+downloadName)));

            if (response.statusCode() == HttpURLConnection.HTTP_OK){
                System.out.println("Файл " + downloadName + " скачан успешно");
            } else if (String.valueOf(response.statusCode()).matches("4..|5..")) {
                System.out.println("Ошибка при скачивании файла" + downloadName + ", код ответа: " + response.statusCode());
                System.out.println(response);
                System.out.println("Ответ от сервера в созданном файле в созданном");
            } else {
                System.out.println("При скачивании файла " + downloadName + ".Код ответа не 200: " + response.statusCode());
                System.out.println(response);
            }
            System.out.println("--------");

        } catch (IOException e) {
            errorPrint(e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            errorPrint(e.getMessage());
            e.printStackTrace();
        }
    }
}
