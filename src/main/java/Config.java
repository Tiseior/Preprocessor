public class Config {

    // Путь к изображениям
    public static String path = "Images\\";
    // Файл руководства
    public static String fileAbout = "AboutPreprocessorApplication.pdf";
    // Настройка масштабирования
    public static Boolean scaling     = true;
    // Настройка трансляции (центрирования)
    public static Boolean translation = true;
    // Настройка ротации (поворота)
    public static Boolean rotation    = true;
    // Настройка автоповорота
    public static Boolean rotAuto     = true;
    // Поворот на угол
    public static Double angle        = 0.0;
    // Настройка включения информационного окна
    public static Boolean infoWindow  = false;
    // Текст информационного окна
    public static String infoStr      = "";

    // Добавление текста в информационное окно
    public static void recordInformation(String info) {
        infoStr += info + "\n";
    }
}
