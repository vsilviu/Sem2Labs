package com.tc.tema2;

/** Represents an item (a dotted production). */
public class Item {
    Production rule; // the production
    int pos; // position of the dot (0 <= pos <= rule.rhs.size())
    public Item( Production rule, int pos ) {
        this.rule = rule;
        this.pos = pos;
    }
    /** Returns true if the dot is not at the end of the RHS. */
    public boolean hasNextSym() {
        return pos < rule.rhs.length;
    }
    /** Returns the symbol immediately after the dot. */
    public String nextSym() {
        if(!hasNextSym()) throw new RuntimeException("Internal error: getting next symbol of an item with no next symbol");
        return rule.rhs[pos];
    }
    /** Returns the item obtained by advancing (shifting) the dot by one
     *  symbol. */
    public Item advance() {
        if(!hasNextSym()) throw new RuntimeException("Internal error: advancing an item with no next symbol");
        return new Item(rule, pos+1);
    }
    public boolean equals(Object other) {
        if(!(other instanceof Item)) return false;
        Item o = (Item) other;
        if(!rule.equals(o.rule)) return false;
        if(pos != o.pos) return false;
        return true;
    }
    public int hashCode() {
        return rule.hashCode() + pos;
    }
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append(rule.lhs);
        ret.append(" ->");
        int i;
        for(i = 0; i < pos; i++) ret.append(" "+rule.rhs[i]);
        ret.append(" ##");
        for(; i < rule.rhs.length; i++) ret.append(" "+rule.rhs[i]);
        return ret.toString();
    }
}