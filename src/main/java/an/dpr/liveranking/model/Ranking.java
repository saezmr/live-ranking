package an.dpr.liveranking.model;

import java.time.Instant;
import java.util.Map;

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
    private Instant startTime;
    private Map<Integer, RankingItem> items;
}