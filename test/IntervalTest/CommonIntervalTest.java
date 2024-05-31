package IntervalTest;

import Interval.CommonIntervalSet;
import Interval.IntervalSet;
import Visitor.CalcConflictRatioInterval;
import Visitor.IntervalVisitor;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class CommonIntervalTest {
    //设定初始限定时间为0-10
    public IntervalSet<String> emptyInstance() {
        return new CommonIntervalSet<>(0,10);
    }

    @Test(expected=AssertionError.class)
    public void AssertionEnabledTest() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /**
     * 测试是否正常建立
     */
    @Test
    public void emptyTest(){
        assertEquals(Collections.emptySet(), IntervalSet.empty(0,10).labels());
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
    public void constructorTest(){
        //覆盖begin = over的情况
        IntervalSet<String> intervalSet = new CommonIntervalSet<>(0, 10);
        assertEquals(0, intervalSet.getBegin());
        assertEquals(10, intervalSet.getOver());
        //覆盖begin > over的情况
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> new CommonIntervalSet<>(10, 0));
        assertEquals("begin must be less than over", exception1.getMessage());
        //覆盖begin = over的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> new CommonIntervalSet<>(0, 0));
        assertEquals("begin must be less than over", exception2.getMessage());
    }
    /**
     * 测试策略：
     * <p>
     * 1.按照时间段个数划分：
     *    测试0个时间段的情况：预期集合为空
     *    测试1个时间段的情况：预期包含一个标签
     *    测试3个时间段的情况：预期包含三个标签
     * 2.测试重复标签的情况：预期替换原标签所属时间段
     * 3.特殊情况：
     *    测试传入标签为null的情况：预期返回NullPointerException
     *    测试传入的起始时间大于终止时间，预期返回IllegalArgumentException
     *    测试传入的时间段在限定的时间段之外，预期插入无效
     */
    @Test
    public void insertTest(){
        IntervalSet<String> intervalSet = emptyInstance();
        //覆盖0个时间段的情况
        Set<String> expected = new HashSet<>();
        assertEquals(expected, intervalSet.labels());
        //覆盖1个时间段的情况
        expected.add("first");
        intervalSet.insert(2,5,"first");
        assertEquals(expected, intervalSet.labels());
        assertEquals(2, intervalSet.start("first"));
        assertEquals(5, intervalSet.end("first"));
        //覆盖3个时间段的情况
        expected.add("second");
        expected.add("third");
        intervalSet.insert(3,6,"second");
        intervalSet.insert(4,7,"third");
        assertEquals(expected, intervalSet.labels());
        assertEquals(3, intervalSet.start("second"));
        assertEquals(6, intervalSet.end("second"));
        assertEquals(4, intervalSet.start("third"));
        assertEquals(7, intervalSet.end("third"));
        //覆盖重复标签的情况
        intervalSet.insert(5,8,"first");
        assertEquals(expected, intervalSet.labels());
        assertEquals(5, intervalSet.start("first"));
        assertEquals(8, intervalSet.end("first"));
        //覆盖null标签的情况
        Exception exception1 = assertThrows(NullPointerException.class, () -> intervalSet.insert(6,9,null));
        assertEquals("label can't be null", exception1.getMessage());
        //覆盖起始时间大于终止时间的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> intervalSet.insert(9,6,"first"));
        assertEquals("start can't larger than end", exception2.getMessage());
        //覆盖传入的时间段在限定的时间段之外的情况
        intervalSet.insert(-1, 5,"forth");
        assertEquals(expected, intervalSet.labels());
        assertEquals(5, intervalSet.start("first"));
        assertEquals(8, intervalSet.end("first"));
        assertEquals(3, intervalSet.start("second"));
        assertEquals(6, intervalSet.end("second"));
        assertEquals(4, intervalSet.start("third"));
        assertEquals(7, intervalSet.end("third"));
        intervalSet.insert(4, 15,"forth");
        assertEquals(expected, intervalSet.labels());
        assertEquals(5, intervalSet.start("first"));
        assertEquals(8, intervalSet.end("first"));
        assertEquals(3, intervalSet.start("second"));
        assertEquals(6, intervalSet.end("second"));
        assertEquals(4, intervalSet.start("third"));
        assertEquals(7, intervalSet.end("third"));
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
        IntervalSet<String> intervalSet = emptyInstance();
        //覆盖0个时间段的情况
        Set<String> expected = new HashSet<>();
        assertEquals(expected, intervalSet.labels());
        //覆盖1个时间段的情况
        expected.add("first");
        intervalSet.insert(2,5,"first");
        assertEquals(expected, intervalSet.labels());
        //覆盖3个时间段的情况
        expected.add("second");
        expected.add("third");
        intervalSet.insert(3,6,"second");
        intervalSet.insert(4,7,"third");
        assertEquals(expected, intervalSet.labels());
    }

    /**
     * 测试策略：
     * <p>
     * 按照标签存在性划分：
     *       测试标签存在于时间段集合的情况：预期成功删除并返回true
     *       测试标签不存在于时间段集合的情况：预期与原集合无区别并返回false
     * 特殊情况：
     *       测试标签为null的情况：预期抛出NullPointerException异常
     */
    @Test
    public void removeTest(){
        IntervalSet<String> intervalSet = emptyInstance();
        Set<String> expected = new HashSet<>();
        intervalSet.insert(2,5,"first");
        intervalSet.insert(3,6,"second");
        intervalSet.insert(4,7,"third");
        expected.add("first");
        expected.add("second");
        expected.add("third");
        assertEquals(expected, intervalSet.labels());
        //覆盖标签存在于时间段集合的情况
        expected.remove("second");
        assertTrue(intervalSet.remove("second"));
        assertEquals(expected, intervalSet.labels());
        Exception exception1 = assertThrows(RuntimeException.class, () -> intervalSet.start("second"));
        assertEquals("label doesn't exist", exception1.getMessage());
        Exception exception2 = assertThrows(RuntimeException.class, () -> intervalSet.end("second"));
        assertEquals("label doesn't exist", exception2.getMessage());
        //覆盖标签不存在于时间段集合的情况
        assertFalse(intervalSet.remove("second"));
        //覆盖标签为null的情况
        Exception exception3 = assertThrows(NullPointerException.class, () -> intervalSet.remove(null));
        assertEquals("label can't be null", exception3.getMessage());
    }

    /**
     * 测试策略：
     * <p>
     * 按照正常/特殊情况划分：
     *      测试正常情况：预期返回标签所属时间段起始时间
     *      特殊情况：
     *          测试标签为null的情况：预期抛出NullPointerException异常
     *          测试标签不存在的情况：预期抛出RuntimeException异常
     */
    @Test
    public void startTest(){
        IntervalSet<String> intervalSet = emptyInstance();
        Set<String> expected = new HashSet<>();
        intervalSet.insert(2,5,"first");
        intervalSet.insert(3,6,"second");
        intervalSet.insert(4,7,"third");
        expected.add("first");
        expected.add("second");
        expected.add("third");
        assertEquals(expected, intervalSet.labels());
        //覆盖正常情况
        assertEquals(2, intervalSet.start("first"));
        assertEquals(3, intervalSet.start("second"));
        assertEquals(4, intervalSet.start("third"));
        //覆盖标签为null的情况
        Exception exception1 = assertThrows(NullPointerException.class, () -> intervalSet.start(null));
        assertEquals("label can't be null", exception1.getMessage());
        //覆盖标签不存在的情况
        Exception exception2 = assertThrows(RuntimeException.class, () -> intervalSet.start("forth"));
        assertEquals("label doesn't exist", exception2.getMessage());
    }

    /**
     * 测试策略：
     * <p>
     * 按照正常/特殊情况划分：
     *      测试正常情况：预期返回标签所属时间段终止时间
     *      特殊情况：
     *          测试标签为null的情况：预期抛出NullPointerException异常
     *          测试标签不存在的情况：预期抛出RuntimeException异常
     */
    @Test
    public void endTest(){
        IntervalSet<String> intervalSet = emptyInstance();
        Set<String> expected = new HashSet<>();
        intervalSet.insert(2,5,"first");
        intervalSet.insert(3,6,"second");
        intervalSet.insert(4,7,"third");
        expected.add("first");
        expected.add("second");
        expected.add("third");
        assertEquals(expected, intervalSet.labels());
        //覆盖正常情况
        assertEquals(5, intervalSet.end("first"));
        assertEquals(6, intervalSet.end("second"));
        assertEquals(7, intervalSet.end("third"));
        //覆盖标签为null的情况
        Exception exception1 = assertThrows(NullPointerException.class, () -> intervalSet.end(null));
        assertEquals("label can't be null", exception1.getMessage());
        //覆盖起始时间大于终止时间的情况
        Exception exception2 = assertThrows(RuntimeException.class, () -> intervalSet.end("forth"));
        assertEquals("label doesn't exist", exception2.getMessage());
    }

    /**
     * 测试是否返回限定的起始时间
     */
    @Test
    public void getBeginTest(){
        IntervalSet<String> intervalSet = emptyInstance();
        assertEquals(0, intervalSet.getBegin());
    }
    /**
     * 测试是否返回限定的终止时间
     */
    @Test
    public void getOverTest(){
        IntervalSet<String> intervalSet = emptyInstance();
        assertEquals(10, intervalSet.getOver());
    }
    /**
     * 测试是否修改成功限定的起始时间
     * 特殊情况：
     *      当begin > over时：预期抛出IllegalArgumentException异常
     *      当begin = over时：预期抛出IllegalArgumentException异常
     */
    @Test
    public void setBeginTest(){
        IntervalSet<String> intervalSet = emptyInstance();
        //覆盖正常的情况
        intervalSet.setBegin(5);
        assertEquals(5, intervalSet.getBegin());
        //覆盖begin > over的情况
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> intervalSet.setBegin(15));
        assertEquals("begin must be less than over", exception1.getMessage());
        //覆盖begin = over的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> intervalSet.setBegin(10));
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
        IntervalSet<String> intervalSet = emptyInstance();
        //覆盖正常的情况
        intervalSet.setOver(5);
        assertEquals(5, intervalSet.getOver());
        //覆盖over < begin的情况
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> intervalSet.setOver(-1));
        assertEquals("over must be larger than begin", exception1.getMessage());
        //覆盖begin = over的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> intervalSet.setOver(0));
        assertEquals("over must be larger than begin", exception2.getMessage());
    }


    /**
     * Testing Strategy:
     * <p>
     *     按照时间段数划分：
     *        测试0个时间段的情况：预期正常建立
     *        测试1个时间段的情况：预期正常建立
     *        测试3个时间段的情况：预期正常建立
     */
    @Test
    public void toStringTest(){
        //覆盖个时间段的情况
        IntervalSet<String> intervalSet = emptyInstance();
        String expected = "There are 0 intervals in the intervalSet\n";
        assertEquals(expected, intervalSet.toString());
        //覆盖1个时间段的情况
        intervalSet.insert(2,5,"first");
        expected = "There are 1 intervals in the intervalSet\n" +
                "first : 2->5\n";
        assertEquals(expected, intervalSet.toString());
        //覆盖3个时间段的情况
        intervalSet.insert(3,6,"second");
        intervalSet.insert(4,7,"third");
        expected = "There are 3 intervals in the intervalSet\n" +
                "first : 2->5\n" +
                "second : 3->6\n" +
                "third : 4->7\n";
        assertEquals(expected, intervalSet.toString());
    }

    /**
     * Testing Strategy:
     * <p>
     * 测试hash是否满足行为等价性
     */
    @Test
    public void HashTest(){
        IntervalSet<String> intervalSet1 = new CommonIntervalSet<>(0, 10);
        IntervalSet<String> intervalSet2 = new CommonIntervalSet<>(0, 10);
        assertNotEquals(intervalSet1.hashCode(), intervalSet2.hashCode());
    }

    /**
     * Testing Strategy:
     * <p>
     * 测试equals是否满足行为等价性
     */
    @Test
    public void EqualsTest(){
        IntervalSet<String> intervalSet1 = new CommonIntervalSet<>(0, 10);
        IntervalSet<String> intervalSet2 = new CommonIntervalSet<>(0, 10);
        intervalSet1.insert(2,4,"first");
        intervalSet2.insert(3,4,"second");
        assertNotEquals(intervalSet1, intervalSet2);
    }

    /**
     * 测试visitor是否正常传递
     */
    @Test
    public void acceptTest(){
        IntervalVisitor<String> visitor = new CalcConflictRatioInterval<>();
        IntervalSet<String> intervalSet = emptyInstance();
        intervalSet.accept(visitor);
    }
}
