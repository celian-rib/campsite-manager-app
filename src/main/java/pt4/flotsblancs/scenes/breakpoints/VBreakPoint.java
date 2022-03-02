package pt4.flotsblancs.scenes.breakpoints;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum VBreakPoint {
    LARGE(1100), MEDIUM(800), SMALL(500), NONE(Integer.MAX_VALUE);

    @Getter
    private int height;
}
