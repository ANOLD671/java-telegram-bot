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
                return "✅Hello " + userName + " your profile has been created!! 100 coins ✨ added🎆\n press /profile to view your profile baby!!\nThank you for choosing our service!!";

        }


        if (command.equals("/start")){
            return "Welcome" + " " + userName + " " + "to our betting company!! press /register to proceed.";
        }

        if (!repository.exists(userId)) {
            return "please /register first!!!";
        }
        User user = repository.findById(userId);


        if (command.startsWith("/setteam")) {
            String team = command.replace("/setteam", " ");
            user.setFavoriteTeam(team);
            return "✅Favorite team set to:" + team;
        }

        return "I don't know that command,baby!!!";
    }
}