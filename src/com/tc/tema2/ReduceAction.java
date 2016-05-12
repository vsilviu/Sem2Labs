package com.tc.tema2;

/**
 * Represents a reduce parser action.
 */
class ReduceAction extends Action {
    Production rule; // the production to reduce by
    int pos;

    public ReduceAction(Production rule) {
        this.rule = rule;
    }

    public ReduceAction(Production rule, int pos) {
        this.rule = rule;
        this.pos = pos;
    }

    public int hashCode() {
        return rule.hashCode();
    }

    public boolean equals(Object other) {
        if (!(other instanceof ReduceAction)) return false;
        ReduceAction o = (ReduceAction) other;
        return rule.equals(o.rule);
    }

    public Production getRule() {
        return rule;
    }

    public void setRule(Production rule) {
        this.rule = rule;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String toString() {

        return "reduce " + pos;
    }
}