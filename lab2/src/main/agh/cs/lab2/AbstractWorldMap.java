package agh.cs.lab2;

abstract public class AbstractWorldMap implements IWorldMap {

    protected Vector2d limit;   // nieczytelna nazwa, czemu limit jest tylko jeden?


    /**
     * getters added in methods RectangularMap and GrassField to satisfy ex.7 conditions
     * @return
     */
    public String toString() {
        return new MapVisualizer(this).draw(new Vector2d(0, 0), getLimit());    // tworzenie obiektu co wywołanie
    }

    abstract public Vector2d getLimit();

    @Override
    abstract public boolean canMoveTo(Vector2d position);   // nie ma sensu przepisywać deklaracji z interfejsu

    @Override
    abstract public boolean place(Animal animal);

    @Override
    abstract public void run(MoveDirection[] directions);

    @Override
    public boolean isOccupied(Vector2d position) {
        if (objectAt(position) == null) return false;

        return true;
    }

    @Override
    abstract public Object objectAt(Vector2d position);

    // nie ma więcej wspólnej funkcjonalności?

}
