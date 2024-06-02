import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;

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

        // Создание окон
        MainWindowInterface mainWindow = new MainWindowInterface();
        final JFrame mainFrame = mainWindow.createMainWindow("Preprocessor Application", mainColor, imgColor);
        SettingsWindowInterface settingsWindow = new SettingsWindowInterface();
        JDialog settingsDialog = settingsWindow.createSettingsWindow(mainFrame, "Настройки", true);
        InfoWindowInterface infoWindow = new InfoWindowInterface();
        JDialog infoDialog = infoWindow.createInfoWindow(mainFrame, "Информационное окно", false);

        // Взаимодействия с главным окном
        // ------------------------------
        // Взаимодействие с кнопкой открытия изображения
        mainWindow.uploadImg.addActionListener(clickUpload -> {
            // Отобразить окно выбора файла
            int result = mainWindow.fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION ) {
                BufferedImage openPic;
                try {
                    imgName = mainWindow.fileChooser.getSelectedFile().toString();
                    openPic = ImageIO.read(mainWindow.fileChooser.getSelectedFile());
                    // Коэффициент сжатия изображения для вывода в интерфейсе
                    picCoef = 250.0/Math.max(openPic.getWidth(), openPic.getHeight());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Image readImg = openPic.getScaledInstance((int) (openPic.getWidth()*picCoef),
                        (int) (openPic.getHeight()*picCoef), Image.SCALE_SMOOTH);
                mainWindow.openImg.setIcon(new ImageIcon(readImg));
                mainWindow.resultImg.setIcon(new ImageIcon());
            }
        });
        // Взаимодействие с кнопкой сохранения изображения
        mainWindow.saveImg.addActionListener(clickSave -> {
            // Ошибка, если нет изображения для сохранения
            if(mainWindow.resultImg.getIcon().getIconHeight() == -1)
                JOptionPane.showMessageDialog(mainFrame,
                        "Результирующее изображение не найдено",
                        "404", JOptionPane.ERROR_MESSAGE);
            else {
                // Отобразить окно выбора файла
                mainWindow.fileChooser.resetChoosableFileFilters();
                int result = mainWindow.fileChooser.showSaveDialog(mainWindow.fileChooser);
                if (result == JFileChooser.APPROVE_OPTION ) {
                    try {
                        String saveFile = mainWindow.fileChooser.getSelectedFile().toString();
                        String fileType = mainWindow.fileChooser.getFileFilter().getDescription();
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
                        JOptionPane.showMessageDialog(mainWindow.fileChooser,
                                "Файл (" + saveFile + ") сохранен");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        // Взаимодействие с кнопкой преобразования
        mainWindow.processing.addActionListener(clickProcess -> {
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
                    mainWindow.resultImg.setIcon(new ImageIcon(newImg));
                    if(Config.infoWindow)
                        infoWindow.informationText.setText(Config.infoStr);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        // Взаимодействие с кнопкой вызова настроек
        mainWindow.openSettingsMenu.addActionListener(clickSettings -> settingsDialog.setVisible(true));
        // Взаимодействие с кнопкой вызова информационного меню
        mainWindow.openInfoWindow.addActionListener(clickInfo -> {
            if(!Config.infoWindow) {
                Config.infoWindow = true;
                infoWindow.informationText.setText(Config.infoStr);
                infoDialog.setVisible(true);
            }
        });
        // Взаимодействие с кнопкой вызова руководства
        mainWindow.openFileAbout.addActionListener(clickAbout -> {
            File file = new File(Config.fileAbout);
            Desktop desktop = Desktop.getDesktop();
            try {
                if(file.exists())
                    desktop.open(file);
                else
                    // Ошибка, если нет файла руководства
                    JOptionPane.showMessageDialog(mainFrame,
                            "Файл " + Config.fileAbout + " не найден",
                            "404", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Взаимодействия с окном настроек
        // -------------------------------
        // Взаимодействие с радиокнопкой автоповорота
        settingsWindow.auto.addActionListener(clickAuto -> settingsWindow.angleStr.setEditable(false));
        // Взаимодействие с радиокнопкой выбора угла
        settingsWindow.angle.addActionListener(clickAngle -> settingsWindow.angleStr.setEditable(true));
        // Взаимодействие с чекбоксом ротации
        settingsWindow.rotation.addActionListener(clickRot -> {
            if(settingsWindow.rotation.getState()) {
                settingsWindow.auto.setEnabled(true);
                settingsWindow.angle.setEnabled(true);
                if(settingsWindow.angle.isSelected())
                    settingsWindow.angleStr.setEditable(true);
            } else {
                settingsWindow.auto.setEnabled(false);
                settingsWindow.angle.setEnabled(false);
                settingsWindow.angleStr.setEditable(false);
            }
        });
        // Взаимодействие с кнопкой настроек по умолчанию
        settingsWindow.defaultSettings.addActionListener(clickDefault -> {
            settingsWindow.scaling.setState(true);
            settingsWindow.translation.setState(true);
            settingsWindow.rotation.setState(true);
            settingsWindow.auto.setEnabled(true);
            settingsWindow.auto.doClick();
            settingsWindow.angle.setEnabled(true);
            settingsWindow.angleStr.setEditable(false);
            settingsWindow.angleStr.setText("0.0");
        });
        // Взаимодействие с кнопкой применения настроек
        settingsWindow.saveSettings.addActionListener(clickSave -> {
            Config.scaling = settingsWindow.scaling.getState();
            Config.translation = settingsWindow.translation.getState();
            Config.rotation = settingsWindow.rotation.getState();
            Config.rotAuto = settingsWindow.auto.isSelected();
            if(!Config.rotAuto)
                Config.angle = Double.parseDouble(settingsWindow.angleStr.getText());
        });

        // Взаимодействия с информационным окном
        // -------------------------------------
        infoDialog.addWindowListener(new WindowListener() {
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

        // Отобразить приложение
        // ---------------------
        mainFrame.setVisible(true);
    }

    // Работа с образом в виде изображения
    public static BufferedImage preprocImg(String imgName) throws IOException {
        CustomImage customImage = new CustomImage(ReadAndWriteBlocks.readAndConvertImageToArray(imgName));
        if(customImage.matrix.length != 0) {
            Config.recordInformation("Изображение: " + imgName);
            Config.recordInformation("Размер изображения: " + customImage.matrix[0].length +
                    "x" + customImage.matrix.length + "\n");
            if(Config.scaling)
                customImage = ScalingBlock.scaling(customImage);
            if(Config.translation)
                customImage = TranslationBlock.translation(customImage);
            if(Config.rotation)
                customImage = RotationBlock.rotation(customImage);
            return ReadAndWriteBlocks.convertArrayToImageAndWrite(customImage.matrix);
        }
        return new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    }

    // Работа с образом в виде двумерного массива
    public static void preprocImg(Integer[][] imgName) {
        CustomImage customImage = new CustomImage(imgName);
        customImage.printImg();
        customImage = ScalingBlock.scaling(customImage);
        //customImage.printImg();
        customImage = TranslationBlock.translation(customImage);
        //customImage.printImg();
        customImage = RotationBlock.rotation(customImage);
        //customImage.printImg();
    }
}
