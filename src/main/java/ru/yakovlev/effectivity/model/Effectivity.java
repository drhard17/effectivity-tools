package ru.yakovlev.effectivity.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Класс для хранения и операций с применяемостью - набором серийных номеров конечных изделий
 */

public class Effectivity {

    private final List<EndItemSet> endItemSets;

    public Effectivity() {
        this.endItemSets = new ArrayList<>();
    }

    public Effectivity(List<EndItemSet> endItemSets) {
        this.endItemSets = endItemSets;
    }

    public List<EndItemSet> getEndItemSets() {
        return endItemSets;
    }

    /**
     * Проверяет вхождение Effectivity в Effectivity
     * @param effectivity
     * @return
     */

    public boolean contains(Effectivity effectivity) {
        return effectivity.endItemSets.stream().
                allMatch(this::containsEndItemSet);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Effectivity effectivity = (Effectivity) obj;
        return this.endItemSets.containsAll(effectivity.endItemSets) &&
                effectivity.endItemSets.containsAll(this.endItemSets);
    }
    
    private boolean containsEndItemSet(EndItemSet endItemSet) {
        return this.endItemSets.stream()
                .anyMatch(set -> 
                        set.endItem == endItemSet.endItem && 
                        set.containsSet(endItemSet));
    }
    

}


  
