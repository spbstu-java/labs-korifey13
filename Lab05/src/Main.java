import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//С использованием только Stream API реализовать следующие методы:
//   ● метод, возвращающий среднее значение списка целых чисел;
//   ● метод, приводящий все строки в списке в верхний регистр и добавляющий к ним префикс «_new_»;
//   ● метод, возвращающий список квадратов всех встречающихся только один раз элементов списка;
//   ● метод, принимающий на вход коллекцию строк и возвращающий все строки, начинающиеся с заданной буквы, отсортированные по алфавиту;
//   ● метод, принимающий на вход коллекцию и возвращающий её последний элемент или кидающий исключение, если коллекция пуста;
//   ● метод, принимающий на вход массив целых чисел, возвращающий сумму чётных чисел или 0, если чётных чисел нет;
//   ● метод, преобразовывающий все строки в списке в Map, где первый символ – ключ, оставшиеся – значение;

public class Main {

    // ● метод, возвращающий среднее значение списка целых чисел
    static double listAvg(List<Integer> lst) {
        return lst.stream().mapToInt((x) -> x).summaryStatistics().getAverage();
    }

    // ● метод, приводящий все строки в списке в верхний регистр и добавляющий к ним префикс «_new_»;
    static List<String> upperNew(List<String> lst) {
        return lst.stream().map(o -> "_new_".concat(o.toUpperCase())).collect(Collectors.toList());
    }
    // ● Ещё метод, приводящий уже употок строк в верхний регистр и добавляющий к ним префикс «_new_»;
    static List<String> upperNewStream(Stream<String> strm) {
        return strm.map(o -> "_new_".concat(o.toUpperCase())).collect(Collectors.toList());
    }

    // ● метод, возвращающий список квадратов всех встречающихся только один раз элементов списка;
    static List<Integer> lonelySqrt(List<Integer> lst) {
        return lst.stream().distinct().map(o -> o * o).collect(Collectors.toList());
    }

    // ● метод, принимающий на вход коллекцию строк и возвращающий все строки, начинающиеся с заданной буквы, отсортированные по алфавиту;
    static List<String> monoSort(String f, List<String> lst) {
        return lst.stream().filter(o -> o.toLowerCase().startsWith(f)).sorted().collect(Collectors.toList());
    }

    // ● метод, принимающий на вход коллекцию и возвращающий её последний элемент или кидающий исключение, если коллекция пуста;
    static String lastException(List<String> lst) {
        return lst.stream().reduce((a, b) -> b).get();
    }

    // ● метод, принимающий на вход массив целых чисел, возвращающий сумму чётных чисел или 0, если чётных чисел нет;
    static Integer even0(List<Integer> lst) {
        return lst.stream().filter(o -> o % 2 == 0).mapToInt(Integer::intValue).sum();
    }

    // ● метод, преобразовывающий все строки в списке в Map, где первый символ – ключ, оставшиеся – значение;
    static Map<String, String> map1(List<String> lst) {
        return lst.stream().collect(Collectors.toMap(o -> o.substring(0, 1), o -> o.substring(1), (a1, a2) -> a1));
    }

    public static void main(String[] args) {
        // Среднее значение списка целых чисел
        System.out.println(
                Stream.of(2, 6, 8, 18).mapToInt((x) -> x).summaryStatistics().getAverage()
        );
        // Используем метод для списка
        List<Integer> lst = Arrays.asList(2, 6, 8, 18);
        System.out.println(listAvg(lst));
        // Используем метод для массива
        Integer[] arr = {2, 6, 8, 18};
        System.out.println(listAvg(Arrays.asList(arr)) + "\n\r");

        // Верхний регистр и префикс «_new_»
        List<String> collect = Stream.of("раз", "дЖва", "три")
                .map(o -> "_new_".concat(o.toUpperCase()))
                .collect(Collectors.toList());
        System.out.println(collect);
        // Используем метод для списка
        List<String> string_lst = Stream.of("раз", "дЖва", "три").collect(Collectors.toList());
        System.out.println(upperNew(string_lst));
        // Используем метод для cтрима
        System.out.println(upperNewStream(Stream.of("раз", "дЖва", "три")) + "\n\r");

        // Список квадратов всех встречающихся только один раз элементов
        List<Integer> square = Stream.of(2, 6, 3, 8, 18, 7, 2).distinct().map(o -> o * o)
                .collect(Collectors.toList());
        System.out.println(square);
        // То же самое через метод
        System.out.println(lonelySqrt(Arrays.asList(2, 6, 3, 8, 18, 7, 2)) + "\n\r");

        // Все строки начинающиеся с заданной буквы, отсортированные по алфавиту
        List<String> collect2 = Stream.of("раз", "Два", "три", "дрозд", "дятел", "дмитрий")
                .filter(o -> o.toLowerCase().startsWith("д"))
                .sorted().collect(Collectors.toList());
        System.out.println(collect2);
        // То же самое через метод
        System.out.println(monoSort("д", Arrays.asList("раз", "Два", "три", "дрозд", "дятел", "дмитрий")) + "\n\r");

        // Последний элемент или кидающий исключение...
        System.out.println(Stream.of("раз", "два", "три", "дрозд", "дятел", "Дмитрий").reduce((a, b) -> b).get());
        // ...если коллекция пуста
        try {
            System.out.println(Stream.of().reduce((a, b) -> b).get());
        } catch (Exception e) {
            System.out.println(e);
        }
        // То же самое через метод
        System.out.println(lastException(Arrays.asList("раз", "два", "три", "дрозд", "дятел", "Дмитрий")));
        try {
            System.out.println(lastException(List.of()) + "\n\r");
        } catch (Exception e) {
            System.out.println(e + "\n\r");
        }

        // Сумма чётных чисел...
        System.out.println(Stream.of(2, 6, 3, 8, 18, 7).filter(o -> o % 2 == 0).mapToInt(Integer::intValue).sum());
        // ...или 0 если чётных чисел нет
        System.out.println(Stream.of(5, 1, 3, 7).filter(o -> o % 2 == 0).mapToInt(Integer::intValue).sum());
        // То же самое через метод
        System.out.println(even0(List.of(2, 6, 3, 8, 18, 7)));
        System.out.println(even0(List.of(5, 1, 3, 7)) + "\n\r");

        // Все строки в списке в Map (из нескольких строк с одинаковыми первыми буквами останется одна)
        // Выглядит так, будто добавление в словарь идёт задом наперёд...
        System.out.println(Stream.of("раз", "два", "три", "Дрозд", "дятел", "дмитрий")
                .collect(Collectors.toMap(o -> o.substring(0, 1), o -> o.substring(1), (a1, a2) -> a1)));
        // То же самое через метод
        System.out.println(map1(List.of("раз", "два", "три", "Дрозд", "дятел", "дмитрий")));
    }
}
