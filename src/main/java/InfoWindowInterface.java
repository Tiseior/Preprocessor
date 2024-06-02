import javax.swing.*;
import java.awt.*;

public class InfoWindowInterface {

    // Поле для отображения информации
    public JTextArea informationText;

    public JDialog createInfoWindow(JFrame mainFrame, String title, boolean modal) {

        // Области окна информации
        // -----------------------
        // Создание немодального окна для вывода информации о преобразовании изображения
        JDialog infoDialog = new JDialog(mainFrame, title, modal);
        // Создание области для элементов отображения информации
        JPanel infoPanel = new JPanel(new BorderLayout());

        // Элементы окна информации
        // ------------------------
        // Создание поля для отображения информации
        informationText = new JTextArea(20, 28);

        // Настройка элементов информационного окна
        // ----------------------------------------
        infoDialog.setSize(350, 400); // Размер информационного окна
        infoDialog.setMinimumSize(new Dimension(350, 400)); // Минимальный размер окна
        infoDialog.setLocationRelativeTo(null); // Расположение окна

        // Добавление элементов информационного окна
        // -----------------------------------------
        infoPanel.add(new JScrollPane(informationText)); // Текстовый элемент -> панель информационная панель
        infoDialog.add(infoPanel);                       // Информационная панель -> информационное окно

        return infoDialog;
    }
}
