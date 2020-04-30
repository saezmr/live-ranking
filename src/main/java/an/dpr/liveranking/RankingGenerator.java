package an.dpr.liveranking;

import java.time.LocalTime;
import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import an.dpr.liveranking.model.Lap;
import an.dpr.liveranking.model.Race;
import an.dpr.liveranking.model.Ranking;
import an.dpr.liveranking.model.RankingItem;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import lombok.Getter;
import lombok.Setter;

@ApplicationScoped
public class RankingGenerator {
    @Setter
    @Getter
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
            int lostLaps = 0;
            if (race.getCurrentLap() > 1) {
                RankingItem item;
                int index = race.getCurrentLap()-2;
                do {
                    Ranking r2 = race.getRankings()[index--];
                    item = r2.getItems().get(lap.getDorsal());
                    if (item == null) lostLaps++;
                    else lostLaps+=item.getLostLaps();
                }
                while(item == null && index >= 0);
            } 
            // TODO also we have to calculate the position, based on lap lost or not
            ranking.addLap(lap, lostLaps);
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