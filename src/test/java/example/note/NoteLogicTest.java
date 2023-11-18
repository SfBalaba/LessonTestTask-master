package example.note;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * класс модульных тестов на фунциональность класса {@link NoteLogic}
 */
public class NoteLogicTest {
    /**
     * Логика по работе с заметками
     */
    public  NoteLogic logic;
    @Before
    public void setUp() {
        this.logic = new NoteLogic();
    }

    /**
     * Тестирование команды /notes
     */
    @Test
    public void handleMessageTestNotes(){
        logic.handleMessage("/add new note");
        String actual =  logic.handleMessage("/notes");
        Assert.assertEquals("Your notes: 1. new note", actual);

    }

    /**
     * Тестирвоание команды /add добавления заметки в список заметок
     */
    @Test
    public void handleMessageTestAdd(){
        logic.handleMessage("/add new note");
        String actual =  logic.handleMessage("/notes");
        Assert.assertEquals("Your notes: 1. new node", actual);
    }

    /**
     * Тестирование команды /edit изменение существующей заметки
     */
    @Test
    public void handleMessageTestEdit() {
        logic.handleMessage("/add new note");
        String actual = logic.handleMessage("/edit another note");
        Assert.assertEquals("Note edited!", actual);
        Assert.assertEquals("Your notes: 1. another note", logic.handleMessage("/notes"));
    }

    /**
     * Тестирвоание команды /del удаление заметки из списка заметок
     */
    @Test
    public void handleMessageTestDel(){
        logic.handleMessage("/add new note");
        logic.handleMessage("/add another note");
        String actual =  logic.handleMessage("/del another note");
        Assert.assertEquals("Your notes: 1. new note",  logic.handleMessage("/notes"));
        Assert.assertEquals("Note deleted!", actual );
    }
}