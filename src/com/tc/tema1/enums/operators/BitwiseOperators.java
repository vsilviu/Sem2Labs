package com.tc.tema1.enums.operators;

/**
 * Created by Silviu on 3/9/16.
 */
public enum BitwiseOperators {

    AND ("&"),
    OR ("|"),
    XOR ("^"),
    COMPLEMENT ("~"),
    LEFT_SHIFT ("<<"),
    RIGHT_SHIFT (">>"),
    ZERO_FILL_RIGHT ( ">>>");

    private String operation;

    private BitwiseOperators(String s) {
        operation = s;
    }

    public boolean equalsOperation(String otherOperation) {
        return otherOperation != null && operation.equals(otherOperation);
    }

    public String toString() {
        return this.operation;
    }

}
