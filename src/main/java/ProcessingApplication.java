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
    public static String imgName = "";    // ���� � �����
    public static Double picCoef = 0.0;   // ����������� ��� ��������������� �����������
    public static BufferedImage resImage; // ��������� �������������� �����������

    public static void main(String[] args) throws IOException {

        //preprocImg("\\B\\microB.png");
        //preprocImg("\\cat.png");
        //preprocImg(images.test10_0);

        Color mainColor = new Color(204, 220, 236); // �������� ���� ����
        Color imgColor = new Color(206, 206, 206);  // ���� ��� ������� � �������������

        // ������� �������� ����
        // ---------------------
        // �������� ��������� ����
        final JFrame mainFrame = new JFrame("Preprocessor Application");
        // �������� �������� ������ ��� ������������ ���� ���������
        JPanel mainPanel = new JPanel(new GridBagLayout());
        // �������� ������ ��� ��������� �����������
        JPanel panelImgOpen = new JPanel(new BorderLayout());
        // �������� ������ ��� ��������������� �����������
        JPanel panelImgResult = new JPanel(new BorderLayout());
        // �������� ����������� ������
        JPanel panelArrow = new JPanel(new GridLayout());
        // �������� �������� ����
        JMenuBar menuBar = new JMenuBar();

        // �������� �������� ����
        // ----------------------
        // �������� ������ ��� ��������������
        JButton processing = new JButton("<html><font size=4>�������������</html>");
        // �������� ������ ��� �������� �����������
        JButton uploadImg = new JButton("�������");
        // �������� ������ ��� ���������� �����������
        JButton saveImg = new JButton("���������");
        // �������� ������������� �������� �� ��������
        final JLabel arrowElem = new JLabel("<html><p style=\"font-size: 800%\">&#10140;</p></html>",
                SwingConstants.CENTER);
        // �������� ������ ������ �������� � ���� ����
        JButton openSettingsMenu = new JButton("���������");
        // �������� ������ ������ ��������������� ���� � ���� ����
        JButton openInfoWindow = new JButton("�������������� ����");
        // �������� ������� ��� ��������� �����������
        final JLabel openImg = new JLabel(new ImageIcon());
        // �������� ������� ��� ��������������� �����������
        final JLabel resultImg = new JLabel(new ImageIcon());
        // ���� ��� ������ �����
        JFileChooser fileChooser = new JFileChooser(Config.path);

        // ������� ���� ��������
        // ---------------------
        // �������� ���������� ���� ��� ���������
        JDialog settingsWindow = new JDialog(mainFrame, "���������", true);
        // �������� ������ ��� ���� ��������
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        // �������� ������ ��� ������ ���� �������
        JPanel choicePanel = new JPanel();
        // �������� ������ ��� ���������� ��������
        JPanel settingsResultPanel = new JPanel();

        // �������� ���� ��������
        // ----------------------
        // �������� �������� ��� ����������
        JCheckBoxMenuItem translation = new JCheckBoxMenuItem("����������");
        // �������� �������� ��� ���������������
        JCheckBoxMenuItem scaling = new JCheckBoxMenuItem("���������������");
        // �������� �������� ��� �������
        JCheckBoxMenuItem rotation = new JCheckBoxMenuItem("�������");
        // �������� ������ ��� ������ ������� �������
        ButtonGroup choiceGroup = new ButtonGroup();
        // �������� ����������� ��� �������������� �������
        JRadioButtonMenuItem auto = new JRadioButtonMenuItem("����");
        // �������� ����������� ��� ����� ���� �������
        JRadioButtonMenuItem angle = new JRadioButtonMenuItem("����:");
        // �������� ���� ����� ��� ���� �������
        JTextField angleStr = new JTextField("0.0", 4);
        // �������� ������ ��� ���������� ��������
        JButton saveSettings = new JButton("���������");
        // �������� ������ ��� �������� �������� � �����������
        JButton defaultSettings = new JButton("�� ���������");

        // ������� ���� ����������
        // -----------------------
        // �������� ������������ ���� ��� ������ ���������� � �������������� �����������
        JDialog infoWindow = new JDialog(mainFrame, "�������������� ����", false);
        // �������� ������� ��� ��������� ����������� ����������
        JPanel infoPanel = new JPanel(new BorderLayout());

        // �������� ���� ����������
        // ------------------------
        // �������� ���� ��� ����������� ����������
        JTextArea informationText = new JTextArea(20, 28);

        // ��������� ��������� �������� ����
        // ---------------------------------
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(
                ProcessingApplication.class.getResource("logo.png")));
        mainFrame.setIconImage(icon.getImage());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             // ���������� ���������� ��� ��������
        mainFrame.setResizable(false);                                        // �� �������� ������
        mainFrame.setSize(700, 400);                              // ������ ����
        mainFrame.setLocationRelativeTo(null);                                // ���� � ������ ������
        mainPanel.setBackground(mainColor);                                   // ���� �������� ������
        panelImgOpen.setPreferredSize(new Dimension(127, 140));   // ������ ������ ��� ��������
        panelImgOpen.setBackground(imgColor);                                 // ���� ������ ��� ��������
        panelImgResult.setPreferredSize(new Dimension(127, 140)); // ������ ������ ��� ����������
        panelImgResult.setBackground(imgColor);                               // ���� ������ ��� ����������
        panelArrow.setPreferredSize(new Dimension(10, 140));      // ������ ������ ��� �������
        panelArrow.setBackground(mainColor);                                  // ���� ������ ��� �������
        processing.setMargin(new Insets(5, 10, 5, 10));  // ������� ������ ��������������
        // ��������� ������ ������ ��������
        openSettingsMenu.setFocusPainted(false);
        openSettingsMenu.setOpaque(false);
        openSettingsMenu.setContentAreaFilled(false);
        openSettingsMenu.setBorderPainted(false);
        // ��������� ������ ������ ���� ����������
        openInfoWindow.setFocusPainted(false);
        openInfoWindow.setOpaque(false);
        openInfoWindow.setContentAreaFilled(false);
        openInfoWindow.setBorderPainted(false);
        FileNameExtensionFilter filterJPG = new FileNameExtensionFilter("JPG(*.jpg)", "jpg");
        fileChooser.setFileFilter(filterJPG);
        FileNameExtensionFilter filterPNG = new FileNameExtensionFilter("PNG(*.png)", "png");
        fileChooser.setFileFilter(filterPNG);

        // ��������� ��������� ���� ��������
        // ---------------------------------
        settingsWindow.setSize(260, 200); // ������ ����
        settingsWindow.setLocationRelativeTo(null);   // ������������
        settingsWindow.setResizable(false);           // ����������� �������� ������
        translation.setState(Config.translation);     // �������� �������� ����������
        scaling.setState(Config.scaling);             // �������� �������� ���������������
        rotation.setState(Config.rotation);           // �������� �������� �������
        // �������� � ������� ������ ������� ��������
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

        // ��������� ��������� ��������������� ����
        // ----------------------------------------
        infoWindow.setSize(350, 400); // ������ ��������������� ����
        infoWindow.setMinimumSize(new Dimension(350, 400)); // ����������� ������ ����
        infoWindow.setLocationRelativeTo(null); // ������������ ����

        // �������������� � ������� �����
        // ------------------------------
        // �������������� � ������� �������� �����������
        uploadImg.addActionListener(clickUpload -> {
            // ���������� ���� ������ �����
            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION ) {
                BufferedImage openPic;
                try {
                    imgName = fileChooser.getSelectedFile().toString();
                    openPic = ImageIO.read(fileChooser.getSelectedFile());
                    // ����������� ������ ����������� ��� ������ � ����������
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
        // �������������� � ������� ���������� �����������
        saveImg.addActionListener(clickSave -> {
            // ������, ���� ��� ����������� ��� ����������
            if(resultImg.getIcon().getIconHeight() == -1)
                JOptionPane.showMessageDialog(mainFrame,
                        "�������������� ����������� �� �������",
                        "404", JOptionPane.ERROR_MESSAGE);
            else {
                // ���������� ���� ������ �����
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
                                "���� (" + saveFile + ") ��������");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        // �������������� � ������� ��������������
        processing.addActionListener(clickProcess -> {
            // ������ ������������� � ����������� ����������
            try {
                // ������, ���� ��� ����������� ��� ��������������
                if(Objects.equals(imgName, "")) {
                    JOptionPane.showMessageDialog(mainFrame,
                            "����������� ��� �������������� �� �������",
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
        // �������������� � ������� ������ ��������
        openSettingsMenu.addActionListener(clickSettings -> settingsWindow.setVisible(true));
        // �������������� � ������� ������ ��������������� ����
        openInfoWindow.addActionListener(clickInfo -> {
            if(!Config.infoWindow) {
                Config.infoWindow = true;
                informationText.setText(Config.infoStr);
                infoWindow.setVisible(true);
            }
        });

        // �������������� � ����� ��������
        // -------------------------------
        // �������������� � ������������ ������������
        auto.addActionListener(clickAuto -> angleStr.setEditable(false));
        // �������������� � ������������ ������ ����
        angle.addActionListener(clickAngle -> angleStr.setEditable(true));
        // �������������� � ��������� �������
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
        // �������������� � ������� �������� �� ���������
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
        // �������������� � ������� ���������� ��������
        saveSettings.addActionListener(clickSave -> {
            Config.translation = translation.getState();
            Config.scaling = scaling.getState();
            Config.rotation = rotation.getState();
            Config.rotAuto = auto.isSelected();
            if(!Config.rotAuto)
                Config.angle = Double.parseDouble(angleStr.getText());
        });

        // �������������� � �������������� �����
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

        // ���������� ��������� �������� ����
        // ----------------------------------
        // �������� ����������� ��� ��������� ����
        GridBagConstraints constraintsMainPanel = new GridBagConstraints();
        // �������� ����������� ��� ���� ��������
        GridBagConstraints constraintsSettings = new GridBagConstraints();
        constraintsMainPanel.anchor     = GridBagConstraints.NORTH;
        constraintsMainPanel.ipadx      = 127;
        constraintsMainPanel.ipady      = 140;
        constraintsMainPanel.weightx    = 5;
        constraintsMainPanel.weighty    = 5;
        constraintsMainPanel.insets.top = 5;
        constraintsMainPanel.gridx      = 0;
        constraintsMainPanel.gridy      = 0;
        mainPanel.add(panelImgOpen, constraintsMainPanel);   // ������ �������� -> �������� ������
        constraintsMainPanel.gridx = 2;
        mainPanel.add(panelImgResult, constraintsMainPanel); // ������ ���������� -> �������� ������
        constraintsMainPanel.gridx = 1;
        mainPanel.add(panelArrow, constraintsMainPanel);     // ������ ������� -> �������� ������
        panelArrow.add(arrowElem);                           // ������� ������� -> ������ �������
        panelImgOpen.add(openImg, BorderLayout.CENTER);      // ������� �������� -> ������ ��������
        panelImgResult.add(resultImg, BorderLayout.CENTER);  // ������� ���������� -> ������ ����������
        panelImgOpen.add(uploadImg, BorderLayout.SOUTH);     // ������ �������� -> ������ ��������
        panelImgResult.add(saveImg, BorderLayout.SOUTH);     // ������ ���������� -> ������ ����������
        constraintsMainPanel.ipadx     = 0;
        constraintsMainPanel.ipady     = 0;
        constraintsMainPanel.gridx     = 0;
        constraintsMainPanel.gridy     = 1;
        constraintsMainPanel.gridwidth = 3;
        mainPanel.add(processing, constraintsMainPanel); // ������ �������������� -> �������� ������
        menuBar.add(openSettingsMenu);                   // ������ ������ ���� -> ������� ����
        menuBar.add(openInfoWindow);                     // ������ ������ ���������� -> ������� ����
        mainFrame.setJMenuBar(menuBar);                  // ������� ���� -> ������� ����
        mainFrame.add(mainPanel);                        // �������� ������ -> ������� ����

        // ���������� ��������� ���� ��������
        // ----------------------------------
        constraintsSettings.fill      = GridBagConstraints.HORIZONTAL;
        constraintsSettings.gridy     = 0;
        constraintsSettings.gridwidth = 3;
        settingsPanel.add(translation, constraintsSettings); // ������� ���������� -> ������ ��������
        constraintsSettings.gridy = 1;
        settingsPanel.add(scaling, constraintsSettings);     // ������� ��������������� -> ������ ��������
        constraintsSettings.gridy = 2;
        settingsPanel.add(rotation, constraintsSettings); // ������� ������� -> ������ ��������
        choicePanel.add(auto);                            // ����������� ���� -> ������ ������
        choicePanel.add(angle);                           // ����������� ���� -> ������ ������
        choicePanel.add(angleStr);                        // ���� ��� ���� -> ������ ������
        choiceGroup.add(auto); choiceGroup.add(angle);    // ���������� � ������ ������
        constraintsSettings.gridy = 3;
        settingsPanel.add(choicePanel, constraintsSettings); // ������ ������ -> ������ ��������
        settingsResultPanel.add(saveSettings);               // ������ ���������� �������� -> ������ ����������
        settingsResultPanel.add(defaultSettings);            // ������ �� ��������� -> ������ ����������
        constraintsSettings.gridy  = 4;
        constraintsSettings.insets = new Insets(20, 0, 0, 0);
        settingsPanel.add(settingsResultPanel, constraintsSettings); // ������ ���������� -> ������ ��������
        settingsWindow.add(settingsPanel);                           // ������ �������� -> ���� ��������

        // ���������� ��������� ��������������� ����
        // -----------------------------------------
        infoPanel.add(new JScrollPane(informationText)); // ��������� ������� -> ������ �������������� ������
        infoWindow.add(infoPanel);                       // �������������� ������ -> �������������� ����

        // ���������� ����������
        // ---------------------
        mainFrame.setVisible(true);
    }

    // ������ � ������� � ���� �����������
    public static BufferedImage preprocImg(String imgName) throws IOException {
        File file = new File(imgName);
        Integer[][] img = Processing.readAndConvertImageToArray(file);
        if(img.length != 0) {
            Config.recordInformation("�����������: " + imgName);
            Config.recordInformation("������ �����������: " + img[0].length + "x" + img.length + "\n");
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

    // ������ � ������� � ���� ���������� �������
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
