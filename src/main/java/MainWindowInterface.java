import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.Objects;

public class MainWindowInterface {

    // Кнопка для преобразования
    public JButton processing;
    // Кнопка для открытия изображения
    public JButton uploadImg;
    // Кнопка для сохранения изображения
    public JButton saveImg;
    // Кнопка вызова настроек в поле меню
    public JButton openSettingsMenu;
    // Кнопка вызова информационного окна в поле меню
    public JButton openInfoWindow;
    // Кнопка о вызове руководства
    public JButton openFileAbout;
    // Область для открытого изображения
    public JLabel openImg;
    // Область для результирующего изображения
    public JLabel resultImg;
    // Окно для выбора файла
    public JFileChooser fileChooser;

    public JFrame createMainWindow(String title, Color mainColor, Color imgColor) {

        // Области главного окна
        // ---------------------
        // Создание основного окна
        JFrame mainFrame = new JFrame(title);
        // Создание основной панели для расположения всех элементов
        JPanel mainPanel = new JPanel(new GridBagLayout());
        // Создание панели для открытого изображения
        JPanel panelImgOpen = new JPanel(new BorderLayout());
        // Создание панели для результирующего изображения
        JPanel panelImgResult = new JPanel(new BorderLayout());
        // Создание центральной панели
        JPanel panelArrow = new JPanel(new GridLayout());
        // Создание верхнего меню
        JMenuBar menuBar = new JMenuBar();

        // Элементы главного окна
        // ----------------------
        // Создание кнопки для преобразования
        processing = new JButton("<html><font size=4>Преобразовать</html>");
        // Создание кнопки для открытия изображения
        uploadImg = new JButton("Открыть");
        // Создание кнопки для сохранения изображения
        saveImg = new JButton("Сохранить");
        // Создание декоративного элемента со стрелкой
        final JLabel arrowElem = new JLabel("<html><p style=\"font-size: 100px\">&#10140;</p></html>",
                SwingConstants.CENTER);
        // Создание кнопки вызова настроек в поле меню
        openSettingsMenu = new JButton("Настройки");
        // Создание кнопки вызова информационного окна в поле меню
        openInfoWindow = new JButton("Информационное окно");
        // Создание кнопки о вызове руководства
        openFileAbout = new JButton("О программе");
        // Создание области для открытого изображения
        openImg = new JLabel(new ImageIcon());
        // Создание области для результирующего изображения
        resultImg = new JLabel(new ImageIcon());
        // Окно для выбора файла
        fileChooser = new JFileChooser(Config.path);

        // Настройка элементов главного окна
        // ---------------------------------
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(
                ProcessingApplication.class.getResource("logo.png")));
        mainFrame.setIconImage(icon.getImage());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             // Остановить приложение при закрытии
        mainFrame.setResizable(false);                                        // Не изменять размер
        mainFrame.setSize(700, 410);                              // Размер окна
        mainFrame.setLocationRelativeTo(null);                                // Окно в центре экрана
        mainPanel.setBackground(mainColor);                                   // Цвет основной панели
        panelImgOpen.setPreferredSize(new Dimension(127, 140));   // Размер панели для открытия
        panelImgOpen.setBackground(imgColor);                                 // Цвет панели для открытия
        panelImgResult.setPreferredSize(new Dimension(127, 140)); // Размер панели для результата
        panelImgResult.setBackground(imgColor);                               // Цвет панели для результата
        panelArrow.setPreferredSize(new Dimension(10, 140));      // Размер панели для стрелки
        panelArrow.setBackground(mainColor);                                  // Цвет панели для стрелки
        processing.setMargin(new Insets(5, 10, 5, 10));  // Отступы кнопки преобразования
        // Настройка кнопки вызова настроек
        openSettingsMenu.setFocusPainted(false);
        openSettingsMenu.setOpaque(false);
        openSettingsMenu.setContentAreaFilled(false);
        openSettingsMenu.setBorderPainted(false);
        // Настройка кнопки вызова окна информации
        openInfoWindow.setFocusPainted(false);
        openInfoWindow.setOpaque(false);
        openInfoWindow.setContentAreaFilled(false);
        openInfoWindow.setBorderPainted(false);
        // Настройка кнопки вызова руководства
        openFileAbout.setFocusPainted(false);
        openFileAbout.setOpaque(false);
        openFileAbout.setContentAreaFilled(false);
        openFileAbout.setBorderPainted(false);
        // Создание фильтров для поиска файлов
        FileNameExtensionFilter filterJPG = new FileNameExtensionFilter("JPG(*.jpg)", "jpg");
        fileChooser.setFileFilter(filterJPG);
        FileNameExtensionFilter filterPNG = new FileNameExtensionFilter("PNG(*.png)", "png");
        fileChooser.setFileFilter(filterPNG);

        // Добавление элементов главного окна
        // ----------------------------------
        // Создание ограничений для основного окна
        GridBagConstraints constraintsMainPanel = new GridBagConstraints();
        constraintsMainPanel.anchor     = GridBagConstraints.NORTH;
        constraintsMainPanel.ipadx      = 127;
        constraintsMainPanel.ipady      = 140;
        constraintsMainPanel.weightx    = 5;
        constraintsMainPanel.weighty    = 5;
        constraintsMainPanel.insets.top = 5;
        constraintsMainPanel.gridx      = 0;
        constraintsMainPanel.gridy      = 0;
        mainPanel.add(panelImgOpen, constraintsMainPanel);   // Панель открытия -> основная панель
        constraintsMainPanel.gridx = 2;
        mainPanel.add(panelImgResult, constraintsMainPanel); // Панель результата -> основная панель
        constraintsMainPanel.gridx = 1;
        mainPanel.add(panelArrow, constraintsMainPanel);     // Панель стрелки -> основная панель
        panelArrow.add(arrowElem);                           // Элемент стрелки -> панель стрелки
        panelImgOpen.add(openImg, BorderLayout.CENTER);      // Элемент открытия -> панель открытия
        panelImgResult.add(resultImg, BorderLayout.CENTER);  // Элемент результата -> панель результата
        panelImgOpen.add(uploadImg, BorderLayout.SOUTH);     // Кнопка открытия -> панель открытия
        panelImgResult.add(saveImg, BorderLayout.SOUTH);     // Кнопка сохранения -> панель результата
        constraintsMainPanel.ipadx     = 0;
        constraintsMainPanel.ipady     = 0;
        constraintsMainPanel.gridx     = 0;
        constraintsMainPanel.gridy     = 1;
        constraintsMainPanel.gridwidth = 3;
        mainPanel.add(processing, constraintsMainPanel); // Кнопка преобразования -> основная панель
        menuBar.add(openSettingsMenu);                   // Кнопка вызова меню -> область меню
        menuBar.add(openInfoWindow);                     // Кнопка вызова информации -> область меню
        menuBar.add(Box.createHorizontalGlue());         // Добавление сдвига в область меню
        menuBar.add(openFileAbout);                      // Кнопка вызова руководства -> область меню
        mainFrame.setJMenuBar(menuBar);                  // Область меню -> главное окно
        mainFrame.add(mainPanel);                        // Основная панель -> главное окно

        return mainFrame;
    }

}
