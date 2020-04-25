package an.dpr.liveranking.kafka;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.apache.kafka.common.serialization.Serializer;

import an.dpr.liveranking.model.Lap;

public class LapSerializer implements Serializer<Lap> {

	@Override
	public byte[] serialize(String arg0, Lap lap) {
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.toJson(lap).getBytes();
    }

}