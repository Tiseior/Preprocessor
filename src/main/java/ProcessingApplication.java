import java.io.File;

public class ProcessingApplication {
    public static Images images = new Images();

    public static void main(String[] args) {

        // ������ � ������������� 9_2
        // ������ ��� ����������: 2_1, 8_1, 9_1
        // ������ ��� ����������: 2_3, 8_4, 9_2

        //preprocImg("\\A\\A10.png");
        //preprocImg("\\cat.png");
        Processing.preprocImg(images.test2_0);

        // ��� ��� �������� ������ �������� � �����������
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
        System.out.println("���������� ��������:     " + pxImg);
        System.out.println("������������ ����������: " + maxDef);
        System.out.println("����������� ����������:  " + minDef);
        System.out.println("������� ����������:      " + delta/i);
         */

    }
}
