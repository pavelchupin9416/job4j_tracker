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

public class DeleteActionTest {
    @Test
    public void whenItemWasDeleteSuccessfully() {
        Output out = new StubOutput();
        MemTracker tracker = new MemTracker();
        tracker.add(new Item("item"));
        DeleteAction del= new DeleteAction(out);

        Input input = mock(Input.class);
        when(input.askInt(any(String.class))).thenReturn(0);
        /*  when(input.askStr(any(String.class))).thenReturn(replacedName);*/

        del.execute(input, tracker);

        String ln = System.lineSeparator();
        assertThat(out.toString(),is("Item is successfully deleted!" + ln));
    }

    @Test
    public void whenItemWasDeleteUnsuccessfuly() {
        Output out = new StubOutput();
        MemTracker tracker = new MemTracker();
        tracker.add(new Item("item"));
        DeleteAction del= new DeleteAction(out);

        Input input = mock(Input.class);
        when(input.askInt(any(String.class))).thenReturn(1);

        del.execute(input, tracker);

        String ln = System.lineSeparator();
        assertThat(out.toString(),is("Wrong id!" + ln));
    }
}
