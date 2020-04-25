package an.dpr.liveranking.kafka;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import an.dpr.liveranking.model.Lap;

public class LapDeserializer implements Deserializer<Lap>{

  private static final Logger logger = LoggerFactory.getLogger(LapDeserializer.class);

	@Override
	public Lap deserialize(String arg0, byte[] deserializedLap) {
        Lap lap = null;
        try {
            
            Jsonb jsonb = JsonbBuilder.create();
            String deserializedString = new String(deserializedLap);
            lap = jsonb.fromJson(deserializedString, Lap.class);
        } catch (Exception e) {
            logger.error("Deserialize error", e);
        }
        return lap;
	}

}