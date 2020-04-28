package an.dpr.liveranking.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ranking {

    private Integer rankingLap;
    /**
     * first rider in this lap time
     */
    private Instant lapTime;
    private Integer lapsRemaining;
    private Map<Integer, RankingItem> items;
    private Boolean finished;

	public void addLap(Lap lap) {
        RankingItem item;
        if (firstLap()) {
            items = new HashMap<Integer, RankingItem>();
            item = RankingItem.builder().dorsal(lap.getDorsal())
            .instant(lap.getInstant()).lap(rankingLap).position(1).build();
        } else {
            item = RankingItem.builder().dorsal(lap.getDorsal())
            .instant(lap.getInstant()).lap(rankingLap).position(calculatePosition())
            .diffFirst(calculateDiffFirst(lap))
            .diffPosition(null) // TODO
            .lapTime(null) // TODO
            .lapsLost(null) // TODO
            .build();
        }
        items.put(lap.getDorsal(), item);
    }
    
    private Integer calculatePosition() {
		return items.size()+1; //TODO rebuild this logic to take in accoutn instant, not order.
    }
    
    private long calculateDiffFirst(Lap lap) {
        RankingItem item = getFirstItem().get();
        return item.getInstant().until(lap.getInstant(), ChronoUnit.MILLIS);
    }

    private Optional<RankingItem> getFirstItem() {
        return items.entrySet().stream()
        .filter(v -> v.getValue().getPosition().equals(1))
        .map(v -> v.getValue())
        .findFirst();
    }

	private boolean firstLap() {
        return items == null;
    }
}