package and.dpr.liveranking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import an.dpr.liveranking.RankingGenerator;
import an.dpr.liveranking.model.Lap;
import an.dpr.liveranking.model.Ranking;

@TestMethodOrder(OrderAnnotation.class)
public class RankingGeneratorTest {

    RankingGenerator generator;
    
    @BeforeEach
    public void beforeAll() {
        generator = new RankingGenerator();
        generator.postConstruct();
    }

    @Test
    @Order(10)
    public void should_generate_ranking() {
        Lap lap = generateLap(1);
        Ranking ranking = generator.addLap(lap);
        assertEquals(1, ranking.getCurrentLap(), "Should be lap nº1"); 
    }

    private Lap generateLap(Integer dorsal) {
        Lap lap = new Lap();
        lap.setDorsal(dorsal);
        lap.setInstant(Instant.now());
        return lap;
    }
    
    @Test
    @Order(30)
    public void should_be_ranking_lap2() {
        Lap lap = generateLap(1);
        Ranking ranking = generator.addLap(lap);
        lap = generateLap(1);
        ranking = generator.addLap(lap);
        assertEquals(2, ranking.getCurrentLap(), "Should be lap nº2"); 
    }
    
    @Test
    @Order(40)
    public void should_be_ranking_lap1_with_2_riders() {
        Lap lap = generateLap(1);
        Ranking ranking = generator.addLap(lap);
        lap = generateLap(2);
        ranking = generator.addLap(lap);
        assertEquals(1, ranking.getCurrentLap(), "Should be lap nº1"); 
        assertEquals(2, ranking.getItems().size(), "Should be lap with 2 riders"); 
    }
}