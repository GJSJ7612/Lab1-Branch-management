package VisitorTest;

import Interval.CommonIntervalSet;
import Interval.IntervalSet;
import Visitor.CalcConflictRatioInterval;
import Visitor.IntervalVisitor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalcConflictRatioIntervalTest {
    /**
     * 测试策略：
     * <p>
     * 1.按照冲突部位划分：
     *      测试两个时间段部分重叠：预期正确计算冲突比例
     *      测试一个时间段为另一个时间段子集情况：预期正确计算冲突比例
     * 2.按照冲突数划分：
     *      测试1个冲突的情况：预期正确计算冲突比例
     *      测试3个冲突的情况：预期正确计算冲突比例
     * 3.特殊情况：
     *      测试无冲突的情况：预期返回0
     *      测试多个冲突相叠的情况：预期正确计算冲突比例
     *      测试完全冲突的情况：预期返回1
     */
    @Test
    public void CalcConflictRatioTest(){
        IntervalSet<String> intervalSet = new CommonIntervalSet<>(0,9);
        IntervalVisitor<String> intervalVisitor = new CalcConflictRatioInterval<>();
        //覆盖无冲突的情况
        intervalSet.insert(1, 5, "first");
        assertEquals(0, intervalSet.accept(intervalVisitor), 0.0001);
        //覆盖部分重叠的情况（一个冲突）
        intervalSet.insert(2,6,"second");
        assertEquals(0.4, intervalSet.accept(intervalVisitor), 0.0001);
        //覆盖完全重叠的情况
        intervalSet.insert(2,3,"second");
        assertEquals(0.2, intervalSet.accept(intervalVisitor), 0.0001);
        //覆盖3个冲突的情况
        intervalSet.insert(2,7,"second");
        intervalSet.insert(6,9,"third");
        intervalSet.insert(8,9,"forth");
        assertEquals(0.8, intervalSet.accept(intervalVisitor), 0.0001);
        //覆盖多个冲突相叠的情况
        intervalSet.insert(3,6,"third");
        intervalSet.insert(4,9,"forth");
        assertEquals(0.6, intervalSet.accept(intervalVisitor), 0.0001);
        //覆盖完全冲突的情况
        intervalSet.insert(0,9,"first");
        intervalSet.insert(0,9,"second");
        assertEquals(1, intervalSet.accept(intervalVisitor), 0.0001);
    }
}
