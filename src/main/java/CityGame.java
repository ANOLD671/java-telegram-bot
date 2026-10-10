import java.util.ArrayList;
import java.util.List;


public class CityGame {
    private boolean isGamestarted = false;
    private List<String> usedCities = new ArrayList<>();
    private char requiredLetter = 'w';
    private String[] dictionary = {"washington", "nairobi", "oslo", "ottawa"};

    public String handleInput(String input) {
        if (input.equals("/start")) {
            isGamestarted = true;
            usedCities.clear();
            requiredLetter = 'w';
            return "Welcome to Cities game! I'll start: Moscow. Give me a city starting with 'w'. Your turn!";
        }

        if (!isGamestarted) {
            return "I don't understand that. Type /start";
        }

        String city = input.toLowerCase();

        if (city.charAt(0) != requiredLetter) {
            return "Wrong letter! You need a city starting with " + requiredLetter;
        }
        if (usedCities.contains(city)) {
            return "Hey! That city was already used. Try another one!";
        }

        usedCities.add(city);
        char userLastLetter = city.charAt(city.length() - 1);

        for (String dictCity : dictionary) {
            if (dictCity.charAt(0) == userLastLetter && !usedCities.contains(dictCity)) {
                usedCities.add(dictCity);
                requiredLetter = dictCity.charAt(dictCity.length() - 1);
                return "I say: " + dictCity + ". Your turn! Give me a city starting with " + requiredLetter;
            }
        }

        return "I give up! You win!";
    }
}