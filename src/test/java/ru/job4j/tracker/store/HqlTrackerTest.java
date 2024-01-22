package ru.job4j.tracker.store;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.tracker.model.Item;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HqlTrackerTest {

    @AfterEach
    public void wipeTable() throws SQLException {
        try (var tracker = new HbmTracker()) {
            tracker.deleteAll();
        }
    }

    @Test
    public void whenAddNewItemThenTrackerHasSameItem() throws Exception {
        try (var tracker = new HbmTracker()) {
            Item item = new Item();
            item.setName("test1");
            tracker.add(item);
            Item result = tracker.findById(item.getId());
            assertThat(result.getName()).isEqualTo(item.getName());
        }
    }

    @Test
    public void whenDelete() {
        try (var tracker = new HbmTracker()) {
            Item first = tracker.add(new Item("first"));
            int id = first.getId();
            tracker.delete(id);
            assertThat(tracker.findById(id)).isNull();
        }
    }


    @Test
    public void whenReplace() {
        try (var tracker = new HbmTracker()) {
            Item first = tracker.add(new Item("first"));
            int id = first.getId();
            Item second = new Item("second");
            tracker.replace(id, second);
            assertThat(tracker.findById(id).getName()).isEqualTo("second");
        }
    }


    @Test
    public void whenTestFindByNameCheckArrayLength() {
        try (var tracker = new HbmTracker()) {
            Item first = tracker.add(new Item("First"));
            tracker.add(new Item("Second"));
            tracker.add(new Item("First"));
            tracker.add(new Item("Second"));
            tracker.add(new Item("First"));
            List<Item> result = tracker.findByName(first.getName());
            assertThat(result.size()).isEqualTo(3);
        }
    }


    @Test
    public void whenTestFindAll() {
        try (var tracker = new HbmTracker()) {
            Item first = tracker.add(new Item("First"));
            Item second = tracker.add(new Item("Second"));
            List<Item> result = tracker.findAll();
            assertThat(result).isEqualTo(List.of(first, second));
        }
    }


    @Test
    public void whenTestFindByNameCheckSecondItemName() {
        try (var tracker = new HbmTracker()) {
            Item first = tracker.add(new Item("First"));
            Item second = tracker.add(new Item("Second"));
            Item three = tracker.add(new Item("First"));
            Item four = tracker.add(new Item("Second"));
            Item five = tracker.add(new Item("First"));
            List<Item> result = tracker.findByName(second.getName());
            assertThat(result).isEqualTo(List.of(second, four));
        }
    }
}
