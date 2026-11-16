import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of a `FootballDataProvider` stub for unit testing.
 * Returns static, predefined objects. Mapping is performed without network access.
 */
public class MockFootballProvider implements FootballDataProvider {
    @Override
    public List<Match> getPastMatches(Integer limit) {
        Match m1 = new Match("ЦСКА", "Спартак Москва", LocalDateTime.of(2025,4,25,19,0),2,2);
        Match m2 = new Match("Локомотив Москва", "Зенит", LocalDateTime.of(2026,3,12,20,30),0,0);
        Match m3 = new Match("Краснодар", "Факел", LocalDateTime.of(2026,10,1,17,45),3,0);
        return Arrays.asList(m1,m2,m3);
    }

    @Override
    public List<Match> getUpcomingMatches(Integer limit) {
        Match m1 = new Match("Динамо Москва", "Спартак Москва", LocalDateTime.of(2025,4,25,19,0),null,null);
        Match m2 = new Match("Локомотив Москва", "Зенит", LocalDateTime.of(2026,3,12,20,30),null,null);
        Match m3 = new Match("Краснодар", "Факел", LocalDateTime.of(2026,10,1,17,45),null,null);
        return Arrays.asList(m1,m2,m3);
    }

    @Override
    public List<Match> getPastMatchesForFT(Integer limit, String teamName) {
        Match m1 = new Match("Динамо Москва", "Спартак Москва", LocalDateTime.of(2025,4,25,19,0),0,0);
        Match m2 = new Match("Динамо Москва", "Зенит", LocalDateTime.of(2026,3,12,20,30),1,1);
        Match m3 = new Match("Динамо Москва", "Факел", LocalDateTime.of(2026,10,1,17,45),2,2);
        return Arrays.asList(m1,m2,m3);
    }

    @Override
    public List<Match> getUpcomingMatchesForFT(Integer limit, String teamName) {
        Match m1 = new Match("Динамо Москва", "Спартак Москва", LocalDateTime.of(2025,4,25,19,0),null,null);
        Match m2 = new Match("Динамо Москва", "Зенит", LocalDateTime.of(2026,3,12,20,30),null,null);
        Match m3 = new Match("Динамо Москва", "Факел", LocalDateTime.of(2026,10,1,17,45),null,null);
        return Arrays.asList(m1,m2,m3);
    }
}
