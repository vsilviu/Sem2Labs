package com.tc.tema1.enums;

/**
 * Created by Silviu on 3/9/16.
 */
public enum Separators {

    ROUND_BRACES_L("("),
    ROUND_BRACES_R(")"),
    SQUARE_BRACES_L("["),
    SQUARE_BRACES_R("]"),
    CURLY_BRACES_L("{"),
    CURLY_BRACES_R("}"),
    SEMICOLON(";"),
    COLON(":"),
    COMMA(","),
    PERIOD(".");

    private String operation;

    private Separators(String s) {
        operation = s;
    }

    public boolean equalsOperation(String otherOperation) {
        return otherOperation != null && operation.equals(otherOperation);
    }

    public String toString() {
        return this.operation;
    }

}
