package DutyRosterAppTest;

import DutyRosterApp.DutyIntervalSet;
import DutyRosterApp.IDutyIntervalSet;
import Interval.CommonIntervalSet;
import Interval.IntervalSet;
import Visitor.CalcFreeTimeRatioInterval;
import Visitor.IntervalVisitor;
import delegationInterface.NoBlankIntervalSet.NoBlankIntervalSet;
import delegationInterface.NonOverlapIntervalSet.IntervalConflictException;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class DutyIntervalTest {
    private final long oneDay = 86400000;
    public IDutyIntervalSet<String> emptyInstance() {
        return new DutyIntervalSet<String>(0, 9 * oneDay);
    }

    @Test(expected=AssertionError.class)
    public void AssertionEnabledTest() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void InitialEmptyTest() {
        assertEquals(Collections.emptySet(), IDutyIntervalSet.empty(0,10).Employees());
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
        IDutyIntervalSet<String> iDutyIntervalSet = new DutyIntervalSet<String>(0, 9 * oneDay);
        assertEquals(0, iDutyIntervalSet.getBegin());
        assertEquals(10 * oneDay-1, iDutyIntervalSet.getOver());
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
     * 2.测试重复员工的情况：预期替换原排班时间段
     * 3.按照测试员工对应时间段重叠的情况划分：
     *    测试前部分与原有某时间段重叠：预期本次插入无效
     *    测试后部分与原有某时间段重叠：预期本次插入无效
     *    测试欲插入时间段为原有某时间段子集：预期本次插入无效
     *    测试欲插入时间段覆盖原有某时间段：预期本次插入无效
     * 4.特殊情况：
     *    测试传入员工为null的情况：预期返回NullPointerException异常
     *    测试传入的时间段在限定的时间段之外：预期插入无效
     */
    @Test
    public void addEmployeeTest() throws IntervalConflictException {
        IDutyIntervalSet<String> dutyIntervalSet = emptyInstance();
        //覆盖0个时间段的情况
        Set<String> expected = new HashSet<>();
        assertEquals(expected, dutyIntervalSet.Employees());
        //覆盖1个时间段的情况
        expected.add("first");
        dutyIntervalSet.addEmployee(2*oneDay,3*oneDay,"first");
        assertEquals(expected, dutyIntervalSet.Employees());
        assertEquals(2*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("first")[0]);
        assertEquals(4*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("first")[1]);
        assertEquals(2, dutyIntervalSet.checkEmployeeTime("first"));
        //覆盖3个时间段的情况
        expected.add("second");
        expected.add("third");
        dutyIntervalSet.addEmployee(5*oneDay,6*oneDay, "second");
        dutyIntervalSet.addEmployee(8*oneDay,9*oneDay, "third");
        assertEquals(expected, dutyIntervalSet.Employees());
        assertEquals(5*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("second")[0]);
        assertEquals(7*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("second")[1]);
        assertEquals(8*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("third")[0]);
        assertEquals(10*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("third")[1]);
        //覆盖重复标签的情况
        dutyIntervalSet.addEmployee(1*oneDay,2*oneDay, "first");
        assertEquals(expected, dutyIntervalSet.Employees());
        assertEquals(1*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("first")[0]);
        assertEquals(3*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("first")[1]);
        //覆盖前部分与原有某时间段重叠的情况
        assertThrows(IntervalConflictException.class, () -> dutyIntervalSet.addEmployee(2*oneDay, 3*oneDay,"forth"));
        assertEquals(expected, dutyIntervalSet.Employees());
        assertEquals(1*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("first")[0]);
        assertEquals(3*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("first")[1]);
        assertEquals(5*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("second")[0]);
        assertEquals(7*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("second")[1]);
        assertEquals(8*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("third")[0]);
        assertEquals(10*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("third")[1]);
        //覆盖后部分与原有某时间段重叠的情况
        assertThrows(IntervalConflictException.class, () -> dutyIntervalSet.addEmployee(3*oneDay, 5*oneDay,"forth" ));
        assertEquals(expected, dutyIntervalSet.Employees());
        assertEquals(1*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("first")[0]);
        assertEquals(3*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("first")[1]);
        assertEquals(5*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("second")[0]);
        assertEquals(7*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("second")[1]);
        assertEquals(8*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("third")[0]);
        assertEquals(10*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("third")[1]);
        //覆盖欲插入时间段为原有某时间段子集的情况
        assertThrows(IntervalConflictException.class, () -> dutyIntervalSet.addEmployee(0*oneDay, 3*oneDay, "forth"));
        assertEquals(expected, dutyIntervalSet.Employees());
        assertEquals(1*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("first")[0]);
        assertEquals(3*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("first")[1]);
        assertEquals(5*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("second")[0]);
        assertEquals(7*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("second")[1]);
        assertEquals(8*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("third")[0]);
        assertEquals(10*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("third")[1]);
        //覆盖欲插入时间段覆盖原有某时间段的情况
        assertThrows(IntervalConflictException.class, () -> dutyIntervalSet.addEmployee(1*oneDay, 1*oneDay,"forth"));
        assertEquals(expected, dutyIntervalSet.Employees());
        assertEquals(1*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("first")[0]);
        assertEquals(3*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("first")[1]);
        assertEquals(5*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("second")[0]);
        assertEquals(7*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("second")[1]);
        assertEquals(8*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("third")[0]);
        assertEquals(10*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("third")[1]);
        //覆盖null标签的情况
        Exception exception1 = assertThrows(NullPointerException.class, () -> dutyIntervalSet.addEmployee(6*oneDay,7*oneDay, null));
        assertEquals("label can't be null", exception1.getMessage());
        //覆盖传入的时间段在限定的时间段之外的情况
        dutyIntervalSet.addEmployee(-1*oneDay, 1*oneDay,"forth");
        assertEquals(expected, dutyIntervalSet.Employees());
        assertEquals(1*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("first")[0]);
        assertEquals(3*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("first")[1]);
        assertEquals(5*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("second")[0]);
        assertEquals(7*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("second")[1]);
        assertEquals(8*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("third")[0]);
        assertEquals(10*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("third")[1]);
        dutyIntervalSet.addEmployee(11*oneDay,14*oneDay, "forth");
        assertEquals(expected, dutyIntervalSet.Employees());
        assertEquals(1*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("first")[0]);
        assertEquals(3*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("first")[1]);
        assertEquals(5*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("second")[0]);
        assertEquals(7*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("second")[1]);
        assertEquals(8*oneDay, dutyIntervalSet.checkEmployeeStartToEnd("third")[0]);
        assertEquals(10*oneDay-1, dutyIntervalSet.checkEmployeeStartToEnd("third")[1]);

    }

    /**
     * 测试策略：
     * <p>
     * 按照时间段个数划分：
     *      测试0个时间段的情况：预期集合为空
     *      测试1个时间段的情况：预期包含一个员工
     *      测试3个时间段的情况：预期包含三个员工
     */
    @Test
    public void EmployeesTest() throws IntervalConflictException {
        IDutyIntervalSet<String> iDutyIntervalSet = emptyInstance();
        //覆盖0个时间段的情况
        Set<String> expected = new HashSet<>();
        assertEquals(expected, iDutyIntervalSet.Employees());
        //覆盖1个时间段的情况
        expected.add("first");
        iDutyIntervalSet.addEmployee(2*oneDay,3*oneDay,"first");
        assertEquals(expected, iDutyIntervalSet.Employees());
        //覆盖3个时间段的情况
        expected.add("second");
        expected.add("third");
        iDutyIntervalSet.addEmployee(5*oneDay, 7*oneDay, "second");
        iDutyIntervalSet.addEmployee(8*oneDay,9*oneDay,"third");
        assertEquals(expected, iDutyIntervalSet.Employees());
    }

    /**
     * 测试策略：
     * <p>
     * 按照员工存在性划分：
     *       测试员工存在于排班表的情况：预期成功删除并返回true
     *       测试员工不存在于排班表的情况：预期与原集合无区别并返回false
     * 特殊情况：
     *       测试员工为null的情况：预期抛出NullPointerException异常
     */
    @Test
    public void removeEmployeeTest() throws IntervalConflictException {
        IDutyIntervalSet<String> iDutyIntervalSet = emptyInstance();
        Set<String> expected = new HashSet<>();
        iDutyIntervalSet.addEmployee(2*oneDay,3*oneDay, "first");
        iDutyIntervalSet.addEmployee(5*oneDay,7*oneDay,"second");
        iDutyIntervalSet.addEmployee(8*oneDay,9*oneDay, "third");
        expected.add("first");
        expected.add("second");
        expected.add("third");
        assertEquals(expected, iDutyIntervalSet.Employees());
        //覆盖员工存在于时间段集合的情况
        expected.remove("second");
        assertTrue(iDutyIntervalSet.removeEmployee("second"));
        assertEquals(expected, iDutyIntervalSet.Employees());
        assertNull(iDutyIntervalSet.checkEmployeeStartToEnd("second"));
        //覆盖员工不存在于时间段集合的情况
        assertFalse(iDutyIntervalSet.removeEmployee("second"));
        //覆盖员工为null的情况
        Exception exception2 = assertThrows(NullPointerException.class, () -> iDutyIntervalSet.removeEmployee(null));
        assertEquals("label can't be null", exception2.getMessage());
    }

    /**
     * 测试策略：
     * <p>
     * 按照正常/特殊情况划分：
     *      测试正常情况：预期返回标签所属时间段起始时间
     *      特殊情况：
     *          测试标签为null的情况：预期抛出NullPointerException异常
     *          测试标签不存在的情况：预期为null
     */
    @Test
    public void checkEmployeeStartToEndTest() throws IntervalConflictException {
        IDutyIntervalSet<String> iDutyIntervalSet = emptyInstance();
        Set<String> expected = new HashSet<>();
        iDutyIntervalSet.addEmployee(2*oneDay,3*oneDay,"first");
        iDutyIntervalSet.addEmployee(5*oneDay,7*oneDay, "second");
        iDutyIntervalSet.addEmployee(8*oneDay,9*oneDay,"third");
        expected.add("first");
        expected.add("second");
        expected.add("third");
        assertEquals(expected, iDutyIntervalSet.Employees());
        //覆盖正常情况
        assertEquals(2*oneDay, iDutyIntervalSet.checkEmployeeStartToEnd("first")[0]);
        assertEquals(4*oneDay-1, iDutyIntervalSet.checkEmployeeStartToEnd("first")[1]);
        assertEquals(5*oneDay, iDutyIntervalSet.checkEmployeeStartToEnd("second")[0]);
        assertEquals(8*oneDay-1, iDutyIntervalSet.checkEmployeeStartToEnd("second")[1]);
        assertEquals(8*oneDay, iDutyIntervalSet.checkEmployeeStartToEnd("third")[0]);
        assertEquals(10*oneDay-1, iDutyIntervalSet.checkEmployeeStartToEnd("third")[1]);
        //覆盖标签为null的情况
        Exception exception1 = assertThrows(NullPointerException.class, () -> iDutyIntervalSet.checkEmployeeStartToEnd(null));
        assertEquals("employee can't be null", exception1.getMessage());
        //覆盖员工不存在的情况
        assertNull(iDutyIntervalSet.checkEmployeeStartToEnd("forth"));
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
    public void checkEmployeeTimeTest() throws IntervalConflictException {
        IDutyIntervalSet<String> iDutyIntervalSet = emptyInstance();
        Set<String> expected = new HashSet<>();
        iDutyIntervalSet.addEmployee(2*oneDay,3*oneDay,"first");
        iDutyIntervalSet.addEmployee(5*oneDay,7*oneDay, "second");
        iDutyIntervalSet.addEmployee(8*oneDay,9*oneDay, "third");
        expected.add("first");
        expected.add("second");
        expected.add("third");
        assertEquals(expected, iDutyIntervalSet.Employees());
        //覆盖正常情况
        assertEquals(2, iDutyIntervalSet.checkEmployeeTime("first"));
        assertEquals(3, iDutyIntervalSet.checkEmployeeTime("second"));
        assertEquals(2, iDutyIntervalSet.checkEmployeeTime("third"));
        //覆盖标签为null的情况
        Exception exception1 = assertThrows(NullPointerException.class, () -> iDutyIntervalSet.checkEmployeeTime(null));
        assertEquals("label can't be null", exception1.getMessage());
        //覆盖员工不存在的情况
        Exception exception2 = assertThrows(RuntimeException.class, () -> iDutyIntervalSet.checkEmployeeTime("forth"));
        assertEquals("label doesn't exist", exception2.getMessage());
    }

    /**
     * 测试是否返回限定的起始时间
     */
    @Test
    public void getBeginTest(){
        IDutyIntervalSet<String> iDutyIntervalSet = emptyInstance();
        assertEquals(0*oneDay, iDutyIntervalSet.getBegin());
    }
    /**
     * 测试是否返回限定的终止时间
     */
    @Test
    public void getOverTest(){
        IDutyIntervalSet<String> iDutyIntervalSet = emptyInstance();
        assertEquals(10*oneDay-1, iDutyIntervalSet.getOver());
    }
    /**
     * 测试是否修改成功限定的起始时间
     * 特殊情况：
     *      当begin > over时：预期抛出IllegalArgumentException异常
     *      当begin = over时：预期抛出IllegalArgumentException异常
     */
    @Test
    public void setBeginTest(){
        IDutyIntervalSet<String> iDutyIntervalSet = emptyInstance();
        //覆盖正常的情况
        iDutyIntervalSet.setBegin(5*oneDay);
        assertEquals(5*oneDay, iDutyIntervalSet.getBegin());
        //覆盖begin > over的情况
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> iDutyIntervalSet.setBegin(15*oneDay));
        assertEquals("begin must be less than over", exception1.getMessage());
        //覆盖begin = over的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> iDutyIntervalSet.setBegin(11*oneDay));
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
        IDutyIntervalSet<String> iDutyIntervalSet = emptyInstance();
        //覆盖正常的情况
        iDutyIntervalSet.setOver(5*oneDay);
        assertEquals(5*oneDay, iDutyIntervalSet.getOver());
        //覆盖over < begin的情况
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> iDutyIntervalSet.setOver(-1*oneDay));
        assertEquals("over must be larger than begin", exception1.getMessage());
        //覆盖begin = over的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> iDutyIntervalSet.setOver(0*oneDay));
        assertEquals("over must be larger than begin", exception2.getMessage());
    }

    /**
     * 测试策略：
     * <p>
     * 1.按照时间段数划分：
     *      测试1个时间段的情况：预期正确计算空白比例
     *      测试3个时间段的情况：预期正确计算空白比例
     * 2.特殊情况：
     *      测试没有时间段的情况：预期返回1
     *      测试没有空白的情况：预期返回0
     */
    @Test
    public void CalcFreeTimeRatioTest() throws IntervalConflictException {
        IDutyIntervalSet<String> dutyIntervalSet = emptyInstance();
        IntervalVisitor<String> intervalVisitor = new CalcFreeTimeRatioInterval<>();
        //覆盖没有时间段的情况
        assertEquals(1, dutyIntervalSet.calculateFreeTime(), 0.0001);
        //覆盖1个时间段的情况
        dutyIntervalSet.addEmployee(0*oneDay,2*oneDay, "first");
        assertEquals(0.7, dutyIntervalSet.calculateFreeTime(), 0.0001);
        //覆盖3个时间段的情况
        dutyIntervalSet.addEmployee(6*oneDay,7*oneDay, "second");
        dutyIntervalSet.addEmployee(8*oneDay,9*oneDay, "third");
        assertEquals(0.3, dutyIntervalSet.calculateFreeTime(), 0.0001);
        //覆盖没有空白的情况
        dutyIntervalSet.addEmployee(3*oneDay,5*oneDay, "forth");
        assertEquals(0, dutyIntervalSet.calculateFreeTime(), 0.0001);
    }

    /**
     * 测试策略：
     * <p>
     * 1.按照限定时间段内是否存在空白划分：
     *       测试存在空白的情况：预期返回false
     *       测试不存在空白的情况：预期返回true
     */
    @Test
    public void checkNoBlankTest() throws IntervalConflictException {
        IDutyIntervalSet<String> dutyIntervalSet = emptyInstance();
        //覆盖存在空白的情况
        assertFalse(dutyIntervalSet.checkNoBlank());
        //覆盖不存在空白的情况
        dutyIntervalSet.addEmployee(0*oneDay, 4*oneDay,"first");
        dutyIntervalSet.addEmployee(5*oneDay, 9*oneDay,"second");
        assertTrue(dutyIntervalSet.checkNoBlank());
    }
}
