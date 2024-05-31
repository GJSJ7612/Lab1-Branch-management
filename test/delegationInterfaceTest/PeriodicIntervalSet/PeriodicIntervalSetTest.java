package delegationInterfaceTest.PeriodicIntervalSet;

import delegationInterface.Period.Period;
import delegationInterface.Period.PeriodImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class PeriodicIntervalSetTest {
    //设定初始重复次数为5次
    public Period emptyInstance() {
        return new PeriodImpl(5);
    }

    /**
     * 测试策略
     * <p>
     * 特殊情况：
     *       测试begin = over：预期抛出IllegalArgumentException异常
     *       测试begin > over：预期抛出IllegalArgumentException异常
     *       测试times = 0：预期抛出IllegalArgumentException异常
     *       测试times > 0：预期抛出IllegalArgumentException异常
     */
    @Test
    public void constructorTest(){
        //覆盖times = 0的情况
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> new PeriodImpl(0));
        assertEquals("times must be positive", exception1.getMessage());
        //覆盖times < 0的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> new PeriodImpl(-6));
        assertEquals("times must be positive", exception2.getMessage());
    }

    /**
     * 测试是否正常返回重复次数
     */
    @Test
    public void getTimesTest(){
        Period periodicIntervalSet = emptyInstance();
        assertEquals(5, periodicIntervalSet.getTimes());
    }

    /**
     * 测试策略：
     * <p>
     * 正常情况：预期正常设定
     * 特殊情况：
     *      测试times < 0的情况：预期抛出IllegalArgumentException异常
     *      测试times = 0的情况：预期抛出IllegalArgumentException异常
     */
    @Test
    public void setTimesTest(){
        Period periodicIntervalSet = emptyInstance();
        assertEquals(5, periodicIntervalSet.getTimes());
        //覆盖正常情况
        periodicIntervalSet.setTimes(4);
        assertEquals(4, periodicIntervalSet.getTimes());
        //覆盖times < 0的情况
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> periodicIntervalSet.setTimes(-6));
        assertEquals("times must be positive", exception1.getMessage());
        //覆盖times = 0的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> periodicIntervalSet.setTimes(0));
        assertEquals("times must be positive", exception2.getMessage());
    }
}
