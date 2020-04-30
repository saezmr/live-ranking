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

    private Integer currentLap;
    private Instant lapTime;
    private Integer lapsRemaining;
    private Map<Integer, RankingItem> items;
    private int[] $nextPosition;//exclude from lombok
    private Ranking lastRanking;

	public void addLap(Lap lap) {
        addLap(lap, 0);
    }

	public void addLap(Lap lap, Integer lostLaps) {
        RankingItem item;
        if (firstLap()) {
            items = new HashMap<Integer, RankingItem>();
        }
        Integer position = calculatePosition(lostLaps);
        item = RankingItem.builder().dorsal(lap.getDorsal())
        .instant(lap.getInstant()).lap(currentLap)
        .position(position)
        .diffFirst(calculateDiffFirst(lap))
        .lostLaps(lostLaps)
        .diffPosition(calculatePositionChanges(lap.getDorsal(), position, lostLaps))
        .lapTime(null) // TODO
        .build();
        items.put(lap.getDorsal(), item);
    }
    
    /**
     * With lostLap riders is not possible to calculate position changes.
     * @param dorsal
     * @param position
     * @return
     */
    private Integer calculatePositionChanges(Integer dorsal, Integer position, Integer lostLaps) {
        if (lastRanking == null || lostLaps > 0) return 0;
        RankingItem item = lastRanking.getItems().get(dorsal);
        return item != null ? item.getPosition() - position : null;
	}

	/**
     * To take in account:
     *  - first no lost laps riders
     *  - then 1 lost laps riders
     *  - then 2 lost...
     *  - ...
     * 
     * We need to recalculate (instant) or maintain status 
     * (currentlap_next_position, lostlap1_next_position etc...)
     * TODO refactor: with last lap riders is not possible to know the position until the
     * finish of the lap, since we don't know new lost lap riders, adbandons etc...
     * @param lostLaps
     * @return
     */
    private Integer calculatePosition(Integer lostLaps) {
        if ($nextPosition == null) {
            $nextPosition = new int[currentLap];
        }
        //TODO rebuild this logic to take in accoutn instant and lost laps, not order.
        int position = ++$nextPosition[lostLaps];
        return position;
    }
    
    private long calculateDiffFirst(Lap lap) {
        return getFirstItem()
        .map(i -> i.getInstant().until(lap.getInstant(), ChronoUnit.MILLIS))
        .orElse(0l);
    }

    private Optional<RankingItem> getFirstItem() {
        return items.size() == 0 ? Optional.empty() : items.entrySet().stream()
        .filter(v -> v.getValue().getPosition().equals(1))
        .map(v -> v.getValue())
        .findFirst();
    }

	private boolean firstLap() {
        return items == null;
    }
}