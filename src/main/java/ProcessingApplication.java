import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ProcessingApplication {
    public static Images images = new Images();
    public static Config config = new Config();
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
        //frame.setResizable(false); // Не изменять размер
        mainFrame.setSize(800, 400); // Размер окна
        mainFrame.setLocationRelativeTo(null); // Окно в центре экрана

        // Создание основной панели для расположения всех элементов
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraintsMainPanel = new GridBagConstraints();
        mainPanel.setBackground(new Color(93, 118, 203));
        constraintsMainPanel.weightx = 5;
        constraintsMainPanel.anchor = GridBagConstraints.NORTH;

        // Создание панели для открытого изображения
        JPanel panelImgOpen = new JPanel(new BorderLayout());
        constraintsMainPanel.ipadx = 120;
        constraintsMainPanel.ipady = 130;
        panelImgOpen.setPreferredSize(new Dimension(120, 130));
        constraintsMainPanel.weighty = 5;
        constraintsMainPanel.insets.top = 5;
        constraintsMainPanel.gridx = 0;
        constraintsMainPanel.gridy = 0;
        mainPanel.add(panelImgOpen, constraintsMainPanel);

        // Создание панели для результирующего изображения
        JPanel panelImgResult = new JPanel(new BorderLayout());
        constraintsMainPanel.gridx = 2;
        panelImgResult.setPreferredSize(new Dimension(120, 130));
        mainPanel.add(panelImgResult, constraintsMainPanel);

        // Создание центральной панели
        JPanel panelArrow = new JPanel(new GridLayout());
        constraintsMainPanel.gridx = 1;
        panelArrow.setPreferredSize(new Dimension(120, 130));
        mainPanel.add(panelArrow, constraintsMainPanel);
        final JLabel arrowElem = new JLabel("<html><h1>=></html>", SwingConstants.CENTER);
        panelArrow.add(arrowElem);

        // Создание области для открытого изображения
        //BufferedImage pic = ImageIO.read(new File(images.path+imgName));
        //Image img1 = pic.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        final JLabel openImg = new JLabel(new ImageIcon(), SwingConstants.CENTER);
        openImg.setVerticalAlignment(SwingConstants.BOTTOM);
        panelImgOpen.add(openImg, BorderLayout.SOUTH);

        // Создание области для результирующего изображения
        //BufferedImage pic2 = ImageIO.read(new File(images.path+imgRes));
        //Image img2 = pic2.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        final JLabel resultImg = new JLabel(new ImageIcon(), SwingConstants.CENTER);
        resultImg.setVerticalAlignment(SwingConstants.BOTTOM);
        panelImgResult.add(resultImg);

        // Создание кнопки для открытия изображения
        JButton uploadImg = new JButton("Открыть");
        uploadImg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Выбор изображения для преобразования
                JFileChooser fileChooser = new JFileChooser(config.path);
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
                        picCoef = 200.0/Math.max(openPic.getWidth(), openPic.getHeight());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Image readImg = openPic.getScaledInstance((int) (openPic.getWidth()*picCoef),
                            (int) (openPic.getHeight()*picCoef), Image.SCALE_SMOOTH);
                    openImg.setIcon(new ImageIcon(readImg));
                }

            }
        });
        panelImgOpen.add(uploadImg, BorderLayout.NORTH);

        // Создание кнопки для сохранения изображения
        JButton saveImg = new JButton("Сохранить");
        saveImg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Сохранение результирующего изображения
                JFileChooser fileChooser = new JFileChooser(config.path);
                FileNameExtensionFilter filterJPG = new FileNameExtensionFilter("JPG(*.jpg)", "jpg");
                fileChooser.setFileFilter(filterJPG);
                FileNameExtensionFilter filterPNG = new FileNameExtensionFilter("PNG(*.png)", "png");
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
                        ImageIO.write(resImage, saveFile.substring(saveFile.lastIndexOf(".")+1), output);
                        JOptionPane.showMessageDialog(fileChooser,
                                    "Файл (" + saveFile + ") сохранен");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        panelImgResult.add(saveImg, BorderLayout.NORTH);

        // Создание кнопки для преобразования
        JButton processing = new JButton("Преобразовать");
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
                    resImage = preprocImg(imgName);
                    Image newImg = resImage.getScaledInstance((int) (resImage.getWidth()*picCoef),
                            (int) (resImage.getHeight()*picCoef), Image.SCALE_SMOOTH);
                    resultImg.setIcon(new ImageIcon(newImg));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        mainPanel.add(processing, constraintsMainPanel);

        // Создание верхнего меню
        JMenuBar menuBar = new JMenuBar();
        JButton openSettingsMenu = new JButton("Настройки");
        openSettingsMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Создание модального окна для настройки
                JDialog settingsWindow = new JDialog(mainFrame, "Настройки", true);
                settingsWindow.setSize(300, 300);
                settingsWindow.setLocationRelativeTo(null);
                JPanel settingsPanel = new JPanel(new GridBagLayout());
                GridBagConstraints constraintsSettings = new GridBagConstraints();
                settingsPanel.setBackground(new Color(93, 118, 203));
                JCheckBoxMenuItem translation  = new JCheckBoxMenuItem("Трансляция");
                constraintsSettings.fill = GridBagConstraints.HORIZONTAL;
                constraintsSettings.gridy = 0;
                constraintsSettings.gridwidth = 3;
                settingsPanel.add(translation, constraintsSettings);
                JCheckBoxMenuItem scaling  = new JCheckBoxMenuItem("Масштабирование");
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
                        config.translation = translation.getState();
                        config.scaling     = scaling.getState();
                        config.rotation    = rotation.getState();
                        config.rotAuto     = auto.isSelected();
                        if(!config.rotAuto)
                            config.angle = Double.parseDouble(angleStr.getText());
                    }
                });
                settingsPanel.add(settingsResultPanel, constraintsSettings);
                translation.setState(config.translation);
                scaling.setState(config.scaling);
                rotation.setState(config.rotation);
                if(config.rotation) {
                    auto.setEnabled(true);
                    angle.setEnabled(true);
                    if(config.rotAuto) {
                        auto.doClick();
                        angleStr.setText("0.0");
                        angleStr.setEditable(false);
                    } else {
                        angle.doClick();
                        angleStr.setText(config.angle.toString());
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
        openSettingsMenu.setFocusPainted(false);
        openSettingsMenu.setBackground(new Color(192, 192, 192));
        menuBar.add(openSettingsMenu);
        JButton openInfoWindow = new JButton("Информационное окно");
        openInfoWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Создание немодального окна для вывода информации о преобразовании изображения
                if(!config.infoWindow) {
                    config.infoWindow = true;
                    JDialog infoWindow = new JDialog(mainFrame, "Информационное окно", false);
                    infoWindow.setSize(300, 300);
                    infoWindow.setLocationRelativeTo(null);
                    infoWindow.addWindowListener(new WindowListener() {
                        @Override
                        public void windowOpened(WindowEvent e) {}
                        @Override
                        public void windowClosing(WindowEvent e) {
                            config.infoWindow = false;
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
                    infoWindow.setVisible(true);
                }
            }
        });
        openInfoWindow.setFocusPainted(false);
        openInfoWindow.setBackground(new Color(192, 192, 192));
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
            //img = Processing.translation(img); // -
            //img = Processing.scaling(img);     // -
            img = Processing.scalingNew(img);
            img = Processing.translation(img);
            System.out.println("Count: " + Processing.pixelCount(img));
            //img = Processing.fillGaps(img);    // -
            //img = Processing.rotation(img);  // -
            //img = Processing.rot(img, -36.645649164620316);
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
