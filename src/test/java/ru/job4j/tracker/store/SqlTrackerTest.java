package ru.job4j.tracker.store;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsNull;
//import org.junit.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.tracker.model.Item;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class SqlTrackerTest {

    private static Connection connection;

    @BeforeAll
    public static void initConnection() {
        try (InputStream in = new FileInputStream("db/liquibase.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")

            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @AfterEach
    public void wipeTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("delete from items")) {
            statement.execute();
        }
    }


    @Test
    public void whenSaveItemAndFindByGeneratedIdThenMustBeTheSame() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = tracker.add(new Item("item"));
        assertThat(tracker.findById(item.getId()), is(item));
    }


    @Test
    public void whenDelete() {
        SqlTracker tracker = new SqlTracker(connection);
        Item first = tracker.add(new Item("first"));
        int id = first.getId();
        tracker.delete(id);
        assertThat(tracker.findById(id), is(IsNull.nullValue()));
    }


    @Test
    public void whenReplace() {
        SqlTracker tracker = new SqlTracker(connection);
        Item first = tracker.add(new Item("first"));
        int id = first.getId();
        Item second = new Item("second");
        tracker.replace(id, second);
        assertThat(tracker.findById(id).getName(), is("second"));
    }


    @Test
    public void whenTestFindByNameCheckArrayLength() {
        SqlTracker tracker = new SqlTracker(connection);
        Item first = tracker.add(new Item("First"));
        tracker.add(new Item("Second"));
        tracker.add(new Item("First"));
        tracker.add(new Item("Second"));
        tracker.add(new Item("First"));
        List<Item> result = tracker.findByName(first.getName());
        assertThat(result.size(), is(3));
    }


    @Test
    public void whenTestFindAll() {
        SqlTracker tracker = new SqlTracker(connection);
        Item first = tracker.add(new Item("First"));
        Item second = tracker.add(new Item("Second"));
        List<Item> result = tracker.findAll();
        assertThat(result, is(List.of(first, second)));
    }


    @Test
    public void whenTestFindByNameCheckSecondItemName() {
        SqlTracker tracker = new SqlTracker(connection);
        Item first = tracker.add(new Item("First"));
        Item second = tracker.add(new Item("Second"));
        Item three = tracker.add(new Item("First"));
        Item four = tracker.add(new Item("Second"));
        Item five = tracker.add(new Item("First"));
        List<Item> result = tracker.findByName(second.getName());
        assertThat(result, is(List.of(second, four)));
    }
}