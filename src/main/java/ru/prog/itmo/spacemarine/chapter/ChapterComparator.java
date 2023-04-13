package ru.prog.itmo.spacemarine.chapter;

import java.util.Comparator;

public class ChapterComparator implements Comparator<Chapter> {
    @Override
    public int compare(Chapter first, Chapter second){
        if (first == null & second == null)
            return 0;
        if (first != null & second == null)
            return 1;
        if (first == null)
            return -1;
        return first.compareTo(second);
    }
}
