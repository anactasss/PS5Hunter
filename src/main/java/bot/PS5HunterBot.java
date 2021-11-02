package bot;

import commands.HelpCommand;
import commands.StartCommand;
import commands.StatusCommand;
import commands.StopCommand;
import concurrent.SearchPools;
import config.BotConfig;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import search_resources.MVideoSearcher;
import search_resources.SonyStoreSearcher;

import static config.BotConfig.BOT_USER;

public class PS5HunterBot extends TelegramLongPollingCommandBot {

    private static volatile PS5HunterBot PS5HunterBot_instance;

    public static PS5HunterBot getPS5HunterBot_instance() {
        PS5HunterBot localInstance = PS5HunterBot_instance;
        if (localInstance == null) {
            synchronized (PS5HunterBot.class) {
                localInstance = PS5HunterBot_instance;
                if (localInstance == null) {
                    PS5HunterBot_instance = localInstance = new PS5HunterBot(BOT_USER);
                }
            }
        }
        return localInstance;
    }

    private static final String LOGTAG = "PS5_HUNTER_BOT";
    public static boolean FOUND = false;
    public static boolean SEARCH_PERMITTED = true;
    public Long chatId = null;

    public PS5HunterBot(String botUsername) {
        super(botUsername);

        register(new StatusCommand());
        register(new StartCommand());
        register(new StopCommand());

        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId());
            commandUnknownMessage.setText("The command '" + message.getText() + "' is not known by this bot.");
            try {
                absSender.execute(commandUnknownMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[] {});
        });
    }

    public void search() {
        this.chatId = StartCommand.chatId;

        while (!FOUND && SEARCH_PERMITTED) {
            SearchPools searchPools = new SearchPools(2);
            searchPools.invoke(SonyStoreSearcher::search);
            searchPools.invoke(MVideoSearcher::search);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (FOUND) {
            found();
        } else {
            System.out.println("Ничего не нашли, но поиск остановлен");
        }
    }

    public void found() {
        StringBuilder foundMessage = new StringBuilder();
        // тут сообщение что нашли с соответствующим источником
        foundMessage.append("PS5 found");
        sendMessage(foundMessage.toString());
    }

    public void sendMessage(String messageToSend) {

        SendMessage message = new SendMessage();

        message.enableMarkdown(true);
        message.setChatId(chatId);
        message.setText(messageToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();


            if (message.hasText()) {
                SendMessage echoMessage = new SendMessage();
                echoMessage.setChatId(message.getChatId());
                echoMessage.setText("Hey heres your message:\n" + message.getText());

                try {
                    execute(echoMessage);
                } catch (TelegramApiException e) {
                    BotLogger.error(LOGTAG, e);
                }
            }
        }
    }

    @Override
    public String getBotToken() {
        return BotConfig.BOT_TOKEN;
    }
}
