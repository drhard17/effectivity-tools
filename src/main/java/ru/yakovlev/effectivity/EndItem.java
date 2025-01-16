package ru.yakovlev.effectivity;

import java.util.Arrays;

enum EndItem {
    MC21("MC-21", "MC21", "МС-21", "МС21") {

        final String journalPattern = "0\\d{3}";

        final String tcId = "MC-21";

        @Override
        String getTCid() {
            return tcId;
        }

        @Override
        String getJournalPattern() {
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
        String getJournalPattern() {
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
        String getJournalPattern() {
            return journalPattern;
        }
    };

    private String[] aliases;

    private EndItem(String... aliases) {
        this.aliases = aliases;
    }

    static EndItem fromAlias(String alias) {
        return Arrays.stream(EndItem.values())
                .filter(endItem -> Arrays.stream(endItem.aliases)
                            .anyMatch(a -> a.equalsIgnoreCase(alias)))
                .findFirst()
                .orElseThrow(
                    () -> new IllegalArgumentException("Enditem recognition error - " + alias));
    }

    abstract String getJournalPattern();

    abstract String getTCid();

}
