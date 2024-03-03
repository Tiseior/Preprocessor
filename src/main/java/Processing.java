import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.imageio.ImageIO;

public class Processing {

    public static Images images = new Images();

    // Работа с образом в виде изображения
    public static void preprocImg(String imgName) throws IOException {
        File file = new File(images.path+imgName);
        Integer[][] img = readAndConvertImageToArray(file);
        //img = translation(img);
        //img = scaling(img);
        img = scalingNew(img);
        img = translation(img);
        //img = fillGaps(img);
        /*Integer pxOld = 0;
        Integer pxNew = 0;
        int iteration = 0;
        do {
            pxOld = pixelCount(img);
            img = scaling(img);
            pxNew = pixelCount(img);
            img = fillGaps(img);
            iteration++;
        } while (!Objects.equals(pxNew, pxOld));
        System.out.println("Количество итераций: " + iteration);*/
        //img = rotation(img);
        //takeAngle(img);
        //houghLine(img);
        //img = rot(img, 54.0);
        img = rot(img, takeAngleV2(img));
        convertArrayToImageAndWrite(img, "Result.png");
    }

    // Работа с образом в виде двумерного массива
    public static void preprocImg(Integer[][] imgName) {
        printImg(imgName);
        //imgName = scalingNew(imgName);
        //imgName = translation(imgName);
        //imgName = fillGaps(imgName);
        //printImg(imgName);
        takeAngleV2(imgName);
        //imgName = rot(imgName, 45.0);
        //printImg(imgName);
        //imgName = rotation(imgName);
        //rot(imgName, 90.0);
        //printImg(imgName);
    }

