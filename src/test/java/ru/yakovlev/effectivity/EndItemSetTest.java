package ru.yakovlev.effectivity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.yakovlev.effectivity.model.EndItem;
import ru.yakovlev.effectivity.model.EndItemSet;
import ru.yakovlev.effectivity.model.UnitRange;

public class EndItemSetTest {

    @Test
    public void endItemSetBuildingTest1() {

        EndItemSet e = new EndItemSet(EndItem.MC21);

        e.addUnitRange(new UnitRange(8,8));
        e.addUnitRange(new UnitRange(3,6));
        e.addUnitRange(new UnitRange(1,5));
        e.addUnitRange(new UnitRange(9,9));
        
        assertEquals("[1-6, 8-9] (MC21)", e.toString());
    }

    @Test
    public void endItemSetBuildingTest2() {

        EndItemSet e = new EndItemSet(EndItem.MC21);

        e.addUnitRange(new UnitRange(1,1));
        e.addUnitRange(new UnitRange(1,1));
        e.addUnitRange(new UnitRange(10,15));
        e.addUnitRange(new UnitRange(3,6));
        e.addUnitRange(new UnitRange(3,6));
        e.addUnitRange(new UnitRange(20,null));
        e.addUnitRange(new UnitRange(20,null));
        
        assertEquals("[1, 3-6, 10-15, 20-UP] (MC21)", e.toString());
    }

    @Test
    public void endItemSetBuildingTest3() {

        EndItemSet e = new EndItemSet(EndItem.MC21);

        e.addUnitRange(new UnitRange(1,5));
        e.addUnitRange(new UnitRange(4,null));
        e.addUnitRange(new UnitRange(10,null));
        e.addUnitRange(new UnitRange(100,100));
        
        assertEquals("[1-UP] (MC21)", e.toString());
    }

    @Test
    public void endItemSetBuildingTest4() {

        EndItemSet e = new EndItemSet(EndItem.MC21);

        e.addUnitRange(new UnitRange(20,null));
        e.addUnitRange(new UnitRange(1,5));
        e.addUnitRange(new UnitRange(6,8));
        e.addUnitRange(new UnitRange(10,10));
        e.addUnitRange(new UnitRange(10,13));
        e.addUnitRange(new UnitRange(17,17));
        
        assertEquals("[1-8, 10-13, 17, 20-UP] (MC21)", e.toString());
    }

    @Test
    public void endItemSetBuildingTest5() {

        EndItemSet e = new EndItemSet(EndItem.MC21);

        e.addUnitRange(new UnitRange(1,10));
        e.addUnitRange(new UnitRange(4,5));
        
        assertEquals("[1-10] (MC21)", e.toString());
    }

    @Test
    public void endItemSetBuildingTest6() {

        EndItemSet e = new EndItemSet(EndItem.MC21);

        e.addUnitRange(new UnitRange(1,10));
        
        assertEquals("[1-10] (MC21)", e.toString());
    }
}
