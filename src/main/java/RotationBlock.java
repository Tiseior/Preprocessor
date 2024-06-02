import java.util.Arrays;

public class RotationBlock {

    // Метод для поворота изображения, используя 4 найденных угла.
    // Здесь выбирается наилучший угол для поворота, исходя из размера области,
    // занимаемой образом. Чем меньше размер этой области, тем прямее
    // расположен образ.
    public static CustomImage rotation(CustomImage image) {
        Config.recordInformation("Выполнение ротации");
        try {
            // Если отключен автоповорот
            if(!Config.rotAuto) {
                Config.recordInformation("Поворот на угол: " + Config.angle);
                CustomImage newCustomImage = rotByAngle(image, Config.angle);
                Config.recordInformation("Ротация выполнена\n");
                return newCustomImage;
            }

            // Получаем возможные углы для поворота
            Double[] angles = takeAngle(image);
            double bestAngle = 0.0;
            System.out.println("Всего углов: " + angles.length);
            // Вычисляем изначальный размер области
            int sectorSize = (image.rightBorder - image.leftBorder) + (image.upperBorder - image.lowerBorder);
            int size;
            CustomImage newCustomImage;
            Config.recordInformation("Первичный размер сектора: " + sectorSize);
            System.out.println("Первичный размер сектора: " + sectorSize);
            // Отклонение для вычисления размера сектора
            // Отклонение составляет 1% от исходного размера сектора
            int deviation = sectorSize/100;
            Config.recordInformation("Возможное отклонение размера сектора: " + deviation);
            System.out.println("Возможное отклонение размера сектора: " + deviation);
            for (Double angle : angles) {
                if (Math.abs(angle) > 0.001) {
                    Config.recordInformation("Угол: " +
                            (Math.round(angle * 10000.0) / 10000.0));
                    System.out.println("Угол: " + angle);
                    // Копирование образа и работа с копией
                    newCustomImage = rotByAngle(image, angle);
                    size = (newCustomImage.rightBorder - newCustomImage.leftBorder) +
                            (newCustomImage.upperBorder - newCustomImage.lowerBorder);
                    Config.recordInformation("Размер сектора: " + size);
                    System.out.println("Размер сектора: " + size);
                    // Выбирается угол, если:
                    // 1) Новый сектор меньше предыдущего
                    // 2) Угол ранее не выбран и новый сектор меньше или равен предыдущему
                    // 3) Угол ранее не выбран и новый сектор больше предыдущего
                    //    не более чем на величину отклонения
                    if (size < sectorSize
                            || (bestAngle == 0.0 && size - sectorSize <= deviation)) {
                        sectorSize = size;
                        bestAngle = angle;
                    }
                }
            }
            Config.recordInformation("Итоговый угол поворота: " +
                    (Math.round(bestAngle * 10000.0) / 10000.0));
            System.out.println("Итоговый угол поворота: " + bestAngle);
            if(Math.abs(bestAngle) > 0.001)
                image = rotByAngle(image, bestAngle);
            Config.recordInformation("Ротация выполнена\n");

            return image;
        } catch (Exception ex) {
            Config.recordInformation("Ошибка ротации!\n");
            return image;
        }
    }

    // Метода для получения угла поворота по
    // четырём прямоугольным треугольникам.
    // Описание принципа работы.
    // Находим прямоугольник, в который заключён образ (левый верхний и правый нижний пиксель).
    // Двигаемся по углам этого прямоугольника по осям X и Y.
    // Первые встречные пиксели образа - это вершины прямоугольного треугольника.
    // Далее вычисляем наименьший угол в треугольнике. Таким образом получаем 4 угла.
    // Для определения знака поворота используем сумму разниц длин катетов прямоугольных треугольников.
    // Берём меньший из четырёх углов и ставим получившийся знак.
    public static Double[] takeAngle(CustomImage image) {
        int sizeX = image.rightBorder-image.leftBorder+1;
        int sizeY = image.upperBorder-image.lowerBorder+1;
        System.out.println("Размер по X:" + sizeX);
        System.out.println("Размер по Y:" + sizeY);
        int sign = 0;
        Double[] angles = new Double[4];

        // Две точки пересечения с прямоугольником для левого нижнего угла
        System.out.print("Left Bottom: ");
        Integer[] cornerLeftBottom =
                calculCoords(image.leftBorder, image.rightBorder, -image.upperBorder, image.lowerBorder, image.matrix);
        sign = Math.abs(cornerLeftBottom[3]-cornerLeftBottom[5]) -
                (Math.abs(cornerLeftBottom[0]-cornerLeftBottom[4]));
        angles[0] = (sign < 0) ? -angle(cornerLeftBottom) : angle(cornerLeftBottom);
        // Две точки пересечения с прямоугольником для левого верхнего угла
        System.out.print("Left Top: ");
        Integer[] cornerLeftTop =
                calculCoords(image.leftBorder, image.rightBorder, image.lowerBorder, image.upperBorder, image.matrix);
        sign = Math.abs(cornerLeftTop[0]-cornerLeftTop[4]) -
                (Math.abs(cornerLeftTop[3]-cornerLeftTop[5]));
        angles[1] = (sign < 0) ? -angle(cornerLeftTop) : angle(cornerLeftTop);
        // Две точки пересечения с прямоугольником для правого верхнего угла
        System.out.print("Right Top: ");
        Integer[] cornerRightTop =
                calculCoords(-image.rightBorder, image.leftBorder, image.lowerBorder, image.upperBorder, image.matrix);
        sign = Math.abs(cornerRightTop[3]-cornerRightTop[5]) -
                (Math.abs(cornerRightTop[0]-cornerRightTop[4]));
        angles[2] = (sign < 0) ? -angle(cornerRightTop) : angle(cornerRightTop);
        // Две точки пересечения с прямоугольником для правого нижнего угла
        System.out.print("Right Bottom: ");
        Integer[] cornerRightBottom =
                calculCoords(-image.rightBorder, image.leftBorder, -image.upperBorder, image.lowerBorder, image.matrix);
        sign = Math.abs(cornerRightBottom[0]-cornerRightBottom[4]) -
                (Math.abs(cornerRightBottom[3]-cornerRightBottom[5]));
        angles[3] = (sign < 0) ? -angle(cornerRightBottom) : angle(cornerRightBottom);

        System.out.println("Angle Left Bottom: " + angles[0]);
        System.out.println("Angle Left Top: " + angles[1]);
        System.out.println("Angle Right Top: " + angles[2]);
        System.out.println("Angle Right Bottom: " + angles[3]);
        Config.recordInformation("Левый нижний угол: " +
                (Math.round(angles[0] * 10000.0) / 10000.0));
        Config.recordInformation("Левый верхний угол: " +
                (Math.round(angles[1] * 10000.0) / 10000.0));
        Config.recordInformation("Правый верхний угол: " +
                (Math.round(angles[2] * 10000.0) / 10000.0));
        Config.recordInformation("Правый нижний угол: " +
                (Math.round(angles[3] * 10000.0) / 10000.0));
        Arrays.sort(angles);

        return angles;
    }

