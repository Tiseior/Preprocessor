import java.io.File;
import java.io.IOException;

public class ProcessingApplication {
    public static Images images = new Images();

    public static void main(String[] args) throws IOException {

        preprocImg("\\B\\microB.png");
        //preprocImg("\\cat.png");
        //preprocImg(images.test10_0);

    }

    // Работа с образом в виде изображения
    public static void preprocImg(String imgName) throws IOException {
        File file = new File(images.path+imgName);
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
            Processing.convertArrayToImageAndWrite(img, "Result.png");
        }
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
