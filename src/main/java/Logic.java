import java.util.List;

/**
 * The core logic class for the Football Bot.
 * Handles all text commands received from the user and processes the business rules.
 */
public class Logic {

    /** The repository responsible for storing user profiles. */
    private final UserRepository repository;

    /** The repository responsible for providing the list of valid RPL teams. */
    private final TeamsRepository teamsRepo = new TeamsRepository();

    /** The provider that handles match-related operations. */
    private final FootballDataProvider matchProvider;

    /**
     * Constructs the logic layer with the required user data repository.
     *
     * @param repository The storage mechanism for user profiles.
     */
    public Logic(UserRepository repository, FootballDataProvider matchProvider) {
        this.repository = repository;
        this.matchProvider = matchProvider;
    }

    /**
     * This method unites matches to a one string
     * @param matches List of matches
     * @return {@link String} a line formatted as a response for the user
     */
    private String formatMatches(List<Match> matches) {
        if (matches == null || matches.isEmpty()) {
            return "No matches found❌";
        }
        StringBuilder response = new StringBuilder();
        for (Match match : matches) {
            response.append(match.displayMatch()).append("\n\n");
        }
        return response.toString().trim();
    }

    /**
     * The main entry point for processing user commands.
     *
     * @param userId   The unique Telegram ID of the user sending the message.
     * @param userName The first name of the user.
     * @param command  The raw text message sent by the user.
     * @return String response to be sent back to the user.
     */
    public String handle(long userId, String userName, String command) {
        if (command == null) {
            return "Invalid command.";
        }

        if (command.equals("/start")) {
            return "Welcome " + userName + " to the Football Bot!\nUse /listOfTeams to see available teams, then /setteam <name> to pick your club!";
        }

        if (command.equals("/listOfTeams")) {
            return "🏆 Current Season Teams:\n" + String.join("\n", teamsRepo.getTeams());
        }

        if (command.startsWith("/setteam")) {
            String teamName = command.replace("/setteam", "").trim();
            if (teamName.isEmpty()) {
                return "Please specify a team name. Example: /setteam Зенит";
            }

            String matchedTeam = teamsRepo.getTeams().stream()
                    .filter(t -> t.equalsIgnoreCase(teamName))
                    .findFirst()
                    .orElse(null);

            if (matchedTeam == null) {
                return "❌ *" + teamName + "* is not in the league list! Check /listOfTeams.";
            }

            UserProfile user = repository.findById(userId);
            if (user == null) {
                user = new UserProfile(userId, userName);
            }
            user.setFavoriteTeam(matchedTeam);
            repository.save(user);
            return "⭐ Your favorite team is now: " + matchedTeam;
        }

        if (command.equals("/profile")) {
            UserProfile user = repository.findById(userId);
            if (user == null || user.getFavoriteTeam() == null) {
                return "👤 Profile Name: " + userName + "\n❤️ Favorite Team: None selected yet. Use /setteam to pick one!";
            }
            return "👤 Profile Name: " + user.getName() + "\n❤️ Favorite Team: " + user.getFavoriteTeam();
        }

        if (command.equals("/help")) {
            return "Available commands:\n/start\n/listOfTeams\n/setteam <team>\n/profile";
        }

        if ("/pastMatches".equals(command)) {
            try {
                List<Match> matches = matchProvider.getPastMatches(7);
                return formatMatches(matches);
            } catch (DataProviderException e) {
                return "The service is temporarily unavailable; please try again later.";
            }
        }

        if ("/upcomingMatches".equals(command)) {
            try {
                List<Match> matches = matchProvider.getUpcomingMatches(7);

                return formatMatches(matches);
            } catch (DataProviderException e) {
                return "The service is temporarily unavailable; please try again later.";
            }
        }

        if ("/pastMatchesForFT".equals(command)) {
            try {
                UserProfile user = repository.findById(userId);
                if (user == null || user.getFavoriteTeam() == null) {return "You have not selected a favorite team.\n" +
                        "Type /setFavoriteTeam <TeamName> o select favorite team";}
                List<Match> matches = matchProvider.getPastMatchesForFT(4, user.getFavoriteTeam());

                return formatMatches(matches);
            } catch (DataProviderException e) {
                return "The service is temporarily unavailable; please try again later.";
            }
        }

        if ("/upcomingMatchesForFT".equals(command)) {
            try {
                UserProfile user = repository.findById(userId);
                if (user == null || user.getFavoriteTeam() == null) {return "You have not selected a favorite team.\n" +
                        "Type /setFavoriteTeam <TeamName> no select favorite team";}
                List<Match> matches = matchProvider.getUpcomingMatchesForFT(4, user.getFavoriteTeam());

                return formatMatches(matches);
            } catch (DataProviderException e) {
                return "The service is temporarily unavailable; please try again later.";
            }
        }

        return "Unknown command. Type /help for options.";
    }
}