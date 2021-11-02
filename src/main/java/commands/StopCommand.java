package commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

import static bot.PS5HunterBot.SEARCH_PERMITTED;
import static bot.PS5HunterBot.getPS5HunterBot_instance;

public class StopCommand extends BotCommand {

    public static final String LOGTAG = "STOP_COMMAND";

    public StopCommand() {
        // тут будет остановка бота = остановка поиска
        super("stop", "Stop bot. ");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        SEARCH_PERMITTED = false;

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        // тут нужно писать соответствующее сообщение
        answer.setText("Бот остановлен. PS5 была найдена/не была найдена");

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }

        getPS5HunterBot_instance().onClosing();
    }
}
