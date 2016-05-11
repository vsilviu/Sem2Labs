package com.tc.tema2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Util {
    public static String readLine(Scanner in, String msg) {
        if(!in.hasNextLine()) throw new Error(msg+" but input file ended");
        return in.nextLine();
    }
    public static int toInt(String line, String msg) {
        try {
            return new Integer(line);
        } catch(NumberFormatException e) {
            throw new Error("Expecting "+msg+" but the line is not a number:\n"+line);
        }
    }
    public static Grammar readGrammar(Scanner in) {
        Grammar grammar = new Grammar();
        System.out.println("Nr de terminale:");
        String line = readLine(in, "Expecting number of terminals");
        int nterm = toInt(line, "number of terminals");
        for(int i = 0; i < nterm; i++) {
            System.out.println("Dati terminal:");
            grammar.terminals.add(readLine(in, "Expecting a terminal"));
        }
        if(grammar.terminals.size() != nterm) throw new Error("Duplicate terminals");

        System.out.println("Nr de neterminale:");
        line = readLine(in, "Expecting number of non-terminals");
        int nnonterm = toInt(line, "number of non-terminals");
        for(int i = 0; i < nnonterm; i++) {
            System.out.println("Dati neterminal:");
            grammar.nonterminals.add(readLine(in, "Expecting a non-terminal"));
        }
        if(grammar.nonterminals.size() != nnonterm) throw new Error("Duplicate non-terminal");

        System.out.println("Dati simbolul de start:");
        grammar.start = readLine(in, "Expecting start symbol");
        if(!grammar.nonterminals.contains(grammar.start)) throw new Error(
                "Start symbol "+grammar.start+" was not declared as a non-terminal.");

        System.out.println("Dati nr de productii:");
        line = readLine(in, "Expecting number of productions");
        int nprods = toInt(line, "number of productions");
        for(int i = 0; i < nprods; i++) {
            System.out.println("Dati productia:");
            grammar.productions.add(readProduction(readLine(in, "Expecting production"), grammar));
        }
        if(grammar.productions.size() != nprods) throw new Error("Duplicate productions");
        return grammar;
    }
    public static Production readProduction(String line, Grammar grammar) {
        Scanner s = new Scanner(line);
        if(!s.hasNext()) throw new Error("Empty line instead of a production");
        String lhs = s.next();
        if(!grammar.isNonTerminal(lhs)) throw new Error("Symbol "+lhs+" was not declared as a non-terminal, but appears on the LHS of production "+line);
        List<String> rhs = new ArrayList<>();
        while(s.hasNext()) {
            String sym = s.next();
            if(!grammar.isNonTerminal(sym) && !grammar.isTerminal(sym)) {
                throw new Error("Symbol "+sym+" is not a part of the grammar");
            }
            rhs.add(sym);
        }
        return new Production(lhs,
                rhs.toArray(new String[rhs.size()]));
    }
    static String checkIndent(String line, int indent) {
        for(int i = 0; i < indent; i++) {
            if(line.length() <= i) throw new Error("Expecting production but got empty line.");
            if(line.charAt(i) != ' ') throw new Error("Production "+line.substring(i)+" should be indented "+indent+" space(s), but it is indented "+i+" spaces");
        }
        if(line.length() <= indent) throw new Error("Expecting production but got empty line.");
        if(line.charAt(indent) == ' ') throw new Error("Production "+line+" should be indented "+indent+" spaces, but it is indented more than that.");
        return line.substring(indent);
    }
    static void printGrammar(Grammar grammar) {
        System.out.println("Terminals:");
        for(String s : grammar.terminals) {
            System.out.println("   "+s);
        }
        System.out.println();

        System.out.println("Nonterminals:");
        for(String s : grammar.nonterminals) {
            System.out.println("   "+s);
        }
        System.out.println();

        System.out.println("Start Symbol:");
        System.out.println("   "+grammar.start);
        System.out.println();

        System.out.println("Production Rules:");
        for(Production s : grammar.productions) {
            System.out.println("   "+s);
        }
    }
    static void writeGrammar(Grammar grammar) {
        System.out.println(grammar.terminals.size());
        for(String s : grammar.terminals) {
            System.out.println(s);
        }
        System.out.println(grammar.nonterminals.size());
        for(String s : grammar.nonterminals) {
            System.out.println(s);
        }
        System.out.println(grammar.start);
        System.out.println(grammar.productions.size());
        for(Production s : grammar.productions) {
            System.out.println(s);
        }
    }
}