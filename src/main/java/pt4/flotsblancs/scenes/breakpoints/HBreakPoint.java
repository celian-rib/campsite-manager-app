package pt4.flotsblancs.scenes.breakpoints;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum HBreakPoint {
    LARGE(1600), MEDIUM(1200), SMALL(900), NONE(Integer.MAX_VALUE);

    @Getter
    private int width;
}
