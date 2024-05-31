package decorator;

import Interval.IntervalSet;
import Interval.MultiIntervalSet;
import delegationInterface.NonOverlapIntervalSet.IntervalConflictException;

import java.util.Set;

public abstract class MultiIntervalSetDecorator<L> implements MultiIntervalSet<L> {

    protected final MultiIntervalSet<L> multiIntervalSet;

    public MultiIntervalSetDecorator(MultiIntervalSet<L> multiIntervalSet) {
        this.multiIntervalSet = multiIntervalSet;
    }

    public abstract void insert(long start, long end, L label);

    @Override
    public Set<L> labels() {
        return multiIntervalSet.labels();
    }

    @Override
    public boolean remove(L label) {
        return multiIntervalSet.remove(label);
    }

    @Override
    public IntervalSet<Integer> intervals(L label) {
        return multiIntervalSet.intervals(label);
    }

    @Override
    public long getBegin() {
        return multiIntervalSet.getBegin();
    }

    @Override
    public long getOver() {
        return multiIntervalSet.getOver();
    }

    @Override
    public void setBegin(long begin) {
        multiIntervalSet.setBegin(begin);
    }

    @Override
    public void setOver(long over) {
        multiIntervalSet.setOver(over);
    }
}
