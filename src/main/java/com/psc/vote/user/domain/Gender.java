package com.psc.vote.user.domain;

public enum Gender {

    HE('M'), SHE('F'), OTHERS('O');
    private char typeChar;

    Gender(char s) {
        this.typeChar = s;
    }

    public char getTypeChar() {
        return typeChar;
    }
}