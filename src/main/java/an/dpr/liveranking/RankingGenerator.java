package an.dpr.liveranking;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import an.dpr.liveranking.model.Lap;
import io.smallrye.reactive.messaging.annotations.Broadcast;

@ApplicationScoped
public class RankingGenerator {

    @Incoming("laps")
    @Outgoing("ranking-stream")
    @Broadcast
    public Lap generateRanking(Lap lap) {
        return lap;
    }
}