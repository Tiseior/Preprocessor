import java.io.IOException;
import java.util.Arrays;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

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

        // ������ � ������������� 9_2
        // ������ ��� ����������: 2_1, 8_1, 9_1
        // ������ ��� ����������: 2_3, 8_4, 9_2
        //preprocImg(images.test2_1);

        preprocImg("\\A\\A1.png");
        //preprocImg("\\Result.png");
    }

    // ������ � ������� � ���� �����������
    public static void preprocImg(String imgName) throws IOException {
        File file = new File(images.path+imgName);
        Integer[][] img = readAndConvertImageToArray(file);
        img = translation(img);
        img = scaling(img);
        img = fillGaps(img);
        convertArrayToImageAndWrite(img, "Result.png");
    }

    // ������ � ������� � ���� ���������� �������
    public static void preprocImg(Integer[][] imgName) {
        printImg(imgName);
        imgName = translation(imgName);
        imgName = scaling(imgName);
        imgName = fillGaps(imgName);
    }

    // ������ ����������� �� �����
    // �������������� ����������� � ��������� ������������� ������ �� ���������� ���������:
    // 0 - ����� �������, 1 - ������ ������� (���������� -1 - �����, 0 - ������)
    // ���������� ��������������� ��������� ������
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
            System.out.println("����������� �� �������");
            Integer[][] imgData = new Integer[0][0];
            return imgData;
        }
    }

    // �������������� ���������� �������������� ������� � ����������� � ����������� .png
    // �� ���������� ��������: -1 - ����� �������, 0 - ������ �������
    // ������ ����������� ����������� � ���������� � ������������� � �������� ������
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

    // ����� ������
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

    // ���������� ��������� ��������
    public static Integer pixelCount(Integer[][] img) {
        Integer pixelCount = 0; // ���������� ��������� ��������
        for(Integer[] row : img)
            for(Integer elem : row)
                pixelCount += elem;
        return pixelCount;
    }

    // ������������� ������
    public static Integer[][] translation(Integer[][] img) {
        Integer matrixSizeY = img.length;
        Integer matrixSizeX = img[0].length;

        Integer pixelCount = pixelCount(img);

        Integer shiftX = 0; // ����� �� ��� X
        Integer shiftY = 0; // ����� �� ��� Y
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

    // ��������������� ������
    public static Integer[][] scaling(Integer[][] img) {
        Integer matrixSizeY = img.length;
        Integer matrixSizeX = img[0].length;
        System.out.println("allPixels: " + matrixSizeX*matrixSizeY);

        Integer pixelCount = pixelCount(img);
        System.out.println("pixelCount: " + pixelCount);

        Double avgDistance = 0.0; // ������� ����������

        for(int i=0; i<matrixSizeY; i++)
            for(int j=0; j<matrixSizeX; j++)
                if(img[i][j] != 0)
                    avgDistance = avgDistance + img[i][j]*Math.sqrt((i+1)*(i+1) + (j+1)*(j+1));
        avgDistance /= pixelCount;

        System.out.println("avg: " + avgDistance);

        // ���������� ������� ����� ������
        //Double frameSizePart = Math.sqrt(matrixSizeX*matrixSizeX+matrixSizeY*matrixSizeY+pixelCount*pixelCount)/4.0;
        Double frameSizePart = Math.sqrt(matrixSizeX*matrixSizeX+matrixSizeY*matrixSizeY);
        // ��� ���������� ������
        //Double scalingCoef = frameSizePart/avgDistance;
        // ��� ���������� ������
        //Double scalingCoef = avgDistance/frameSizePart;
        // !!! ������� �� ������ ������������� �� ���������� ��� ����������
        Double scalingCoef = 0.0;
        if(avgDistance>pixelCount)
            scalingCoef = frameSizePart/avgDistance;
        else if(avgDistance<pixelCount)
            scalingCoef = avgDistance/frameSizePart;
        System.out.println("scalingCoef: " + scalingCoef);

        Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
        for(Integer[] row: resultingMatrix)
            Arrays.fill(row, 0);
        // ��������� ������� ������������� ��������������� ������
        try {
            for(int y=0; y<matrixSizeY; y++)
                for(int x=0; x<matrixSizeX; x++) {
                    if(img[y][x] != 0) {
                        // ��� ���������� ������
                        //int xm = Math.abs((int) Math.round(matrixSizeX/2.0 - x*scalingCoef));
                        //int ym = Math.abs((int) Math.round(matrixSizeY/2.0 - y*scalingCoef));
                        // ��� ���������� ������
                        //int xm = Math.abs((int) Math.round(x*scalingCoef));
                        //int ym = Math.abs((int) Math.round(y*scalingCoef));
                        // !!! ������� �� ������ ������������� �� ���������� ��� ����������
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
            System.out.println("������ ����� ������ ��������������");
            resultingMatrix = img;
        }

        printImg(resultingMatrix);

        return resultingMatrix;
    }

    // ���������� ��������� � �������
    // ��� ��������. � ������ ���� �� ��� �������� ���������, ���� ������� ����� 1, �� � ������� ����� 1.
    // ����� ��� ��������, �� �� ��������.
    // 101 = 111
    public static Integer[][] fillGaps(Integer[][] img) {
        Integer matrixSizeY = img.length;
        Integer matrixSizeX = img[0].length;
        // �������� �������� �� ������ ������
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
        // �������� �������� �� ������� �������
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
