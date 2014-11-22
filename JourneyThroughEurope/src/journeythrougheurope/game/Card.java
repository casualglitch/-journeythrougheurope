package journeythrougheurope.game;

public class Card {
    private String name;
    private String frontImgPath;
    private String backImgPath;
    private String color;
    private String letter;
    private int num;
    private boolean special;

    public String getName() {
        return name;
    }

    public String getFrontImgPath() {
        return frontImgPath;
    }

    public String getBackImgPath() {
        return backImgPath;
    }

    public String getColor() {
        return color;
    }

    public String getLetter() {
        return letter;
    }

    public int getNum() {
        return num;
    }

    public boolean isSpecial() {
        return special;
    }
    
    @Override
    public String toString() {
        return "[Card]: " + name + ", color: " + color + ", location: " + letter + num;
    }
}
