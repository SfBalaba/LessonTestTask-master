package example.bot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static example.bot.Constants.*;


/**
 * класс модульных тестов на класс {@link BotLogic}
 */
public class BotLogicTest {

    /**
     * экземпляр логики бота, имулирующего удобную для тестирования отправку сообщений
     */
    private BotLogic fakeLogic;
    /**
     * пользователь
     */
    private User user;
    /**
     * список всех сообщенией от бота
     */
    private LinkedList<String> botMessages;

    @Before
    public void setUp() {
        /**
         * видоизменённый бот удобен для тестирования, имитирует {@link ConsoleBot}
         */
        FakeBot fakeBot = new FakeBot();
        fakeLogic = new BotLogic(fakeBot);
        user = new User(1L);
        botMessages = fakeBot.getMessages();
    }

    /**
     * Тестирование распознавания правильного ответа
     */
    @Test
    public void BotLogicprocessNonCommandTestCorrect() {
        fakeLogic.processCommand(user, "/test");
        Assert.assertEquals("Вычислите степень: 10^2", botMessages.get(0));
        fakeLogic.processCommand(user, "100");
        Assert.assertEquals("Правильный ответ!", botMessages.get(1));
        Assert.assertEquals(0, user.getWrongAnswerQuestions().size());
    }

    /**
     * Тестирование распознавания неправильного ответа
     */
    @Test
    public void BotLogicprocessNonCommandTestIncorrect() {
        fakeLogic.processCommand(user, "/test");
        Assert.assertEquals("Вычислите степень: 10^2", botMessages.get(0));
        fakeLogic.processCommand(user, "101");
        Assert.assertEquals("Вы ошиблись, верный ответ: 100", botMessages.get(1));
        Assert.assertEquals(1, user.getWrongAnswerQuestions().size());
    }


    /**
     * <p>Тестирование правильного ответа второго вопроса теста после того,</p>
     * <p>как ответ на первый вопрос неправильный</p>
     */
    @Test
    public void BotLogicprocessNonCommandTestSecondCorrect() {
        fakeLogic.processCommand(user, "/test");
        fakeLogic.processCommand(user, "101");
        fakeLogic.processCommand(user, "6");
        Assert.assertEquals("Правильный ответ!", botMessages.get(3));
    }


    /**
     * Тестирование завершения прохождения тестов
     */
    @Test
    public void BotLogicprocessNonCommandAllTestsComplete() {
        fakeLogic.processCommand(user, "/test");
        fakeLogic.processCommand(user, "100");
        fakeLogic.processCommand(user, "6");
        Assert.assertEquals("Тест завершен", botMessages.get(4));
    }

    /**
     * Тестирование команды /repeat, если нет вопросов для повторения
     */
    @Test
    public void BotLogicprocessRepeatIfNotPresent() {
        fakeLogic.processCommand(user, "/repeat");
        Assert.assertEquals("Нет вопросов для повторения", botMessages.get(0));
    }

    /**
     * Тестирование команды /repeat, когда есть неправильно отвеченные вопросы
     */
    @Test
    public void BotLogicprocessRepeatIfPresent() {
        fakeLogic.processCommand(user, "/test");
        fakeLogic.processCommand(user, "102");
        fakeLogic.processCommand(user, COMMAND_STOP);
        fakeLogic.processCommand(user, "/repeat");
        Assert.assertEquals(State.REPEAT, user.getState());
        Assert.assertTrue(user.getCurrentWrongAnswerQuestion().isPresent());
        Assert.assertEquals(1, user.getWrongAnswerQuestions().size());
    }

    /**
     * Тестирование команды /notify
     */
    @Test
    public void BotLogicprocessNotifyCommand() {
        fakeLogic.processCommand(user, "/notify");
        Assert.assertEquals("Введите текст напоминания", botMessages.pop());
    }


    /**
     * Тестирование команды /notify устанвоки напоминаний
     */
    @Test
    public void BotLogicprocessNotificationDelay(){
        fakeLogic.processCommand(user, "/notify");
        fakeLogic.processCommand(user, "Тестирование напоминаний");
        Assert.assertTrue(user.getNotification().isPresent());
        Assert.assertEquals("Через сколько секунд напомнить?", botMessages.get(1));
        fakeLogic.processCommand(user, "102");
        Assert.assertEquals("Напоминание установлено", botMessages.get(2));
        Assert.assertEquals(3, botMessages.size());
    }


    /**
     * Тестирование двух отложенных напоминаний одновременно
     * @throws InterruptedException исключение прерывания выполнения теста
     */
    @Test
    public void BotLogicprocessTwoNotificationDelay() throws InterruptedException {
        fakeLogic.processCommand(user, "/notify");
        fakeLogic.processCommand(user,"Тестирование напоминаний");
        fakeLogic.processCommand(user,"4");
        fakeLogic.processCommand(user, "/notify");
        fakeLogic.processCommand(user,"Тестирование напоминаний2");
        fakeLogic.processCommand(user,"1");
        Thread.sleep(1110);
        Assert.assertEquals("Сработало напоминание: 'Тестирование напоминаний2'", botMessages.get(6));
        Thread.sleep(3010);
        Assert.assertEquals("Сработало напоминание: 'Тестирование напоминаний'", botMessages.get(7));
    }


    /**
     * Тестирвоание обработки некорректного пользовательского ввода длительности напоминаний
     */
    @Test
    public void commandNotificationIncorrectInput() {
        fakeLogic.processCommand(user, "/notify");
        Assert.assertEquals("Введите текст напоминания", botMessages.get(0));
        fakeLogic.processCommand(user,"Тестирование напоминаний");
        Assert.assertEquals("Через сколько секунд напомнить?", botMessages.get(1));
        fakeLogic.processCommand(user,"ssf");
        Assert.assertEquals("Пожалуйста, введите целое число", botMessages.get(2));
    }

}