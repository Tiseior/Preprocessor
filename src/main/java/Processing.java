import java.io.IOException;
import java.util.Arrays;

public class Processing {

    public static Images images = new Images();

    public static void main(String[] args) throws IOException {
        Integer[][][] tests = {images.test4_1, images.test4_2, images.test4_3,
                                images.test4_4, images.test4_5, images.test4_6,
                                images.test4_7, images.test4_8};

        /*for(Integer[][] test: tests) {
            printImg(test);
            translation(test);
            System.out.println();
            //System.in.read();
        }*/

        // Ошибка в центрировании 9_2
        // Образы для увеличения: 2_1, 8_1, 9_1
        // Образы для уменьшения: 2_3, 8_4, 9_2
        Integer[][] arr = images.test2_1;
        printImg(arr);
        arr = translation(arr);
        arr = scaling(arr);
        arr = fillGaps(arr);
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

        Integer pixelCount = pixelCount(img);

        Integer shiftX = 0; // Сдвиг по оси X
        Integer shiftY = 0; // Сдвиг по оси Y
        for(int i=0; i<matrixSizeY; i++)
            for(int j=0; j<matrixSizeX; j++) {
                shiftX = shiftX + (j+1)*img[i][j];
                shiftY = shiftY + (i+1)*img[i][j];
            }

        shiftX = Math.round((float)(matrixSizeX/2.0) - (float)(shiftX/pixelCount));
        shiftY = Math.round((float)(matrixSizeY/2.0) - (float)(shiftY/pixelCount));
        System.out.println(shiftX);
        System.out.println(shiftY);
        /*if(shiftX<=0)
            shiftX+=1;
        if(shiftY<=0)
            shiftY+=1;*/

        Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
        for(Integer[] row: resultingMatrix)
            Arrays.fill(row, 0);

        for(int i=0; i<matrixSizeY; i++)
            for(int j=0; j<matrixSizeX; j++)
                if(img[i][j] != 0)
                    resultingMatrix[i+shiftY][j+shiftX] = 1;

        printImg(resultingMatrix);

        return resultingMatrix;
    }

    // Масштабирование образа
    public static Integer[][] scaling(Integer[][] img) {
        Integer matrixSizeY = img.length;
        Integer matrixSizeX = img[0].length;
        System.out.println("allPixels: " + matrixSizeX*matrixSizeY);

        Integer pixelCount = pixelCount(img);
        System.out.println("pixelCount: " + pixelCount);

        Double avgDistance = 0.0; // Среднее расстояние

        for(int i=0; i<matrixSizeY; i++)
            for(int j=0; j<matrixSizeX; j++)
                if(img[i][j] != 0)
                    avgDistance = avgDistance + img[i][j]*Math.sqrt((i+1)*(i+1) + (j+1)*(j+1));
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
        if(avgDistance>pixelCount)
            scalingCoef = frameSizePart/avgDistance;
        else if(avgDistance<pixelCount)
            scalingCoef = avgDistance/frameSizePart;
        System.out.println("scalingCoef: " + scalingCoef);

        Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
        for(Integer[] row: resultingMatrix)
            Arrays.fill(row, 0);
        // Что-то не то, что-то не так
//        for(int y=0; y<matrixSizeY; y++)
//            for(int x=0; x<matrixSizeX; x++) {
//                if(img[y][x] != 0) {
//                    int xm = x + 1;
//                    int ym = matrixSizeY - y;
//                    System.out.println(xm + " " + ym + " = ...");
//                    if (xm >= matrixSizeX / 2 && ym >= matrixSizeY / 2) {
//                        ym = /*matrixSizeY - */(int) Math.round(ym * scalingCoef);
//                        xm = (int) Math.round(xm * scalingCoef);// - 1;
//                    }
//                    else if (xm < matrixSizeX / 2 && ym > matrixSizeY / 2) {
//                        ym = /*matrixSizeY - */(int) Math.round(ym * scalingCoef);
//                        xm = (int) Math.round(xm / scalingCoef);// - 1;
//                    }
//                    else if (xm <= matrixSizeX / 2 && ym <= matrixSizeY / 2) {
//                        ym = /*matrixSizeY - */(int) Math.round(ym / scalingCoef);
//                        xm = (int) Math.round(xm / scalingCoef);// - 1;
//                    }
//                    else if (x > matrixSizeX / 2 && y < matrixSizeY / 2) {
//                        ym = /*matrixSizeY - */(int) Math.round(ym / scalingCoef);
//                        xm = (int) Math.round(xm * scalingCoef);// - 1;
//                    }
//                }
//            }
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
                        if(avgDistance>pixelCount) {
                            xm = Math.abs((int) Math.round(matrixSizeX/2.0 - x*scalingCoef));
                            ym = Math.abs((int) Math.round(matrixSizeY/2.0 - y*scalingCoef));
                        }
                        else if(avgDistance<pixelCount) {
                            xm = Math.abs((int) Math.round(x*scalingCoef));
                            ym = Math.abs((int) Math.round(y*scalingCoef));
                        }
                        resultingMatrix[ym][xm] = 1;
                    }
                }
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            System.out.println("Данный образ нельзя масштабировать");
            resultingMatrix = img;
        }

//        for(int i=0; i<matrixSizeY; i++)
//            for(int j=0; j<matrixSizeX; j++)
//                if(img[i][j] != 0)
//                    resultingMatrix[(int) ((int) Math.round(scalingCoef*i)-(Math.round(scalingCoef)))]
//                                   [(int) ((int) Math.round(scalingCoef*j)-(Math.round(scalingCoef)))] = 1;

        printImg(resultingMatrix);

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
        System.out.println();
        printImg(img);
        return img;
    }
}
