package parser;

public class MainParser {
    private static MainParser instance ;

    public static MainParser getInstance() {
        if (instance == null) {
            instance = new MainParser();
        }
        return instance;
    }

    public String weather;
    public String cityName;
    public String cityUrl;

    public String getWeather(String message){
        cityName = message.split(" ")[1];
        ParserCity parserCity = new ParserCity();
        cityUrl = parserCity.getCityUrl(cityName);
        if (cityUrl == null) {
            weather = "Unknown city";
        } else {
            ParserData parser = new ParserData("https://yandex.ru" + cityUrl);
            weather = parser.parsingWeather();
        }
        return weather;
    }
}
