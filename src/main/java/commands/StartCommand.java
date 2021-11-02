package commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

import static bot.PS5HunterBot.getPS5HunterBot_instance;

public class StartCommand extends BotCommand {

    public static final String LOGTAG = "START_COMMAND";
    public static Long chatId;

    public StartCommand() {
        // тут будет старт бота = старт поиска
        super("start", "Start bot. ");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        StringBuilder messageBuilder = new StringBuilder();

        String userName = user.getFirstName() + " " + user.getLastName();

        messageBuilder.append("Hi ").append(userName).append("\n");
        messageBuilder.append("i think we know each other already!");

        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(messageBuilder.toString());

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }

        chatId = chat.getId();
        getPS5HunterBot_instance().search();
    }
}