package decorator;

import Interval.IntervalSet;
import Interval.MultiIntervalSet;
import Visitor.MultiIntervalSetVisitor;
import delegationInterface.NonOverlapIntervalSet.IntervalConflictException;

import java.util.ArrayList;
import java.util.Comparator;

public class NonOverlapMultiIntervalSet<L> extends MultiIntervalSetDecorator<L> {
    public NonOverlapMultiIntervalSet(MultiIntervalSet<L> multiIntervalSet) {
        super(multiIntervalSet);
    }

    @Override
    public void insert(long start, long end, L label) {
        if(label == null) throw new NullPointerException("label can't be null");
        if(start > end) throw new IllegalArgumentException("start can't larger than end");
        for(L l : super.labels()){
            IntervalSet<Integer> intervalSet = multiIntervalSet.intervals(l);
            for(int i : intervalSet.labels()){
                if((start >= intervalSet.start(i) && start <= intervalSet.end(i)) || (end >= intervalSet.start(i) && end <= intervalSet.end(i))
                        || (start <= intervalSet.start(i) && end >= intervalSet.end(i)))
                    throw new IllegalArgumentException("Overlap exists");
            }
        }
        multiIntervalSet.insert(start, end, label);
    }

    @Override
    public double accept(MultiIntervalSetVisitor<L> v) {
        return v.visit(this);
    }

}
