package com.petproject.slr.parser.slr;

public record AcceptAction(Integer index) implements Action {
    @Override
    public int getIndex() {
        return index;
    }
}
