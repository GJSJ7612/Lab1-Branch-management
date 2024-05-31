package delegationInterfaceTest.NonOverlapIntervalSetTest;

import Interval.CommonIntervalSet;
import Interval.IntervalSet;
import delegationInterface.NonOverlapIntervalSet.IntervalConflictException;
import delegationInterface.NonOverlapIntervalSet.NonOverlapIntervalSet;
import delegationInterface.NonOverlapIntervalSet.NonOverlapIntervalSetImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NonOverlapIntervalSetTest {
    public NonOverlapIntervalSet<String> emptyInstance() {
        return new NonOverlapIntervalSetImpl<>();
    }

    /**
     * 测试策略
     * <p>
     * 1.按照时间段个数划分（不重叠）：
     *      测试0个时间段的情况：预期正常
     *      测试1个时间段的情况：预期正常
     *      测试3个时间段的情况：预期正常
     * 2.特殊情况
     *      测试传入null的情况：预期抛出NullPointerException异常
     *      测试存在重叠的情况：预期抛出IntervalConflictException异常
     */
    @Test
    public void checkNoOverlapTest1() throws IntervalConflictException {
        NonOverlapIntervalSet<String> nonOverlapIntervalSet = emptyInstance();
        IntervalSet<String> intervalSet = new CommonIntervalSet<>(0, 10);
        //覆盖0个时间段的情况
        nonOverlapIntervalSet.checkNoOverlap(intervalSet);
        //覆盖1个时间段的情况
        intervalSet.insert(2,5,"first");
        nonOverlapIntervalSet.checkNoOverlap(intervalSet);
        //覆盖3个时间段的情况
        intervalSet.insert(6,7, "second");
        intervalSet.insert(8,9, "third");
        nonOverlapIntervalSet.checkNoOverlap(intervalSet);
        //覆盖null的情况
        Exception exception1 = assertThrows(NullPointerException.class, () -> nonOverlapIntervalSet.checkNoOverlap(null));
        assertEquals("IntervalSet can't be null", exception1.getMessage());
        //覆盖存在重叠的情况
        intervalSet.insert(3,8,"forth");
        Exception exception2 = assertThrows(IntervalConflictException.class, () -> nonOverlapIntervalSet.checkNoOverlap(intervalSet));
        assertEquals("Overlap exists", exception2.getMessage());
    }
}
