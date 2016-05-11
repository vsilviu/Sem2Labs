package com.tc.tema2;

import java.util.*;

/**
 * The main Jslr class.
 */
public class Jslr {
    /**
     * The context-free grammar.
     */
    Grammar grammar;
    /**
     * A map from each non-terminal to the productions that expand it.
     */
    Map<String, List<Production>> lhsToRules = new HashMap<String, List<Production>>();

    /**
     * The NULLABLE, FIRST, and FOLLOW sets. See Appel, pp. 47-49 \
     * */
    Set<String> nullable = new HashSet<String>();
    Map<String, Set<String>> first = new HashMap<String, Set<String>>();
    Map<String, Set<String>> follow = new HashMap<String, Set<String>>();

    /**
     * The computed parse table.
     */
    Map<Pair<Set<Item>, String>, Action> table =
            new HashMap<Pair<Set<Item>, String>, Action>();
    Set<Item> initialState;

    public Jslr(Grammar grammar) {
        this.grammar = grammar;
        for (Production p : grammar.productions) {
            List<Production> mapRules = lhsToRules.get(p.lhs);
            if (mapRules == null) {
                mapRules = new ArrayList<Production>();
                lhsToRules.put(p.lhs, mapRules);
            }
            mapRules.add(p);
        }
    }

    //calculeaza first follow ptr productia data
    void repeater(Production p, int rhsIndex) {
        try {

            if (grammar.isTerminal(p.rhs[rhsIndex + 1])) {
                String key = p.rhs[rhsIndex];
                Set<String> value = follow.get(key);
                if (!value.contains(p.rhs[rhsIndex + 1])) {
                    value.add(p.rhs[rhsIndex + 1]);
                }
                follow.put(key, value);
                ++rhsIndex; //skip this terminal on iteration
            }

            repeater(p, ++rhsIndex);


        } catch (ArrayIndexOutOfBoundsException e) {
            if (grammar.isTerminal(p.rhs[rhsIndex])) return;
            String key = p.rhs[rhsIndex];
            Set<String> value = follow.get(key);
            if (value == null) value = new HashSet<>();
            Set<String> leftSideValue = follow.get(p.lhs);
            for (String s : leftSideValue) {
                if (!value.contains(s)) {
                    value.add(s);
                }
            }
            follow.put(key, value);
        }
    }

    void calculateFirstFollow() {
        //init lista cu cheie - valoare
        for (String nonterminal : grammar.nonterminals) {
            Set<String> value = new HashSet<>();
            value.add("$");
            follow.put(nonterminal, value);
        }
        //parcurg productiile, calculez pt fiecare lhs (cheie) first follow-ul.
        for (Production p : grammar.productions) {
            repeater(p, 0);
        }
    }

    void showFirstFollowOutput() {
        for (String key : follow.keySet()) {
            System.out.print(key + "---------");
            for (String val : follow.get(key)) {
                System.out.print(val + ", ");
            }
            System.out.println();
        }
    }

    /**
     * Compute the closure of a set of items using the algorithm of
     * Appel, p. 60
     */
    public Set<Item> closure(Set<Item> i) {
        while (true) {
            Set<Item> oldI = new HashSet<Item>(i);
            for (Item item : oldI) {
                if (!item.hasNextSym()) continue;
                String x = item.nextSym();
                if (grammar.isTerminal(x)) continue;
                for (Production r : lhsToRules.get(x)) {
                    i.add(new Item(r, 0));
                }
            }
            if (i.equals(oldI)) return i;
        }
    }

    /**
     * Compute the goto set for state i and symbol x, using the algorithm
     * of Appel, p. 60
     */
    public Set<Item> goto_(Set<Item> i, String x) {
        Set<Item> j = new HashSet<Item>();
        for (Item item : i) {
            if (!item.hasNextSym()) continue;
            if (!item.nextSym().equals(x)) continue;
            j.add(item.advance());
        }
        return closure(j);
    }

