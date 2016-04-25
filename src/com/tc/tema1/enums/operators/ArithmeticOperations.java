package com.tc.tema1.enums.operators;

/**
 * Created by Silviu on 3/9/16.
 */
public enum ArithmeticOperations {

    ADDITION ( "+"),
    SUBSTRACTION ( "-"),
    MULTIPLICATION ( "*"),
    DIVISION ( "/"),
    MODULUS ( "%"),
    INCREMENT ( "++"),
    DECREMENT ( "--");

    private final String operation;

    private ArithmeticOperations(String s) {
        operation = s;
    }

    public boolean equalsOperation(String otherOperation) {
        return otherOperation != null && operation.equals(otherOperation);
    }

    public String toString() {
        return this.operation;
    }
    
}
