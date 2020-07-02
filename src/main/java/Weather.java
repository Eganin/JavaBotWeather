package main.java;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    public static String getWeather(String message) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message
                + "&units=metric&appid=66fcef49db577be892771be5e3148516"); // делаем запрос к странице

        // передаем в scanner содержимое страницы
        Scanner scanner = new Scanner((InputStream) url.getContent()); // преобразуя в InputStream
        String result = new String("");
        while (scanner.hasNext()) { // считываем из scanner данные
            result += scanner.nextLine();
        }
        return result;
    }

    public static Model parsingJSON(String json, Model model) {
        // метод отвечает за взятие инфы из json
        JSONObject object = new JSONObject(json); // создаем JSON - объект
        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");

        model.setTemp(main.getDouble("temp"));

        model.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = object.getJSONArray("weather");// получаем массив из json
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon(obj.getString("icon"));
            model.setMain(obj.getString("main"));
        }

        return model;
    }

    public static String getInfoFromModel(Model model) {
        String url = new String("http://openweathermap.org/img/w/");
        String urlEnd = new String(".png");
        String city = model.getName();
        String main = model.getMain();
        Double temp = model.getTemp();
        Double humidity = model.getHumidity();
        String icon = model.getIcon();

        return "City: " + city + "\n" + "Weather: " + main + "\n" + "Temperature: " + temp.toString() + "\n"
                + "Humidity: " + humidity.toString() + "\n" + "Icon: " + "\n" + url + icon + urlEnd;
    }
}
