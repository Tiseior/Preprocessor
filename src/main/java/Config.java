public class Config {

    // Путь к изображениям
    public static String path = "src\\main\\Images\\";
    public static Boolean translation = true;
    public static Boolean scaling     = true;
    public static Boolean rotation    = true;
    public static Boolean rotAuto     = true;
    public static Double angle        = 0.0;
    public static Boolean infoWindow  = false;
    public static String infoStr      = "";

    public static void recordInformation(String info) {
        infoStr += info + "\n";
    }
}
