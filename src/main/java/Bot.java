package main.java;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    // наследуемся от API
    public static void main(String[] args) {
        ApiContextInitializer.init(); // инициализируем API
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();// объект бота
        try {
            telegramBotsApi.registerBot(new Bot()); // регистрируем бота

        } catch (TelegramApiRequestException e) {
            System.out.println("Ошибка подключения");

        }
    }

    public void sendMsg(Message message, String text) {
        // метод отвечает за отправку сообщения от бота
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true); // включаем разметку текста
        sendMessage.setChatId(message.getChatId().toString()); // находим chat-id и устанавливаем его
        sendMessage.setReplyToMessageId(message.getMessageId());// определяем id - message
        sendMessage.setText(text); // текст сообщения
        try {
            setButton(sendMessage); // устанавливаем клавиатуру
            sendMessage(sendMessage); // отправляем сообщение
        } catch (TelegramApiException e) {
            System.out.println("Ошибка во время отправки сообщения");
            e.printStackTrace();
        }

    }

    public void onUpdateReceived(Update update) {
        // метод для приема сообщений
        Model model = new Model();

        Message message = update.getMessage(); // получаем сообщение от пользователя
        if (message != null && message.hasText()) {// если в сообщении присутствует текст
            switch (message.getText()) {// выбираем действия в зависимости от текста
                case "/help":
                    sendMsg(message, "просто введи название города");
                    break;

                case "/setting":
                    sendMsg(message, "Пока что никаких настоек не предусмотрено");
                    break;

                case "/start":
                    sendMsg(message, "Я бот который раскажет погоду в дюбом городе \n просто введи название города");
                    break;

                default:
                    try {
                        String textJson = Weather.getWeather(message.getText());
                        Model jsonAnswer = Weather.parsingJSON(textJson, model);
                        String textMessage = Weather.getInfoFromModel(jsonAnswer);
                        System.out.println(textMessage);
                        sendMsg(message, textMessage);
                    } catch (IOException e) {
                        sendMsg(message, "Такой город не найден");
                    }

            }
        }
    }

    public void setButton(SendMessage sendMessage) {
        // метод отвечает за создание клавиатуры под текстом
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(); // создание клавиатуры
        sendMessage.setReplyMarkup(replyKeyboardMarkup);// устанавливаем разметку
        replyKeyboardMarkup.setSelective(true); // вывод клавы всем пользователям
        replyKeyboardMarkup.setResizeKeyboard(true);// подгон размера клавиатуры
        replyKeyboardMarkup.setOneTimeKeyboard(false); // не скрываем клавиатуру

        List<KeyboardRow> keyboardRowsList = new ArrayList<KeyboardRow>(); // создаем массив кнопок-поле

        KeyboardRow keyboardFirstRow = new KeyboardRow();// создаем кнопку-поле
        keyboardFirstRow.add(new KeyboardButton("/help"));// создаем кнопку с текстом
        keyboardFirstRow.add(new KeyboardButton("/setting"));// создаем кнопку с текстом

        keyboardRowsList.add(keyboardFirstRow); // добавляем в list кнопку

        replyKeyboardMarkup.setKeyboard(keyboardRowsList); // и устанавливаем список на клавиатуру

    }

    public String getBotUsername() {
        // метод для возвращения имени бота
        String nameBot = new String("JavaBot");
        return null;
    }

    public String getBotToken() {
        // возвращение token бота
        String token = new String("1361902027:AAGH3o4Hloz3sjbC_oe6m-vWvVxgwwxRaXk");
        return token;
    }
}
