public class ProfileService {
    private final UserRepository repository;

    /*
     * @param repository
     */
    public ProfileService(UserRepository repository) {
        this.repository = repository;
    }

    /*
     * @param userId
     * @param userName
     * @param command
     * @return
     */
    public String handle(long userId, String userName,String command) {
        if (command.equals("/register") ) {
            if (repository.exists(userId))
                return "Already registered shit!!!";

                repository.save(new User(userId,userName));
                return "✅profile created baby!! 100 coins ✨ added🎆";

        }


        if (command.equals("/start")){
            return "Welcome" + " " + userName + " " + "to our betting company!! press /register to proceed.";
        }

        if (!repository.exists(userId)) {
            return "please /register first!!!";
        }
        User user = repository.findById(userId);

        if (command.equals("/profile")) {
            return "👤 profile name : " + " " + userName +"\n💰 Balance:"+ user.getBalance() + "coins ✨" + "\n⚽ Team:" + user.getFavoriteTeam();
        }

        if (command.startsWith("/setteam")) {
            String team = command.replace("/setteam   ", "");
            user.setFavoriteTeam(team);
            return "✅Favorite team set to:" + team;
        }

        return "I don't know that command,baby!!!";
    }
}