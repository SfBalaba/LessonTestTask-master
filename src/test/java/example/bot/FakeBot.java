package example.bot;


import java.util.LinkedList;

/**
 * класс для удобного тестирования BotLogic
 */
public class FakeBot implements Bot {
    /**
     * Коллекция, хранящая все сообщения
     */
    private final LinkedList<String> messages = new LinkedList<>();


    /**
     * @return список сообщений от бота
     */
    public LinkedList<String> getMessages() {
        return messages;
    }

    /**
     * <p>имитирует пересылку сообщеий от бота,</p>
     * <p>теперь мы можем читать все сообщения от бота</p>
     * @param chatId  идентификатор чата
     * @param message текст сообщения
     */
    @Override
    public void sendMessage(Long chatId, String message) {
        messages.add(message);
    }
}