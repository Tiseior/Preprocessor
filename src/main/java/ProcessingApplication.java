import java.io.File;
import java.io.IOException;

public class ProcessingApplication {
    public static Images images = new Images();

    public static void main(String[] args) throws IOException {

        // Ошибка в центрировании 9_2
        // Образы для увеличения: 2_1, 8_1, 9_1
        // Образы для уменьшения: 2_3, 8_4, 9_2

        //Processing.preprocImg("\\A\\A1.png");
        Processing.preprocImg("\\nawA.png");
        //Processing.preprocImg(images.test8_0);

        // Тесты.
        // Увеличение: test2_0, test6_7, test8_1, test9_1
        // Уменьшение: test8_0, test9_0
        /*Integer[][] img = images.test9_1;
        Processing.printImg(img);
        img = Processing.translation(img);
        img = Processing.scalTest(img);
        Processing.printImg(img);*/

        // Код для проверки потерь пикселей у изображений
        /*
        String imgName = "\\bigA.png";
        File file = new File(images.path+imgName);
        Integer[][] img = Processing.readAndConvertImageToArray(file);
        img = Processing.translation(img);
        Integer delta = 0;
        Integer pxImg = Processing.pixelCount(img);
        int i = 0;
        int maxDef = 0;
        int minDef = 0;
        int px, def;
        for (Double deg=0.0; deg<=360.0; deg+=10.0) {
            px = Processing.pixelCount(Processing.rot(img, deg));
            def = pxImg - px;
            if(def > maxDef)
                maxDef = def;
            if(def < minDef)
                minDef = def;
            delta += Math.abs(def);
            i++;
        }
        System.out.println("Количество пикселей:     " + pxImg);
        System.out.println("Максимальное отклонение: " + maxDef);
        System.out.println("Минимальное отклонение:  " + minDef);
        System.out.println("Среднее отклонение:      " + delta/i);
         */

    }
}
