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

	public void addLap(Lap lap) {
        addLap(lap, 0);
    }

	public void addLap(Lap lap, Integer lostLaps) {
        RankingItem item;
        if (firstLap()) {
            items = new HashMap<Integer, RankingItem>();
        }
        item = RankingItem.builder().dorsal(lap.getDorsal())
        .instant(lap.getInstant()).lap(currentLap)
        .position(calculatePosition(lostLaps))
        .diffFirst(calculateDiffFirst(lap))
        .lostLaps(lostLaps)
        .diffPosition(null) // TODO
        .lapTime(null) // TODO
        .build();
        items.put(lap.getDorsal(), item);
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