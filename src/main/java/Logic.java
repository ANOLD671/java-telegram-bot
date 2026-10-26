public class Logic {
    private final UserRepository repository;
    private final TeamsRepository teamsRepo = new TeamsRepository();

    public Logic(UserRepository repository) {
        this.repository = repository;
    }

    public String handle(long userId, String userName, String command) {
        if (command == null) return "Invalid command.";

        if (command.equals("/start")) {
            return "Welcome " + userName + " to the Football Bot!\nUse /listOfTeams to see available teams, then /setteam <name> to pick your club!";
        }

        if (command.equals("/listOfTeams")) {
            return "🏆 Current Season Teams:\n" + String.join("\n", teamsRepo.getTeams());
        }

        if (command.startsWith("/setteam")) {
            String teamName = command.replace("/setteam", "").trim();
            if (teamName.isEmpty()) return "Please specify a team name. Example: /setteam Зенит";

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

        return "Unknown command. Type /help for options.";
    }
}