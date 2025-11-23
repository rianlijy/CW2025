package com.comp2042.logic.bricks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SevenBagGenerator implements BrickGenerator {
    private final List<Brick> bag = new ArrayList<>();
    private Brick nextBrick;
    public SevenBagGenerator() {
        refillBag();
        nextBrick = drawFromBag();
    }

    private void refillBag() {
        bag.clear();
        bag.add(new IBrick());
        bag.add(new JBrick());
        bag.add(new LBrick());
        bag.add(new OBrick());
        bag.add(new SBrick());
        bag.add(new TBrick());
        bag.add(new ZBrick());
        Collections.shuffle(bag);
    }

    private Brick drawFromBag() {
        if (bag.isEmpty()) {
            refillBag();
        }
        return bag.remove(0);
    }

    @Override
    public Brick getBrick() {
        Brick current = nextBrick;
        nextBrick = drawFromBag();
        return current;
    }

    @Override
    public Brick getNextBrick() {
        return nextBrick;
    }
}
