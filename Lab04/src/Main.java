import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

// Написать аннотацию с целочисленным параметром: RunCountAnnotation.java
// Создать класс, содержащий публичные, защищённые и приватные методы
//     (2-3 каждого вида), аннотировать любые из них: TestClass.java
// Вызвать из другого класса все аннотированные защищённые и приватные методы столько раз,
//     сколько указано в параметре аннотации: вызываем из Main

public class Main {
    public static void main(String[] args) { // throws InvocationTargetException, IllegalAccessException {
        TestClass testClass = new TestClass();  // Экземпляр класса с аннотированными и неаннотироваными методами
        Class<? extends TestClass> extClass = testClass.getClass();
        // Список методов унаследованных расширенным классом, т.е. публичных
        List<Method> publicMethods = Arrays.asList(extClass.getMethods());
        // Список методов класса TestClass
        Method[] declaredMethods = extClass.getDeclaredMethods();
        System.out.println("Публичных методов: " + publicMethods.size());
        System.out.println("Методов класса: " + declaredMethods.length + "\n\r");
        for (Method method : declaredMethods) {  // Для каждого из методов TestClass
            System.out.println(method.getName() + ":");
            // Выбираем только аннотированные и НЕ публичные методы
            if (method.isAnnotationPresent(RunCountAnnotation.class) && !publicMethods.contains(method)) {
                method.setAccessible(true);  // Обход проверок доступа для запуска приватных и защищённых методов
                RunCountAnnotation annotation = method.getAnnotation(RunCountAnnotation.class);
                // Вызываем столько раз, какое число в параметре аннотации
                try {
                    for (int i = 0; i < annotation.value(); i++)
                        method.invoke(testClass);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    System.out.println("Ошибочка вышла: " + e.getMessage());
                }
            }
        }
    }
}