    /**
     * Add the action a to the parse table for the state state and
     * symbol sym. Report a conflict if the table already contiains
     * an action for the same state and symbol.
     */
    private boolean addAction(Set<Item> state, String sym, Action a) {
        boolean ret = false;
        Pair<Set<Item>, String> p = new Pair<Set<Item>, String>(state, sym);
        Action old = table.get(p);
        if (old != null && !old.equals(a)) {
            throw new Error(
                    "Conflict on symbol " + sym + " in state " + state + "\n" +
                            "Possible actions:\n" +
                            old + "\n" + a);
        }
        if (old == null || !old.equals(a)) ret = true;
        table.put(p, a);
        return ret;
    }

    /**
     * Return true if all the symbols in l are in the set nullable.
     */
    private boolean allNullable(String[] l) {
        return allNullable(l, 0, l.length);
    }

    /**
     * Return true if the symbols start..end in l are in the set nullable.
     */
    private boolean allNullable(String[] l, int start, int end) {
        boolean ret = true;
        for (int i = start; i < end; i++) {
            if (!nullable.contains(l[i])) ret = false;
        }
        return ret;
    }

    /**
     * Computes NULLABLE, FIRST, and FOLLOW sets using the algorithm
     * of Appel, p. 49
     */
    public void computeFirstFollowNullable() {
        for (String z : grammar.syms()) {
            first.put(z, new HashSet<String>());
            if (grammar.isTerminal(z)) first.get(z).add(z);
            follow.put(z, new HashSet<String>());
        }
        boolean change;
        do {
            change = false;
            for (Production rule : grammar.productions) {
                if (allNullable(rule.rhs)) {
                    if (nullable.add(rule.lhs)) change = true;
                }
                int k = rule.rhs.length;
                for (int i = 0; i < k; i++) {
                    if (allNullable(rule.rhs, 0, i)) {
                        if (first.get(rule.lhs).addAll(
                                first.get(rule.rhs[i])))
                            change = true;
                    }
                    if (allNullable(rule.rhs, i + 1, k)) {
                        if (follow.get(rule.rhs[i]).addAll(
                                follow.get(rule.lhs)))
                            change = true;
                    }
                    for (int j = i + 1; j < k; j++) {
                        if (allNullable(rule.rhs, i + 1, j)) {
                            if (follow.get(rule.rhs[i]).addAll(
                                    first.get(rule.rhs[j])))
                                change = true;
                        }
                    }
                }
            }
        } while (change);
    }

    public void addTheDollars() {
        for(String key : follow.keySet()) {
            if(grammar.isNonTerminal(key)) {
                HashSet oldSet = (HashSet) follow.get(key);
                oldSet.add("$");
                follow.put(key,oldSet);
            }
        }
    }

    /**
     * Generates the SLR(1) parse table using the algorithms on
     * pp. 60 and 62 of Appel.
     */
    public void generateTable() {

        //adds the dollar symbols to the follow table, for each non-terminal
        addTheDollars();

        Set<Item> startRuleSet = new HashSet<Item>();
        for (Production r : lhsToRules.get(grammar.start)) {
            startRuleSet.add(new Item(r, 0));
        }
        initialState = closure(startRuleSet);
        Set<Set<Item>> t = new HashSet<Set<Item>>();
        t.add(initialState);
        boolean change;
        // compute goto actions
        do {
            change = false;
            for (Set<Item> i : new HashSet<Set<Item>>(t)) {
                for (Item item : i) {
                    if (!item.hasNextSym()) continue;
                    String x = item.nextSym();
                    Set<Item> j = goto_(i, x);
                    if (t.add(j)) change = true;
                    if (addAction(i, x, new ShiftAction(j))) change = true;
                }
            }
        } while (change);
        // compute reduce actions
        for (Set<Item> i : t) {
            for (Item item : i) {
                if (item.hasNextSym()) continue;
                for (String x : follow.get(item.rule.lhs)) {
                    addAction(i, x, new ReduceAction(item.rule));
                }
            }
        }
    }

