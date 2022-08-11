package ru.job4j.tracker.store;

import java.util.List;
import ru.job4j.tracker.model.Item;

public interface Store {
    Item add(Item item);
    boolean replace(int id, Item item);
    boolean delete(int id);
    List<Item> findAll();
    List<Item> findByName(String key);
    Item findById(int id);
}
