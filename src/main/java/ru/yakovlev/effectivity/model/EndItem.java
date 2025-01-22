package ru.yakovlev.effectivity.model;

import java.util.Arrays;
import ru.yakovlev.effectivity.exception.EffectivityException;

public enum EndItem {
    MC21("MC-21", "MC21", "МС-21", "МС21") {

        final String journalPattern = "0\\d{2}[1-9](-UP)?";

        final String tcId = "MC-21";

        @Override
        String getTCid() {
            return tcId;
        }

        @Override
        public String getJournalPattern() {
            return journalPattern;
        }
    },

    ST21("ST-21", "ST21") {

        final String journalPattern = "99\\d{3,4}";

        final String tcId = "ST-21";

        @Override
        String getTCid() {
            return tcId;
        }

        @Override
        public String getJournalPattern() {
            return journalPattern;
        }
    },

    YAK152("152", "YAK152", "YAK-152", "Як-152", "Як152") {

        final String journalPattern = "1520\\d{3}";

        final String tcId = "YAK-152";

        @Override
        String getTCid() {
            return tcId;
        }

        @Override
        public String getJournalPattern() {
            return journalPattern;
        }
    };

    private String[] aliases;

    private EndItem(String... aliases) {
        this.aliases = aliases;
    }

    public static EndItem fromAlias(String alias) {
        return Arrays.stream(EndItem.values())
                .filter(endItem -> Arrays.stream(endItem.aliases)
                            .anyMatch(a -> a.equalsIgnoreCase(alias)))
                .findFirst()
                .orElseThrow(
                    () -> new EffectivityException("Enditem recognition error - " + alias));
    }

    public abstract String getJournalPattern();

    abstract String getTCid();

}
