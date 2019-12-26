package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserData {
    String siteUrl;
    Pattern pattern;
    Matcher matcher;
    String city;
    String temp;
    String wind;
    String hymidity;
    String pressure;

    public ParserData(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String parsingWeather() {

        String input;

        try {
            URL url = new URL(this.siteUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            while((input = in.readLine()) != null) {
                getCityName(input);
                getTemp(input);
                getWindSpeed(input);
                getHumidity(input);
                getPressure(input);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getWeather();
    }

    private String getWeather(){
        String answer = city + ":\n"+
                "Температура = " + temp + " C\n" +
                "Влажность = " + hymidity + " %\n" +
                "Скорость ветра = " + wind + " м.с\n" +
                "Давление = " + pressure + " мм.рт.ст.\n";
        return answer;
    }


    private void getCityName(String input) {
        String city = match(input, "<h1 class=\"title title_level_1 header-title__title\" id=\"main_title\">(.*?)</h1>");
        if(city != null) {
           this.city = city;
        }
    }

    private void getTemp(String input) {
        String temp = match(input, "<div class=\"temp fact__temp fact__temp_size_s\" role=\"text\"><span class=\"temp__value\">(.*?)?</span>");
        if(temp != null) {
            this.temp = temp;
        }
    }

    private void getWindSpeed(String input) {
        String wind = match(input, "<span class=\"wind-speed\">(.*?)?</span>");
        if(wind != null) {
            this.wind = wind;
        }
    }

    private void getHumidity(String input) {
        String hymidity = match(input, "<i class=\"icon icon_humidity-white term__fact-icon\" aria-hidden=\"true\"></i>(..)%");
        if(hymidity != null) {
            this.hymidity = hymidity;
        }
    }

    private void getPressure(String input) {
        String pressure = match(input, "(...) <span class=\"fact__unit\">мм рт. ст.</span>");
        if(pressure != null) {
            this.pressure = pressure;
        }
    }

    private String match(String input, String regex) {
        String needed = null;
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            needed = matcher.group(1);
        }
        return needed;
    }
}
