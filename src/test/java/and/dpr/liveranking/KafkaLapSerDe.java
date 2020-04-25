package and.dpr.liveranking;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import an.dpr.liveranking.kafka.LapDeserializer;
import an.dpr.liveranking.kafka.LapSerializer;
import an.dpr.liveranking.model.Lap;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class KafkaLapSerDe {

    @Test
    public void shouldSerialize() {
        LapSerializer serializer = new LapSerializer();
        LapDeserializer deserializer = new LapDeserializer();
        Lap lap = Lap.builder().dorsal(1).instant(Instant.now()).build();
        byte[] serialized = serializer.serialize("lap", lap);
        Lap lapDeserialized = deserializer.deserialize("lap", serialized);
        assertEquals(lap, lapDeserialized, "Lap is not equals, SerDe doesnt run properly");
        serializer.close();
        deserializer.close();
    }
}