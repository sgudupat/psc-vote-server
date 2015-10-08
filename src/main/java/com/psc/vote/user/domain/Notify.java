package com.psc.vote.user.domain;

public enum Notify {

    YES('Y'), NO('N');
    private char typeChar;

    Notify(char s) {
        this.typeChar = s;
    }

    public char getTypeChar() {
        return typeChar;
    }
}