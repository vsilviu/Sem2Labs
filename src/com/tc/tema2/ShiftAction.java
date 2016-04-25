package com.tc.tema2;

import java.util.Set;

/** Represents a shift parser action. */
class ShiftAction extends Action {
    Set<Item> nextState; // the automaton state to move to after the shift
    public ShiftAction(Set<Item> nextState) {
        this.nextState = nextState;
    }
    public int hashCode() { return nextState.hashCode(); }
    public boolean equals(Object other) {
        if(!(other instanceof ShiftAction)) return false;
        ShiftAction o = (ShiftAction) other;
        return nextState.equals(o.nextState);
    }
    public String toString() {
        return "shift " + nextState;
    }
}