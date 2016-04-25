package com.tc.tema1.enums.operators;

/**
 * Created by Silviu on 3/9/16.
 */
public enum SpecialOperators {

    TERNARY_CONDITIONAL("?:"),
    INSTANCEOF("instance of");

    private String operation;

    private SpecialOperators(String s) {
        operation = s;
    }

    public boolean equalsOperation(String otherOperation) {
        return otherOperation != null && operation.equals(otherOperation);
    }

    public String toString() {
        return this.operation;
    }

}
