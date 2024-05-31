package VisitorTest;

import Interval.CommonIntervalSet;
import Interval.IntervalSet;
import Visitor.CalcFreeTimeRatioInterval;
import Visitor.IntervalVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CalcFreeTimeRatioIntervalTest {
    /**
     * 测试策略：
     * <p>
     * 1.按照时间段数划分：
     *      测试1个时间段的情况：预期正确计算空白比例
     *      测试3个时间段的情况：预期正确计算空白比例
     * 2.特殊情况：
     *      已有时间段有重叠的情况：预期正确计算空白比例
     *      测试没有时间段的情况：预期返回1
     *      测试没有空白的情况：预期返回0
     */
    @Test
    public void CalcFreeTimeRatioTest(){
        IntervalSet<String> intervalSet = new CommonIntervalSet<>(0,9);
        IntervalVisitor<String> intervalVisitor = new CalcFreeTimeRatioInterval<>();
        //覆盖没有时间段的情况
        assertEquals(1, intervalSet.accept(intervalVisitor), 0.0001);
        //覆盖1个时间段的情况
        intervalSet.insert(1,3,"first");
        assertEquals(0.7, intervalSet.accept(intervalVisitor), 0.0001);
        //覆盖3个时间段的情况
        intervalSet.insert(6,7,"second");
        intervalSet.insert(8,9,"third");
        assertEquals(0.3, intervalSet.accept(intervalVisitor), 0.0001);
        //覆盖已有时间段有重叠的情况
        intervalSet.insert(0,2,"forth");
        assertEquals(0.2, intervalSet.accept(intervalVisitor), 0.0001);
        //覆盖没有空白的情况
        intervalSet.insert(4,5, "fifth");
        assertEquals(0, intervalSet.accept(intervalVisitor), 0.0001);
    }
}
