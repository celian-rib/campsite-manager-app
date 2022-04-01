package pt4.flotsblancs.scenes.utils;

import java.util.HashMap;

public class Timer {

    private HashMap<String,Long> starts = new HashMap<String,Long>();
    private long start = 0;
    private long end = 0;
    private StringBuilder sb = new StringBuilder();
    private static final String stamp = "[TIMER] - " ;

    public Timer() {
        sb.append(stamp);
    }

    public void start(String name) {
        starts.put(name, System.currentTimeMillis());
    }

    public void time(String name) {
        start = starts.get(name);
        end = System.currentTimeMillis();
        print(name);
    }

    private void print(String name) {
        System.out.println(stamp + name + getTime());
    }

    private String getTime() {
        return " - " + (end - start) + "ms";
    }
    
}
