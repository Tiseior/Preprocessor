import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Processing {

    // Чтение изображения из файла
    // Преобразование изображения в двумерный целочисленный массив со значениями элементов:
    // 0 - белый пиксель, 1 - чёрный пиксель (изначально -1 - белый, 0 - чёрный)
    // Возвращает преобразованный двумерный массив
    public static Integer[][] readAndConvertImageToArray(File file) {
        try {
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
                // Тусклые пиксели делаем нулевыми -4610000
                if (elem > -4610000)
                    imgData[i%height][i/height] = 0;
                else
                    imgData[i%height][i/height] = 1; // Здесь можно заменить на elem или 1
            }
            return imgData;
        } catch (IOException e) {
            System.out.println("Image not found");
            Integer[][] imgData = new Integer[0][0];
            return imgData;
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

    // Вывод образа
    public static void printImg(Integer[][] img) {
        for (Integer[] row: img) {
            for (Integer elem : row) {
                if (elem != 0)
                    //System.out.print("\u001B[31m" + elem + "\u001B[0m" + " ");
                    System.out.print(elem + " ");
                else
                    //System.out.print(elem + " ");
                    System.out.print(". ");
            }
            System.out.println();
        }
    }

    // Количество единичных пикселей
    public static Integer pixelCount(Integer[][] img) {
        Integer pixelCount = 0; // Количество не нулевых пикселей
        for(Integer[] row : img)
            for(Integer elem : row)
                if(elem != 0)
                    pixelCount += 1;
        return pixelCount;
    }

    // Метод для получения координат образа на изображении.
    // На выходе массив с координатами (x, y) левого верхнего пикселя образа и
    // координатами (x, y) правого нижнего пикселя образа
    public static Integer[] imgCoords(Integer[][] img) {
        Integer matrixSizeY = img.length;
        Integer matrixSizeX = img[0].length;
        Integer[] coords = new Integer[4];
        for(int y=0; y<matrixSizeY; y++)
            for(int x=0; x<matrixSizeX; x++) {
                if(img[y][x] != 0) {
                    if (coords[0] == null) {
                        // Первое заполнение массивов с координатами
                        coords[0] = x; coords[1] = y;
                        coords[2] = x; coords[3] = y;
                    } else {
                        // Дальнейшее изменение координат образов
                        if(x < coords[0]) coords[0] = x;
                        if(y < coords[1]) coords[1] = y;
                        if(x > coords[2]) coords[2] = x;
                        if(y > coords[3]) coords[3] = y;
                    }
                }
            }
        return coords;
    }

    // Центрирование образа
    public static Integer[][] translation(Integer[][] img) {
        Config.recordInformation("Выполнение трансляции");
        try {
            Integer[] coords = imgCoords(img);
            Config.recordInformation("Координаты углов образа: (" + coords[0] + "; " + coords[1] + "), (" +
                    coords[2] + "; " + coords[3] + ")");
            Integer matrixSizeY = img.length;
            Integer matrixSizeX = img[0].length;
            Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
            for(Integer[] row: resultingMatrix)
                Arrays.fill(row, 0);

            Integer pixelCount = pixelCount(img);

            Integer shiftX = 0; // Сдвиг по оси X
            Integer shiftY = 0; // Сдвиг по оси Y
            for(int y=0; y<matrixSizeY; y++)
                for(int x=0; x<matrixSizeX; x++) {
                    if(img[y][x] != 0) {
                        shiftX = shiftX + (x+1);
                        shiftY = shiftY + (y+1);
                    }
                }

            shiftX = Math.round((float)(matrixSizeX/2.0) - (float)(shiftX/pixelCount));
            shiftY = Math.round((float)(matrixSizeY/2.0) - (float)(shiftY/pixelCount));
            System.out.println(shiftX);
            System.out.println(shiftY);

            for(int y=0; y<matrixSizeY; y++)
                for(int x=0; x<matrixSizeX; x++) {
                    int elem = img[y][x];
                    if(elem != 0)
                        resultingMatrix[y+shiftY][x+shiftX] = elem;
                }
            coords = imgCoords(resultingMatrix);
            Config.recordInformation("Координаты углов образа: (" + coords[0] + "; " + coords[1] + "), (" +
                    coords[2] + "; " + coords[3] + ")");
            Config.recordInformation("Трансляция выполнена\n");

            return resultingMatrix;
        } catch (Exception ex) {
            Config.recordInformation("Ошибка трансляции!\n");
            return img;
        }
    }

    // Подобранный алгоритм масштабирования
    public static Integer[][] scalingNew(Integer[][] img) {
        Config.recordInformation("Выполнение масштабирования");
        try {
            Config.recordInformation("Количество пикселей в образе: " + pixelCount(img));
            Integer matrixSizeY = img.length;
            Integer matrixSizeX = img[0].length;
            Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
            for(Integer[] row: resultingMatrix)
                Arrays.fill(row, 0);
            Integer[] coordsOld = imgCoords(img);
            // Вычисление ширины и высоты старого образа
            int sizeOldX = coordsOld[2]-coordsOld[0]+1;
            int sizeOldY = coordsOld[3]-coordsOld[1]+1;
            Double coefX = matrixSizeX / (2.0 * sizeOldX);
            Double coefY = matrixSizeY / (2.0 * sizeOldY);
            Double scalingCoef = (coefX<coefY) ? coefX : coefY;
            System.out.println("scalingCoef: " + scalingCoef);
            Integer[] coordsNew = new Integer[4];
            coordsNew[0] = 0; coordsNew[1] = 0;
            coordsNew[2] = (int) Math.round(sizeOldX*scalingCoef);
            coordsNew[3] = (int) Math.round(sizeOldY*scalingCoef);
            for(int i=0; i<coordsOld.length; i++) {
                System.out.println("old: " + coordsOld[i]);
                System.out.println("new: " + (coordsNew[i]));
            }
            nearestNeighbourInterpolation(img, coordsOld, resultingMatrix, coordsNew);
            Config.recordInformation("Количество пикселей в образе: " + pixelCount(resultingMatrix));
            Config.recordInformation("Масштабирование выполнено\n");

            return resultingMatrix;
        } catch (Exception ex) {
            Config.recordInformation("Ошибка масштабирования!\n");
            return img;
        }
    }

    // Масштабирование образа
    public static Integer[][] scaling(Integer[][] img) {
        Integer matrixSizeY = img.length;
        Integer matrixSizeX = img[0].length;
        Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
        for(Integer[] row: resultingMatrix)
            Arrays.fill(row, 0);

        System.out.println("allPixels: " + matrixSizeX*matrixSizeY);

        Integer pixelCount = pixelCount(img);
        System.out.println("pixelCount: " + pixelCount);

        Double avgDistance = 0.0; // Среднее расстояние

        for(int y=0; y<matrixSizeY; y++)
            for(int x=0; x<matrixSizeX; x++)
                if(img[y][x] != 0)
                    avgDistance = avgDistance + img[y][x]*Math.sqrt((y+1)*(y+1) + (x+1)*(x+1));
        avgDistance /= pixelCount;

        System.out.println("avg: " + avgDistance);

        // Вычисление размера кадра образа
        //Double frameSizePart = Math.sqrt(matrixSizeX*matrixSizeX+matrixSizeY*matrixSizeY+pixelCount*pixelCount)/4.0;
        Double frameSizePart = Math.sqrt(matrixSizeX*matrixSizeX+matrixSizeY*matrixSizeY);
        // Для увеличения образа
        //Double scalingCoef = frameSizePart/avgDistance;
        // Для уменьшения образа
        //Double scalingCoef = avgDistance/frameSizePart;
        // !!! Условие на основе предположения об уменьшении или увеличении
        Double scalingCoef = 0.0;
        if(matrixSizeX*matrixSizeY>4*pixelCount/*avgDistance>pixelCount*/)
            scalingCoef = frameSizePart/avgDistance;
        else if(matrixSizeX*matrixSizeY<4*pixelCount/*avgDistance<pixelCount*/)
            scalingCoef = avgDistance/frameSizePart;
        System.out.println("scalingCoef: " + scalingCoef);

        // Координаты образа до масштабирования: x min, y min, x max, y max
        Integer[] coordsOld = new Integer[4];
        // Координаты образа после масштабирования: x min, y min, x max, y max
        Integer[] coordsNew = new Integer[4];
        // Возможное решение невозможности масштабирования образа
        try {
            for(int y=0; y<matrixSizeY; y++)
                for(int x=0; x<matrixSizeX; x++) {
                    if(img[y][x] != 0) {
                        // Для увеличения образа
                        //int xm = Math.abs((int) Math.round(matrixSizeX/2.0 - x*scalingCoef));
                        //int ym = Math.abs((int) Math.round(matrixSizeY/2.0 - y*scalingCoef));
                        // Для уменьшения образа
                        //int xm = Math.abs((int) Math.round(x*scalingCoef));
                        //int ym = Math.abs((int) Math.round(y*scalingCoef));
                        // !!! Условие на основе предположения об уменьшении или увеличении
                        int xm = 0, ym = 0;
                        if(matrixSizeX*matrixSizeY>4*pixelCount/*avgDistance>pixelCount*/) {
                            xm = Math.abs((int) Math.round(matrixSizeX/2.0 - x*scalingCoef));
                            ym = Math.abs((int) Math.round(matrixSizeY/2.0 - y*scalingCoef));
                        }
                        else if(matrixSizeX*matrixSizeY<4*pixelCount/*avgDistance<pixelCount*/) {
                            xm = Math.abs((int) Math.round(x*scalingCoef));
                            ym = Math.abs((int) Math.round(y*scalingCoef));
                        }

                        if (coordsOld[0] == null) {
                            // Первое заполнение массивов с координатами
                            coordsOld[0] = x; coordsOld[1] = y;
                            coordsOld[2] = x; coordsOld[3] = y;
                            coordsNew[0] = xm; coordsNew[1] = ym;
                            coordsNew[2] = xm; coordsNew[3] = ym;
                        } else {
                            // Дальнейшее изменение координат образов
                            if(x < coordsOld[0]) coordsOld[0] = x;
                            if(y < coordsOld[1]) coordsOld[1] = y;
                            if(x > coordsOld[2]) coordsOld[2] = x;
                            if(y > coordsOld[3]) coordsOld[3] = y;
                            if(xm < coordsNew[0]) coordsNew[0] = xm;
                            if(ym < coordsNew[1]) coordsNew[1] = ym;
                            if(xm > coordsNew[2]) coordsNew[2] = xm;
                            if(ym > coordsNew[3]) coordsNew[3] = ym;
                        }
                        System.out.println(xm + " " + ym);
                        resultingMatrix[ym][xm] = 1;
                    }
                }
            //printImg(resultingMatrix);
            //nearestNeighbourInterpolation(img, coordsOld, resultingMatrix, coordsNew);
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            System.out.println("Данный образ нельзя масштабировать");
            resultingMatrix = img;
        }
        return resultingMatrix;
    }

    // Заполнение пропусков в матрице
    // Идём тройками. В начале берём по три элемента построчно, если боковые равны 1, то и средний равен 1.
    // Потом идём тройками, но по столбцам.
    // 101 = 111
    public static Integer[][] fillGaps(Integer[][] img) {
        Integer matrixSizeY = img.length;
        Integer matrixSizeX = img[0].length;
        // Проходим тройками по каждой строке
        if(matrixSizeX >= 3) {
            for(int y=0, indexX=1; y < matrixSizeY; y++) {
                while(indexX+1 < matrixSizeX) {
                    if(img[y][indexX-1] == 1 && img[y][indexX+1] == 1)
                        img[y][indexX] = 1;
                    indexX++;
                }
                indexX = 1;
            }
        }
        // Проходим тройками по каждому столбцу
        if(matrixSizeY >= 3) {
            for(int x=0, indexY=1; x < matrixSizeX; x++) {
                while(indexY+1 < matrixSizeY) {
                    if(img[indexY-1][x] == 1 && img[indexY+1][x] == 1)
                        img[indexY][x] = 1;
                    indexY++;
                }
                indexY = 1;
            }
        }

        return img;
    }

    // Интерполяция методом ближайшего соседа
    // На вход старый образ и его координаты, новый образ и его координаты
    // На выходе изменённый новый образ
    // coordsOld и coordsNew имеют следующую структуру: x min, y min, x max, y max
    public static void nearestNeighbourInterpolation (Integer[][] imgOld, Integer[] coordsOld,
                                                      Integer[][] imgNew, Integer[] coordsNew) {
        // Вычисление ширины и высоты старого образа
        int sizeOldX = coordsOld[2]-coordsOld[0]+1;
        int sizeOldY = coordsOld[3]-coordsOld[1]+1;
        // Вычисление ширины и высоты нового образа
        int sizeNewX = coordsNew[2]-coordsNew[0]+1;
        int sizeNewY = coordsNew[3]-coordsNew[1]+1;
        // Вычисление коэффициентов масштабирования образа по осям
        float coefX = (float)(sizeOldX)/(sizeNewX);
        float coefY = (float)(sizeOldY)/(sizeNewY);
        //for(Integer[] row: imgNew)
        //    Arrays.fill(row, 0);
        int oldX, oldY;
        for(int y=coordsNew[1]; y<=coordsNew[3]; y++)
            for(int x=coordsNew[0]; x<=coordsNew[2]; x++)
                /*if(imgNew[y][x] == 0)*/ {
                    // Вычисление координат старого образа по преобразованным
                    // координатам нового образа
                    oldX = (int)Math.floor((x - coordsNew[0]) * coefX);
                    oldY = (int)Math.floor((y - coordsNew[1]) * coefY);
                    // Заполнение элемента нового образа, возвращаясь
                    // к стандартным координатам
                    imgNew[y][x] = imgOld[oldY + coordsOld[1]][oldX + coordsOld[0]];
                }
    }

    // Метод для поворота изображения, используя 4 найденных угла.
    // Здесь выбирается наилучший угол для поворота, исходя из размера области,
    // занимаемой образом. Чем меньше размер этой области, тем прямее
    // расположен образ.
    public static Integer[][] rotationNew(Integer[][] img) {
        Config.recordInformation("Выполнение ротации");
        try {
            // Если отключен автоповорот
            if(!Config.rotAuto) {
                Config.recordInformation("Поворот на угол: " + Config.angle);
                img = rot(img, Config.angle);
                Config.recordInformation("Ротация выполнена\n");
                return img;
            }

            // Получаем возможные углы для поворота
            Double[] angles = takeAngle(img);
            Config.recordInformation("Левый нижний угол: " + angles[0]);
            Config.recordInformation("Левый верхний угол: " + angles[1]);
            Config.recordInformation("Правый верхний угол: " + angles[2]);
            Config.recordInformation("Правый нижний угол: " + angles[3]);
            Double bestAngle = 0.0;
            System.out.println("Всего углов: " + angles.length);
            // Вычисляем изначальные координаты образа и размер области
            Integer[] coords = imgCoords(img);
            int sectorSize = (coords[2]-coords[0]) + (coords[3]-coords[1]);
            int size;
            Integer[][] imgCopy = new Integer[img.length][img[0].length];
            Config.recordInformation("Первичный размер сектора: " + sectorSize);
            System.out.println("Первичный размер сектора: " + sectorSize);
            // Отклонение для вычисления размера сектора
            // Отклонение составляет 1% от исходного размера сектора
            int deviation = sectorSize/100;
            Config.recordInformation("Возможное отклонение размера сектора: " + deviation);
            System.out.println("Возможное отклонение размера сектора: " + deviation);
            for (Double angle : angles) {
                if (Math.abs(angle) > 0.001) {
                    Config.recordInformation("Угол: " + angle);
                    System.out.println("Угол: " + angle);
                    // Копирование образа и работа с копией
                    for (int i = 0; i < img.length; i++)
                        System.arraycopy(img[i], 0, imgCopy[i], 0, img[0].length);
                    imgCopy = rot(imgCopy, angle);
                    coords = imgCoords(imgCopy);
                    size = (coords[2] - coords[0]) + (coords[3] - coords[1]);
                    Config.recordInformation("Размер сектора: " + size);
                    System.out.println("Размер сектора: " + size);
                    // Выбирается угол, если:
                    // 1) Новый сектор меньше предыдущего
                    // 2) Угол ранее не выбран и новый сектор меньше или равен предыдущему
                    // 3) Угол ранее не выбран и новый сектор больше предыдущего
                    //    не более чем на величину отклонения
                    if (size < sectorSize
                            || (bestAngle == 0.0 && size <= sectorSize)
                            || (bestAngle == 0.0 && size - sectorSize <= deviation)) {
                        sectorSize = size;
                        bestAngle = angle;
                    }
                }
            }
            Config.recordInformation("Итоговый угол поворота: " + bestAngle);
            System.out.println("Итоговый угол поворота: " + bestAngle);
            if(Math.abs(bestAngle) > 0.001)
                img = rot(img, bestAngle);
            Config.recordInformation("Ротация выполнена\n");
            return img;
        } catch (Exception ex) {
            Config.recordInformation("Ошибка ротации!\n");
            return img;
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
    public static Double[] takeAngle(Integer[][] img) {
        // (x, y) левого верхнего угла и (x, y) правого нижнего угла образа
        Integer[] coords = imgCoords(img);
        int sizeX = coords[2]-coords[0];
        int sizeY = coords[3]-coords[1];
        System.out.println("Размер по X:" + sizeX);
        System.out.println("Размер по Y:" + sizeY);
        Integer sign = 0;
        Double[] angles = new Double[4];

        // Две точки пересечения с прямоугольником для левого нижнего угла
        System.out.print("Left Bottom: ");
        Integer[] cornerLeftBottom =
                calculCoords(coords[0], coords[2], -coords[3], coords[1], img);
        sign = Math.abs(cornerLeftBottom[3]-cornerLeftBottom[5]) -
                (Math.abs(cornerLeftBottom[0]-cornerLeftBottom[4]));
        angles[0] = (sign < 0) ? -angle(cornerLeftBottom) : angle(cornerLeftBottom);
        // Две точки пересечения с прямоугольником для левого верхнего угла
        System.out.print("Left Top: ");
        Integer[] cornerLeftTop =
                calculCoords(coords[0], coords[2], coords[1], coords[3], img);
        sign = Math.abs(cornerLeftTop[0]-cornerLeftTop[4]) -
                (Math.abs(cornerLeftTop[3]-cornerLeftTop[5]));
        angles[1] = (sign < 0) ? -angle(cornerLeftTop) : angle(cornerLeftTop);
        // Две точки пересечения с прямоугольником для правого верхнего угла
        System.out.print("Right Top: ");
        Integer[] cornerRightTop =
                calculCoords(-coords[2], coords[0], coords[1], coords[3], img);
        sign = Math.abs(cornerRightTop[3]-cornerRightTop[5]) -
                (Math.abs(cornerRightTop[0]-cornerRightTop[4]));
        angles[2] = (sign < 0) ? -angle(cornerRightTop) : angle(cornerRightTop);
        // Две точки пересечения с прямоугольником для правого нижнего угла
        System.out.print("Right Bottom: ");
        Integer[] cornerRightBottom =
                calculCoords(-coords[2], coords[0], -coords[3], coords[1], img);
        sign = Math.abs(cornerRightBottom[0]-cornerRightBottom[4]) -
                (Math.abs(cornerRightBottom[3]-cornerRightBottom[5]));
        angles[3] = (sign < 0) ? -angle(cornerRightBottom) : angle(cornerRightBottom);

        System.out.println("Angle Left Bottom: " + angles[0]);
        System.out.println("Angle Left Top: " + angles[1]);
        System.out.println("Angle Right Top: " + angles[2]);
        System.out.println("Angle Right Bottom: " + angles[3]);
        System.out.println("Sign = " + sign);

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

    // Поворот образа
    public static Integer[][] rotation(Integer[][] img) {
        Integer matrixSizeY = img.length;
        Integer matrixSizeX = img[0].length;
        Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
        for(Integer[] row: resultingMatrix)
            Arrays.fill(row, 0);

        Integer pixelCount = pixelCount(img);
        Integer txx = 0;
        Integer tyy = 0;
        Integer txy = 0;
        Integer[] coordsOld = imgCoords(img);
        int coordX, coordY = 0;
        for(int y=0; y<matrixSizeY; y++) {
            for(int x=0; x<matrixSizeX; x++) {
                if(img[y][x] != 0) {
                    coordX = x - (coordsOld[0]+1);
                    coordY = y - (coordsOld[1]+1);
                    txx += (img[y][x]* coordX * coordX);
                    tyy += (img[y][x]* coordY * coordY);
                    txy += (img[y][x]* coordX * coordY);
                }
            }
        }
        /*for(int y=0; y<matrixSizeY; y++) {
            for(int x=0; x<matrixSizeX; x++) {
                if(img[y][x] != 0) {
                    txx += (img[y][x]*(x+1)*(x+1));
                    tyy += (img[y][x]*(y+1)*(y+1));
                    txy += (img[y][x]*(x+1)*(y+1));
                }
            }
        }*/
        System.out.println("Txx = " + txx);
        System.out.println("Tyy = " + tyy);
        System.out.println("Txy = " + txy);
        Double m1 = 8.0 * txy * txy;
        Double m2 = 2.0 * Math.pow(tyy - txx, 2);
        Double m3 = 1.0 * (tyy - txx);
        Double m4 = Math.sqrt(Math.pow(tyy - txx, 2) + 4.0 * txy * txy);
        Double bigM = Math.sqrt(2.0 * (m4 + m3) * m4);
        //Double bigM = Math.sqrt(8*txy*txy + 2*Math.pow(tyy-txx, 2) + 2*(tyy-txx) *
        //                         Math.sqrt(Math.pow(tyy-txx, 2)) + 4*txy*txy);
        //Double bigM = Math.sqrt(2 * (Math.sqrt(Math.pow(tyy-txx, 2) + 4*txy*txy) + (tyy-txx)) *
        //                        Math.sqrt(Math.pow(tyy-txx, 2) + 4*txy*txy));
        System.out.println("M = " + bigM);
        Double sinTeta = ((tyy - txx) + m4) / bigM;
        Double cosTeta = (2 * txy) / bigM;
        //Double sinTeta = (tyy - txx + Math.sqrt(Math.pow(tyy-txx, 2)) + 4*txy*txy)/bigM;
        //Double cosTeta = (2*tyy)/bigM;
        //Double sinTeta = ((tyy-txx) + Math.sqrt(Math.pow(tyy-txx, 2) + 4*txy*txy))/bigM;
        //Double cosTeta = (2*txy)/bigM;
        System.out.println("sin = " + sinTeta + " => " + Math.toDegrees(Math.asin(sinTeta)));
        Double degS = Math.sin(sinTeta);
        System.out.println(degS + " => " + Math.toDegrees(degS));
        System.out.println("cos = " + cosTeta + " => " + Math.toDegrees(Math.acos(cosTeta)));
        Double degC = Math.cos(cosTeta);
        System.out.println(degC + " => " + Math.toDegrees(degC));
        System.out.println(Math.toDegrees(sinTeta-cosTeta));
        for(int y=0; y<matrixSizeY; y++) {
            for(int x=0; x<matrixSizeX; x++) {
                if(img[y][x] != 0) {
                    //Double newX = x*cosTeta - y*sinTeta;
                    //Double newY = x*sinTeta + y*cosTeta;
                    //Double newX = cosTeta*x + sinTeta*y;
                    //Double newY = -sinTeta*x + cosTeta*y;
                    //int newX = Math.abs((int)Math.round(matrixSizeX/2.0 - (cosTeta*x +
                    //        sinTeta*y)));
                    //int newY = Math.abs((int)Math.round(matrixSizeY/2.0 - (-sinTeta*x +
                    //       cosTeta*y)));
                    int newX = Math.abs((int)Math.round(matrixSizeX/2.0 - ((x+1)*cosTeta - (y+1)*sinTeta)));
                    int newY = Math.abs((int)Math.round(matrixSizeY/2.0 - ((x+1)*sinTeta + (y+1)*cosTeta)));
                    resultingMatrix[newY][newX] = 1;
                }
            }
        }

        return resultingMatrix;
    }

    // Поворот матрицы на заданный угол
    public static Integer[][] rot(Integer[][] img, Double angle) {
        Integer matrixSizeY = img.length;    // Высота
        Integer matrixSizeX = img[0].length; // Ширина

        Double radians = angle/180.0 * Math.PI;

        Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
        for(Integer[] row: resultingMatrix)
            Arrays.fill(row, 0);

        Double S = Math.sin(-radians);
        Double C = Math.cos(-radians);

        for (int outy = 0; outy< matrixSizeY; outy++) {
            for (int outx = 0; outx< matrixSizeX; outx++) {
                Double cox = outx - matrixSizeX /2.0+1;
                Double coy = outy - matrixSizeY /2.0+1;

                Double inx = cox*C-coy*S + matrixSizeX/2;
                Double iny = coy*C+cox*S + matrixSizeY/2;

                Integer basex = inx.intValue();
                Integer basey = iny.intValue();
                resultingMatrix[outy][outx] = sampleAt(img, basex, basey);
            }
        }

        return resultingMatrix;
    }

    // Функция для вычисления значения пикселя
    public static Integer sampleAt(Integer[][] img, int x, int y) {
        Integer matrixSizeY = img.length;    // Высота
        Integer matrixSizeX = img[0].length; // Ширина
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
