package com.tc.tema1.enums.operators;

/**
 * Created by Silviu on 3/9/16.
 */
public enum RelationalOperators {

    EQUAL_TO ( "=="),
    NOT_EQUAL_TO ( "!="),
    GREATER_THAN ( ">"),
    LESS_THAN ( "<"),
    GREATER_THAN_OR_EQUAL ( ">="),
    LESS_THAN_OR_EQUAL_TO ( "<=");

    private final String operation;

    private RelationalOperators(String s) {
        operation = s;
    }

    public boolean equalsOperation(String otherOperation) {
        return otherOperation != null && operation.equals(otherOperation);
    }

    public String toString() {
        return this.operation;
    }
    
}
