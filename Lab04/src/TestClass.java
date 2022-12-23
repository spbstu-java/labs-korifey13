import java.lang.reflect.Modifier;

// Класс, содержащий публичные, защищённые и приватные методы (2-3 каждого вида). Некоторые аннотированы.
public class TestClass {

    // Каждый метод выводит своё имя и модификаторы
    public void testPublicFirst(){
        System.out.println(" Меня зовут " + new Object() {}.getClass().getEnclosingMethod().getName()
                + " и я " + Modifier.toString(new Object() {}.getClass().getEnclosingMethod().getModifiers()));
    }
    @RunCountAnnotation(value = 7)
    public void testPublicSecond(){
        System.out.println(" Меня зовут " + new Object() {}.getClass().getEnclosingMethod().getName()
                + " и я " + Modifier.toString(new Object() {}.getClass().getEnclosingMethod().getModifiers()));
    }

    protected void testProtectedFirst(){
        System.out.println(" Меня зовут " + new Object() {}.getClass().getEnclosingMethod().getName()
                + " и я " + Modifier.toString(new Object() {}.getClass().getEnclosingMethod().getModifiers()));
    }
    @RunCountAnnotation(value = 6)
    protected void testProtectedSecond(){
        System.out.println(" Меня зовут " + new Object() {}.getClass().getEnclosingMethod().getName()
                + " и я " + Modifier.toString(new Object() {}.getClass().getEnclosingMethod().getModifiers()));
    }

    @RunCountAnnotation(value = 4)
    private void testPrivateFirst(){
        System.out.println(" Меня зовут " + new Object() {}.getClass().getEnclosingMethod().getName()
                + " и я " + Modifier.toString(new Object() {}.getClass().getEnclosingMethod().getModifiers()));
    }
    @RunCountAnnotation(value = 2)
    private static void testPrivateSecond(){
        System.out.println(" Меня зовут " + new Object() {}.getClass().getEnclosingMethod().getName()
                + " и я " + Modifier.toString(new Object() {}.getClass().getEnclosingMethod().getModifiers()));
    }
}
