package Visitor;

import Interval.IntervalSet;

public interface IntervalVisitor<L> {

    /**
     * 对IntervalSet接口的扩展
     *
     * @param intervalSet<L> 期望扩展的IntervalSet
     */
    public double visit(IntervalSet<L> intervalSet);
}
