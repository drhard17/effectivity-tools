package ru.yakovlev.effectivity.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.yakovlev.effectivity.exception.EffectivityException;

@Getter
@Setter
@EqualsAndHashCode
public class UnitRange {
    
    private @NonNull Integer start;
    
    private Integer end;
    
    public UnitRange(Integer start, Integer end) {
        if (end != null && (start == 0 || end == 0 || end < start)) {
            throw new EffectivityException("Error building range " + start + "-" + end);
        }
        this.start = start;
        this.end = end;
    }

    public boolean contains(UnitRange r) {
        return r.start >= this.start && (this.end == null
                || (r.end != null && (this.end >= r.end)));
    }
    
    // public boolean intersects(UnitRange r) {
    //     return (r.end == null || this.start < r.end)
    //             && (this.end == null || r.start < this.end);
    // }

    public boolean isOpenRange() {
        return this.end == null;
    }

    @Override
    public String toString() {
        return start == end ? Integer.toString(start)
                : end == null ? start + "-UP" : start + "-" + end;
    }
}
