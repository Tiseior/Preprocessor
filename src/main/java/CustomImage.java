public class CustomImage {

    public Integer[][] matrix;
    public int leftBorder  = -1;
    public int rightBorder = -1;
    public int upperBorder = -1;
    public int lowerBorder = -1;

    CustomImage(Integer[][] matrix) {
        this.matrix = matrix;
        calculateBorderCoordinates(matrix);
    }

    // Метод для получения координат образа на изображении.
    // На выходе массив с координатами (x, y) левого верхнего пикселя образа и
    // координатами (x, y) правого нижнего пикселя образа
    public void calculateBorderCoordinates(Integer[][] matrix) {
        int matrixSizeY = matrix.length;
        int matrixSizeX = matrix[0].length;
        for(int y=0; y<matrixSizeY; y++)
            for(int x=0; x<matrixSizeX; x++) {
                if(matrix[y][x] != 0) {
                    if (leftBorder == -1) {
                        // Первое заполнение массивов с координатами
                        leftBorder = x; lowerBorder = y;
                        rightBorder = x; upperBorder = y;
                    } else {
                        // Дальнейшее изменение координат образов
                        if(x < leftBorder) leftBorder = x;
                        if(y < lowerBorder) lowerBorder = y;
                        if(x > rightBorder) rightBorder = x;
                        if(y > upperBorder) upperBorder = y;
                    }
                }
            }
    }

    // Количество единичных пикселей
    public Integer getSignificantPixelCount() {
        int pixelCount = 0; // Количество не нулевых пикселей
        for(Integer[] row : this.matrix)
            for(Integer elem : row)
                if(elem != 0)
                    pixelCount += 1;

        return pixelCount;
    }

    // Вывод образа
    public void printImg() {
        for (Integer[] row: this.matrix) {
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

}
