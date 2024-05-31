package delegationInterface.NoBlankIntervalSet;

import Interval.IntervalSet;

public interface NoBlankIntervalSet<L> {

    /**
     * 检查限定时间段是否存在空白时间段
     *
     * @param intervalSet, 表示待检查的时间段集合
     * @return Boolean, 当限定时间段内不存在空白时间段，则返回true；否则，返回false
     */
    public boolean checkNoBlank(IntervalSet<L> intervalSet);
}
