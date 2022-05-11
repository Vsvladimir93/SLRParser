package com.petproject.slr.parser.slr;

public record ShiftAction(Integer index) implements Action {
    @Override
    public int getIndex() {
        return index;
    }
}
