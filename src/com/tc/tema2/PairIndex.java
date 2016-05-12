package com.tc.tema2;

import java.util.Set;

/**
 * Created by Silviu on 5/12/16.
 */
public class PairIndex {

    Pair<Set<Item>, String> pair;
    Integer position;

    PairIndex() {
    }

    PairIndex(Pair p, Integer pos) {
        this.pair = p;
        this.position = pos;
    }

    public Pair<Set<Item>, String> getPair() {
        return pair;
    }

    public void setPair(Pair<Set<Item>, String> pair) {
        this.pair = pair;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String toString() {
        return position + "," + pair.getO2();
    }

    @Override
    public boolean equals(Object that) {
        PairIndex res = null;
        if(that instanceof PairIndex) {
            res = (PairIndex) that;
        }
        if(!this.getPair().equals(res.getPair())) return false;
        if(!this.getPosition().equals(res.getPosition())) return false;
        return true;
    }
}
