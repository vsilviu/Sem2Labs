package com.tc.tema1.enums;

/**
 * Created by Silviu on 3/11/16.
 */
public enum Comments {

    ONE_LINE("//"),
    MULTI_LINE_LEFT("/*"),
    MULTI_LINE_RIGHT("*/"),
    MULTI_LINE_DOC_LEFT("/**"),
    MULTI_LINE_DOC_RIGHT("*/"),
    MULTI_LINE_DOC_CENTER("*");

    private String comment;

    Comments(String s) {
        comment = s;
    }

    public boolean equalsComment(String othercomment) {
        return othercomment != null && comment.equals(othercomment);
    }

    public String toString() {
        return this.comment;
    }

}
