import java.util.Arrays;

public class TranslationBlock {

    // Смещение образа по осям X и Y в относительный центр изображения
    public static CustomImage translation(CustomImage image) {
        Config.recordInformation("Выполнение трансляции");
        try {
            Config.recordInformation("Координаты углов образа: (" +
                    image.leftBorder + "; " + image.lowerBorder + "), (" +
                    image.rightBorder + "; " + image.upperBorder + ")");
            int matrixSizeY = image.matrix.length;
            int matrixSizeX = image.matrix[0].length;
            Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
            for(Integer[] row: resultingMatrix)
                Arrays.fill(row, 0);

            Integer pixelCount = image.getSignificantPixelCount();

            int shiftX = 0; // Сдвиг по оси X
            int shiftY = 0; // Сдвиг по оси Y
            for(int y=0; y<matrixSizeY; y++)
                for(int x=0; x<matrixSizeX; x++) {
                    if(image.matrix[y][x] != 0) {
                        shiftX = shiftX + (x+1);
                        shiftY = shiftY + (y+1);
                    }
                }

            shiftX = Math.round((float)(matrixSizeX/2.0) - (float)(shiftX/pixelCount));
            shiftY = Math.round((float)(matrixSizeY/2.0) - (float)(shiftY/pixelCount));
            System.out.println("Сдвиг X: " + shiftX);
            System.out.println("Сдвиг Y: " + shiftY);

            for(int y=0; y<matrixSizeY; y++)
                for(int x=0; x<matrixSizeX; x++) {
                    int elem = image.matrix[y][x];
                    if(elem != 0)
                        resultingMatrix[y+shiftY][x+shiftX] = elem;
                }
            CustomImage newCustomImage = new CustomImage(resultingMatrix);
            Config.recordInformation("Координаты углов образа: (" +
                    newCustomImage.leftBorder + "; " + newCustomImage.lowerBorder + "), (" +
                    newCustomImage.rightBorder + "; " + newCustomImage.upperBorder + ")");
            Config.recordInformation("Трансляция выполнена\n");

            return newCustomImage;
        } catch (Exception ex) {
            Config.recordInformation("Ошибка трансляции!\n");
            return image;
        }
    }

}
