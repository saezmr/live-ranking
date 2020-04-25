package an.dpr.liveranking;

import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import an.dpr.liveranking.model.Lap;
import an.dpr.liveranking.model.Ranking;
import an.dpr.liveranking.model.RankingItem;
import io.smallrye.reactive.messaging.annotations.Broadcast;

@ApplicationScoped
public class RankingGenerator {


    private Ranking ranking;

    @PostConstruct
    public void postConstruct() {
        ranking = new Ranking();
        ranking.setItems(new HashMap<Integer, RankingItem>());
    }

    @Incoming("laps")
    @Outgoing("ranking-stream")
    @Broadcast
    public Ranking addLap(Lap lap) {
        if (ranking == null) ranking = new Ranking();
        if (ranking.getCurrentLap() == null) ranking.setCurrentLap(1);
        RankingItem item = ranking.getItems().get(lap.getDorsal());
        if (item == null) {
            item = new RankingItem();
            item.setLap(ranking.getCurrentLap());
            item.setDorsal(lap.getDorsal());
            ranking.getItems().put(item.getDorsal(), item);
        } else {
            Integer currentLap = item.getLap()+1;
            if (currentLap.equals(ranking.getCurrentLap())) {
                item.setLap(currentLap);
            } else if (currentLap < ranking.getCurrentLap()) {
                item.setLapsLost(ranking.getCurrentLap()-currentLap);
                item.setLap(ranking.getCurrentLap());
            } else {
                item.setLap(currentLap);
                ranking.setCurrentLap(currentLap);
            }
        }

        return ranking;
    }
}