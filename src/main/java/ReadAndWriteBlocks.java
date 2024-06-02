import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ReadAndWriteBlocks {

    // Чтение изображения из файла
    // Преобразование изображения в двумерный целочисленный массив со значениями элементов:
    // 0 - белый пиксель, 1 - чёрный пиксель (изначально -1 - белый, 0 - чёрный)
    // Возвращает преобразованный двумерный массив
    public static Integer[][] readAndConvertImageToArray(String imagePath) {
        try {
            File file = new File(imagePath);
            BufferedImage image = ImageIO.read(file);
            int height = image.getHeight();
            int width = image.getWidth();
            // Преобразование изображение в чёрно-белое
            BufferedImage blackAndWhiteImg = new BufferedImage(width, height,
                    BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D graphics = blackAndWhiteImg.createGraphics();
            graphics.drawImage(image, 0, 0, null);
            Integer[][] imgData = new Integer[height][width];
            for(int i=0; i<height*width; i++) {
                int elem = blackAndWhiteImg.getRGB(i/height, i%height);
                // Тусклые пиксели делаем нулевыми -1907989
                if (elem > -1907989)
                    imgData[i%height][i/height] = 0;
                else
                    imgData[i%height][i/height] = 1; // Здесь можно заменить на elem или 1
            }

            return imgData;
        } catch (IOException e) {
            System.out.println("Image not found");
            return new Integer[0][0];
        }
    }

    // Преобразование двумерного целочисленного массива в изображение с расширением .png
    // со значениями пикселей: -1 - белый пиксель, 0 - чёрный пиксель
    // Запись полученного изображения в директорию с изображениями с заданным именем
    public static BufferedImage convertArrayToImageAndWrite(Integer[][] img) throws IOException {
        BufferedImage newImage = new BufferedImage(img[0].length, img.length, BufferedImage.TYPE_INT_RGB);
        for(int i=0; i<img.length; i++){
            for (int j=0; j<img[0].length; j++){
                int elem = img[i][j];
                if(elem == 0)
                    newImage.setRGB(j, i, -1);
                else
                    newImage.setRGB(j, i, 0); // Здесь можно заменить на elem или 0
            }
        }

        return newImage;
    }
}
