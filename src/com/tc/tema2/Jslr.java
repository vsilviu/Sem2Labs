package com.tc.tema2;

import java.util.*;

public class Jslr {

    Grammar grammar;
    Map<String, List<Production>> lhsToRules = new HashMap<>();
    Map<String, Set<String>> follow = new HashMap<>();
    Map<Pair<Set<Item>, String>, Action> table = new HashMap<>();
    Map<PairIndex, Action> indexTable = new LinkedHashMap<>();
    Set<Item> initialState;
    ArrayList<Production> productionArrayList = new ArrayList<>();

    public Jslr(Grammar grammar) {
        this.grammar = grammar;
        this.productionArrayList.addAll(grammar.productions);
        for (Production p : grammar.productions) {
            List<Production> mapRules = lhsToRules.get(p.lhs);
            if (mapRules == null) {
                mapRules = new ArrayList<>();
                lhsToRules.put(p.lhs, mapRules);
            }
            mapRules.add(p);
        }
    }

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
            Set<Item> oldI = new HashSet<>(i);
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
        Set<Item> j = new LinkedHashSet<Item>();
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
    private boolean addAction(Set<Item> state, String sym, Action a, Integer i) {
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
        //find occurences of this key first.
        PairIndex pairIndex = new PairIndex(p, i);
        boolean found = false;
        for (PairIndex pairIndex1 : indexTable.keySet()) {
            if (pairIndex1.getPair().equals(p) && pairIndex1.getPosition().equals(i)) {
                found = true;
            }
        }
        if (!found) {
            indexTable.put(pairIndex, a);
        }
        return ret;
    }

