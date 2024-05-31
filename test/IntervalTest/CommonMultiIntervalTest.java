package IntervalTest;

import Interval.CommonIntervalSet;
import Interval.CommonMultiIntervalSet;
import Interval.IntervalSet;
import Interval.MultiIntervalSet;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class CommonMultiIntervalTest extends MultiIntervalSetInstanceTest{
    @Override
    public MultiIntervalSet<String> emptyInstance() {
        return new CommonMultiIntervalSet<>(0,15);
    }

    /**
     * 测试策略：
     * 正常情况：
     *      当begin < over时：正常建立
     * 特殊情况：
     *      当begin > over时：预期抛出IllegalArgumentException异常
     *      当begin = over时：预期抛出IllegalArgumentException异常
     */
    @Test
    public void constructorTest1(){
        //覆盖begin = over的情况
        MultiIntervalSet<String> multiIntervalSet = new CommonMultiIntervalSet<>(0, 10);
        assertEquals(0, multiIntervalSet.getBegin());
        assertEquals(10, multiIntervalSet.getOver());
        //覆盖begin > over的情况
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> new CommonMultiIntervalSet<>(10, 0));
        assertEquals("begin must be less than over", exception1.getMessage());
        //覆盖begin = over的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> new CommonMultiIntervalSet<>(0, 0));
        assertEquals("begin must be less than over", exception2.getMessage());
    }

    /**
     * 测试策略：
     * <p>
     * 1.按照时间段个数划分：
     *      测试0个时间段的情况：预期集合为空
     *      测试1个时间段的情况：预期包含一个时间段
     *      测试3个时间段的情况：预期包含三个时间段
     * 2.特殊情况
     *      测试传入null的情况：预期抛出NullPointerException异常
     */
    @Test
    public void constructorTest2(){
        IntervalSet<String> intervalSet = new CommonIntervalSet<>(0, 10);
        Set<String> expected = new HashSet<>();
        //覆盖0个时间段的情况
        MultiIntervalSet<String> multiIntervalSet1 = new CommonMultiIntervalSet<>(intervalSet);
        assertEquals(expected, multiIntervalSet1.labels());
        //覆盖1个时间段的情况
        intervalSet.insert(2,5, "first");
        expected.add("first");
        MultiIntervalSet<String> multiIntervalSet2 = new CommonMultiIntervalSet<>(intervalSet);
        assertEquals(expected, multiIntervalSet2.labels());
        assertEquals(2, multiIntervalSet2.intervals("first").start(0));
        assertEquals(5, multiIntervalSet2.intervals("first").end(0));
        //覆盖3个时间段的情况
        intervalSet.insert(4,7, "second");
        intervalSet.insert(6,9, "third");
        expected.add("second");
        expected.add("third");
        MultiIntervalSet<String> multiIntervalSet3 = new CommonMultiIntervalSet<>(intervalSet);
        assertEquals(expected, multiIntervalSet3.labels());
        assertEquals(2, multiIntervalSet3.intervals("first").start(0));
        assertEquals(5, multiIntervalSet3.intervals("first").end(0));
        assertEquals(4, multiIntervalSet3.intervals("second").start(0));
        assertEquals(7, multiIntervalSet3.intervals("second").end(0));
        assertEquals(6, multiIntervalSet3.intervals("third").start(0));
        assertEquals(9, multiIntervalSet3.intervals("third").end(0));
        //覆盖null标签的情况
        Exception exception = assertThrows(NullPointerException.class, () -> new CommonMultiIntervalSet<>(null));
        assertEquals("IntervalSet can't be null", exception.getMessage());
    }

    /**
     * Testing Strategy:
     * <p>
     * 1.按照时间段个数划分：
     *     测试0个时间段的情况：预期集合为空
     *     测试1个时间段的情况：预期包含一个时间段
     *     测试3个时间段的情况：预期包含三个时间段
     * 2.按照标签数划分：
     *     测试0个标签的情况：预期集合为空
     *     测试1个标签的情况：预期包含一个标签
     *     测试3个标签的情况：预期包含三个标签
     */
    @Test
    public void toStringTest(){
        //覆盖0个时间段的情况（0个标签）
        MultiIntervalSet<String> intervalSet = emptyInstance();
        String expected = "There are 0 intervals in the intervalSet\n";
        assertEquals(expected, intervalSet.toString());
        //覆盖1个时间段的情况（1个标签）
        intervalSet.insert(2,3,"first");
        expected = "There are 1 intervals in the intervalSet\n" +
                "first : \n" +
                "\t2->3\n";
        assertEquals(expected, intervalSet.toString());
        //覆盖3个时间段的情况
        intervalSet.insert(4,6,"first");
        intervalSet.insert(8,9,"first");
        expected = "There are 3 intervals in the intervalSet\n" +
                "first : \n" +
                "\t2->3\n" +
                "\t4->6\n" +
                "\t8->9\n";
        assertEquals(expected, intervalSet.toString());
        //覆盖3个标签的情况
        intervalSet.insert(6,7,"second");
        intervalSet.insert(2,5,"second");
        intervalSet.insert(1,9,"third");
        expected = "There are 6 intervals in the intervalSet\n" +
                "first : \n" +
                "\t2->3\n" +
                "\t4->6\n" +
                "\t8->9\n" +
                "second : \n" +
                "\t2->5\n" +
                "\t6->7\n" +
                "third : \n" +
                "\t1->9\n";
        assertEquals(expected, intervalSet.toString());
    }

    /**
     * Testing Strategy:
     * <p>
     * 测试hash是否满足行为等价性
     */
    @Test
    public void HashTest(){
        MultiIntervalSet<String> multiIntervalSet1 = new CommonMultiIntervalSet<>(0,10);
        MultiIntervalSet<String> multiIntervalSet2 = new CommonMultiIntervalSet<>(0,10);
        assertNotEquals(multiIntervalSet1.hashCode(), multiIntervalSet2.hashCode());
    }

    /**
     * Testing Strategy:
     * <p>
     * 测试equals是否满足行为等价性
     */
    @Test
    public void EqualsTest(){
        MultiIntervalSet<String> multiIntervalSet1 = new CommonMultiIntervalSet<>(0,10);
        MultiIntervalSet<String> multiIntervalSet2 = new CommonMultiIntervalSet<>(0,10);
        multiIntervalSet1.insert(2,4,"first");
        multiIntervalSet2.insert(3,4,"second");
        assertNotEquals(multiIntervalSet1, multiIntervalSet2);
    }


}
