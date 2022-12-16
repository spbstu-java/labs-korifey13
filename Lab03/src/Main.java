import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// В Run/Debug configurations IntelliJ IDEA в качестве паратметров запуска заданы
// файл словаря /src/res/dict.txt, входной файл /src/res/input.txt
// и уровень логгирования 2 (по умолчанию 0 - выводится только перевод).
// Перевожу и вывожу построчно чтобы не держать потенциально большой текст в памяти.

//  Реализовать программу-переводчик.
//        • При запуске программы выполняется чтение словаря в
//        следующем формате:
//            • слово или выражение | перевод
//        • Затем читается входной файл и выполняется перевод
//        • Перевод осуществляется по следующим правилам:
//            • регистр букв игнорируется
//            • если искомого слова нет в словаре – выводится без перевода
//            • если есть несколько подходящих вариантов, выбирается вариант максимально длинной левой части
//        • Результат перевода выводится в консоль

public class Main {
    public static void main(String[] args) {
        int debug = (args.length > 2) ? Integer.parseInt(args[2]) : 0;
        //debug = 10;
        try (
                BufferedReader readerSentence = new BufferedReader(new FileReader(args[1]));
                BufferedReader readerDictionary = new BufferedReader(new FileReader(args[0]))
        ) {
            // Заполнение словаря, пока есть строки
            Map<String, String> dictionary = new HashMap<>();
            String lineDictionary;
            while ((lineDictionary = readerDictionary.readLine()) != null) {  // Читаем посточно
                String[] lineSplit = lineDictionary.split("\\|");       // разбиваем по |
                dictionary.put(lineSplit[0].toLowerCase().trim(), lineSplit[1].trim());  // добавляем в словарь
            }
            // Вывод словаря
            if (debug>=1) {
                System.out.println(" ============== Словарь: ==============");
                dictionary.forEach((key, value) -> System.out.println(" " + key + " --> " + value));
                System.out.println(" ======================================\n\r");
            }

            // Чтение входной последовательности, пока есть строки, и перевод
            String original;
            StringBuilder translation = new StringBuilder();
            while ((original = readerSentence.readLine()) != null) {
                if (debug>=2) System.out.println(original);
                //Сокращаем пробельные символы в строке и разбиваем по пробелам
                String[] origSplit = original.trim().replaceAll("\\s+", " ").trim().split(" ");
                int buffer_head = 0;
                boolean found = false;
                while (buffer_head < origSplit.length) {
                    // В этом цикле обрезаем строку слева, а во вложенном ищем слова и обрезаем справа
                    int buffer_tail = origSplit.length;
                    while (buffer_head < buffer_tail) {
                        // Сборка из непереведённого остатка строки - буфер постепенно уменьшается справа
                        String buffer = String.join(" ", Arrays.copyOfRange(origSplit, buffer_head, buffer_tail));
                        // Ищем в буфере первую букву и последнюю букву (последнюю цифру)
                        // таким образом отрезаем скобки, кавычки и знаки препинания
                        int first_letter;
                        int after_last_letter;
                        for (first_letter = 0; first_letter < buffer.length(); first_letter++)
                            if (Character.isLetter(buffer.charAt(first_letter)))
                                break;
                        for (after_last_letter = buffer.length(); first_letter < after_last_letter; after_last_letter--)
                            if (Character.isLetter(buffer.charAt(after_last_letter-1)) ||
                                    Character.isDigit(buffer.charAt(after_last_letter-1)))
                                break;
                        String prefix = buffer.substring(0, first_letter);
                        String word = buffer.substring(first_letter, after_last_letter);
                        String postfix = buffer.substring(after_last_letter);
                        String word_translation;
                        if (debug>=4) System.out.println("    " + buffer + " {" + prefix + "|" + word + "|" + postfix + "}");
                        // Находим варианты перевода
                        if ((word_translation = dictionary.get(word.toLowerCase())) != null) {
                            // Если есть совпадение, переносим слово в перевод
                            // Предварительно делаем регистр первой буквы таким же, как в исходном тексте
                            if (Character.isUpperCase(word.charAt(0)))
                                translation.
                                        append(prefix).
                                        append(word_translation.substring(0,1).toUpperCase()).
                                        append(word_translation.substring(1)).
                                        append(postfix).append(" ");
                            else
                                translation.
                                        append(prefix).
                                        append(word_translation.substring(0,1).toLowerCase()).
                                        append(word_translation.substring(1)).
                                        append(postfix).append(" ");
                            buffer_head = buffer_tail;
                            found = true;
                            if (debug>=3) System.out.println("  " + word + " > " + word_translation);
                            break;
                        } else {
                            buffer_tail--;
                        }
                    }
                    if (!found) {
                        translation.append(origSplit[buffer_head]).append(" ");
                        buffer_head++;
                    } else
                        found = false;
                }

                translation.setLength(translation.length() - 1);  // Убираем лишний пробел в конце
                // Выводим перевод очередной строки
                System.out.println(translation);
                if (debug>=2) System.out.println();
                translation.setLength(0);  // Обнуляем стрингбуфер перед повторным использованием
            }

        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Не указан путь входного файла: " + e.getMessage());
        } catch (FileNotFoundException e){
            System.out.println("Входной файл не найден, проверьте путь: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Ошибка чтения из файла: " + e.getMessage());
        }
    }
}
