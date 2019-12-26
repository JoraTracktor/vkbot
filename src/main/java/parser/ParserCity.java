package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserCity {
    public String getCityUrl(String cityName){
        String cityUrl = null;
        String city = null;
        try {
            city = "https://yandex.ru/pogoda/search?request=" + URLEncoder.encode(cityName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(city);
        String input;

        Pattern pattern = Pattern.compile("<a class=\"link place-list__item-name i-bem\" tabindex=\"0\" href=\"(.*?)\" data-bem=");
        Matcher matcher;

        try {
            URL url = new URL(city);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            while((input = in.readLine()) != null) {

                matcher = pattern.matcher(input);
                if (matcher.find()) {
                    cityUrl = matcher.group(1);
                }
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cityUrl;
    }
}
