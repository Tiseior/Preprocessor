import java.io.IOException;
import java.util.Arrays;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Processing {

    public static Images images = new Images();

    // ������ � ������� � ���� �����������
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
        System.out.println("���������� ��������: " + iteration);*/
        //img = rotation(img);
        //img = rot(img, 90.0);
        convertArrayToImageAndWrite(img, "Result.png");
    }

    // ������ � ������� � ���� ���������� �������
    public static void preprocImg(Integer[][] imgName) {
        printImg(imgName);
        printImg(imgName);
        imgName = scalingNew(imgName);
        imgName = translation(imgName);
        //imgName = fillGaps(imgName);
        printImg(imgName);
        //imgName = rotation(imgName);
        //rot(imgName, 90.0);
        //printImg(imgName);
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
        Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
        for(Integer[] row: resultingMatrix)
            Arrays.fill(row, 0);

        Integer pixelCount = pixelCount(img);

        Integer shiftX = 0; // ����� �� ��� X
        Integer shiftY = 0; // ����� �� ��� Y
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

    // �������� ����� ��� ������� ��������� ���������������
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
        // ���������� ������ ��� ������
        for(int y=0; y<matrixSizeY; y++)
            for(int x=0; x<matrixSizeX; x++) {
                if(img[y][x] != 0) {
                    if (coordsOld[0] == null) {
                        // ������ ���������� �������� � ������������
                        coordsOld[0] = x; coordsOld[1] = y;
                        coordsOld[2] = x; coordsOld[3] = y;
                    } else {
                        // ���������� ��������� ��������� �������
                        if(x < coordsOld[0]) coordsOld[0] = x;
                        if(y < coordsOld[1]) coordsOld[1] = y;
                        if(x > coordsOld[2]) coordsOld[2] = x;
                        if(y > coordsOld[3]) coordsOld[3] = y;
                    }
                }
            }
        // ���������� ������ � ������ ������� ������
        int sizeOldX = coordsOld[2]-coordsOld[0]+1;
        int sizeOldY = coordsOld[3]-coordsOld[1]+1;
        System.out.println("img: " + matrixSizeX + " x " + matrixSizeY);
        System.out.println("sizeOld: " + sizeOldX + " x " + sizeOldY);
        Integer imgPixels = sizeOldX*sizeOldY;
        System.out.println("imgPixels: " + imgPixels);
        // ���������� �������� ���������� � ���������������� ������������ ������
        //avgDistance = Math.sqrt((coordsOld[0]-coordsOld[2])*(coordsOld[0]-coordsOld[2]) +
        //                        (coordsOld[1]-coordsOld[3])*(coordsOld[1]-coordsOld[3]));
        int coordX, coordY;
        Double avgDistance = 0.0; // ������� ����������
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
        scalingCoef = (coefX<coefY) ? coefX : coefY;
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

    // ����������� �������� ���������������
    public static Integer[][] scalingNew(Integer[][] img) {
        Integer matrixSizeY = img.length;
        Integer matrixSizeX = img[0].length;
        Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
        for(Integer[] row: resultingMatrix)
            Arrays.fill(row, 0);
        Integer[] coordsOld = new Integer[4];
        // ���������� ������ ��� ������
        for(int y=0; y<matrixSizeY; y++)
            for(int x=0; x<matrixSizeX; x++) {
                if(img[y][x] != 0) {
                    if (coordsOld[0] == null) {
                        // ������ ���������� �������� � ������������
                        coordsOld[0] = x; coordsOld[1] = y;
                        coordsOld[2] = x; coordsOld[3] = y;
                    } else {
                        // ���������� ��������� ��������� �������
                        if(x < coordsOld[0]) coordsOld[0] = x;
                        if(y < coordsOld[1]) coordsOld[1] = y;
                        if(x > coordsOld[2]) coordsOld[2] = x;
                        if(y > coordsOld[3]) coordsOld[3] = y;
                    }
                }
            }
        // ���������� ������ � ������ ������� ������
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

    // ��������������� ������
    public static Integer[][] scaling(Integer[][] img) {
        Integer matrixSizeY = img.length;
        Integer matrixSizeX = img[0].length;
        Integer[][] resultingMatrix = new Integer[matrixSizeY][matrixSizeX];
        for(Integer[] row: resultingMatrix)
            Arrays.fill(row, 0);

        System.out.println("allPixels: " + matrixSizeX*matrixSizeY);

        Integer pixelCount = pixelCount(img);
        System.out.println("pixelCount: " + pixelCount);

        Double avgDistance = 0.0; // ������� ����������

        for(int y=0; y<matrixSizeY; y++)
            for(int x=0; x<matrixSizeX; x++)
                if(img[y][x] != 0)
                    avgDistance = avgDistance + img[y][x]*Math.sqrt((y+1)*(y+1) + (x+1)*(x+1));
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
        if(matrixSizeX*matrixSizeY>4*pixelCount/*avgDistance>pixelCount*/)
            scalingCoef = frameSizePart/avgDistance;
        else if(matrixSizeX*matrixSizeY<4*pixelCount/*avgDistance<pixelCount*/)
            scalingCoef = avgDistance/frameSizePart;
        System.out.println("scalingCoef: " + scalingCoef);

        // ���������� ������ �� ���������������: x min, y min, x max, y max
        Integer[] coordsOld = new Integer[4];
        // ���������� ������ ����� ���������������: x min, y min, x max, y max
        Integer[] coordsNew = new Integer[4];
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
                        if(matrixSizeX*matrixSizeY>4*pixelCount/*avgDistance>pixelCount*/) {
                            xm = Math.abs((int) Math.round(matrixSizeX/2.0 - x*scalingCoef));
                            ym = Math.abs((int) Math.round(matrixSizeY/2.0 - y*scalingCoef));
                        }
                        else if(matrixSizeX*matrixSizeY<4*pixelCount/*avgDistance<pixelCount*/) {
                            xm = Math.abs((int) Math.round(x*scalingCoef));
                            ym = Math.abs((int) Math.round(y*scalingCoef));
                        }

                        if (coordsOld[0] == null) {
                            // ������ ���������� �������� � ������������
                            coordsOld[0] = x; coordsOld[1] = y;
                            coordsOld[2] = x; coordsOld[3] = y;
                            coordsNew[0] = xm; coordsNew[1] = ym;
                            coordsNew[2] = xm; coordsNew[3] = ym;
                        } else {
                            // ���������� ��������� ��������� �������
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
            System.out.println("������ ����� ������ ��������������");
            resultingMatrix = img;
        }
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

        return img;
    }

    // ������������ ������� ���������� ������
    // �� ���� ������ ����� � ��� ����������, ����� ����� � ��� ����������
    // �� ������ ���������� ����� �����
    // coordsOld � coordsNew ����� ��������� ���������: x min, y min, x max, y max
    public static Integer[][] nearestNeighbourInterpolation (Integer[][] imgOld, Integer[] coordsOld,
                                                             Integer[][] imgNew, Integer[] coordsNew) {
        // ���������� ������ � ������ ������� ������
        int sizeOldX = coordsOld[2]-coordsOld[0]+1;
        int sizeOldY = coordsOld[3]-coordsOld[1]+1;
        // ���������� ������ � ������ ������ ������
        int sizeNewX = coordsNew[2]-coordsNew[0]+1;
        int sizeNewY = coordsNew[3]-coordsNew[1]+1;
        // ���������� ������������� ��������������� ������ �� ����
        float coefX = (float)(sizeOldX)/(sizeNewX);
        float coefY = (float)(sizeOldY)/(sizeNewY);
        //for(Integer[] row: imgNew)
        //    Arrays.fill(row, 0);
        int oldX, oldY;
        for(int y=coordsNew[1]; y<=coordsNew[3]; y++)
            for(int x=coordsNew[0]; x<=coordsNew[2]; x++)
                /*if(imgNew[y][x] == 0)*/ {
                    // ���������� ��������� ������� ������ �� ���������������
                    // ����������� ������ ������
                    oldX = (int)Math.floor((x - coordsNew[0]) * coefX);
                    oldY = (int)Math.floor((y - coordsNew[1]) * coefY);
                    // ���������� �������� ������ ������, �����������
                    // � ����������� �����������
                    imgNew[y][x] = imgOld[oldY + coordsOld[1]][oldX + coordsOld[0]];
                }

        return imgNew;
    }

    // ������� ������
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
        for(int y=0; y<matrixSizeY; y++) {
            for(int x=0; x<matrixSizeX; x++) {
                if(img[y][x] != 0) {
                    txx += (img[y][x]*(x+1)*(x+1));
                    tyy += (img[y][x]*(y+1)*(y+1));
                    txy += (img[y][x]*(x+1)*(y+1));
                }
            }
        }
        System.out.println("Txx = " + txx);
        System.out.println("Tyy = " + tyy);
        System.out.println("Txy = " + txy);
        //Double bigM = Math.sqrt(8*txy*txy + 2*Math.pow(tyy-txx, 2) + 2*(tyy-txx) *
        //                         Math.sqrt(Math.pow(tyy-txx, 2)) + 4*txy*txy);
        Double bigM = Math.sqrt(2 * (Math.sqrt(Math.pow(tyy-txx, 2) + 4*txy*txy) + (tyy-txx)) *
                                Math.sqrt(Math.pow(tyy-txx, 2) + 4*txy*txy));
        System.out.println("M = " + bigM);
        //Double sinTeta = (tyy - txx + Math.sqrt(Math.pow(tyy-txx, 2)) + 4*txy*txy)/bigM;
        //Double cosTeta = (2*tyy)/bigM;
        Double sinTeta = ((tyy-txx) + Math.sqrt(Math.pow(tyy-txx, 2) + 4*txy*txy))/bigM;
        Double cosTeta = (2*txy)/bigM;
        System.out.println("sin = " + sinTeta);
        System.out.println("cos = " + cosTeta);
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

    // ������� ������� �� �������� ����
    public static Integer[][] rot(Integer[][] img, Double angle) {
        Integer matrixSizeY = img.length;    // ������
        Integer matrixSizeX = img[0].length; // ������

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

    // ������� ��� ���������� �������� �������
    public static Integer sampleAt(Integer[][] img, int x, int y) {
        Integer matrixSizeY = img.length;    // ������
        Integer matrixSizeX = img[0].length; // ������
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
