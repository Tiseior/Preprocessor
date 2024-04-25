public class Config {

    // Путь к изображениям
    public static String path = "src\\main\\Images\\";
    // Настройка трансляции (центрирования)
    public static Boolean translation = true;
    // Настройка масштабирования
    public static Boolean scaling     = true;
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
