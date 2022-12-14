import movetype.Fly;
import movetype.Walk;
import movetype.Creep;

public class Main {
    public static void main(String[] args) {
        Hero hero = new Hero("Студент ИКНТ");

        hero.move("из корпуса 5","в корпус 3", new Fly());
        hero.move("от входа","в аудиторию 102", new Walk());
        hero.move("из аудитории 102","в кабинет 300", new Creep());
    }
}