    // Метод для вычисления координат вершин прямоугольного треугольника
    public static Integer[] calculCoords(int xFrom, int xTo, int yFrom, int yTo, Integer[][] img) {
        Integer[] coords = new Integer[6];
        int xFromAbs = Math.abs(xFrom);
        int yFromAbs = Math.abs(yFrom);
        for(int x=xFrom; x<=xTo; x++)
            if(img[yFromAbs][Math.abs(x)] != 0) {
                coords[0] = Math.abs(x);
                coords[1] = yFromAbs;
                break;
            }
        for(int y=yFrom; y<=yTo; y++)
            if(img[Math.abs(y)][xFromAbs] != 0) {
                coords[2] = xFromAbs;
                coords[3] = Math.abs(y);
                break;
            }
        coords[4] = xFromAbs;
        coords[5] = yFromAbs;
        System.out.print("(" + coords[0] + " " + coords[1] + ") ");
        System.out.print("(" + coords[2] + " " + coords[3] + ") ");
        System.out.print("(" + coords[4] + " " + coords[5] + ")\n");

        return coords;
    }

    // Метод для вычисления наименьшего угла у прямоугольного треугольника
    public static double angle(Integer[] triangle) {
        // Проверка, является ли образ "прямым"
        if(triangle[1].equals(triangle[3]))
            return 0.0;
        // Вычисление гипотенузы прямоугольного треугольника
        double hypo = Math.sqrt((triangle[0]-triangle[2])*(triangle[0]-triangle[2]) +
                (triangle[1]-triangle[3])*(triangle[1]-triangle[3]));
        // Малый катет прямоугольного треугольника
        double leg = Math.min(Math.abs(triangle[0]-triangle[2]), Math.abs(triangle[1]-triangle[3]));
        // Вычисление угла поворота
        double angle = Math.toDegrees(Math.asin(leg/hypo));

        return angle;
    }

    // Поворот матрицы на заданный угол
    public static CustomImage rotByAngle(CustomImage image, Double angle) {
        int matrixSizeY = image.matrix.length;    // Высота
        int matrixSizeX = image.matrix[0].length; // Ширина
        int centerX = image.leftBorder + (image.rightBorder - image.leftBorder)/2;
        int centerY = image.lowerBorder + (image.upperBorder - image.lowerBorder)/2;

        Double radians = angle/180.0 * Math.PI;
        System.out.println(radians);

        Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
        for(Integer[] row: resultingMatrix)
            Arrays.fill(row, 0);

        double sinRes = Math.sin(-radians);
        System.out.println("sin = " + sinRes);
        double cosRes = Math.cos(-radians);
        System.out.println("cos = " + cosRes);

        for (int y = 0; y< matrixSizeY; y++) {
            for (int x = 0; x< matrixSizeX; x++) {
                int xNew = (int) ((x - centerX) * cosRes - (y - centerY) * sinRes + centerX);
                int yNew = (int) ((x - centerX) * sinRes + (y - centerY) * cosRes + centerY);
                resultingMatrix[y][x] = sampleAt(image.matrix, xNew, yNew);
            }
        }

        return new CustomImage(resultingMatrix);
    }

    // Функция для вычисления значения пикселя
    public static Integer sampleAt(Integer[][] img, int x, int y) {
        int matrixSizeY = img.length;    // Высота
        int matrixSizeX = img[0].length; // Ширина
        Integer clamp = 1;
        if (x < 0) {
            x = 0; clamp = 0;
        } else {
            if (x >= matrixSizeX) {
                x = matrixSizeX - 1; clamp = 0;
            }
        }

        if (y < 0) {
            y = 0; clamp = 0;
        } else {
            if (y >= matrixSizeY) {
                y = matrixSizeY -1; clamp = 0;
            }
        }

        Integer pixel = img[y][x];

        return pixel*clamp;
    }

}
