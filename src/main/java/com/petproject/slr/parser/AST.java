package com.petproject.slr.parser;

import java.util.ArrayList;
import java.util.List;

public class AST {
    public List<Node> nodes = new ArrayList<>();

    static class Node {
        public String type;
        public String value;
        public List<Node> nodes;
    }
}
