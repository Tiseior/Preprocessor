import javax.swing.*;
import java.awt.*;

public class SettingsWindowInterface {

    // Чекбокс для масштабирования
    public JCheckBoxMenuItem scaling;
    // Чекбокс для трансляции
    public JCheckBoxMenuItem translation;
    // Чекбокс для ротации
    public JCheckBoxMenuItem rotation;
    // Радиокнопка для автоматической ротации
    public JRadioButtonMenuItem auto;
    // Радиокнопка для ввода угла ротации
    public JRadioButtonMenuItem angle;
    // Поле ввода для угла ротации
    public JTextField angleStr;
    // Кнопка для применения настроек
    public JButton saveSettings;
    // Кнопка для возврата настроек к изначальным
    public JButton defaultSettings;

    public JDialog createSettingsWindow(JFrame mainFrame, String title, boolean modal) {

        // Области окна настроек
        // ---------------------
        // Создание модального окна для настройки
        JDialog settingsDialog = new JDialog(mainFrame, title, modal);
        // Создание панели для окна настроек
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        // Создание панели для выбора вида ротации
        JPanel choicePanel = new JPanel();
        // Создание панели для завершения настроек
        JPanel settingsResultPanel = new JPanel();

        // Элементы окна настроек
        // ----------------------
        // Создание чекбокса для масштабирования
        scaling = new JCheckBoxMenuItem("Масштабирование");
        // Создание чекбокса для трансляции
        translation = new JCheckBoxMenuItem("Трансляция");
        // Создание чекбокса для ротации
        rotation = new JCheckBoxMenuItem("Ротация");
        // Создание группы для выбора способа ротации
        ButtonGroup choiceGroup = new ButtonGroup();
        // Создание радиокнопки для автоматической ротации
        auto = new JRadioButtonMenuItem("Авто");
        // Создание радиокнопки для ввода угла ротации
        angle = new JRadioButtonMenuItem("Угол:");
        // Создание поля ввода для угла ротации
        angleStr = new JTextField("0.0", 4);
        // Создание кнопки для применения настроек
        saveSettings = new JButton("Применить");
        // Создание кнопки для возврата настроек к изначальным
        defaultSettings = new JButton("По умолчанию");

        // Настройка элементов окна настроек
        // ---------------------------------
        settingsDialog.setSize(260, 200); // Размер окна
        settingsDialog.setLocationRelativeTo(null);   // Расположение
        settingsDialog.setResizable(false);           // Возможность изменять размер
        scaling.setState(Config.scaling);             // Значение чекбокса масштабирования
        translation.setState(Config.translation);     // Значение чекбокса трансляции
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

        // Добавление элементов окна настроек
        // ----------------------------------
        // Создание ограничений для окна настроек
        GridBagConstraints constraintsSettings = new GridBagConstraints();
        constraintsSettings.fill      = GridBagConstraints.HORIZONTAL;
        constraintsSettings.gridy     = 0;
        constraintsSettings.gridwidth = 3;
        settingsPanel.add(scaling, constraintsSettings);     // Чекбокс масштабирования -> панель настроек
        constraintsSettings.gridy = 1;
        settingsPanel.add(translation, constraintsSettings); // Чекбокс трансляции -> панель настроек
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
        settingsDialog.add(settingsPanel);                           // Панель настроек -> окно настроек

        return settingsDialog;
    }

}
