package delegationInterfaceTest.NoBlankIntervalSetTest;

import Interval.CommonIntervalSet;
import Interval.IntervalSet;
import delegationInterface.NoBlankIntervalSet.NoBlankIntervalSet;
import delegationInterface.NoBlankIntervalSet.NoBlankIntervalSetImpl;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class NoBlankIntervalSetTest{
    //以0-10作为时间段限制
    public NoBlankIntervalSet<String> emptyInstance() {
        return new NoBlankIntervalSetImpl<>();
    }

    /**
     * 测试策略：
     * <p>
     * 1.按照限定时间段内是否存在空白划分：
     *       测试存在空白的情况：预期返回false
     *       测试不存在空白的情况：预期返回true
     * 2.按照覆盖时间段重叠的情况划分：
     *       测试存在空白的情况：预期返回false
     *       测试不存在空表的情况：预期返回true
     * 2.特殊情况
     *       测试传入null的情况：预期抛出NullPointerException异常
     */
    @Test
    public void checkNoBlankTest(){
        NoBlankIntervalSet<String> noBlankIntervalSet = emptyInstance();
        IntervalSet<String> intervalSet = new CommonIntervalSet<>(0, 10);
        //覆盖存在空白的情况
        assertFalse(noBlankIntervalSet.checkNoBlank(intervalSet));
        //覆盖时间段重叠且存在空白的情况
        intervalSet.insert(0,5, "first");
        intervalSet.insert(3,7, "second");
        assertFalse(noBlankIntervalSet.checkNoBlank(intervalSet));
        //覆盖不存在空白的情况（包括时间段重叠
        intervalSet.insert(6,10, "third");
        assertTrue(noBlankIntervalSet.checkNoBlank(intervalSet));
        //覆盖null的情况
        Exception exception = assertThrows(NullPointerException.class, () -> noBlankIntervalSet.checkNoBlank(null));
        assertEquals("IntervalSet can't be null", exception.getMessage());
    }
}
