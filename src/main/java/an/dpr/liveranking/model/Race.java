package an.dpr.liveranking.model;

import java.time.LocalTime;
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
public class Race {

    private Long code;
    private Integer totalLaps;
    private Integer currentLap;
    private String name;
    private String info;
    private LocalTime date;
    private Ranking[] rankings;

	public Ranking generateRanking(Lap lap) {
        Ranking lastRanking = null;
        if (rankings == null) rankings = new Ranking[totalLaps];
        if (currentLap == null) {
            currentLap = 0;
        } else {
            lastRanking = rankings[currentLap-1];
        }
        rankings[++currentLap -1] = Ranking.builder()
        .currentLap(currentLap)
        .lapTime(lap.getInstant())
        .lapsRemaining(this.getTotalLaps()-currentLap)
        .lastRanking(lastRanking).build();
        return rankings[currentLap-1];
    } 

	public boolean needMoreRankings(Lap lap) {
        return rankings == null || 
            rankings.length == 0 || 
            Optional.ofNullable(getCurrentRanking()).stream()
            .map(r -> r.getItems())
            .filter(item -> item != null)
            .anyMatch(item -> item.get(lap.getDorsal())!=null);
	}

	public Ranking getCurrentRanking() {
		return rankings[currentLap-1];
	}
}