package Visitor;

import Interval.IntervalSet;
import Interval.MultiIntervalSet;

public interface MultiIntervalSetVisitor<L> {
    /**
     * 对MultiIntervalSet接口的扩展
     * @param multiIntervalSet 期望扩展的MultiIntervalSet
     */
    public double visit(MultiIntervalSet<L> multiIntervalSet);
}
