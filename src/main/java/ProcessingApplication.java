import java.io.File;
import java.io.IOException;

public class ProcessingApplication {
    public static Images images = new Images();

    public static void main(String[] args) throws IOException {

        preprocImg("\\U\\U168.png");
        //preprocImg("\\text1.png");
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
            //img = Processing.fillGaps(img);    // -
            //img = Processing.rotation(img);  // -
            //Processing.takeAngle(img);
            //img = Processing.rot(img, -35.49);
            //img = Processing.rot(img, Processing.takeAngle(img));
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
