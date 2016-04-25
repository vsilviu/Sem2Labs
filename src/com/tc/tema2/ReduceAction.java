package com.tc.tema2;

/** Represents a reduce parser action. */
class ReduceAction extends Action {
    Production rule; // the production to reduce by
    public ReduceAction(Production rule) {
        this.rule = rule;
    }
    public int hashCode() { return rule.hashCode(); }
    public boolean equals(Object other) {
        if(!(other instanceof ReduceAction)) return false;
        ReduceAction o = (ReduceAction) other;
        return rule.equals(o.rule);
    }
    public String toString() {
        return "reduce " + rule;
    }
}