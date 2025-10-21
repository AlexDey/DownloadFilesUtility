package example;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ThrottlingInputStream extends FilterInputStream {

    private final int bytePerSecond;
    private long lastReadTime;
    private int bytesReadSinceLastCheck;
    private final long MILLIS_TO_SECOND = 1000L;

    public ThrottlingInputStream(InputStream in, int bytePerSecond) {
        super(in);
        this.bytePerSecond = bytePerSecond;
        lastReadTime = System.currentTimeMillis();
        bytesReadSinceLastCheck = 0;
    }

//    @Override
//    public int read(byte[] b) throws IOException {
//        throttle(1);
//        return super.read(b);
//    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = super.read(b, off, len);
        if (bytesRead > 0) {
            throttle(bytesRead);
        }
        return bytesRead;
    }

    private void throttle(int bytes) {
        bytesReadSinceLastCheck += bytes;
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastReadTime;
//        System.out.println("elapsedTime = " + elapsedTime);

        // биты делим на скорость бит/сек -> получаем желаемое время в секундах и
        // умножаем на 1000, чтобы перевести время в миллисекунды
        long expectedTime = (long)((double) bytesReadSinceLastCheck / bytePerSecond * MILLIS_TO_SECOND);

        if (elapsedTime < expectedTime) {
            long sleepTime = (expectedTime - elapsedTime);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (elapsedTime > MILLIS_TO_SECOND / 10 || bytesReadSinceLastCheck > bytePerSecond / 10) {
            lastReadTime = System.currentTimeMillis();
            bytesReadSinceLastCheck = 0;
        }
    }
}
