package delegationInterface.NonOverlapIntervalSet;

import Interval.IntervalSet;

public interface NonOverlapIntervalSet<L> {

    /**
     * 检查限定时间段内是否存在重叠
     *
     * @param intervalSet, 表示待检查的时间段集合
     * @throws IntervalConflictException, 当发生时间段重叠时抛出此异常
     */
    public void checkNoOverlap(IntervalSet<L> intervalSet) throws IntervalConflictException;
}
