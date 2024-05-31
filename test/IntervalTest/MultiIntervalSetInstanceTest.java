package IntervalTest;

import Interval.CommonIntervalSet;
import Interval.IntervalSet;
import Interval.MultiIntervalSet;
import Visitor.CalcConflictRatioMultiIntervalSet;
import Visitor.MultiIntervalSetVisitor;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public abstract class MultiIntervalSetInstanceTest {
    /**
     * 由其他具体测试实现类重写实现方法
     *
     * @return 由被测试实现实例化的空时间段集合
     */
    public abstract MultiIntervalSet<String> emptyInstance();

    @Test(expected=AssertionError.class)
    public void AssertionsEnabledTest() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void EmptyTest() {
        assertEquals(Collections.emptySet(), MultiIntervalSet.empty(0, 10).labels());
    }

    /**
     * 测试策略：
     * <p>
     * 1.按照时间段个数划分：
     *    测试0个时间段的情况：预期集合为空
     *    测试1个时间段的情况：预期包含一个时间段
     *    测试3个时间段的情况：预期包含三个时间段
     * 2.按照标签数划分：
     *    测试0个标签的情况：预期集合为空
     *    测试1个标签的情况：预期包含一个标签
     *    测试3个标签的情况：预期包含三个标签
     * 3.按照测试标签所属时间段重叠的情况划分：
     *    测试前部分与原有某时间段重叠：预期本次插入无效
     *    测试后部分与原有某时间段重叠：预期本次插入无效
     *    测试欲插入时间段为原有某时间段子集：预期本次插入无效
     *    测试欲插入时间段覆盖原有某时间段：预期本次插入无效
     * 4.特殊情况：
     *    测试传入标签为null的情况：预期返回NullPointerException
     *    测试传入的起始时间大于终止时间，预期返回IllegalArgumentException
     *    测试传入起始时间在限制时间之外的情况：预期本次插入无效
     */
    @Test
    public void insertTest(){
        MultiIntervalSet<String> multiIntervalSet = emptyInstance();
        IntervalSet<Integer> expectedFirst = new CommonIntervalSet<>(0, 10);
        Set<String> expectedLabels = new HashSet<>();
        //覆盖0个标签、0个节点情况
        assertEquals(expectedFirst, multiIntervalSet.intervals("first"));
        assertEquals(expectedLabels, multiIntervalSet.labels());
        //覆盖1个标签、1个时间段情况
        multiIntervalSet.insert(2,4,"first");
        expectedLabels.add("first");
        assertEquals(expectedLabels, multiIntervalSet.labels());
        assertEquals(2, multiIntervalSet.intervals("first").start(0));
        assertEquals(4, multiIntervalSet.intervals("first").end(0));
        //覆盖1个标签、3个时间段情况（插入的时间段start等于某已有时间段的end）
        multiIntervalSet.insert(5,6, "first");
        multiIntervalSet.insert(7,9, "first");
        assertEquals(2, multiIntervalSet.intervals("first").start(0));
        assertEquals(4, multiIntervalSet.intervals("first").end(0));
        assertEquals(5, multiIntervalSet.intervals("first").start(1));
        assertEquals(6, multiIntervalSet.intervals("first").end(1));
        assertEquals(7, multiIntervalSet.intervals("first").start(2));
        assertEquals(9, multiIntervalSet.intervals("first").end(2));
        assertEquals(expectedLabels, multiIntervalSet.labels());
        //覆盖3个标签的情况
        multiIntervalSet.insert(4,7,"second");
        multiIntervalSet.insert(5,9, "third");
        expectedLabels.add("second");
        expectedLabels.add("third");
        assertEquals(expectedLabels, multiIntervalSet.labels());
        assertEquals(4, multiIntervalSet.intervals("second").start(0));
        assertEquals(7, multiIntervalSet.intervals("second").end(0));
        assertEquals(5, multiIntervalSet.intervals("third").start(0));
        assertEquals(9, multiIntervalSet.intervals("third").end(0));
        //覆盖前部分与原有某时间段重叠的情况
        multiIntervalSet.insert(6,10,"first");
        assertEquals(2, multiIntervalSet.intervals("first").start(0));
        assertEquals(4, multiIntervalSet.intervals("first").end(0));
        assertEquals(5, multiIntervalSet.intervals("first").start(1));
        assertEquals(6, multiIntervalSet.intervals("first").end(1));
        assertEquals(7, multiIntervalSet.intervals("first").start(2));
        assertEquals(9, multiIntervalSet.intervals("first").end(2));
        //覆盖后部分与原有某时间段重叠的情况
        multiIntervalSet.insert(0,5,"first");
        assertEquals(2, multiIntervalSet.intervals("first").start(0));
        assertEquals(4, multiIntervalSet.intervals("first").end(0));
        assertEquals(5, multiIntervalSet.intervals("first").start(1));
        assertEquals(6, multiIntervalSet.intervals("first").end(1));
        assertEquals(7, multiIntervalSet.intervals("first").start(2));
        assertEquals(9, multiIntervalSet.intervals("first").end(2));
        //覆盖欲插入时间段为原有某时间段子集的情况
        multiIntervalSet.insert(3,6,"first");
        assertEquals(2, multiIntervalSet.intervals("first").start(0));
        assertEquals(4, multiIntervalSet.intervals("first").end(0));
        assertEquals(5, multiIntervalSet.intervals("first").start(1));
        assertEquals(6, multiIntervalSet.intervals("first").end(1));
        assertEquals(7, multiIntervalSet.intervals("first").start(2));
        assertEquals(9, multiIntervalSet.intervals("first").end(2));
        //覆盖欲插入时间段覆盖原有某时间段的情况
        multiIntervalSet.insert(0,10,"first");
        assertEquals(2, multiIntervalSet.intervals("first").start(0));
        assertEquals(4, multiIntervalSet.intervals("first").end(0));
        assertEquals(5, multiIntervalSet.intervals("first").start(1));
        assertEquals(6, multiIntervalSet.intervals("first").end(1));
        assertEquals(7, multiIntervalSet.intervals("first").start(2));
        assertEquals(9, multiIntervalSet.intervals("first").end(2));
        //覆盖null标签的情况
        Exception exception1 = assertThrows(NullPointerException.class, () -> multiIntervalSet.insert(6,9,null));
        assertEquals("label can't be null", exception1.getMessage());
        //覆盖起始时间大于终止时间的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> multiIntervalSet.insert(9,6,"first"));
        assertEquals("start can't larger than end", exception2.getMessage());
        //覆盖传入起始时间在限制时间之外的情况
        multiIntervalSet.insert(-3,4,"first");
        assertEquals(2, multiIntervalSet.intervals("first").start(0));
        assertEquals(4, multiIntervalSet.intervals("first").end(0));
        assertEquals(5, multiIntervalSet.intervals("first").start(1));
        assertEquals(6, multiIntervalSet.intervals("first").end(1));
        assertEquals(7, multiIntervalSet.intervals("first").start(2));
        assertEquals(9, multiIntervalSet.intervals("first").end(2));
        multiIntervalSet.insert(6,15,"first");
        assertEquals(2, multiIntervalSet.intervals("first").start(0));
        assertEquals(4, multiIntervalSet.intervals("first").end(0));
        assertEquals(5, multiIntervalSet.intervals("first").start(1));
        assertEquals(6, multiIntervalSet.intervals("first").end(1));
        assertEquals(7, multiIntervalSet.intervals("first").start(2));
        assertEquals(9, multiIntervalSet.intervals("first").end(2));
    }

    /**
     * 测试策略：
     * <p>
     * 按照时间段个数划分：
     *      测试0个时间段的情况：预期集合为空
     *      测试1个时间段的情况：预期包含一个标签
     *      测试3个时间段的情况：预期包含三个标签
     */
    @Test
    public void labelsTest(){
        MultiIntervalSet<String> multiIntervalSet = emptyInstance();
        //覆盖0个时间段的情况
        Set<String> expected = new HashSet<>();
        assertEquals(expected, multiIntervalSet.labels());
        //覆盖1个时间段的情况
        expected.add("first");
        multiIntervalSet.insert(2,3,"first");
        assertEquals(expected, multiIntervalSet.labels());
        //覆盖3个时间段的情况
        expected.add("second");
        expected.add("third");
        multiIntervalSet.insert(4,6,"second");
        multiIntervalSet.insert(7,8,"third");
        assertEquals(expected, multiIntervalSet.labels());
    }

    /**
     * 测试策略：
     * <p>
     * 按照标签存在性划分：
     *       测试标签存在于时间段集合的情况：预期成功删除并返回true
     *       测试标签不存在于时间段集合的情况：预期与原集合无区别并返回false
     * 按照时间段数划分：
     *       测试标签所属时间段个数为1的情况：预期成功删除并返回true
     *       测试标签所属时间段个数为3的情况：预期成功删除并返回true
     * 特殊情况：
     *       测试标签为null的情况：预期抛出NullPointerException异常
     */
    @Test
    public void removeTest(){
        MultiIntervalSet<String> multiIntervalSet = emptyInstance();
        Set<String> expectedSet = new HashSet<>();
        IntervalSet<Integer> expectedInterval = new CommonIntervalSet<>(0, 15);
        //覆盖标签不存在的情况
        assertFalse(multiIntervalSet.remove("second"));
        //覆盖标签存在的情况（所属时间段个数为1）
        multiIntervalSet.insert(2,5,"first");
        expectedSet.add("first");
        assertEquals(expectedSet, multiIntervalSet.labels());
        assertEquals(2, multiIntervalSet.intervals("first").start(0));
        assertEquals(5, multiIntervalSet.intervals("first").end(0));
        assertTrue(multiIntervalSet.remove("first"));
        expectedSet.remove("first");
        assertEquals(expectedSet, multiIntervalSet.labels());
        assertEquals(expectedInterval, multiIntervalSet.intervals("first"));
        //覆盖标签存在的情况（所属时间段个数为3）
        multiIntervalSet.insert(2,5,"first");
        multiIntervalSet.insert(6,7,"first");
        multiIntervalSet.insert(8,9,"first");
        expectedSet.add("first");
        assertEquals(expectedSet, multiIntervalSet.labels());
        assertEquals(2, multiIntervalSet.intervals("first").start(0));
        assertEquals(5, multiIntervalSet.intervals("first").end(0));
        assertEquals(6, multiIntervalSet.intervals("first").start(1));
        assertEquals(7, multiIntervalSet.intervals("first").end(1));
        assertEquals(8, multiIntervalSet.intervals("first").start(2));
        assertEquals(9, multiIntervalSet.intervals("first").end(2));
        assertTrue(multiIntervalSet.remove("first"));
        expectedSet.remove("first");
        assertEquals(expectedSet, multiIntervalSet.labels());
        assertEquals(expectedInterval, multiIntervalSet.intervals("first"));
        //覆盖标签为null的情况
        Exception exception = assertThrows(NullPointerException.class, () -> multiIntervalSet.remove(null));
        assertEquals("label can't be null", exception.getMessage());
    }

    /**
     * 测试策略：
     * <p>
     * 按照标签关联时间段数划分：
     *       测试0个时间段的情况（标签不存在）：预期返回空集合
     *       测试1个时间段的情况：预期返回包含1个时间段的集合
     *       测试3个时间段的情况：预期返回包含3个时间段的集合
     * 特殊情况：
     *       时间段由大到小插入：预期返回从小到大的时间段的集合
     *       测试标签为null的情况：预期抛出NullPointerException异常
     */
    @Test
    public void intervalsTest(){
        MultiIntervalSet<String> multiIntervalSet = emptyInstance();
        IntervalSet<Integer> expected = new CommonIntervalSet<>(0, 10);
        //覆盖0个时间段的情况
        assertEquals(expected, multiIntervalSet.intervals("first"));
        //覆盖1个时间段的情况
        multiIntervalSet.insert(8,9,"first");
        assertEquals(8, multiIntervalSet.intervals("first").start(0));
        assertEquals(9, multiIntervalSet.intervals("first").end(0));
        //覆盖3个时间段的情况（由大到小输入）
        multiIntervalSet.insert(5,7,"first");
        assertEquals(5, multiIntervalSet.intervals("first").start(0));
        assertEquals(7, multiIntervalSet.intervals("first").end(0));
        assertEquals(8, multiIntervalSet.intervals("first").start(1));
        assertEquals(9, multiIntervalSet.intervals("first").end(1));
        multiIntervalSet.insert(2,3,"first");
        assertEquals(2, multiIntervalSet.intervals("first").start(0));
        assertEquals(3, multiIntervalSet.intervals("first").end(0));
        assertEquals(5, multiIntervalSet.intervals("first").start(1));
        assertEquals(7, multiIntervalSet.intervals("first").end(1));
        assertEquals(8, multiIntervalSet.intervals("first").start(2));
        assertEquals(9, multiIntervalSet.intervals("first").end(2));
        //覆盖null的情况
        Exception exception = assertThrows(NullPointerException.class, () -> multiIntervalSet.intervals(null));
        assertEquals("label can't be null", exception.getMessage());
    }

    /**
     * 测试是否返回限定的起始时间
     */
    @Test
    public void getBeginTest(){
        MultiIntervalSet<String> multiIntervalSet = emptyInstance();
        assertEquals(0, multiIntervalSet.getBegin());
    }
    /**
     * 测试是否返回限定的终止时间
     */
    @Test
    public void getOverTest(){
        MultiIntervalSet<String> multiIntervalSet = emptyInstance();
        assertEquals(15, multiIntervalSet.getOver());
    }
    /**
     * 测试是否修改成功限定的起始时间
     * 特殊情况：
     *      当begin > over时：预期抛出IllegalArgumentException异常
     *      当begin = over时：预期抛出IllegalArgumentException异常
     */
    @Test
    public void setBeginTest(){
        MultiIntervalSet<String> multiIntervalSet = emptyInstance();
        //覆盖正常的情况
        multiIntervalSet.setBegin(5);
        assertEquals(5, multiIntervalSet.getBegin());
        //覆盖begin > over的情况
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> multiIntervalSet.setBegin(20));
        assertEquals("begin must be less than over", exception1.getMessage());
        //覆盖begin = over的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> multiIntervalSet.setBegin(15));
        assertEquals("begin must be less than over", exception2.getMessage());
    }

    /**
     * 测试是否修改成功限定的终止时间
     * 特殊情况：
     *      当over < begin时：预期抛出IllegalArgumentException异常
     *      当over = begin时：预期抛出IllegalArgumentException异常
     */
    @Test
    public void setOverTest(){
        MultiIntervalSet<String> multiIntervalSet = emptyInstance();
        //覆盖正常的情况
        multiIntervalSet.setOver(5);
        assertEquals(5, multiIntervalSet.getOver());
        //覆盖over < begin的情况
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> multiIntervalSet.setOver(-1));
        assertEquals("over must be larger than begin", exception1.getMessage());
        //覆盖begin = over的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> multiIntervalSet.setOver(0));
        assertEquals("over must be larger than begin", exception2.getMessage());
    }

    /**
     * 测试visitor是否正常传递
     */
    @Test
    public void acceptTest(){
        MultiIntervalSetVisitor<String> visitor = new CalcConflictRatioMultiIntervalSet<>();
        MultiIntervalSet<String> multiIntervalSet = emptyInstance();
        multiIntervalSet.accept(visitor);
    }
}
