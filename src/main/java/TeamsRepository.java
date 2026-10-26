import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository class responsible for loading and providing the list of valid football teams.
 */
public class TeamsRepository {

    /** The list of valid football teams. */
    private List<String> teams = new ArrayList<>();

    /**
     * Constructs the repository and loads the teams from the file.
     */
    public TeamsRepository() {
        loadTeamsFromFile();
    }

    /**
     * Reads the {@code teams.txt} file from resources and populates the teams list.
     */
    private void loadTeamsFromFile() {
        try (InputStream is = getClass().getResourceAsStream("/teams.txt")) {
            if (is == null) {
                System.err.println("❌ CRITICAL: teams.txt not found in resources folder!");
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                this.teams = reader.lines()
                        .map(String::trim)
                        .filter(line -> !line.isEmpty())
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the list of all loaded teams.
     *
     * @return A {@link List} of team names.
     */
    public List<String> getTeams() {
        return teams;
    }
}