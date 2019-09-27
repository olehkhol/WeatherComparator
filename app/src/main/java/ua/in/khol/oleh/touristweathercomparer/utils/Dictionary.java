package ua.in.khol.oleh.touristweathercomparer.utils;

import java.util.Arrays;
import java.util.List;

public final class Dictionary {

    private static final String[] languages = {"en", "uk", "ru"};

    private static final List<String> sentences = Arrays.asList(
            // Yahoo
            "tornado", "торнадо", "торнадо",
            "tropical storm", "тропічний шторм", "тропическая буря",
            "hurricane", "ураган", "ураган",
            "severe thunderstorms", "сильні грози", "сильные грозы",
            "thunderstorms", "грози", "грозы",
            "mixed rain and snow", "змішаний дощ і сніг", "смешанный дождь и снег",
            "mixed rain and sleet", "змішаний дощ і мокрий сніг", "смешанный дождь и мокрый снег",
            "mixed snow and sleet", "змішаний сніг і мокрий сніг", "смешанный снег и мокрый снег",
            "freezing drizzle", "ожеледь", "изморозь",
            "drizzle", "мжичка", "изморось",
            "freezing rain", "крижаний дощ", "ледяной дождь",
            "showers", "зливи", "ливни",
            "rain", "дощ", "дождь",
            "snow flurries", "сніговий шум", "порывы снега",
            "light snow showers", "легкий сніг", "небольшой снегопад",
            "blowing snow", "буревій", "позёмок",
            "snow", "сніг", "снег",
            "hail", "град", "град",
            "sleet", "мокрий сніг", "дождь со снегом",
            "dust", "пил", "пыль",
            "foggy", "імла", "мгла",
            "haze", "туман", "туман",
            "smoky", "серпанок", "дымка",
            "blustery", "погана погода", "ненастная погода",
            "windy", "вітряно", "ветрено",
            "cold", "холодно", "холодно",
            "cloudy", "хмарно", "облачно",
            "mostly cloudy", "переважно хмарно", "в основном облачно",
            "partly cloudy", "мінлива хмарність", "переменная облачность",
            "clear", "ясно", "ясно",
            "sunny", "сонячно", "солнечно",
            "fair", "ясно", "ясно",
            "mixed rain and hail", "змішаний дощ і град", "смешанный дождь и град",
            "hot", "жарко", "жарко",
            "isolated thunderstorms", "локальні грози", "локальные грозы",
            "scattered thunderstorms", "розсіяні грози", "рассеянные грозы",
            "scattered showers", "розсіяні зливи", "рассеянные ливни",
            "heavy rain", "злива", "ливень",
            "scattered snow showers", "розсіяний снігопад", "рассеянный снегопад",
            "heavy snow", "снігопад", "снегопад",
            "blizzard", "хуртовина", "метель",
            "mostly sunny", "переважно сонячно", "в основном солнечно",
            // Dark Sky
            "clear day", "ясний день", "ясный день",
            "clear night", "ясна ніч", "ясная ночь",
            "cloudy", "хмарно", "облачно",
            "fog", "туман", "туман",
            "partly cloudy day", "мінлива хмарність", "переменная облачность",
            "partly cloudy night", "мінлива хмарність", "переменная облачность",
            "rain", "дощ", "дождь",
            "sleet", "мокрий сніг", "дождь со снегом",
            "snow", "сніг", "снег",
            "wind", "вітер", "ветер",
            // WWO
            "sunny", "сонячно", "солнечно",
            "partly cloudy", "мінлива хмарність", "Переменная облачность",
            "cloudy", "хмарно", "облачный",
            "overcast", "Похмуро", "пасмурная",
            "mist", "туман", "туман",
            "patchy rain possible", "можливий невеликий дощ", "возможен небольшой дождь",
            "patchy snow possible", "можливий невеликий сніг", "возможен небольшой сніг",
            "patchy sleet possible", "можлива сльота", "возможна слякоть",
            "patchy freezing drizzle possible", "місцями можливі морози", "местами возможны морозы",
            "thundery outbreaks possible", "можливі грозові спалахи", "возможны грозовые вспышки",
            "blowing snow", "буревій", "позёмок",
            "blizzard", "хуртовина", "метель",
            "fog", "туман", "туман",
            "freezing fog", "крижаний туман", "ледяной туман",
            "patchy light drizzle", "невеликий дощ", "легкий дождь",
            "light drizzle", "легкий дощ", "мелкий дождь",
            "freezing drizzle", "ожеледь", "изморозь",
            "heavy freezing drizzle", "сильний мороз", "сильный мороз",
            "patchy light rain", "невеликий дощ", "легкий дождь",
            "light rain", "невеликий дощ", "легкий дождь",
            "moderate rain at times", "помірний дощ", "умеренный дождь",
            "moderate rain", "помірний дощ", "умеренный дождь",
            "heavy rain at times", "час від часу сильний дощ", "временами сильный дождь",
            "heavy rain", "сильний дощ", "ливень",
            "light freezing rain", "легкий морозний дощ", "легкий морозный дождь",
            "moderate or heavy rain shower", "помірний або сильний дощ", "умеренный или сильный ливневый дождь",
            "patchy light rain with thunder", "місцями невеликий дощ з грозою", "местами легкий дождь с грозой",
            "shower in vicinity", "дощ поблизу", "дождь рядом",
            "light rain shower", "легкий дощ", "легкий дождь"
    );

    public static String translate(String text, String language) {
        if (text != null) { // check why this happens
            int languageIndex = getLanguageIndex(language);
            String spaced = text.replace('-', ' ');
            String lowerCased = spaced.toLowerCase();
            String trimmed = lowerCased.trim();
            int sentenceIndex = sentences.indexOf(trimmed);

            if (sentenceIndex != -1)
                return capitalize(sentences.get(sentenceIndex + languageIndex));
        }
        return text;
    }

    private static int getLanguageIndex(String language) {
        for (int i = 0; i < languages.length; i++)
            if (languages[i].equals(language))
                return i;

        return -1;
    }

    public static String capitalize(String input) {
        StringBuilder text = new StringBuilder();
        String[] array = input.split(" ");

        for (String word : array) {
            text.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1)).append(" ");
        }

        return text.toString();
    }
}
