package an.dpr.liveranking;

import java.time.Duration;
import java.time.Instant;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Outgoing;

import an.dpr.liveranking.model.Lap;
import io.smallrye.mutiny.Multi;

/**
 * Simulates laps. In a real scenario would be a chip program
 */
@ApplicationScoped
public class LapsProducer {

    @Outgoing("generated-lap")
    public Multi<Lap> generateLaps() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(1))
        .onItem().apply(n->
            Lap.builder().instant(Instant.now()).dorsal(LapsProducer.randomDorsal()).build());
    }
    
    private static Integer randomDorsal() {
        double ran = Math.random()*10;
        System.out.println(ran);
        return Double.valueOf(ran).intValue();
    }
}