package and.dpr.liveranking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import an.dpr.liveranking.RankingGenerator;
import an.dpr.liveranking.model.Lap;
import an.dpr.liveranking.model.Race;

@TestMethodOrder(OrderAnnotation.class)
public class RankingGeneratorTest {

    RankingGenerator generator;

    private final static Long RACE_CODE = 2L;
    
    @BeforeEach
    public void beforeAll() {
        generator = new RankingGenerator();
    }

    @Test
    public void should_generate_ranking() {
        Lap lap = generateLap(1);
        Race race = generator.addLap(lap);
        assertEquals(1, race.getCurrentLap(), "Should be lap nº1"); 
    }
    
    private Lap generateLap(Integer dorsal) {
        return Lap.builder()
        .dorsal(dorsal)
        .instant(Instant.now())
        .race(RACE_CODE)
        .build();
    }
    
    @Test
    public void should_be_race_lap2() {
        Lap lap = generateLap(1);
        Race race = generator.addLap(lap);
        lap = generateLap(1);
        race = generator.addLap(lap);
        assertEquals(2, race.getCurrentLap(), "Should be lap nº2"); 
    }
    
    @Test
    public void should_be_race_lap1_with_several_riders() {
        Race race = null;
        for (int i=1; i < 10 ; i++) {
            race = generator.addLap(generateLap(i));
        }
        assertEquals(1, race.getCurrentLap(), "Should be lap nº1"); 
        assertEquals(9, race.getRankings()[0].getItems().size(), "Should be lap with 9 riders"); 
    }

    @Test
    public void should_be_complete_race() {
        Race race = Race.builder().code(RACE_CODE).totalLaps(4).build();
        generator.setRace(race);
        Integer[] laps = {1,2,3,4,5,6, 1,2,3,4,5,6, 2,1,3,4,5,6, 2,3,1,6,4,5};
        Arrays.stream(laps).forEach(d -> generator.addLap(generateLap(d)));
        race = generator.getRace();
        assertEquals(4, race.getCurrentLap(), "Should be lap nº4"); 
        assertEquals(6, race.getRankings()[0].getItems().size(), "Should be lap with 6 riders");
        assertEquals(6, race.getRankings()[1].getItems().size(), "Should be lap with 6 riders");
        assertEquals(6, race.getRankings()[2].getItems().size(), "Should be lap with 6 riders");
        assertEquals(6, race.getRankings()[3].getItems().size(), "Should be lap with 6 riders");
    }

    @Test
    public void should_be_lost_lap() {
        Race race = Race.builder().code(RACE_CODE).totalLaps(5).build();
        generator.setRace(race);
        Integer[] laps = {1,2,3,4,5,6, 1,2,3,4,5, 2,6,1,3,4,5, 2,1,4,3, 2,6,5,3,1,4};
        Arrays.stream(laps).forEach(d -> generator.addLap(generateLap(d)));
        race = generator.getRace();
        assertEquals(5, race.getCurrentLap(), "Should be lap nº3"); 
        assertEquals(6, race.getRankings()[0].getItems().size(), "Should be lap with 6 riders");
        assertEquals(5, race.getRankings()[1].getItems().size(), "Should be lap with 5 riders(one lost lap)");
        assertEquals(6, race.getRankings()[2].getItems().size(), "Should be lap with 6 riders");
        assertEquals(1, race.getRankings()[2].getItems().get(6).getLostLaps(), "rider 6 must lost 1 lap");
        assertEquals(2, race.getRankings()[4].getItems().get(6).getLostLaps(), "rider 6 must lost 2 lap");
        assertEquals(1, race.getRankings()[4].getItems().get(5).getLostLaps(), "rider 5 must lost 1 lap");
    }
    
    @Test
    public void should_has_position_ok_first_lap() {
        Race race = Race.builder().code(RACE_CODE).totalLaps(4).build();
        generator.setRace(race);
        Integer[] laps = {6,5,4,3,2,1, 1,2,3,4,5,6, 1,2,3,4, 3,2,6,1,4,5};
        Arrays.stream(laps).forEach(d -> generator.addLap(generateLap(d)));
        race = generator.getRace();
        for(int dorsal = 1; dorsal<7; dorsal++) {
            assertEquals(7-dorsal, race.getRankings()[0].getItems().get(dorsal).getPosition());
            assertEquals(dorsal, race.getRankings()[1].getItems().get(dorsal).getPosition());
        }
        assertEquals(1, race.getRankings()[3].getItems().get(3).getPosition());
        assertEquals(2, race.getRankings()[3].getItems().get(2).getPosition());
        assertEquals(3, race.getRankings()[3].getItems().get(1).getPosition());
        assertEquals(4, race.getRankings()[3].getItems().get(4).getPosition());
        //TODO we are assert first of the lostlap riders, but would be better the actual position
        assertEquals(1, race.getRankings()[3].getItems().get(6).getPosition());
        assertEquals(2, race.getRankings()[3].getItems().get(5).getPosition());

    }

    //desdoblarse

}