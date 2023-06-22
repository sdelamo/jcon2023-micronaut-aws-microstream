package example.micronaut;

import java.util.ArrayList;
import java.util.List;

public class FruitContainer {

    private final List<Fruit> fruits = new ArrayList<>();

    public List<Fruit> getFruits() {
        return fruits;
    }
}
