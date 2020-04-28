package and.dpr.liveranking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import an.dpr.liveranking.RankingGenerator;
import an.dpr.liveranking.model.Lap;
import an.dpr.liveranking.model.Race;
import an.dpr.liveranking.model.Ranking;

@TestMethodOrder(OrderAnnotation.class)
public class RankingGeneratorTest {

    RankingGenerator generator;
    
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
        .race(2L)
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
}