    /**
     * Print the elements of a list separated by spaces.
     */
    public static String listToString(List l) {
        StringBuffer ret = new StringBuffer();
        boolean first = true;
        for (Object o : l) {
            if (!first) ret.append(" ");
            first = false;
            ret.append(o);
        }
        return ret.toString();
    }

    /**
     * Produce output according to the output specification.
     */
    public void generateOutput() {
        Map<Production, Integer> ruleMap = new HashMap<Production, Integer>();
        int i = 0;
        for (Production r : grammar.productions) {
            ruleMap.put(r, i++);
        }
        Map<Set<Item>, Integer> stateMap = new HashMap<Set<Item>, Integer>();
        i = 0;
        stateMap.put(initialState, i++);
        for (Action a : table.values()) {
            if (!(a instanceof ShiftAction)) continue;
            Set<Item> state = ((ShiftAction) a).nextState;
            if (!stateMap.containsKey(state)) {
                stateMap.put(state, i++);
            }
        }
        for (Pair<Set<Item>, String> key : table.keySet()) {
            Set<Item> state = key.getO1();
            if (!stateMap.containsKey(state)) {
                stateMap.put(state, i++);
            }
        }
        System.out.println(i);
        System.out.println(table.size());
        for (Map.Entry<Pair<Set<Item>, String>, Action> e : table.entrySet()) {
            Pair<Set<Item>, String> p = e.getKey();
            System.out.print(stateMap.get(p.getO1()) + " " + p.getO2() + " ");
            Action a = e.getValue();
            if (a instanceof ShiftAction) {
                System.out.println("shift " +
                        stateMap.get(((ShiftAction) a).nextState));
            } else if (a instanceof ReduceAction) {
                System.out.println("reduce " +
                        ruleMap.get(((ReduceAction) a).rule));
            } else throw new Error("Internal error: unknown action");
        }
    }

    public static final void main(String[] args) {

          Grammar grammar = initGrammar();

//        try {
//            grammar = Util.readGrammar(new Scanner(System.in));
////            Util.writeGrammar(grammar);
//        } catch(Error e) {
//            System.err.println("Error reading grammar: "+e);
//            System.exit(1);
//            return;
//        }

        Jslr jslr = new Jslr(grammar);
        try {
            jslr.calculateFirstFollow();
//            jslr.computeFirstFollowNullable();
            jslr.showFirstFollowOutput();
            jslr.generateTable();
//            jslr.generateOutput();
        } catch (Error e) {
            System.err.println("Error performing SLR(1) construction: " + e);
            System.exit(1);
        }
    }

    static Grammar initGrammar() {
        Grammar grammar = new Grammar();
        grammar.terminals.add("a");
        grammar.terminals.add("+");
        grammar.terminals.add("*");
        grammar.terminals.add("$");
        grammar.nonterminals.add("E");
        grammar.nonterminals.add("T");
        grammar.nonterminals.add("F");
        grammar.start = "E";

        String[] rhs1 = new String[3];
        rhs1[0] = "E";
        rhs1[1] = "+";
        rhs1[2] = "T";
        Production p1 = new Production("E", rhs1);
        grammar.productions.add(p1);

        String[] rhs2 = new String[1];
        rhs2[0] = "T";
        Production p2 = new Production("E", rhs2);
        grammar.productions.add(p2);

        String[] rhs3 = new String[3];
        rhs3[0] = "T";
        rhs3[1] = "*";
        rhs3[2] = "F";
        Production p3 = new Production("T", rhs3);
        grammar.productions.add(p3);

        String[] rhs4 = new String[1];
        rhs4[0] = "F";
        Production p4 = new Production("T", rhs4);
        grammar.productions.add(p4);

        String[] rhs5 = new String[1];
        rhs5[0] = "a";
        Production p5 = new Production("F", rhs5);
        grammar.productions.add(p5);

        return grammar;
    }
}