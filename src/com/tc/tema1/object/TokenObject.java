package com.tc.tema1.object;

/**
 * Created by Silviu on 3/9/16.
 */
public class TokenObject {

    private Enum tokenType;
    private long tokenLength;
    private long crtFileLine;
    private long tokenPointer; //to replace with proper datatype
    private String errorMsg;
    private String value;

    public TokenObject() {
    }

    public TokenObject(Enum tokenType, long tokenLength, long crtFileLine, long tokenPointer, String errorMsg, String value) {
        this.tokenType = tokenType;
        this.tokenLength = tokenLength;
        this.crtFileLine = crtFileLine;
        this.tokenPointer = tokenPointer;
        this.errorMsg = errorMsg;
        this.value = value;
    }

    public Enum getTokenType() {
        return tokenType;
    }

    public void setTokenType(Enum tokenType) {
        this.tokenType = tokenType;
    }

    public long getTokenLength() {
        return tokenLength;
    }

    public void setTokenLength(long tokenLength) {
        this.tokenLength = tokenLength;
    }

    public long getCrtFileLine() {
        return crtFileLine;
    }

    public void setCrtFileLine(long crtFileLine) {
        this.crtFileLine = crtFileLine;
    }

    public long getTokenPointer() {
        return tokenPointer;
    }

    public void setTokenPointer(long tokenPointer) {
        this.tokenPointer = tokenPointer;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
