package example.container;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * класс модульных тестов {@link Container}
 */
public class ContainerTest{
    /**
     * Контейнер
     */
    public Container container;
    /**
     * элемент контейнера
     */
    public Item item1;

    /**
     * элемент контейнера
     */
    public Item item2;

    @Before
    public void setUp(){
        this.container = new Container();
        this.item1 = new Item(1L);
        this.item2 = new Item(2L);
    }

    /**
     * Тестирвоание добавления элемента в контейнер
     */
    @Test
    public void ContainerTestAdd(){
        container.add(item1);
        Assert.assertEquals(1, container.size());
        Assert.assertTrue(container.contains(item1));
    }

    /**
     * Тестирование получения элемента из контейнера
     */
    @Test
    public void ContainerTestGet(){
        container.add(item1);
        container.add(item2);
        Assert.assertEquals(item1, container.get(0));
        Assert.assertEquals(item2, container.get(1));
    }

    /**
     * Тестирвоание удаления элемента из контейнера
     */
    @Test
    public void ContainerTestRemove(){
        container.add(item1);
        container.add(item2);
        container.remove(item2);
        Assert.assertEquals(1, container.size());
        Assert.assertTrue(container.contains(item1));
    }
}