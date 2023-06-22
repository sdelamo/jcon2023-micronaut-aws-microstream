package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.microstream.RootProvider;
import io.micronaut.microstream.annotations.StoreParams;
import io.micronaut.microstream.annotations.StoreReturn;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Singleton
public class FruitRepositoryMicrostream implements FruitRepository {

    private final RootProvider<FruitContainer> rootProvider;

    public FruitRepositoryMicrostream(RootProvider<FruitContainer> rootProvider) {
        this.rootProvider = rootProvider;
    }

    @Override
    public @NonNull Collection<Fruit> list() {
        return rootProvider.root().getFruits();
    }

    @Override
    public @NonNull Fruit create(@NonNull @NotNull @Valid FruitCommand fruit) throws FruitDuplicateException {
        if (findByName(fruit.getName()).isPresent()) {
            throw new FruitDuplicateException(fruit.getName());
        }
        return addFruit(rootProvider.root().getFruits(), fruit);
    }

    @StoreParams("fruits")
    protected Fruit addFruit(List<Fruit> fruits, FruitCommand fruitCommand) {
        Fruit fruit = new Fruit(fruitCommand.getName(), fruitCommand.getDescription());
        fruits.add(fruit);
        return fruit;
    }

    @Override
    public @Nullable Fruit update(@NonNull @NotNull @Valid FruitCommand fruit) {
        Optional<Fruit> optionalFruit = findByName(fruit.getName());
        if (optionalFruit.isEmpty()) {
            return null;
        }
        Fruit f = optionalFruit.get();
        return update(f, fruit);
    }

    @StoreReturn
    protected Fruit update(Fruit f, FruitCommand command) {
        f.setDescription(command.getDescription());
        return f;
    }

    @Override
    public void delete(@NonNull @NotNull @Valid FruitCommand fruit) {
        findByName(fruit.getName()).ifPresent(f -> delete(rootProvider.root().getFruits(), f));
    }

    @Override
    public int count() {
        return rootProvider.root().getFruits().size();
    }

    @StoreParams("fruits")
    protected void delete(List<Fruit> fruits, Fruit f) {
        fruits.remove(f);
    }

    @Override
    public @Nullable Fruit find(@NonNull @NotBlank String name) {
        return findByName(name).orElse(null);
    }

    private Optional<Fruit> findByName(String name) {
        return rootProvider.root().getFruits().stream().filter(f -> f.getName().equals(name)).findFirst();
    }

}
