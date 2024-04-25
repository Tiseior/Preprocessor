import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ProcessingApplication {
    public static Images images = new Images();
    public static String imgName = "";    // Путь к файлу
    public static Double picCoef = 0.0;   // Коэффициент для масштабирования изображения
    public static BufferedImage resImage; // Результат преобразования изображения

    public static void main(String[] args) throws IOException {

        //preprocImg("\\B\\microB.png");
        //preprocImg("\\cat.png");
        //preprocImg(images.test10_0);

        Color mainColor = new Color(204, 220, 236); // Основной цвет окна
        Color imgColor = new Color(206, 206, 206);  // Цвет для панелей с изображениями

        // Области главного окна
        // ---------------------
        // Создание основного окна
        final JFrame mainFrame = new JFrame("Preprocessor Application");
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
        JButton processing = new JButton("<html><font size=4>Преобразовать</html>");
        // Создание кнопки для открытия изображения
        JButton uploadImg = new JButton("Открыть");
        // Создание кнопки для сохранения изображения
        JButton saveImg = new JButton("Сохранить");
        // Создание декоративного элемента со стрелкой
        final JLabel arrowElem = new JLabel("<html><p style=\"font-size: 800%\">&#10140;</p></html>",
                SwingConstants.CENTER);
        // Создание кнопки вызова настроек в поле меню
        JButton openSettingsMenu = new JButton("Настройки");
        // Создание кнопки вызова информационного окна в поле меню
        JButton openInfoWindow = new JButton("Информационное окно");
        // Создание области для открытого изображения
        final JLabel openImg = new JLabel(new ImageIcon());
        // Создание области для результирующего изображения
        final JLabel resultImg = new JLabel(new ImageIcon());
        // Окно для выбора файла
        JFileChooser fileChooser = new JFileChooser(Config.path);

        // Области окна настроек
        // ---------------------
        // Создание модального окна для настройки
        JDialog settingsWindow = new JDialog(mainFrame, "Настройки", true);
        // Создание панели для окна настроек
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        // Создание панели для выбора вида ротации
        JPanel choicePanel = new JPanel();
        // Создание панели для завершения настроек
        JPanel settingsResultPanel = new JPanel();

        // Элементы окна настроек
        // ----------------------
        // Создание чекбокса для трансляции
        JCheckBoxMenuItem translation = new JCheckBoxMenuItem("Трансляция");
        // Создание чекбокса для масштабирования
        JCheckBoxMenuItem scaling = new JCheckBoxMenuItem("Масштабирование");
        // Создание чекбокса для ротации
        JCheckBoxMenuItem rotation = new JCheckBoxMenuItem("Ротация");
        // Создание группы для выбора способа ротации
        ButtonGroup choiceGroup = new ButtonGroup();
        // Создание радиокнопки для автоматической ротации
        JRadioButtonMenuItem auto = new JRadioButtonMenuItem("Авто");
        // Создание радиокнопки для ввода угла ротации
        JRadioButtonMenuItem angle = new JRadioButtonMenuItem("Угол:");
        // Создание поля ввода для угла ротации
        JTextField angleStr = new JTextField("0.0", 4);
        // Создание кнопки для применения настроек
        JButton saveSettings = new JButton("Применить");
        // Создание кнопки для возврата настроек к изначальным
        JButton defaultSettings = new JButton("По умолчанию");

        // Области окна информации
        // -----------------------
        // Создание немодального окна для вывода информации о преобразовании изображения
        JDialog infoWindow = new JDialog(mainFrame, "Информационное окно", false);
        // Создание области для элементов отображения информации
        JPanel infoPanel = new JPanel(new BorderLayout());

        // Элементы окна информации
        // ------------------------
        // Создание поля для отображения информации
        JTextArea informationText = new JTextArea(20, 28);

        // Настройка элементов главного окна
        // ---------------------------------
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(
                ProcessingApplication.class.getResource("logo.png")));
        mainFrame.setIconImage(icon.getImage());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             // Остановить приложение при закрытии
        mainFrame.setResizable(false);                                        // Не изменять размер
        mainFrame.setSize(700, 400);                              // Размер окна
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
        FileNameExtensionFilter filterJPG = new FileNameExtensionFilter("JPG(*.jpg)", "jpg");
        fileChooser.setFileFilter(filterJPG);
        FileNameExtensionFilter filterPNG = new FileNameExtensionFilter("PNG(*.png)", "png");
        fileChooser.setFileFilter(filterPNG);

        // Настройка элементов окна настроек
        // ---------------------------------
        settingsWindow.setSize(260, 200); // Размер окна
        settingsWindow.setLocationRelativeTo(null);   // Расположение
        settingsWindow.setResizable(false);           // Возможность изменять размер
        translation.setState(Config.translation);     // Значение чекбокса трансляции
        scaling.setState(Config.scaling);             // Значение чекбокса масштабирования
        rotation.setState(Config.rotation);           // Значение чекбокса ротации
        // Значения в области выбора способа поворота
        if(Config.rotation) {
            auto.setEnabled(true);
            angle.setEnabled(true);
            if(Config.rotAuto) {
                auto.doClick();
                angleStr.setText("0.0");
                angleStr.setEditable(false);
            } else {
                angle.doClick();
                angleStr.setText(Config.angle.toString());
            }
        } else {
            auto.doClick();
            auto.setEnabled(false);
            angle.setEnabled(false);
            angleStr.setText("0.0");
            angleStr.setEditable(false);
        }

        // Настройка элементов информационного окна
        // ----------------------------------------
        infoWindow.setSize(350, 400); // Размер информационного окна
        infoWindow.setMinimumSize(new Dimension(350, 400)); // Минимальный размер окна
        infoWindow.setLocationRelativeTo(null); // Расположение окна

        // Взаимодействия с главным окном
        // ------------------------------
        // Взаимодействие с кнопкой открытия изображения
        uploadImg.addActionListener(clickUpload -> {
            // Отобразить окно выбора файла
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION ) {
                BufferedImage openPic;
                try {
                    imgName = fileChooser.getSelectedFile().toString();
                    openPic = ImageIO.read(fileChooser.getSelectedFile());
                    // Коэффициент сжатия изображения для вывода в интерфейсе
                    picCoef = 250.0/Math.max(openPic.getWidth(), openPic.getHeight());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Image readImg = openPic.getScaledInstance((int) (openPic.getWidth()*picCoef),
                        (int) (openPic.getHeight()*picCoef), Image.SCALE_SMOOTH);
                openImg.setIcon(new ImageIcon(readImg));
                resultImg.setIcon(new ImageIcon());
            }
        });
        // Взаимодействие с кнопкой сохранения изображения
        saveImg.addActionListener(clickSave -> {
            // Ошибка, если нет изображения для сохранения
            if(resultImg.getIcon().getIconHeight() == -1)
                JOptionPane.showMessageDialog(mainFrame,
                        "Результирующее изображение не найдено",
                        "404", JOptionPane.ERROR_MESSAGE);
            else {
                // Отобразить окно выбора файла
                fileChooser.resetChoosableFileFilters();
                int result = fileChooser.showSaveDialog(fileChooser);
                if (result == JFileChooser.APPROVE_OPTION ) {
                    try {
                        String saveFile = fileChooser.getSelectedFile().toString();
                        String fileType = fileChooser.getFileFilter().getDescription();
                        if(Objects.equals(fileType, "JPG(*.jpg)"))
                            fileType = ".jpg";
                        else if(Objects.equals(fileType, "PNG(*.png)"))
                            fileType = ".png";
                        else if(Objects.equals(fileType, "All Files"))
                            if(saveFile.lastIndexOf(".") != -1)
                                fileType = "";
                            else
                                fileType = ".png";
                        saveFile += fileType;
                        File output = new File(saveFile);
                        ImageIO.write(resImage,
                                saveFile.substring(saveFile.lastIndexOf(".")+1), output);
                        JOptionPane.showMessageDialog(fileChooser,
                                "Файл (" + saveFile + ") сохранен");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        // Взаимодействие с кнопкой преобразования
        processing.addActionListener(clickProcess -> {
            // Запуск препроцессора и отображение результата
            try {
                // Ошибка, если нет изображения для преобразования
                if(Objects.equals(imgName, "")) {
                    JOptionPane.showMessageDialog(mainFrame,
                            "Изображение для преобразования не найдено",
                            "404", JOptionPane.ERROR_MESSAGE);
                } else {
                    Config.infoStr = "";
                    resImage = preprocImg(imgName);
                    Image newImg = resImage.getScaledInstance((int) (resImage.getWidth()*picCoef),
                            (int) (resImage.getHeight()*picCoef), Image.SCALE_SMOOTH);
                    resultImg.setIcon(new ImageIcon(newImg));
                    if(Config.infoWindow)
                        informationText.setText(Config.infoStr);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        // Взаимодействие с кнопкой вызова настроек
        openSettingsMenu.addActionListener(clickSettings -> settingsWindow.setVisible(true));
        // Взаимодействие с кнопкой вызова информационного меню
        openInfoWindow.addActionListener(clickInfo -> {
            if(!Config.infoWindow) {
                Config.infoWindow = true;
                informationText.setText(Config.infoStr);
                infoWindow.setVisible(true);
            }
        });

        // Взаимодействия с окном настроек
        // -------------------------------
        // Взаимодействие с радиокнопкой автоповорота
        auto.addActionListener(clickAuto -> angleStr.setEditable(false));
        // Взаимодействие с радиокнопкой выбора угла
        angle.addActionListener(clickAngle -> angleStr.setEditable(true));
        // Взаимодействие с чекбоксом ротации
        rotation.addActionListener(clickRot -> {
            if(rotation.getState()) {
                auto.setEnabled(true);
                angle.setEnabled(true);
                if(angle.isSelected())
                    angleStr.setEditable(true);
            } else {
                auto.setEnabled(false);
                angle.setEnabled(false);
                angleStr.setEditable(false);
            }
        });
        // Взаимодействие с кнопкой настроек по умолчанию
        defaultSettings.addActionListener(clickDefault -> {
            translation.setState(true);
            scaling.setState(true);
            rotation.setState(true);
            auto.setEnabled(true);
            auto.doClick();
            angle.setEnabled(true);
            angleStr.setEditable(false);
            angleStr.setText("0.0");
        });
        // Взаимодействие с кнопкой применения настроек
        saveSettings.addActionListener(clickSave -> {
            Config.translation = translation.getState();
            Config.scaling = scaling.getState();
            Config.rotation = rotation.getState();
            Config.rotAuto = auto.isSelected();
            if(!Config.rotAuto)
                Config.angle = Double.parseDouble(angleStr.getText());
        });

        // Взаимодействия с информационным окном
        // -------------------------------------
        infoWindow.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {
                Config.infoWindow = false;
            }
            @Override
            public void windowClosed(WindowEvent e) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });

        // Добавление элементов главного окна
        // ----------------------------------
        // Создание ограничений для основного окна
        GridBagConstraints constraintsMainPanel = new GridBagConstraints();
        // Создание ограничений для окна настроек
        GridBagConstraints constraintsSettings = new GridBagConstraints();
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
        mainFrame.setJMenuBar(menuBar);                  // Область меню -> главное окно
        mainFrame.add(mainPanel);                        // Основная панель -> главное окно

        // Добавление элементов окна настроек
        // ----------------------------------
        constraintsSettings.fill      = GridBagConstraints.HORIZONTAL;
        constraintsSettings.gridy     = 0;
        constraintsSettings.gridwidth = 3;
        settingsPanel.add(translation, constraintsSettings); // Чекбокс трансляции -> панель настроек
        constraintsSettings.gridy = 1;
        settingsPanel.add(scaling, constraintsSettings);     // Чекбокс масштабирования -> панель настроек
        constraintsSettings.gridy = 2;
        settingsPanel.add(rotation, constraintsSettings); // Чекбокс ротации -> панель настроек
        choicePanel.add(auto);                            // Радиокнопка авто -> панель выбора
        choicePanel.add(angle);                           // Радиокнопка угла -> панель выбора
        choicePanel.add(angleStr);                        // Поле для угла -> панель выбора
        choiceGroup.add(auto); choiceGroup.add(angle);    // Добавление в группу выбора
        constraintsSettings.gridy = 3;
        settingsPanel.add(choicePanel, constraintsSettings); // Панель выбора -> панель настроек
        settingsResultPanel.add(saveSettings);               // Кнопка применения настроек -> панель результата
        settingsResultPanel.add(defaultSettings);            // Кнопка по умолчанию -> панель результата
        constraintsSettings.gridy  = 4;
        constraintsSettings.insets = new Insets(20, 0, 0, 0);
        settingsPanel.add(settingsResultPanel, constraintsSettings); // Панель результата -> панель настроек
        settingsWindow.add(settingsPanel);                           // Панель настроек -> окно настроек

        // Добавление элементов информационного окна
        // -----------------------------------------
        infoPanel.add(new JScrollPane(informationText)); // Текстовый элемент -> панель информационная панель
        infoWindow.add(infoPanel);                       // Информационная панель -> информационное окно

        // Отобразить приложение
        // ---------------------
        mainFrame.setVisible(true);
    }

    // Работа с образом в виде изображения
    public static BufferedImage preprocImg(String imgName) throws IOException {
        File file = new File(imgName);
        Integer[][] img = Processing.readAndConvertImageToArray(file);
        if(img.length != 0) {
            Config.recordInformation("Изображение: " + imgName);
            Config.recordInformation("Размер изображения: " + img[0].length + "x" + img.length + "\n");
            //img = Processing.translation(img); // -
            //img = Processing.scaling(img);     // -
            if(Config.scaling)
                img = Processing.scalingNew(img);
            if(Config.translation)
                img = Processing.translation(img);
            //img = Processing.fillGaps(img);    // -
            //img = Processing.rotation(img);  // -
            //img = Processing.rot(img, -36.645649164620316);
            if(Config.rotation)
                img = Processing.rotationNew(img);
            return Processing.convertArrayToImageAndWrite(img);
        }
        return new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    }

    // Работа с образом в виде двумерного массива
    public static void preprocImg(Integer[][] imgName) {
        Processing.printImg(imgName);
        //imgName = Processing.scalingNew(imgName);
        //imgName = Processing.translation(imgName);
        //imgName = Processing.fillGaps(imgName);
        //Processing.printImg(imgName);
        Processing.takeAngle(imgName);
        //imgName = Processing.rot(imgName, 45.0);
        //Processing.printImg(imgName);
        //imgName = Processing.rotation(imgName);
        //Processing.rot(imgName, 90.0);
        //Processing.printImg(imgName);
    }
}
