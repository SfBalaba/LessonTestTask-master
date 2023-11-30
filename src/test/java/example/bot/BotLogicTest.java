package example.bot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static example.bot.Constants.*;


/**
 * класс модульных тестов на класс {@link BotLogic}
 */
public class BotLogicTest {
    private BotLogic logic;

    /**
     * пользователь
     */
    private User user;
    /**
     * видоизменённый бот удобен для тестирования, имитирует {@link ConsoleBot}
     */
    private FakeBot fakeBot;

    @Before
    public void setUp() {
        fakeBot = new FakeBot();
        logic = new BotLogic(fakeBot);
        user = new User(1L);
    }

    /**
     * Тестирование команды /test,  если все отвтеты правильные
     */
    @Test
    public void BotLogicprocessNonCommandTestCorrect() {
        logic.processCommand(user, "/test");
        Assert.assertEquals("Вычислите степень: 10^2", fakeBot.getMessages().get(0));
        logic.processCommand(user, "100");
        Assert.assertEquals("Правильный ответ!", fakeBot.getMessages().get(1));
        Assert.assertEquals("Сколько будет 2 + 2 * 2", fakeBot.getMessages().get(2));
        logic.processCommand(user, "6");
        Assert.assertEquals("Правильный ответ!", fakeBot.getMessages().get(3));
        Assert.assertEquals("Тест завершен", fakeBot.getMessages().get(4));
        Assert.assertEquals(0, user.getWrongAnswerQuestions().size());

    }

    /**
     * Тестирование распознавания неправильного ответа
     */
    @Test
    public void BotLogicprocessNonCommandTestIncorrect() {
        logic.processCommand(user, "/test");
        Assert.assertEquals("Вычислите степень: 10^2", fakeBot.getMessages().get(0));
        logic.processCommand(user, "101");
        Assert.assertEquals("Вы ошиблись, верный ответ: 100", fakeBot.getMessages().get(1));
        Assert.assertEquals("Сколько будет 2 + 2 * 2", fakeBot.getMessages().get(2));
        logic.processCommand(user, "8");
        Assert.assertEquals("Вы ошиблись, верный ответ: 6", fakeBot.getMessages().get(3));
        Assert.assertEquals("Тест завершен", fakeBot.getMessages().get(4));
        Assert.assertEquals(State.INIT, user.getState());
        Assert.assertEquals(2, user.getWrongAnswerQuestions().size());
    }


    /**
     * Тестирование команды /repeat, если нет вопросов для повторения
     */
    @Test
    public void BotLogicprocessRepeatIfNotPresent() {
        logic.processCommand(user, "/repeat");
        Assert.assertEquals("Нет вопросов для повторения", fakeBot.getMessages().get(0));
    }
    /**
     * Тестирование команды /repeat, когда есть неправильно отвеченные вопросы
     */
    @Test
    public void BotLogicprocessRepeatIfPresent() {
        logic.processCommand(user, "/test");
        logic.processCommand(user, "102");
        logic.processCommand(user, COMMAND_STOP);
        Assert.assertEquals(4, fakeBot.getMessages().size());
        Assert.assertEquals("Тест завершен", fakeBot.getMessages().get(3));
        logic.processCommand(user, "/repeat");
        Assert.assertEquals(State.REPEAT, user.getState());
        Assert.assertTrue(user.getCurrentWrongAnswerQuestion().isPresent());
        Assert.assertEquals(1, user.getWrongAnswerQuestions().size());
        Assert.assertEquals("Вычислите степень: 10^2", fakeBot.getMessages().get(4));
        logic.processCommand(user, "100");
        Assert.assertEquals("Тест завершен", fakeBot.getMessages().get(6));
        Assert.assertEquals(0, user.getWrongAnswerQuestions().size());
        Assert.assertEquals(State.INIT, user.getState());
    }

    /**
     * Тестирование команды /notify устанвоки напоминаний
     */
    @Test
    public void BotLogicprocessNotificationDelay() {
        logic.processCommand(user, "/notify");
        Assert.assertEquals("Введите текст напоминания", fakeBot.getMessages().get(0));
        logic.processCommand(user, "Тестирование напоминаний");
        Assert.assertTrue(user.getNotification().isPresent());
        Assert.assertEquals("Через сколько секунд напомнить?", fakeBot.getMessages().get(1));
        logic.processCommand(user, "1");
        Assert.assertEquals("Напоминание установлено", fakeBot.getMessages().get(2));
        Assert.assertEquals(3, fakeBot.getMessages().size());
        try {
            Thread.sleep(1110);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals("Сработало напоминание: 'Тестирование напоминаний'", fakeBot.getMessages().get(3));
    }


    /**
     * Тестирование двух отложенных напоминаний одновременно
     *
     * @throws InterruptedException исключение прерывания выполнения теста
     */
    @Test
    public void BotLogicprocessTwoNotificationDelay() throws InterruptedException {
        logic.processCommand(user, "/notify");
        for (String s : Arrays.asList("Тестирование напоминаний", "2", "/notify", "Тестирование напоминаний2", "1")) {
            logic.processCommand(user, s);
        }
        Assert.assertEquals(fakeBot.getMessages().size(), 6);
        Thread.sleep(1110);
        Assert.assertEquals("Сработало напоминание: 'Тестирование напоминаний2'", fakeBot.getMessages().get(6));
        Thread.sleep(1010);
        Assert.assertEquals("Сработало напоминание: 'Тестирование напоминаний'", fakeBot.getMessages().get(7));
    }


    /**
     * Тестирвоание обработки некорректного пользовательского ввода длительности напоминаний
     */
    @Test
    public void commandNotificationIncorrectInput() {
        logic.processCommand(user, "/notify");
        Assert.assertEquals("Введите текст напоминания", fakeBot.getMessages().get(0));
        logic.processCommand(user, "Тестирование напоминаний");
        Assert.assertEquals("Через сколько секунд напомнить?", fakeBot.getMessages().get(1));
        logic.processCommand(user, "ssf");
        Assert.assertEquals("Пожалуйста, введите целое число", fakeBot.getMessages().get(2));
    }

}