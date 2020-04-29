package an.dpr.liveranking.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingItem {

    private Integer dorsal;
    private Integer lap;
    private Integer position;
    private Integer diffPosition;
    private Instant instant;
    private Long diffFirst;
    private Integer lostLaps;
    private Instant lapTime;
}