package example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CheckedInputArgs {
    private final String[] ARGS;
    private String directoryPath;

    private File file;
    private String inputFile;
    private List<String> inputHttps;
    private boolean goodArgs;

    public String getDirectoryPath() {
        return directoryPath;
    }

    public List<String> getInputHttps() {
        return inputHttps;
    }

    public CheckedInputArgs(String[] args) {
        this.ARGS = args;
        goodArgs = true;
    }

    public boolean check() {
        if (!checkCount()) return false;
        if (!checkOutputDirectory()) return false;
        if (!checkInputFile()) return false;
        if (!parseInputFiles()) return false;
        if (!checkProtocolHttp()) return false;
        return true;
    }

    private boolean checkProtocolHttp() {
//        Pattern p = Pattern.compile("https{0,1}?://.*");
//        Matcher m = null;
        List<String> checkedHttps = new ArrayList<>();
        for (String str : inputHttps) {
//            m = p.matcher(str);
            if (Pattern.matches("https{0,1}?://.*",str)) checkedHttps.add(str);
            else System.out.println("Строка " + str + " не является url протокола http(s)\n--------");
        }
        inputHttps = checkedHttps;
        return !checkedHttps.isEmpty();
    }

    private boolean parseInputFiles() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            inputHttps = reader.lines().collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Main.errorPrint(e.getMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            Main.errorPrint(e.getMessage());
            return false;
        }
        return true;
    }

    private boolean checkInputFile() {
        file = new File(inputFile);
        if (!file.exists()) {
            Main.errorPrint("Указанного входного файла не существует");
            return false;
        } else if (!file.isFile()) {
            Main.errorPrint("Указанный входной файл не является файлом");
            return false;
        }
        return true;
    }

    private boolean checkOutputDirectory() {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            Main.errorPrint("Указанной выходной директории не существует");
            return false;
        } else if (!directory.isDirectory()) {
            Main.errorPrint("Указанной выходной директории не является директорией");
            return false;
        }
        return true;
    }

    private boolean checkCount() {
        if (ARGS.length != 2) {
            Main.errorPrint("Требуется 2 аргумента");
            return false;
        }
        inputFile = ARGS[0];
        directoryPath = ARGS[1];
        return true;
    }


}
