package com.tc.tema2;

/** A production in the grammar. */
class Production {
    public String lhs;
    public String[] rhs;
    public Production(String lhs, String[] rhs) {
        this.lhs = lhs; this.rhs = rhs;
    }
    public int hashCode() { return lhs.hashCode(); }
    public boolean equals(Object other) {
        if(!(other instanceof Production)) return false;
        Production o = (Production) other;
        if(!lhs.equals(o.lhs)) return false;
        if(rhs.length != o.rhs.length) return false;
        for(int i = 0; i < rhs.length; i++) {
            if(!rhs[i].equals(o.rhs[i])) return false;
        }
        return true;
    }
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append(lhs);
        //ret.append(" ->");
        for(String sym : rhs) {
            ret.append(" "+sym);
        }
        return ret.toString();
    }
}