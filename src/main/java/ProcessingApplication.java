import java.io.IOException;

public class ProcessingApplication {
    public static Images images = new Images();

    public static void main(String[] args) throws IOException {

        // ������ � ������������� 9_2
        // ������ ��� ����������: 2_1, 8_1, 9_1
        // ������ ��� ����������: 2_3, 8_4, 9_2

        Processing.preprocImg("\\B\\B345.png");
        //Processing.preprocImg("\\text1.png");
        //Processing.preprocImg(images.test10_0);

    }
}
