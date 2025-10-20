package example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreatedDownloadFilesName {
    private final List<String> inputHttps;
    private List<String> downloadNames;


    public CreatedDownloadFilesName(List<String> inputHttps) {
        this.inputHttps = inputHttps;
    }

    public List<String> createNamesFromInput() {
        downloadNames = new ArrayList<>(inputHttps.size());
        Pattern p = Pattern.compile("(.*/)(.*)");
        for (String http : inputHttps) {
            Matcher m = p.matcher(http);
            m.find();
            downloadNames.add(m.group(2));
        }
        return downloadNames;
    }
}
