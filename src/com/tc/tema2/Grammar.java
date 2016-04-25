package com.tc.tema2;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Representation of a context-free grammar. */
class Grammar {
    Set<String> terminals = new LinkedHashSet<String>();
    Set<String> nonterminals = new LinkedHashSet<String>();
    Set<Production> productions = new LinkedHashSet<Production>();
    String start;

    public boolean isTerminal(String s) {
        return terminals.contains(s);
    }
    public boolean isNonTerminal(String s) {
        return nonterminals.contains(s);
    }
    public List<String> syms() {
        List<String> ret = new ArrayList<String>();
        ret.addAll(terminals);
        ret.addAll(nonterminals);
        return ret;
    }
}