import java.util.Arrays;

public class ScalingBlock {

    // Масштабирование образа
    public static CustomImage scaling(CustomImage image) {
        Config.recordInformation("Выполнение масштабирования");
        try {
            Config.recordInformation("Количество пикселей в образе: " + image.getSignificantPixelCount());
            int matrixSizeY = image.matrix.length;
            int matrixSizeX = image.matrix[0].length;
            Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
            for(Integer[] row: resultingMatrix)
                Arrays.fill(row, 0);
            // Вычисление ширины и высоты старого образа
            int sizeOldX = image.rightBorder-image.leftBorder+1;
            int sizeOldY = image.upperBorder-image.lowerBorder+1;
            // Вычисление коэффициента масштабирования
            double coefX = matrixSizeX / (2.0 * sizeOldX);
            double coefY = matrixSizeY / (2.0 * sizeOldY);
            double scalingCoef = Math.min(coefX, coefY);
            System.out.println("Коэффициент масштабирования: " + scalingCoef);
            // Новые координаты образа
            Integer[] coordsNew = new Integer[4];
            coordsNew[0] = 0; coordsNew[1] = 0;
            coordsNew[2] = (int) Math.round(sizeOldX*scalingCoef);
            coordsNew[3] = (int) Math.round(sizeOldY*scalingCoef);
            // Применение интерполяции
            nearestNeighbourInterpolation(image, sizeOldX, sizeOldY, resultingMatrix, coordsNew);
            CustomImage newCustomImage = new CustomImage(resultingMatrix);
            Config.recordInformation("Количество пикселей в образе: " + newCustomImage.getSignificantPixelCount());
            Config.recordInformation("Масштабирование выполнено\n");

            return newCustomImage;
        } catch (Exception ex) {
            Config.recordInformation("Ошибка масштабирования!\n");
            return image;
        }
    }

    // Интерполяция методом ближайшего соседа
    // На вход старый образ и его координаты, новый образ и его координаты
    // На выходе изменённый новый образ
    // coordsOld и coordsNew имеют следующую структуру: x min, y min, x max, y max
    public static void nearestNeighbourInterpolation (CustomImage imgOld, int sizeOldX, int sizeOldY,
                                                      Integer[][] imgNew, Integer[] coordsNew) {
        // Вычисление ширины и высоты нового образа
        int sizeNewX = coordsNew[2]-coordsNew[0]+1;
        int sizeNewY = coordsNew[3]-coordsNew[1]+1;
        // Вычисление коэффициентов масштабирования образа по осям
        float coefX = (float)(sizeOldX)/(sizeNewX);
        float coefY = (float)(sizeOldY)/(sizeNewY);
        int oldX, oldY;
        for(int y=coordsNew[1]; y<=coordsNew[3]; y++)
            for(int x=coordsNew[0]; x<=coordsNew[2]; x++) {
                // Вычисление координат старого образа по преобразованным
                // координатам нового образа
                oldX = (int)Math.floor((x - coordsNew[0]) * coefX);
                oldY = (int)Math.floor((y - coordsNew[1]) * coefY);
                // Заполнение элемента нового образа, возвращаясь
                // к стандартным координатам
                imgNew[y][x] = imgOld.matrix[oldY + imgOld.lowerBorder][oldX + imgOld.leftBorder];
            }
    }

}
