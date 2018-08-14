package unikom.skripsi.angga.petugas.helper;

public class FilePath {

    private static String pathFile = null;

    public static String getPathFile() {
        return pathFile;
    }

    public static void setPathFile(String pathFile) {
        FilePath.pathFile = pathFile;
    }
}