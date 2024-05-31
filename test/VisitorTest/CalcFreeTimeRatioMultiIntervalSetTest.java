package VisitorTest;

import Interval.CommonMultiIntervalSet;
import Interval.MultiIntervalSet;
import Visitor.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalcFreeTimeRatioMultiIntervalSetTest {
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
        MultiIntervalSet<String> multiIntervalSet = new CommonMultiIntervalSet<>(0,9);
        MultiIntervalSetVisitor<String> multiIntervalSetVisitor = new CalcFreeTimeRatioMultiIntervalSet<>();
        //覆盖没有时间段的情况
        assertEquals(1, multiIntervalSet.accept(multiIntervalSetVisitor), 0.0001);
        //覆盖1个时间段的情况
        multiIntervalSet.insert(1,3,"first");
        assertEquals(0.7, multiIntervalSet.accept(multiIntervalSetVisitor), 0.0001);
        //覆盖3个时间段的情况
        multiIntervalSet.insert(6,7,"second");
        multiIntervalSet.insert(8,9,"third");
        assertEquals(0.3, multiIntervalSet.accept(multiIntervalSetVisitor), 0.0001);
        //覆盖已有时间段有重叠的情况
        multiIntervalSet.insert(0,2,"forth");
        assertEquals(0.2, multiIntervalSet.accept(multiIntervalSetVisitor), 0.0001);
        //覆盖没有空白的情况
        multiIntervalSet.insert(4,5, "first");
        assertEquals(0, multiIntervalSet.accept(multiIntervalSetVisitor), 0.0001);
    }
}
