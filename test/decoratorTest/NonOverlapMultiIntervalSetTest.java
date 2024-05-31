package decoratorTest;

import Interval.CommonIntervalSet;
import Interval.CommonMultiIntervalSet;
import Interval.IntervalSet;
import Interval.MultiIntervalSet;
import IntervalTest.MultiIntervalSetInstanceTest;
import decorator.NonOverlapMultiIntervalSet;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class NonOverlapMultiIntervalSetTest extends MultiIntervalSetInstanceTest {
    //以0-15为范围
    @Override
    public MultiIntervalSet<String> emptyInstance() {
        return new NonOverlapMultiIntervalSet<>(new CommonMultiIntervalSet<>(0,15));
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
     * 4.按照欲插入时间段与不同标签所属时间段重叠的情况划分：
     *    测试前部分与原有某时间段重叠：预期本次插入无效
     *    测试后部分与原有某时间段重叠：预期本次插入无效
     *    测试欲插入时间段为原有某时间段子集：预期本次插入无效
     *    测试欲插入时间段覆盖原有某时间段：预期本次插入无效
     * 5.特殊情况：
     *    测试传入标签为null的情况：预期返回NullPointerException
     *    测试传入的起始时间大于终止时间，预期返回IllegalArgumentException
     *    测试传入起始时间在限制时间之外的情况：预期本次插入无效
     */
    @Test
    @Override
    public void insertTest(){
        MultiIntervalSet<String> multiIntervalSet = emptyInstance();
        IntervalSet<Integer> expectedFirst = new CommonIntervalSet<>(0, 15);
        Set<String> expectedLabels = new HashSet<>();
        //覆盖0个标签、0个节点情况
        assertEquals(expectedFirst, multiIntervalSet.intervals("first"));
        assertEquals(expectedLabels, multiIntervalSet.labels());
        //覆盖1个标签、1个时间段情况
        multiIntervalSet.insert(1,2,"first");
        expectedLabels.add("first");
        assertEquals(expectedLabels, multiIntervalSet.labels());
        assertEquals(1, multiIntervalSet.intervals("first").start(0));
        assertEquals(2, multiIntervalSet.intervals("first").end(0));
        //覆盖1个标签、3个时间段情况
        multiIntervalSet.insert(5,6, "first");
        multiIntervalSet.insert(10,12, "first");
        assertEquals(1, multiIntervalSet.intervals("first").start(0));
        assertEquals(2, multiIntervalSet.intervals("first").end(0));
        assertEquals(5, multiIntervalSet.intervals("first").start(1));
        assertEquals(6, multiIntervalSet.intervals("first").end(1));
        assertEquals(10, multiIntervalSet.intervals("first").start(2));
        assertEquals(12, multiIntervalSet.intervals("first").end(2));
        assertEquals(expectedLabels, multiIntervalSet.labels());
        //覆盖3个标签的情况
        multiIntervalSet.insert(3,4,"second");
        multiIntervalSet.insert(14,15, "third");
        expectedLabels.add("second");
        expectedLabels.add("third");
        assertEquals(expectedLabels, multiIntervalSet.labels());
        assertEquals(3, multiIntervalSet.intervals("second").start(0));
        assertEquals(4, multiIntervalSet.intervals("second").end(0));
        assertEquals(14, multiIntervalSet.intervals("third").start(0));
        assertEquals(15, multiIntervalSet.intervals("third").end(0));
        //覆盖同标签下前部分与原有某时间段重叠的情况
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> multiIntervalSet.insert(5,8,"first"));
        assertEquals("Overlap exists", exception1.getMessage());
        //覆盖同标签下后部分与原有某时间段重叠的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> multiIntervalSet.insert(8,11,"first"));
        assertEquals("Overlap exists", exception2.getMessage());
        //覆盖同标签下欲插入时间段为原有某时间段子集的情况
        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> multiIntervalSet.insert(10,11,"first"));
        assertEquals("Overlap exists", exception3.getMessage());
        //覆盖同标签下欲插入时间段覆盖原有某时间段的情况
        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> multiIntervalSet.insert(0,8,"first"));
        assertEquals("Overlap exists", exception4.getMessage());
        //覆盖不同标签下前部分与原有某时间段重叠的情况
        Exception exception5 = assertThrows(IllegalArgumentException.class, () -> multiIntervalSet.insert(5,8,"forth"));
        assertEquals("Overlap exists", exception5.getMessage());
        //覆盖不同标签下后部分与原有某时间段重叠的情况
        Exception exception6 = assertThrows(IllegalArgumentException.class, () -> multiIntervalSet.insert(8,11,"forth"));
        assertEquals("Overlap exists", exception6.getMessage());
        //覆盖不同标签下时间段为原有某时间段子集的情况
        Exception exception7 = assertThrows(IllegalArgumentException.class, () -> multiIntervalSet.insert(10,11,"forth"));
        assertEquals("Overlap exists", exception7.getMessage());
        //覆盖不同标签下时间段覆盖某时间段子集的情况
        Exception exception8 = assertThrows(IllegalArgumentException.class, () -> multiIntervalSet.insert(0,8,"forth"));
        assertEquals("Overlap exists", exception8.getMessage());
        //覆盖null标签的情况
        Exception exception9 = assertThrows(NullPointerException.class, () -> multiIntervalSet.insert(6,9,null));
        assertEquals("label can't be null", exception9.getMessage());
        //覆盖起始时间大于终止时间的情况
        Exception exception10 = assertThrows(IllegalArgumentException.class, () -> multiIntervalSet.insert(9,6,"first"));
        assertEquals("start can't larger than end", exception10.getMessage());
        //覆盖传入起始时间在限制时间之外的情况
        multiIntervalSet.insert(-3,0,"first");
        assertEquals(1, multiIntervalSet.intervals("first").start(0));
        assertEquals(2, multiIntervalSet.intervals("first").end(0));
        assertEquals(5, multiIntervalSet.intervals("first").start(1));
        assertEquals(6, multiIntervalSet.intervals("first").end(1));
        assertEquals(10, multiIntervalSet.intervals("first").start(2));
        assertEquals(12, multiIntervalSet.intervals("first").end(2));
        multiIntervalSet.insert(17,20,"first");
        assertEquals(1, multiIntervalSet.intervals("first").start(0));
        assertEquals(2, multiIntervalSet.intervals("first").end(0));
        assertEquals(5, multiIntervalSet.intervals("first").start(1));
        assertEquals(6, multiIntervalSet.intervals("first").end(1));
        assertEquals(10, multiIntervalSet.intervals("first").start(2));
        assertEquals(12, multiIntervalSet.intervals("first").end(2));
    }
}
