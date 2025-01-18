package ru.yakovlev.effectivity;

import ru.yakovlev.effectivity.model.Effectivity;

import static ru.yakovlev.effectivity.service.EffectivityServiceImpl.parseJournalString;
import static ru.yakovlev.effectivity.service.EffectivityServiceImpl.parseTcString;
import static ru.yakovlev.effectivity.service.EffectivityServiceImpl.toJournalString;

public class DemoEffectivityApp {
    public static void main(String[] args) throws Exception {

        Effectivity eff1 = parseTcString("11, 13-UP (MC-21) 990151, 99012 (ST-21)");
        Effectivity eff2 = parseTcString("1, 3, 4 (MC-21) 99011 (ST-21)");
        Effectivity eff3 = parseTcString("99001 (ST-21) 1-3, 6-UP (MC-21)");
        Effectivity eff4 = parseTcString("11 (MC-21) 990151, 99012, 990151 (ST-21)");
        Effectivity eff5 = parseJournalString("0001; 99012; 0002; 0003; 0012");
        Effectivity eff6 = parseJournalString("99012; 0001; 0013-UP");

        System.out.println(toJournalString(eff1));
        System.out.println(toJournalString(eff2));
        System.out.println(toJournalString(eff3));
        System.out.println(toJournalString(eff4));
        System.out.println(eff1.contains(eff4));
        System.out.println(eff3.contains(eff5));
        System.out.println(eff5.contains(eff4));
        System.out.println(eff5.contains(eff6));

        Effectivity eff10 = parseJournalString("0001; 0002; 0003-UP");
        Effectivity eff11 = parseJournalString("0001-UP");
        System.out.println(eff10.equals(eff11));

    }
}
