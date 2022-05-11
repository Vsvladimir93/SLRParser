package com.petproject.slr.parser.slr;

public record ReduceAction(Integer index) implements Action {
    @Override
    public int getIndex() {
        return index;
    }
}
