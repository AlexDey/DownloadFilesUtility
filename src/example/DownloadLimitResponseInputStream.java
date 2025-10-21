package example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;

import static example.Main.BYTE_PER_SECOND;
import static example.Main.errorPrint;

public class DownloadLimitResponseInputStream extends DownloadUnlimited{

    public DownloadLimitResponseInputStream(String stringHttpUrl, String downloadName) {
        super(stringHttpUrl, downloadName);
    }

    @Override
    public void run() {
        try (HttpClient client = HttpClient.newHttpClient()){
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(stringHttpUrl))
                    .build();
            HttpResponse<InputStream> response = client.send(request,
                    HttpResponse.BodyHandlers.ofInputStream());

//            response.headers().allValues("content-type").forEach(System.out::println);
            try (ThrottlingInputStream inputStream = new ThrottlingInputStream(response.body(), BYTE_PER_SECOND);
                 FileOutputStream outputStream = new FileOutputStream(directoryPath+"\\"+downloadName)) {
                byte[] buffer = new byte[1024*1];
                int byteRead;
                Instant startTime = Instant.now();
                while ((byteRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, byteRead);
                }
                System.out.println("Текущий поток " + Thread.currentThread().getName()
                        + ". Время на скачивание = " + Duration.between(startTime, Instant.now())
                        + ". Файл " + downloadName + " скачан успешно");
            }

        } catch (IOException e) {
            errorPrint(e.getMessage());

//            e.printStackTrace();
        } catch (InterruptedException e) {
            errorPrint(e.getMessage());
//            e.printStackTrace();
        }
    }
}
