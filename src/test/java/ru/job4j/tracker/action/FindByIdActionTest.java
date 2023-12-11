package ru.job4j.tracker.action;

import org.junit.jupiter.api.Test;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.output.StubOutput;
import ru.job4j.tracker.store.MemTracker;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FindByIdActionTest {
    @Test
    public void whenItemWasFindSuccessfully() {
        Output out = new StubOutput();
        MemTracker tracker = new MemTracker();
        tracker.add(new Item("item test"));

        FindByIdAction find = new FindByIdAction(out);

        Input input = mock(Input.class);
        when(input.askInt(any(String.class))).thenReturn(0);

        find.execute(input, tracker);

        String ln = System.lineSeparator();
        assertThat(out.toString(),is("id: 0 name: item test" + ln));
    }

    @Test
    public void whenItemWasFindUnsuccessfully() {
        Output out = new StubOutput();
        MemTracker tracker = new MemTracker();
        tracker.add(new Item("item test"));

        FindByIdAction find = new FindByIdAction(out);

        Input input = mock(Input.class);
        when(input.askInt(any(String.class))).thenReturn(1);

        find.execute(input, tracker);

        String ln = System.lineSeparator();
        assertThat(out.toString(),is("Wrong id! Not found" + ln));
    }
}