    public void addTheDollars() {
        for (String key : follow.keySet()) {
            if (grammar.isNonTerminal(key)) {
                HashSet oldSet = (HashSet) follow.get(key);
                oldSet.add("$");
                follow.put(key, oldSet);
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
        if (initialState.size() == grammar.productions.size()) {
            initialState = new LinkedHashSet<>();
            for (Production p : grammar.productions) {
                initialState.add(new Item(p, 0));
            }
        }
        //reorder initial state by the grammar.production order
        Map<Set<Item>, Integer> t = new LinkedHashMap<>();
        t.put(initialState, 0);
        boolean change;
        int index1 = 1;
        int index2;
        // compute goto actions
        do {
            change = false;
            for (Set<Item> i : new LinkedHashSet<Set<Item>>(t.keySet())) {
                for (Item item : i) {
                    if (!item.hasNextSym()) continue;
                    String x = item.nextSym();
                    Set<Item> j = goto_(i, x);
                    if (!t.keySet().contains(j)) {
                        change = true;
                        t.put(j, index1++);
                    }
                    index2 = t.get(i); //j e element in t
                    if (addAction(i, x, new ShiftAction(j, t.get(j)), index2)) change = true;
                }
            }
        } while (change);
        // compute reduce actions
        for (Set<Item> i : t.keySet()) {
            for (Item item : i) {
                if (item.hasNextSym()) continue;
                for (String x : follow.get(item.rule.lhs)) {
                    List<Production> arrayList = new ArrayList<>();
                    arrayList.addAll(grammar.productions);
                    index2 = t.get(i);
                    addAction(i, x, new ReduceAction(item.rule, arrayList.indexOf(item.rule) + 1), index2);
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

    public void showTableOutput() {
        for (Pair pair : table.keySet()) {
            System.out.println(pair.toString() + " ---------------goes to------------------- " + table.get(pair));
        }
//        System.out.println(table.toString());
    }

    public void showIndexTableOutput() {
        for (PairIndex pair : indexTable.keySet()) {
            System.out.println(pair.toString() + " ----> " + indexTable.get(pair));
        }
//        System.out.println(table.toString());
    }

    public String parseInputWord(String word) {

        String stack = "0";
        String reductionStack = "";
        Character crtChar;
        Integer lastPos = Integer.parseInt(String.valueOf(stack.charAt(stack.length() - 1)));
        Action action = null;
        boolean change = false;

        while (!word.equals("") && !change) {
            crtChar = word.charAt(0);
            if(!crtChar.toString().equals("$")) {
                lastPos = Integer.parseInt(String.valueOf(stack.charAt(stack.length() - 1)));
                for (PairIndex pairIndex : indexTable.keySet()) {
                    if (pairIndex.getPosition().equals(lastPos) && pairIndex.getPair().getO2().equals(crtChar.toString())) {
                        action = indexTable.get(pairIndex);
                        break;
                    }
                }
                if (action != null)
                    if (action instanceof ShiftAction) {
                        ShiftAction shiftAction = (ShiftAction) action;
                        stack = stack + crtChar.toString() + shiftAction.getPos();
                        word = word.substring(1);
//                        System.out.println("stack: " + stack);
//                        System.out.println("word:" + word);
                    } else if (action instanceof ReduceAction) {
                        int offset = ((ReduceAction) action).getRule().rhs.length;
                        stack = stack.substring(0, stack.length() - 2*offset);
                        ReduceAction reduceAction = (ReduceAction) action;
                        reductionStack = new StringBuilder(reductionStack).reverse().toString();
                        reductionStack += reduceAction.getPos();
                        reductionStack = new StringBuilder(reductionStack).reverse().toString();
                        Production p = productionArrayList.get(reduceAction.getPos() - 1);
                        stack += p.lhs;
//                    word = word.substring(1);
                        //find position for lhs of p
                        Integer anotherPos = Integer.parseInt(String.valueOf(stack.charAt((stack.length() - 2) > 0 ? stack.length() - 2 : 0)));
                        Character anotherChar = stack.charAt(stack.length() - 1);
                        for (PairIndex pairIndex1 : indexTable.keySet()) {
                            if (pairIndex1.getPosition().equals(anotherPos) && pairIndex1.getPair().getO2().equals(anotherChar.toString())) {
                                Action action1 = indexTable.get(pairIndex1);
                                stack += ((ShiftAction) action1).getPos();
                                break;
                            }
                        }
                    } else {
                        change = true;
                    }
            } else {
                while(lastPos != 1) {
                    lastPos = Integer.parseInt(String.valueOf(stack.charAt(stack.length() - 1)));
                    if(lastPos == 1) {change = true; break;}
                    for (PairIndex pairIndex : indexTable.keySet()) {
                        if (pairIndex.getPosition().equals(lastPos) && pairIndex.getPair().getO2().equals(crtChar.toString())) {
                            action = indexTable.get(pairIndex);
                            break;
                        }
                    }
                    if(action instanceof ReduceAction) {
                        int offset = ((ReduceAction) action).getRule().rhs.length;
                        stack = stack.substring(0, stack.length() - 2*offset);
                        ReduceAction reduceAction = (ReduceAction) action;
                        reductionStack = new StringBuilder(reductionStack).reverse().toString();
                        reductionStack += reduceAction.getPos();
                        reductionStack = new StringBuilder(reductionStack).reverse().toString();
                        Production p = productionArrayList.get(reduceAction.getPos() - 1);
                        stack += p.lhs;
//                    word = word.substring(1);
                        //find position for lhs of p
                        Integer anotherPos = Integer.parseInt(String.valueOf(stack.charAt((stack.length() - 2) > 0 ? stack.length() - 2 : 0)));
                        Character anotherChar = stack.charAt(stack.length() - 1);
                        for (PairIndex pairIndex1 : indexTable.keySet()) {
                            if (pairIndex1.getPosition().equals(anotherPos) && pairIndex1.getPair().getO2().equals(anotherChar.toString())) {
                                Action action1 = indexTable.get(pairIndex1);
                                stack += ((ShiftAction) action1).getPos();
                                break;
                            }
                        }
                    }
                }
            }
        }

        return reductionStack;

    }

    public Action findProperAction(Set<Item> set, Character c) {
        Set<Pair<Set<Item>, String>> keySet = table.keySet();
        for (Iterator<Pair<Set<Item>, String>> it = keySet.iterator(); it.hasNext(); ) {
            Pair<Set<Item>, String> crtElem = it.next();
            if (crtElem.getO1().equals(set) && crtElem.getO2().equals(c.toString())) {
                return table.get(crtElem);
            }
        }
        return null;
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
            jslr.showIndexTableOutput();
            System.out.println(jslr.parseInputWord("a+a*a$"));
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