package an.dpr.liveranking;

import java.time.LocalTime;
import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import an.dpr.liveranking.model.Lap;
import an.dpr.liveranking.model.Race;
import an.dpr.liveranking.model.Ranking;
import io.smallrye.reactive.messaging.annotations.Broadcast;

@ApplicationScoped
public class RankingGenerator {

    private Race race; // FIXME it should be a list of races to allow multirace in same service

    @Incoming("laps")
    @Outgoing("ranking-stream")
    @Broadcast
    public Race addLap(Lap lap) {
        Race race = getRace(lap.getRace());
        if (race == null) return null; // FIXME maybe is better to throw any exception
        if (race.needMoreRankings(lap)) {
            Ranking ranking = race.generateRanking(lap);
            ranking.addLap(lap);
        } else {
            Ranking ranking = race.getCurrentRanking();
            ranking.addLap(lap);
        }
        
        return race;
    }



	private Race getRace(Long raceCode) {
        if (race == null) {
            race = Race.builder().code(raceCode).date(LocalTime.now())
            .currentLap(0).totalLaps(7).name("katapin race").build();
        }
        if (race.getCode().equals(raceCode)) {
            return race;
        } else {
            return null;
        }
	}
}