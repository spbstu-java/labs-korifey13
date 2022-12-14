import movetype.MoveType;

public class Hero {

    private String name;

    public Hero(String name) {
        this.name = name;
    }

    public void move(String x, String y, MoveType type) {
        System.out.printf("%s %s %s %s%n", name, type.action(), x, y);
    }
}
