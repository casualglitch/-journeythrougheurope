package journeythrougheurope.game;

public class City {
    private String name;
    private String color;
    private int quarter;
    private int x;
    private int y;
    private City[] landBorders;
    private City[] seaBorders;
    
    public City(String name, String color, int quarter, int x, int y) {
        this.name = name;
        this.color = color;
        this.quarter = quarter;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public City[] getLandBorders() {
        return landBorders;
    }

    public void setLandBorders(City[] landBorders) {
        this.landBorders = landBorders;
    }

    public City[] getSeaBorders() {
        return seaBorders;
    }

    public void setSeaBorders(City[] seaBorders) {
        this.seaBorders = seaBorders;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

}
