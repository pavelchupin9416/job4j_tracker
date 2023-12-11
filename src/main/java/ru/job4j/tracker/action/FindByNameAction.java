package ru.job4j.tracker.action;

import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.store.MemTracker;
import ru.job4j.tracker.store.Store;

import java.util.List;

public class FindByNameAction implements UserAction {

    private final Output out;

    public FindByNameAction(Output out) {
        this.out = out;
    }

    @Override
    public String name() {
        return "=== Find items by name ====";
    }

    @Override
    public boolean execute(Input input, Store store) {
        String name = input.askStr("Enter name: ");
        List<Item> items = store.findByName(name);
        if (items.size() > 0) {
            for (Item item: items) {
            out.println("id: " + item.getId() + " name: " + item.getName());
            }
        } else {
            out.println("item mot found");
        }
        return true;
    }
}
