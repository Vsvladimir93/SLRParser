package com.petproject.slr.parser.slr;

public interface Action {
    int getIndex();

    default String getType() {
        return this.getClass().getSimpleName();
    }
}