    // Чтение изображения из файла
    // Преобразование изображения в двумерный целочисленный массив со значениями элементов:
    // 0 - белый пиксель, 1 - чёрный пиксель (изначально -1 - белый, 0 - чёрный)
    // Возвращает преобразованный двумерный массив
    public static Integer[][] readAndConvertImageToArray(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            int height = image.getHeight();
            int width = image.getWidth();
            Integer[][] imgData = new Integer[height][width];
            for(int i=0; i<height*width; i++) {
                int elem = image.getRGB(i/height, i%height);
                if (elem == -1)
                    imgData[i%height][i/height] = 0;
                else
                    imgData[i%height][i/height] = 1;
            }
            return imgData;
        } catch (IOException e) {
            System.out.println("Изображение не найдено");
            Integer[][] imgData = new Integer[0][0];
            return imgData;
        }
    }

    // Преобразование двумерного целочисленного массива в изображение с расширением .png
    // со значениями пикселей: -1 - белый пиксель, 0 - чёрный пиксель
    // Запись полученного изображения в директорию с изображениями с заданным именем
    public static void convertArrayToImageAndWrite(Integer[][] img, String name) throws IOException {
        BufferedImage newImage = new BufferedImage(img[0].length, img.length, BufferedImage.TYPE_INT_RGB);
        for(int i=0; i<img.length; i++){
            for (int j=0; j<img[0].length; j++){
                if(img[i][j] == 0)
                    newImage.setRGB(j, i,-1);
                else
                    newImage.setRGB(j, i, 0);
            }
        }
        File output = new File(images.path+name);
        ImageIO.write(newImage, "png", output);
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
        Integer pixelCount = 0; // Количество единичных пикселей
        for(Integer[] row : img)
            for(Integer elem : row)
                pixelCount += elem;
        return pixelCount;
    }

    // Центрирование образа
    public static Integer[][] translation(Integer[][] img) {
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
                shiftX = shiftX + (x+1)*img[y][x];
                shiftY = shiftY + (y+1)*img[y][x];
            }

        shiftX = Math.round((float)(matrixSizeX/2.0) - (float)(shiftX/pixelCount));
        shiftY = Math.round((float)(matrixSizeY/2.0) - (float)(shiftY/pixelCount));
        System.out.println(shiftX);
        System.out.println(shiftY);
        /*if(shiftX<=0)
            shiftX+=1;
        if(shiftY<=0)
            shiftY+=1;*/

        for(int y=0; y<matrixSizeY; y++)
            for(int x=0; x<matrixSizeX; x++)
                if(img[y][x] != 0)
                    resultingMatrix[y+shiftY][x+shiftX] = 1;


        return resultingMatrix;
    }

    // Тестовый метод для подбора алгоритма масштабирования
    public static Integer[][] scalTest(Integer[][] img) {
        Integer matrixSizeY = img.length;
        Integer matrixSizeX = img[0].length;
        Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
        for(Integer[] row: resultingMatrix)
            Arrays.fill(row, 0);
        Integer allPixels = matrixSizeX*matrixSizeY;
        System.out.println("allPixels: " + allPixels);
        Integer pixelCount = pixelCount(img);
        Integer[] coordsOld = new Integer[4];
        // Вычисления данных для образа
        for(int y=0; y<matrixSizeY; y++)
            for(int x=0; x<matrixSizeX; x++) {
                if(img[y][x] != 0) {
                    if (coordsOld[0] == null) {
                        // Первое заполнение массивов с координатами
                        coordsOld[0] = x; coordsOld[1] = y;
                        coordsOld[2] = x; coordsOld[3] = y;
                    } else {
                        // Дальнейшее изменение координат образов
                        if(x < coordsOld[0]) coordsOld[0] = x;
                        if(y < coordsOld[1]) coordsOld[1] = y;
                        if(x > coordsOld[2]) coordsOld[2] = x;
                        if(y > coordsOld[3]) coordsOld[3] = y;
                    }
                }
            }
        // Вычисление ширины и высоты старого образа
        int sizeOldX = coordsOld[2]-coordsOld[0]+1;
        int sizeOldY = coordsOld[3]-coordsOld[1]+1;
        System.out.println("img: " + matrixSizeX + " x " + matrixSizeY);
        System.out.println("sizeOld: " + sizeOldX + " x " + sizeOldY);
        Integer imgPixels = sizeOldX*sizeOldY;
        System.out.println("imgPixels: " + imgPixels);
        // Вычисление среднего расстояния с преобразованными координатами образа
        //avgDistance = Math.sqrt((coordsOld[0]-coordsOld[2])*(coordsOld[0]-coordsOld[2]) +
        //                        (coordsOld[1]-coordsOld[3])*(coordsOld[1]-coordsOld[3]));
        int coordX, coordY;
        Double avgDistance = 0.0; // Среднее расстояние
        for(int y=0; y<matrixSizeY; y++)
            for(int x=0; x<matrixSizeX; x++)
                if(img[y][x] != 0) {
                    coordX = x - coordsOld[0]+1;
                    coordY = y - coordsOld[1]+1;
                    //coordX = x+1; coordY = y+1;
                    avgDistance = avgDistance + img[y][x]*Math.sqrt((coordX)*(coordX) +
                                                                    (coordY)*(coordY));
                }
        avgDistance /= pixelCount;
        System.out.println("avgDistance: " + avgDistance);
        //Double frameSizePart = Math.sqrt(matrixSizeX*matrixSizeX+matrixSizeY*matrixSizeY);
        Double frameSizePart = (double) allPixels/4;//(double) (matrixSizeX/(sizeOldX) + matrixSizeY/(sizeOldY));
        System.out.println("frameSizePart: " + frameSizePart);
        Double scalingCoef = 0.0;
        //if(allPixels>4*imgPixels) {
            scalingCoef = frameSizePart/avgDistance;
        //}
        //else if(allPixels<4*imgPixels)
        //    scalingCoef = avgDistance/frameSizePart;
        //scalingCoef = Math.floor(allPixels / (4.0*imgPixels));
        Double coefX = matrixSizeX / (2.0 * sizeOldX);
        Double coefY = matrixSizeY / (2.0 * sizeOldY);
        scalingCoef = (coefX>coefY) ? coefX : coefY;
        System.out.println("scalingCoef: " + scalingCoef);
        Integer[] coordsNew = new Integer[4];
        /*for(int i=0; i<coordsOld.length; i++)
            if(allPixels>4*imgPixels)
                coordsNew[i] = Math.abs((int) Math.round(matrixSizeX/2.0 - coordsOld[i]*scalingCoef));
            else if(allPixels<4*imgPixels)
                coordsNew[i] = Math.abs((int) Math.round(coordsOld[i]*scalingCoef));*/
        coordsNew[0] = 0; coordsNew[1] = 0;
        coordsNew[2] = (int) Math.round(sizeOldX*scalingCoef);
        coordsNew[3] = (int) Math.round(sizeOldY*scalingCoef);
        for(int i=0; i<coordsOld.length; i++) {
            System.out.println("old: " + coordsOld[i]);
            System.out.println("new: " + (coordsNew[i]));
        }
        nearestNeighbourInterpolation(img, coordsOld, resultingMatrix, coordsNew);
        resultingMatrix = translation(resultingMatrix);

        return resultingMatrix;
    }

    // Подобранный алгоритм масштабирования
    public static Integer[][] scalingNew(Integer[][] img) {
        Integer matrixSizeY = img.length;
        Integer matrixSizeX = img[0].length;
        Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
        for(Integer[] row: resultingMatrix)
            Arrays.fill(row, 0);
        Integer[] coordsOld = new Integer[4];
        // Вычисления данных для образа
        for(int y=0; y<matrixSizeY; y++)
            for(int x=0; x<matrixSizeX; x++) {
                if(img[y][x] != 0) {
                    if (coordsOld[0] == null) {
                        // Первое заполнение массивов с координатами
                        coordsOld[0] = x; coordsOld[1] = y;
                        coordsOld[2] = x; coordsOld[3] = y;
                    } else {
                        // Дальнейшее изменение координат образов
                        if(x < coordsOld[0]) coordsOld[0] = x;
                        if(y < coordsOld[1]) coordsOld[1] = y;
                        if(x > coordsOld[2]) coordsOld[2] = x;
                        if(y > coordsOld[3]) coordsOld[3] = y;
                    }
                }
            }
        // Вычисление ширины и высоты старого образа
        int sizeOldX = coordsOld[2]-coordsOld[0]+1;
        int sizeOldY = coordsOld[3]-coordsOld[1]+1;
        Double scalingCoef = 0.0;
        Double coefX = matrixSizeX / (2.0 * sizeOldX);
        Double coefY = matrixSizeY / (2.0 * sizeOldY);
        scalingCoef = (coefX<coefY) ? coefX : coefY;
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

        return resultingMatrix;
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
            nearestNeighbourInterpolation(img, coordsOld, resultingMatrix, coordsNew);
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
    public static Integer[][] nearestNeighbourInterpolation (Integer[][] imgOld, Integer[] coordsOld,
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

        return imgNew;
    }

    public static double takeAngleV2(Integer[][] img) {
        Integer matrixSizeY = img.length;
        Integer matrixSizeX = img[0].length;

        // (x, y) левого верхнего угла и (x, y) правого нижнего угла образа
        Integer[] coordsOld = new Integer[4];
        // Вычисления данных для образа
        for(int y=0; y<matrixSizeY; y++)
            for(int x=0; x<matrixSizeX; x++) {
                if(img[y][x] != 0) {
                    if (coordsOld[0] == null) {
                        // Первое заполнение массивов с координатами
                        coordsOld[0] = x; coordsOld[1] = y;
                        coordsOld[2] = x; coordsOld[3] = y;
                    } else {
                        // Дальнейшее изменение координат образов
                        if(x < coordsOld[0]) coordsOld[0] = x;
                        if(y < coordsOld[1]) coordsOld[1] = y;
                        if(x > coordsOld[2]) coordsOld[2] = x;
                        if(y > coordsOld[3]) coordsOld[3] = y;
                    }
                }
            }
        // Вычисление ширины и высоты старого образа
        int sizeOldX = coordsOld[2]-coordsOld[0]+1;
        int sizeOldY = coordsOld[3]-coordsOld[1]+1;

        Double[] angles = new Double[4];
        // Две точки пересечения с прямоугольником для левого верхнего угла
        Integer[] cornerLeftTop = new Integer[6];
        for(int x=coordsOld[0]; x<=coordsOld[2]; x++)
            if(img[coordsOld[1]][x] != 0) {
                cornerLeftTop[0] = x;
                cornerLeftTop[1] = coordsOld[1];
                break;
            }
        for(int y=coordsOld[1]; y<=coordsOld[3]; y++)
            if(img[y][coordsOld[0]] != 0) {
                cornerLeftTop[2] = coordsOld[0];
                cornerLeftTop[3] = y;
                break;
            }
        cornerLeftTop[4] = coordsOld[0];
        cornerLeftTop[5] = coordsOld[1];
        System.out.print("Left Top: ");
        System.out.print("(" + cornerLeftTop[0] + " " + cornerLeftTop[1] + ") ");
        System.out.print("(" + cornerLeftTop[2] + " " + cornerLeftTop[3] + ") ");
        System.out.print("(" + cornerLeftTop[4] + " " + cornerLeftTop[5] + ")\n");
        angles[0] = angle(cornerLeftTop);
        // Две точки пересечения с прямоугольником для правого верхнего угла
        Integer[] cornerRightTop = new Integer[6];
        for(int x=coordsOld[2]; x>=coordsOld[0]; x--)
            if(img[coordsOld[1]][x] != 0) {
                cornerRightTop[0] = x;
                cornerRightTop[1] = coordsOld[1];
                break;
            }
        for(int y=coordsOld[1]; y<=coordsOld[3]; y++)
            if(img[y][coordsOld[2]] != 0) {
                cornerRightTop[2] = coordsOld[2];
                cornerRightTop[3] = y;
                break;
            }
        cornerRightTop[4] = coordsOld[2];
        cornerRightTop[5] = coordsOld[1];
        System.out.print("Right Top: ");
        System.out.print("(" + cornerRightTop[0] + " " + cornerRightTop[1] + ") ");
        System.out.print("(" + cornerRightTop[2] + " " + cornerRightTop[3] + ") ");
        System.out.print("(" + cornerRightTop[4] + " " + cornerRightTop[5] + ")\n");
        angles[1] = angle(cornerRightTop);
        // Две точки пересечения с прямоугольником для левого нижнего угла
        Integer[] cornerLeftBottom = new Integer[6];
        for(int x=coordsOld[0]; x<=coordsOld[2]; x++)
            if(img[coordsOld[3]][x] != 0) {
                cornerLeftBottom[0] = x;
                cornerLeftBottom[1] = coordsOld[3];
                break;
            }
        for(int y=coordsOld[3]; y>=coordsOld[1]; y--)
            if(img[y][coordsOld[0]] != 0) {
                cornerLeftBottom[2] = coordsOld[0];
                cornerLeftBottom[3] = y;
                break;
            }
        cornerLeftBottom[4] = coordsOld[0];
        cornerLeftBottom[5] = coordsOld[3];
        System.out.print("Left Bottom: ");
        System.out.print("(" + cornerLeftBottom[0] + " " + cornerLeftBottom[1] + ") ");
        System.out.print("(" + cornerLeftBottom[2] + " " + cornerLeftBottom[3] + ") ");
        System.out.print("(" + cornerLeftBottom[4] + " " + cornerLeftBottom[5] + ")\n");
        angles[2] = angle(cornerLeftBottom);
        boolean isPositive;
        // Проверка, является ли нижний левый угол положительным или отрицательным
        if(Math.abs(cornerLeftBottom[0]-cornerLeftBottom[4])<Math.abs(cornerLeftBottom[3]-cornerLeftBottom[5]))
            isPositive = true;
        else
            isPositive = false;
        System.out.println(isPositive);
        // Две точки пересечения с прямоугольником для правого нижнего угла
        Integer[] cornerRightBottom = new Integer[6];
        for(int x=coordsOld[2]; x>=coordsOld[0]; x--)
            if(img[coordsOld[3]][x] != 0) {
                cornerRightBottom[0] = x;
                cornerRightBottom[1] = coordsOld[3];
                break;
            }
        for(int y=coordsOld[3]; y>=coordsOld[1]; y--)
            if(img[y][coordsOld[2]] != 0) {
                cornerRightBottom[2] = coordsOld[2];
                cornerRightBottom[3] = y;
                break;
            }
        cornerRightBottom[4] = coordsOld[2];
        cornerRightBottom[5] = coordsOld[3];
        System.out.print("Right Bottom: ");
        System.out.print("(" + cornerRightBottom[0] + " " + cornerRightBottom[1] + ") ");
        System.out.print("(" + cornerRightBottom[2] + " " + cornerRightBottom[3] + ") ");
        System.out.print("(" + cornerRightBottom[4] + " " + cornerRightBottom[5] + ")\n");
        angles[3] = angle(cornerRightBottom);

        System.out.println("Angle Left Top: " + angles[0]);
        System.out.println("Angle Right Top: " + angles[1]);
        System.out.println("Angle Left Bottom: " + angles[2]);
        System.out.println("Angle Right Bottom: " + angles[3]);
        double ac = 1.0;
        Arrays.sort(angles);
        if(isPositive) {
            System.out.println("Angle: " + angles[0]);
            return angles[0];
        } else {
            System.out.println("Angle: " + -angles[0]);
            return -angles[0];
        }
    }

    public static double angle(Integer[] triangle) {
        // Проверка, является ли образ "прямым"
        if(triangle[1].equals(triangle[3]))
            return 0.0;
        // Вычисление гипотенузы прямоугольного треугольника
        double hypo = Math.sqrt((triangle[0]-triangle[2])*(triangle[0]-triangle[2]) +
                (triangle[1]-triangle[3])*(triangle[1]-triangle[3]));
        // Малый катет прямоугольного треугольника
        double leg = Math.sqrt((triangle[0] - triangle[2])*(triangle[0] - triangle[2]));
        // Вычисление угла поворота
        double angle = Math.toDegrees(Math.asin(leg/hypo));
        return angle;

    }

    public static double angleOld(Integer[] triangle) {
        int dopX, dopY;
        if(triangle[1] < triangle[3]) {
            dopX = triangle[0]; dopY = triangle[3];
            double a = Math.sqrt((triangle[2] - dopX)*(triangle[2] - dopX) + (triangle[3] - dopY)*(triangle[3] - dopY));
            //System.out.println("a = " + a);
            double c = Math.sqrt((triangle[0]- triangle[2])*(triangle[0]- triangle[2]) + (triangle[1] - triangle[3])*(triangle[1] - triangle[3]));
            //System.out.println("c = " + c);
            //System.out.println("var 1");
            double angle = Math.toDegrees(Math.asin(a/c));
            //System.out.println(angle);
            return angle;
        } else if(triangle[1] > triangle[3]) {
            dopX = triangle[2]; dopY = triangle[1];
            double a = Math.sqrt((triangle[0] - dopX)*(triangle[0] - dopX) + (triangle[1] - dopY)*(triangle[1] - dopY));
            //System.out.println("a = " + a);
            double c = Math.sqrt((triangle[0] - triangle[2])*(triangle[0] - triangle[2]) + (triangle[1] - triangle[3])*(triangle[1] - triangle[3]));
            //System.out.println("c = " + c);
            //System.out.println("var 2");
            double angle = -Math.toDegrees(Math.asin(a/c));
            //System.out.println(angle);
            return angle;
        } else
            //System.out.println(0.0);

        return 0.0;
    }

    // Описание принципа работы.
    // У образа находим самый близкий и самый дальний пиксель к оси Y.
    // По этим двум точкам находим третью, чтоб получился прямоугольный треугольник.
    // Находим длины противолежащей стороны и гипотенузы.
    // Дальше находим заданный угол отклонения образа.
    // Есть два варианта. Если прямой угол внизу треугольника, то получившийся угол положительный.
    // Если прямой угол вверху треугольника, то получившийся угол отрицательный.
    public static double takeAngle(Integer[][] img) {
        Integer matrixSizeY = img.length;
        Integer matrixSizeX = img[0].length;
        // Координаты (x, y) верхнего левого пикселя и
        // координаты (x, y) верхнего правого пикселя
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
                        if(x < coords[0]) {
                            coords[0] = x; coords[1] = y;
                        }
                        if(x > coords[2]) {
                            coords[2] = x; coords[3] = y;
                        }
                    }
                    break;
                }
            }
        System.out.println(coords[0] + " " + coords[1] + " " + coords[2] + " " + coords[3]);
        int dopX, dopY;
        if(coords[1] < coords[3]) {
            dopX = coords[0]; dopY = coords[3];
            double a = Math.sqrt((coords[2] - dopX)*(coords[2] - dopX) + (coords[3] - dopY)*(coords[3] - dopY));
            System.out.println("a = " + a);
            double c = Math.sqrt((coords[0]- coords[2])*(coords[0]- coords[2]) + (coords[1] - coords[3])*(coords[1] - coords[3]));
            System.out.println("c = " + c);
            System.out.println("var 1");
            double angle = Math.toDegrees(Math.asin(a/c));
            System.out.println(angle);
            return angle;
        } else if(coords[1] > coords[3]) {
            dopX = coords[2]; dopY = coords[1];
            double a = Math.sqrt((coords[0] - dopX)*(coords[0] - dopX) + (coords[1] - dopY)*(coords[1] - dopY));
            System.out.println("a = " + a);
            double c = Math.sqrt((coords[0] - coords[2])*(coords[0] - coords[2]) + (coords[1] - coords[3])*(coords[1] - coords[3]));
            System.out.println("c = " + c);
            System.out.println("var 2");
            double angle = -Math.toDegrees(Math.asin(a/c));
            System.out.println(angle);
            return angle;
        } else
            System.out.println(0.0);

        return 0.0;
    }
    // Метод Хафа для поиска линий
    public static double houghLine(Integer[][] img) {
        Integer matrixSizeY = img.length;
        Integer matrixSizeX = img[0].length;

        Integer[] coordsOld = new Integer[4];
        // Вычисления данных для образа
        for(int y=0; y<matrixSizeY; y++)
            for(int x=0; x<matrixSizeX; x++) {
                if(img[y][x] != 0) {
                    if (coordsOld[0] == null) {
                        // Первое заполнение массивов с координатами
                        coordsOld[0] = x; coordsOld[1] = y;
                        coordsOld[2] = x; coordsOld[3] = y;
                    } else {
                        // Дальнейшее изменение координат образов
                        if(x < coordsOld[0]) coordsOld[0] = x;
                        if(y < coordsOld[1]) coordsOld[1] = y;
                        if(x > coordsOld[2]) coordsOld[2] = x;
                        if(y > coordsOld[3]) coordsOld[3] = y;
                    }
                }
            }
        // Вычисление ширины и высоты старого образа
        int sizeOldX = coordsOld[2]-coordsOld[0]+1;
        int sizeOldY = coordsOld[3]-coordsOld[1]+1;

        // Формула x*cosT + y*sinT = r
        // Перебирать T от 0 до 180 и точки (x, y) образа
        // Вычислять, при какой паре (r, T) будет больше всего точек
        // Эта пара и будет той самой прямой

        int diag = (int)Math.sqrt(sizeOldX*sizeOldX + sizeOldY*sizeOldY);
        int[] arr = new int[180];
        for(int i=0; i<180; i++)
            arr[i] = 0;
        double teta = 0.0;
        Double accuracy = 0.1;
        System.out.println(coordsOld[0] + " " + coordsOld[1] + " " + coordsOld[2] + " " + coordsOld[3]);
        for(int y=coordsOld[1]; y<=coordsOld[3]; y++) {
            for(int x=coordsOld[0]; x<=coordsOld[2]; x++) {
                if(img[y][x] != 0) {
                    for(int f=0; f<180; f++) {
                        for(int r=0; r<diag; r++) {
                            teta = Math.toRadians(f);
                            if((Math.abs((x+1)*Math.cos(teta) + (y+1)*Math.sin(teta)) - r) < accuracy)
                                arr[f]++;
                        }
                    }
                }
            }
        }
        int max = 0;
        int f = 0;
        for(int i=0; i<arr.length; i++){
            if(arr[i] > max) {
                max = arr[i];
                f = i;
            }
        }
        System.out.println(max + ", " + f);
        // Точность
        //Double accuracy = 0.1;
        // Тройка (r, teta, счётчик)
        // Может, не лист, а пары?
        /*int diag = (int)Math.sqrt(sizeOldX*sizeOldX + sizeOldY*sizeOldY);
        int[] arr = new int[diag];
        for(int y=0; y<matrixSizeY; y++)
            for(int x=0; x<matrixSizeX; x++) {
                if (img[y][x] != 0) {
                    for(int f=0; f<180; f+=1) {
                        for(int r=0; r<diag; r++) {
                            double teta = Math.toRadians(f);
                            if((Math.abs((x+1)*Math.cos(teta) + (y+1)*Math.sin(teta)) - r) < accuracy)
                                arr[r]++;
                        }
                    }
                }
            }
        int max = 0;
        double teta = 0.0;
        int R = 0;
        for(int f=0; f<180; f++) {
            for(int r=0; r<diag; r++) {
                if(arr[r]>max) {
                    max = arr[r];
                    teta = f;
                    R = r;
                }
            }
        }
        System.out.println(max + ", " + teta + ", " + R);*/
        return 0.0;
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
        Integer[] coordsOld = new Integer[4];
        // Вычисления данных для образа
        for(int y=0; y<matrixSizeY; y++)
            for(int x=0; x<matrixSizeX; x++) {
                if(img[y][x] != 0) {
                    if (coordsOld[0] == null) {
                        // Первое заполнение массивов с координатами
                        coordsOld[0] = x; coordsOld[1] = y;
                        coordsOld[2] = x; coordsOld[3] = y;
                    } else {
                        // Дальнейшее изменение координат образов
                        if(x < coordsOld[0]) coordsOld[0] = x;
                        if(y < coordsOld[1]) coordsOld[1] = y;
                        if(x > coordsOld[2]) coordsOld[2] = x;
                        if(y > coordsOld[3]) coordsOld[3] = y;
                    }
                }
            }
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
        Double S = Math.sin(radians);
        Double C = Math.cos(radians);

        Double a = matrixSizeX/2*C;
        Double b = matrixSizeY/2*S;
        Double c = matrixSizeY/2*C;
        Double d = matrixSizeX/2*S;

        //Integer outputWidth  = (int)Math.round(Math.max(Math.max(-a+b, a+b), Math.max(-a-b, a-b)) * 2.0);
        //Integer outputHeight = (int)Math.round(Math.max(Math.max(-c+d, c+d), Math.max(-c-d, c-d)) * 2.0);
        Integer outputWidth = matrixSizeX;
        Integer outputHeight = matrixSizeY;
        Integer[][] resultingMatrix = new Integer[outputHeight][outputWidth];
        for(Integer[] row: resultingMatrix)
            Arrays.fill(row, 0);

        S = Math.sin(-radians);
        C = Math.cos(-radians);

        for (int outy=0; outy<outputHeight; outy++) {
            for (int outx=0; outx<outputWidth; outx++) {
                Double cox = outx - outputWidth/2.0+1;
                Double coy = outy - outputHeight/2.0+1;

                Double inx = cox*C-coy*S + matrixSizeX/2;
                Double iny = coy*C+cox*S + matrixSizeY/2;

                Integer basex = inx.intValue();
                Double tx     = inx - basex;
                Integer basey = iny.intValue();
                Double ty     = iny -basey;
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
