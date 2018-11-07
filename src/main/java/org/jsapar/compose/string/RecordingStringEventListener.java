package org.jsapar.compose.string;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Saves all lines that was composed into a list of list of strings that can be retrieved by calling {@link #getLines()}
 * at any time or when done composing.
 */
public class RecordingStringEventListener implements StringComposedEventListener {
    private List< List <String> > lines = new ArrayList<>();

    /**
     * @return All composed lines.
     */
    public List< List<String> > getLines() {
        return lines;
    }

    /**
     * Called every time that a bean, on root level, is successfully composed. Child lines do not generate events.
     * This implementation saves the composed bean into an internal list to be retrieved later by calling
     * {@link #getLines()}
     * @param event The event that contains the composed bean.
     */
    @Override
    public void stringComposedEvent(StringComposedEvent event) {
        lines.add(event.stream().collect(Collectors.toList()));
    }

    /**
     * Clears all recorded lines.
     */
    public void clear(){
        lines.clear();
    }

    /**
     * @return Number of lines recorded so far.
     */
    public int size(){
        return lines.size();
    }

    /**
     * @return True if there were no recorded lines, false otherwise.
     */
    public boolean isEmpty(){
        return lines.isEmpty();
    }
}
