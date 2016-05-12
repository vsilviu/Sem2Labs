package com.tc.tema2;

import java.util.Set;

/**
 * Represents a shift parser action.
 */
class ShiftAction extends Action {
    Set<Item> nextState; // the automaton state to move to after the shift
    int pos;

    public ShiftAction(Set<Item> nextState) {
        this.nextState = nextState;
    }

    public ShiftAction(Set<Item> nextState, int pos) {
        this.nextState = nextState;
        this.pos = pos;
    }

    public Set<Item> getNextState() {
        return nextState;
    }

    public void setNextState(Set<Item> nextState) {
        this.nextState = nextState;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int hashCode() {
        return nextState.hashCode();
    }

    public boolean equals(Object other) {
        if (!(other instanceof ShiftAction)) return false;
        ShiftAction o = (ShiftAction) other;
        return nextState.equals(o.nextState);
    }

    public String toString() {
        return "shift " + pos;
    }
}