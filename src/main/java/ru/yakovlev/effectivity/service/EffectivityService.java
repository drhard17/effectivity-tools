package ru.yakovlev.effectivity.service;

import ru.yakovlev.effectivity.model.Effectivity;

public interface EffectivityService {
    
    public Effectivity parseTcString(String tcString);

    public Effectivity parseJournalString(String journalString);

    public String toJournalString();
    
    public String toTcString();

}
