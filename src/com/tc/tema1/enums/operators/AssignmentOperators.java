package com.tc.tema1.enums.operators;

/**
 * Created by Silviu on 3/9/16.
 */
public enum AssignmentOperators {

    ASSIGN ("=="),
    PLUS_EQUALS ("+="),
    MINUS_EQUALS ("-="),
    MULTIPLY_EQUALS ("*="),
    DIVIDE_EQUALS ("/="),
    MODULUS_EQUALS ("%="),
    LSHIFT_EQUALS ("<<="),
    RSHIFT_EQUALS (">>="),
    BIT_AND_EQUALS ("&="),
    BIT_OR_EQUALS ("|=");

    private String operation;

    private AssignmentOperators(String s) {
        operation = s;
    }

    public boolean equalsOperation(String otherOperation) {
        return otherOperation != null && operation.equals(otherOperation);
    }

    public String toString() {
        return this.operation;
    }

}
