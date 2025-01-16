package ru.yakovlev.effectivity;

import static ru.yakovlev.effectivity.Effectivity.parseJournalString;

public class DemoEffectivityApp {
    public static void main(String[] args) throws Exception {

        Effectivity eff1 = Effectivity.parseTcString("11, 13-UP (MC-21) 990151, 99012 (ST-21)");
        Effectivity eff2 = Effectivity.parseTcString("1, 3, 4 (MC-21) 99011 (ST-21)");
        Effectivity eff3 = Effectivity.parseTcString("99001 (ST-21) 1-3, 6-UP (MC-21)");
        Effectivity eff4 = Effectivity.parseTcString("11 (MC-21) 990151, 99012, 990151 (ST-21)");
        Effectivity eff5 = Effectivity.parseJournalString("0001; 99012; 0002; 0003; 0012");
        Effectivity eff6 = Effectivity.parseJournalString("99012; 0001; 0013-UP");

        System.out.println(eff1.toJournalString());
        System.out.println(eff2.toJournalString());
        System.out.println(eff3.toJournalString());
        System.out.println(eff4.toJournalString());
        System.out.println(eff1.contains(eff4));
        System.out.println(eff3.contains(eff5));
        System.out.println(eff5.contains(eff4));
        System.out.println(eff5.contains(eff6));

        Effectivity eff10 = parseJournalString("0001; 0002; 0003-UP");
        Effectivity eff11 = parseJournalString("0001-UP");
        System.out.println(eff10.equals(eff11));

    }
}
