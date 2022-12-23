import exception.DivisionByZeroException;
import exception.FileNotFoundOrBusyException;
import exception.OverOneMillionException;

import java.io.*;
import java.nio.file.Path;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// В Run/Debug configurations IntelliJ IDEA в качестве паратметра запуска
// задан файл с размером матрицы /src/res/matrixSize.txt.

// Вероятно, имело смысл с самого начала сделать класс матрицы с методами и
// делать "вращение" установкой свойства (rotation = 270), которое влияло бы
// на метод вывода в файл, но в задании написано поворачивать...

// По условию должны быть исключения деления на ноль - значит матрица целочисленная, но
// для целых чисел эта программа будет выкидывать исключение деления на ноль примерно всегда...
// Чтобы переделать во float, нужно закомменетировать строки 65, 70 и раскомментировать строки 66, 71.

// Написать консольное приложение, которое:
//   a. Считывает из текстового файл размерность матрицы N*N.
//   b. Создаёт и заполняет матрицу случайными числами от -N до N.
//   c. Последовательно поворачивает матрицу на 90, 180 и 270 градусов
//      против часовой стрелки и делит каждый элемент на сумму соседних.
//   d. Каждую из трёх получившихся матриц вывести в общий файл
// Требования к обработке исключительных ситуаций:
//   a. контролировать состояние потоков ввода/вывода (отсутствие
//      записи в файле, недопустимые значения, etc);
//   b. генерировать и обрабатывать исключение при некорректных
//      математических операциях;
//   c. логировать исключение при нехватке памяти;
//   d. реализовать собственные классы исключений для случаев
//      ● деление на 0
//      ● файл не существует/нет доступа к файлу
//      ● N > 1_000_000

public class Main {
//    public static final Logger logger = Logger.getAnonymousLogger();
//    public static final Logger logger = Logger.getLogger("MyLog");
//FileHandler
    public static void main(String[] args) {

        // Только ради логгирования исключения при нехватке памяти (нужно задать размер матрицы 999999)
        Logger logger = Logger.getLogger("MyLog");
        //logger.setUseParentHandlers(false);  // Если не хотим дублирования в консоль
        try {
            //logger.addHandler(new FileHandler("MyLog.log"));  // Можно в одну строку, но формат XML...
            FileHandler fh = new FileHandler("MyLog.log");
            logger.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            throw new FileNotFoundOrBusyException("Ошибка доступа к файлу лога! " + e.getMessage());
        }

        // Чтение/запись и манипуляции с матрицей внутри try чтобы отловить соответствующие исключения
        try (BufferedReader matrixSizeReader = new BufferedReader(new FileReader(args[0]))
        ) { // Пытаемся прочитать первую строку из файла с размером матрицы
            String matrixSizeRaw = matrixSizeReader.readLine();
            // Проверяем, что получили именно число, и число в нужном диапазоне - вынес в функцию
            int matrixSize = validateMatrix(matrixSizeRaw);
            // Создаём матрицу и заполняем случайными целыми числами
            Integer[][] matrix = new Integer[matrixSize][matrixSize];
            //Float[][] matrix = new Float[matrixSize][matrixSize];  // Не будет исключений деления на ноль
            Random random = new Random();
            for (int x = 0; x < matrix.length; x++) {
                for (int y = 0; y < matrix[x].length; y++) {
                    matrix[x][y] = random.nextInt(matrixSize*2+1) - matrixSize;  // От -N до N
                    //matrix[x][y] = random.nextFloat()*2*matrixSize - matrixSize;  // От -N до N
                }
            }
            //printMatrixToFile(matrix, args[0], false);  // В задании написано "Каждую из трёх получившихся матриц
            rotateMatrixCCW(matrix);  // 90               // вывести в общий файл", поэтому исходную матрицу не вывожу
            DivideByNeighboursSum(matrix);
            printMatrixToFile(matrix, args[0], false);  // Перезапись старого содержимого файла
            rotateMatrixCCW(matrix);  // 180
            DivideByNeighboursSum(matrix);
            printMatrixToFile(matrix, args[0]);
            rotateMatrixCCW(matrix);  // 270
            DivideByNeighboursSum(matrix);
            printMatrixToFile(matrix, args[0]);

        } catch (ArrayIndexOutOfBoundsException e){
            throw new RuntimeException("Для запуска нужен аргумент с именем файла! " + e.getMessage());
        } catch (FileNotFoundException e){
            throw new FileNotFoundOrBusyException("Не найден файл с размером матрицы! " + e.getMessage());
        } catch (IOException e) {
            throw new FileNotFoundOrBusyException("Файл занят другой программой! " + e.getMessage());
        } catch (ArithmeticException e) {
            throw new DivisionByZeroException("Деление на ноль! " + e.getMessage());
        } catch (OutOfMemoryError e) {
            logger.warning("Не хватает памяти для обработки! " + e.getMessage());
        }
    }

    public static int validateMatrix(String matrixSize) {
        int result;
        if (matrixSize == null || matrixSize.isBlank()) {
            throw new RuntimeException("Файл с размером матрицы пуст!");
        }
        try {
            result = Integer.parseInt(matrixSize);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Размер матрицы должен быть целым числом! " + e.getMessage());
        }
        if (result <= 0) {
            throw new RuntimeException("Размер матрицы должен быть положительным числом!");
        }
        if (result > 1000000 ) {
            throw new OverOneMillionException("Размер матрицы больше 1'000'000!");
        }
        return result;
    }

