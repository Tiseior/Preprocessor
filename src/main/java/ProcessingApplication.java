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
    public static String imgName = "";
    public static Double picCoef = 0.0;
    public static BufferedImage resImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);

    public static void main(String[] args) throws IOException {

        //preprocImg("\\B\\microB.png");
        //preprocImg("\\cat.png");
        //preprocImg(images.test10_0);

        // Создание основного окна
        final JFrame mainFrame = new JFrame("Preprocessor Application");
        // Остановить приложение при закрытии
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false); // Не изменять размер
        mainFrame.setSize(700, 400); // Размер окна
        mainFrame.setLocationRelativeTo(null); // Окно в центре экрана

        // Создание основной панели для расположения всех элементов
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraintsMainPanel = new GridBagConstraints();
        mainPanel.setBackground(new Color(204, 220, 236));
        constraintsMainPanel.weightx = 5;
        constraintsMainPanel.anchor = GridBagConstraints.NORTH;

        // Создание панели для открытого изображения
        JPanel panelImgOpen = new JPanel(new BorderLayout());
        constraintsMainPanel.ipadx = 127;
        constraintsMainPanel.ipady = 140;
        panelImgOpen.setPreferredSize(new Dimension(127, 140));
        panelImgOpen.setBackground(new Color(206, 206, 206));
        constraintsMainPanel.weighty = 5;
        constraintsMainPanel.insets.top = 5;
        constraintsMainPanel.gridx = 0;
        constraintsMainPanel.gridy = 0;
        mainPanel.add(panelImgOpen, constraintsMainPanel);

        // Создание панели для результирующего изображения
        JPanel panelImgResult = new JPanel(new BorderLayout());
        constraintsMainPanel.gridx = 2;
        panelImgResult.setPreferredSize(new Dimension(127, 140));
        panelImgResult.setBackground(new Color(206, 206, 206));
        mainPanel.add(panelImgResult, constraintsMainPanel);

        // Создание центральной панели
        JPanel panelArrow = new JPanel(new GridLayout());
        constraintsMainPanel.gridx = 1;
        panelArrow.setPreferredSize(new Dimension(10, 140));
        mainPanel.add(panelArrow, constraintsMainPanel);
        final JLabel arrowElem = new JLabel("<html><p style=\"font-size: 800%\">&#10140;</p></html>",
                SwingConstants.CENTER);
        panelArrow.setBackground(new Color(204, 220, 236));
        panelArrow.add(arrowElem);

        // Создание области для открытого изображения
        final JLabel openImg = new JLabel(new ImageIcon());
        panelImgOpen.add(openImg, BorderLayout.CENTER);

        // Создание области для результирующего изображения
        final JLabel resultImg = new JLabel(new ImageIcon());
        panelImgResult.add(resultImg, BorderLayout.CENTER);

        // Создание кнопки для открытия изображения
        JButton uploadImg = new JButton("Открыть");
        uploadImg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Выбор изображения для преобразования
                JFileChooser fileChooser = new JFileChooser(Config.path);
                FileNameExtensionFilter filterJPG = new FileNameExtensionFilter("JPG(*.jpg)", "jpg");
                fileChooser.setFileFilter(filterJPG);
                FileNameExtensionFilter filterPNG = new FileNameExtensionFilter("PNG(*.png)", "png");
                fileChooser.setFileFilter(filterPNG);
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

            }
        });
        panelImgOpen.add(uploadImg, BorderLayout.SOUTH);

        // Создание кнопки для сохранения изображения
        JButton saveImg = new JButton("Сохранить");
        saveImg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(resultImg.getIcon().getIconHeight() == -1)
                    JOptionPane.showMessageDialog(mainFrame,
                            "Результирующее изображение не найдено",
                            "404", JOptionPane.ERROR_MESSAGE);
                else {
                    // Сохранение результирующего изображения
                    JFileChooser fileChooser = new JFileChooser(Config.path);
                    FileNameExtensionFilter filterJPG =
                            new FileNameExtensionFilter("JPG(*.jpg)", "jpg");
                    fileChooser.setFileFilter(filterJPG);
                    FileNameExtensionFilter filterPNG =
                            new FileNameExtensionFilter("PNG(*.png)", "png");
                    fileChooser.setFileFilter(filterPNG);
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
            }
        });
        panelImgResult.add(saveImg, BorderLayout.SOUTH);

        JTextArea informationText = new JTextArea(20, 28);
        // Создание кнопки для преобразования
        JButton processing = new JButton("<html><font size=4>Преобразовать</html>");
        processing.setMargin(new Insets(5, 10, 5, 10));
        constraintsMainPanel.ipadx = 0;
        constraintsMainPanel.ipady = 0;
        constraintsMainPanel.gridx = 0;
        constraintsMainPanel.gridy = 1;
        constraintsMainPanel.gridwidth = 3;
        processing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Запуск препроцессора и отображение результата
                try {
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
            }
        });
        mainPanel.add(processing, constraintsMainPanel);

        // Создание верхнего меню
        JMenuBar menuBar = new JMenuBar();
        JButton openSettingsMenu = new JButton("Настройки");
        openSettingsMenu.setFocusPainted(false);
        openSettingsMenu.setOpaque(false);
        openSettingsMenu.setContentAreaFilled(false);
        openSettingsMenu.setBorderPainted(false);
        openSettingsMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Создание модального окна для настройки
                JDialog settingsWindow = new JDialog(mainFrame, "Настройки", true);
                //settingsWindow.setBackground(new Color(93, 118, 203));
                settingsWindow.setSize(260, 200);
                settingsWindow.setLocationRelativeTo(null);
                settingsWindow.setResizable(false);
                JPanel settingsPanel = new JPanel(new GridBagLayout());
                GridBagConstraints constraintsSettings = new GridBagConstraints();
                //settingsPanel.setBackground(new Color(93, 118, 203));
                JCheckBoxMenuItem translation  = new JCheckBoxMenuItem("Трансляция");
                //translation.setBackground(new Color(93, 118, 203));
                constraintsSettings.fill = GridBagConstraints.HORIZONTAL;
                constraintsSettings.gridy = 0;
                constraintsSettings.gridwidth = 3;
                settingsPanel.add(translation, constraintsSettings);
                JCheckBoxMenuItem scaling  = new JCheckBoxMenuItem("Масштабирование");
                //scaling.setBackground(new Color(93, 118, 203));
                constraintsSettings.gridy = 1;
                settingsPanel.add(scaling, constraintsSettings);
                JCheckBoxMenuItem rotation  = new JCheckBoxMenuItem("Ротация");
                constraintsSettings.gridy = 2;
                settingsPanel.add(rotation, constraintsSettings);
                JPanel choicePanel = new JPanel();
                JRadioButtonMenuItem auto = new JRadioButtonMenuItem("Авто");
                JRadioButtonMenuItem angle = new JRadioButtonMenuItem("Угол:");
                JTextField angleStr = new JTextField("0.0", 4);
                auto.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        angleStr.setEditable(false);
                    }
                });
                choicePanel.add(auto);
                choicePanel.add(angle);
                angle.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        angleStr.setEditable(true);
                    }
                });
                choicePanel.add(angleStr);
                constraintsSettings.gridy = 3;
                ButtonGroup choiceGroup = new ButtonGroup();
                choiceGroup.add(auto); choiceGroup.add(angle);
                settingsPanel.add(choicePanel, constraintsSettings);
                rotation.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
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
                    }
                });
                JPanel settingsResultPanel = new JPanel();
                JButton saveSettings = new JButton("Применить");
                settingsResultPanel.add(saveSettings);
                JButton defaultSettings = new JButton("По умолчанию");
                settingsResultPanel.add(defaultSettings);
                constraintsSettings.gridy = 4;
                constraintsSettings.insets = new Insets(20, 0, 0, 0);
                defaultSettings.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        translation.setState(true);
                        scaling.setState(true);
                        rotation.setState(true);
                        auto.setEnabled(true);
                        auto.doClick();
                        angle.setEnabled(true);
                        angleStr.setEditable(false);
                        angleStr.setText("0.0");
                    }
                });
                saveSettings.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Config.translation = translation.getState();
                        Config.scaling = scaling.getState();
                        Config.rotation = rotation.getState();
                        Config.rotAuto = auto.isSelected();
                        if(!Config.rotAuto)
                            Config.angle = Double.parseDouble(angleStr.getText());
                    }
                });
                settingsPanel.add(settingsResultPanel, constraintsSettings);
                translation.setState(Config.translation);
                scaling.setState(Config.scaling);
                rotation.setState(Config.rotation);
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
                }
                else {
                    auto.doClick();
                    auto.setEnabled(false);
                    angle.setEnabled(false);
                    angleStr.setText("0.0");
                    angleStr.setEditable(false);
                }
                settingsWindow.add(settingsPanel);
                settingsWindow.setVisible(true);
            }
        });
        menuBar.add(openSettingsMenu);
        JButton openInfoWindow = new JButton("Информационное окно");
        openInfoWindow.setFocusPainted(false);
        openInfoWindow.setOpaque(false);
        openInfoWindow.setContentAreaFilled(false);
        openInfoWindow.setBorderPainted(false);
        openInfoWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Создание немодального окна для вывода информации о преобразовании изображения
                if(!Config.infoWindow) {
                    Config.infoWindow = true;
                    JDialog infoWindow = new JDialog(mainFrame, "Информационное окно", false);
                    infoWindow.setSize(350, 400);
                    infoWindow.setMinimumSize(new Dimension(350, 400));
                    infoWindow.setLocationRelativeTo(null);
                    JPanel infoPanel = new JPanel(new BorderLayout());
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
                    infoPanel.add(new JScrollPane(informationText));
                    informationText.setText(Config.infoStr);
                    infoWindow.add(infoPanel);
                    infoWindow.setVisible(true);
                }
            }
        });
        menuBar.add(openInfoWindow);
        mainFrame.setJMenuBar(menuBar);

        // Отобразить окно
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    // Работа с образом в виде изображения
    public static BufferedImage preprocImg(String imgName) throws IOException {
        File file = new File(imgName);//images.path+imgName);
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
        return new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
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
