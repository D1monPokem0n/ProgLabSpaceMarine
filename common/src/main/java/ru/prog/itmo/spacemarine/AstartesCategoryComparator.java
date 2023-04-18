package ru.prog.itmo.spacemarine;

import java.util.Comparator;

public class AstartesCategoryComparator implements Comparator<AstartesCategory> {
    @Override
    public int compare(AstartesCategory first, AstartesCategory second) {
        if (first == null & second == null)
            return 0;
        if (first != null & second == null)
            return 1;
        if (first == null)
            return -1;
        return first.compareTo(second);
    }
}