    // Вращение квадратной матрицы против часовой стрелки через транспонирование и вертикальное отражение
    private static <T>void rotateMatrixCCW(T[][] matrix) {
        int n = matrix.length;
        // Транспонируем
        for (int i=0; i<n; i++) {
            for(int j=i; j<n; j++) {
                T temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        // Переворачиваем
        for (int i=0; i<n; i++) {  // В каждой колонке
            int j = 0, jj = n-1;
            while(j < jj) {  // Переворачиваем
                T temp = matrix[j][i];
                matrix[j][i] = matrix[jj][i];
                matrix[jj][i] = temp;
                j++;
                jj--;
            }
        }
    }

    // Деление каждого элемента на сумму соседних. Соседними считаю элементы по кругу, а не сверху-снизу и справа-слева
    // Судя по всему, нужно сделать копию матрицы, чтобы не менять соседей в процессе,
    // а очевидная оптимизация - делать копию только трёх строк.
    // Но я сделал полную копию, ведь у нас не соревнование по оптимизации.
    private static void DivideByNeighboursSum(Integer[][] matrix) {
        int n = matrix.length;
        // Глубокая копия исходной матрицы
        Integer[][] c = matrix.clone();
        for (int i = 0; i < n; i++) {
            c[i] = matrix[i].clone();
        }
        // Эту конструкцию можно оптимизировать, если отдельно посчитать ядро без
        // ифов - получится сложно и многословно, но на большой матрице из 10000 float
        // такая оптимизация ускорила процесс в 1,5 раза (см. эту же функцию перегруженную для float в самом конце).
        for (int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                int sum = 0;  // Для матрицы из одной ячейки сумма пустого множества окружающих клеток равна нулю
                if (i!=0 && j!=0) sum += c[i-1][j-1];
                if (i!=0) sum += c[i-1][j];
                if (i!=0 && j<n-1) sum += c[i-1][j+1];
                if (j!=0) sum += c[i][j-1];
                if (j<n-1) sum += c[i][j+1];
                if (i<n-1 && j!=0) sum += c[i+1][j-1];
                if (i<n-1) sum += c[i+1][j];
                if (i<n-1 && j<n-1) sum += c[i+1][j+1];
                matrix[i][j] = c[i][j] / sum;
            }
        }
    }

    // Вывод матрицы в файл
    private static <T>void printMatrixToFile(T[][] matrix, String arg, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(
                // Выводим в ту же папку, где файл с размером матрицы
                new FileWriter(Path.of(arg).getParent().resolve("matrixResult.txt").toFile(), append))
        ) {
            for (T[] row : matrix) {
                for (T col : row) {
                    writer.write("["+col+"] ");
                }
                writer.newLine();
            }
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи в файл: " + e.getMessage());
        }
    }
    // Перегрузка функции чтобы задать значение по умолчанию параметру append
    private static <T>void printMatrixToFile(T[][] matrix, String arg) {
        printMatrixToFile(matrix, arg, true);
    }

    // Перегрузка функции деления на соседей для Float с оптимизацией,
    // но очень многословная и в ней легко было ошибиться
    private static void DivideByNeighboursSum(Float[][] matrix) {
        int n = matrix.length;
        // Глубокая копия исходной матрицы
        Float[][] c = matrix.clone();
        for (int i = 0; i < n; i++) {
            c[i] = matrix[i].clone();
        }

        //long timer = System.nanoTime();

        // Сначала посчитаем ядро матрицы без краевых случаев - очевидно не исполнится при n < 3
        for (int i=1; i<n-1; i++) {
            for(int j=1; j<n-1; j++) {
                matrix[i][j] = c[i][j] /
                        ( c[i-1][j-1] + c[i-1][j] + c[i-1][j+1]
                                + c[i][j-1] + c[i][j+1]
                                + c[i+1][j-1] + c[i+1][j] + c[i+1][j+1] );
            }
        }
        // Верхняя строка без углов - очевидно не исполнится при n < 3
        for (int j=1; j<n-1; j++) {
            int i = 0;
            matrix[i][j] = c[i][j] /
                    ( c[i][j-1] + c[i][j+1]
                            + c[i+1][j-1] + c[i+1][j] + c[i+1][j+1] );
        }
        // Нижняя строка без углов - очевидно не исполнится при n < 3
        for (int j=1; j<n-1; j++) {
            int i = n-1;
            matrix[i][j] = c[i][j] /
                    ( c[i-1][j-1] + c[i-1][j] + c[i-1][j+1]
                            + c[i][j-1] + c[i][j+1] );
        }
        // Левый столбец без углов - очевидно не исполнится при n < 3
        for (int i=1; i<n-1; i++) {
            int j = 0;
            matrix[i][j] = c[i][j] /
                    ( c[i-1][j] + c[i-1][j+1] + c[i][j+1] + c[i+1][j+1] + c[i+1][j] );
        }
        // Правый столбец без углов - очевидно не исполнится при n < 3
        for (int i=1; i<n-1; i++) {
            int j = n-1;
            matrix[i][j] = c[i][j] /
                    ( c[i-1][j] + c[i-1][j-1] + c[i][j-1] + c[i+1][j-1] + c[i+1][j] );
        }
        // Углы:
        if (n > 1) {
            int i = 0, j = 0;
            matrix[i][j] = c[i][j] / ( c[i][j+1] + c[i+1][j] + c[i+1][j+1] );
            i = 0; j = n-1;
            matrix[i][j] = c[i][j] / ( c[i][j-1] + c[i+1][j-1] + c[i+1][j] );
            i = n-1; j = 0;
            matrix[i][j] = c[i][j] / ( c[i-1][j] + c[i-1][j+1] + c[i][j+1] );
            i = n-1; j = n-1;
            matrix[i][j] = c[i][j] / ( c[i-1][j-1] + c[i-1][j] + c[i][j-1] );
        }
        // Если матрица из одной ячейки, делить на 0, или null? :)
        if (n == 1) matrix[0][0] = c[0][0] / 0;

        //System.out.println(System.nanoTime() - timer);
    }

}
