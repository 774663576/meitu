package com.meitu.data.result;

public class StringResult extends Result {

    private String str = "";

    public StringResult() {
    }

    public StringResult(String s) {
        this.str = s;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "StringResult [data=" + str + ", status=" + status + ", err="
                + err + "]";
    }

}
