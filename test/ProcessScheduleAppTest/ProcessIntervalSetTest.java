package ProcessScheduleAppTest;
import DutyRosterApp.IDutyIntervalSet;
import Interval.CommonIntervalSet;
import Interval.IntervalSet;
import Interval.MultiIntervalSet;
import ProcessScheduleApp.IProcessIntervalSet;
import ProcessScheduleApp.ProcessIntervalSet;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ProcessIntervalSetTest {
    public IProcessIntervalSet<String> emptyInstance() {
        return new ProcessIntervalSet<String>(0,Long.MAX_VALUE);
    }

    @Test(expected=AssertionError.class)
    public void AssertionEnabledTest() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void EmptyTest() {
        assertEquals(Collections.emptySet(), IProcessIntervalSet.empty().processes());
    }

    /**
     * 测试策略：
     * <p>
     * 1.按照同一标签下时间段个数划分：
     *    测试0个时间段的情况：预期集合为空
     *    测试1个时间段的情况：预期包含一个时间段
     *    测试3个时间段的情况：预期包含三个时间段
     * 2.按照进程数划分：
     *    测试0个进程的情况：预期集合为空
     *    测试1个进程的情况：预期包含一个进程
     *    测试3个进程的情况：预期包含三个进程
     * 3.按照测试同一进程所属时间段重叠的情况划分：
     *    测试前部分与原有某时间段重叠：预期本次插入无效
     *    测试后部分与原有某时间段重叠：预期本次插入无效
     *    测试欲插入时间段为原有某时间段子集：预期本次插入无效
     *    测试欲插入时间段覆盖原有某时间段：预期本次插入无效
     * 4.按照欲插入进程时间段与不同进程所属时间段重叠的情况划分：
     *    测试前部分与原有某时间段重叠：预期本次插入无效
     *    测试后部分与原有某时间段重叠：预期本次插入无效
     *    测试欲插入时间段为原有某时间段子集：预期本次插入无效
     *    测试欲插入时间段覆盖原有某时间段：预期本次插入无效
     * 4.特殊情况：
     *    测试传入进程为null的情况：预期返回NullPointerException
     *    测试传入的进程起始时间大于终止时间，预期返回IllegalArgumentException
     */
    @Test
    public void addProcessTest(){
        IProcessIntervalSet<String> processIntervalSet = emptyInstance();
        IntervalSet<Integer> expectedFirst = new CommonIntervalSet<>(0, 15);
        Set<String> expectedLabels = new HashSet<>();
        //覆盖0个进程、0个时间段情况
        assertEquals(expectedFirst, processIntervalSet.checkProgress("first"));
        assertEquals(expectedLabels, processIntervalSet.processes());
        //覆盖1个进程、1个时间段情况
        processIntervalSet.addProcess(1,2,"first");
        expectedLabels.add("first");
        assertEquals(expectedLabels, processIntervalSet.processes());
        assertEquals(1, processIntervalSet.checkProgress("first").start(0));
        assertEquals(2, processIntervalSet.checkProgress("first").end(0));
        //覆盖1个进程、3个时间段情况
        processIntervalSet.addProcess(5,6, "first");
        processIntervalSet.addProcess(10,12, "first");
        assertEquals(1, processIntervalSet.checkProgress("first").start(0));
        assertEquals(2, processIntervalSet.checkProgress("first").end(0));
        assertEquals(5, processIntervalSet.checkProgress("first").start(1));
        assertEquals(6, processIntervalSet.checkProgress("first").end(1));
        assertEquals(10, processIntervalSet.checkProgress("first").start(2));
        assertEquals(12, processIntervalSet.checkProgress("first").end(2));
        assertEquals(expectedLabels, processIntervalSet.processes());
        //覆盖3个进程的情况
        processIntervalSet.addProcess(3,4,"second");
        processIntervalSet.addProcess(14,15, "third");
        expectedLabels.add("second");
        expectedLabels.add("third");
        assertEquals(expectedLabels, processIntervalSet.processes());
        assertEquals(3, processIntervalSet.checkProgress("second").start(0));
        assertEquals(4, processIntervalSet.checkProgress("second").end(0));
        assertEquals(14, processIntervalSet.checkProgress("third").start(0));
        assertEquals(15, processIntervalSet.checkProgress("third").end(0));
        //覆盖同进程下前部分与原有某时间段重叠的情况
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> processIntervalSet.addProcess(5,8,"first"));
        assertEquals("Overlap exists", exception1.getMessage());
        //覆盖同进程下后部分与原有某时间段重叠的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> processIntervalSet.addProcess(8,11,"first"));
        assertEquals("Overlap exists", exception2.getMessage());
        //覆盖同进程下欲插入时间段为原有某时间段子集的情况
        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> processIntervalSet.addProcess(10,11,"first"));
        assertEquals("Overlap exists", exception3.getMessage());
        //覆盖同进程下欲插入时间段覆盖原有某时间段的情况
        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> processIntervalSet.addProcess(0,8,"first"));
        assertEquals("Overlap exists", exception4.getMessage());
        //覆盖不同进程下前部分与原有某时间段重叠的情况
        Exception exception5 = assertThrows(IllegalArgumentException.class, () -> processIntervalSet.addProcess(5,8,"forth"));
        assertEquals("Overlap exists", exception5.getMessage());
        //覆盖不同进程下后部分与原有某时间段重叠的情况
        Exception exception6 = assertThrows(IllegalArgumentException.class, () -> processIntervalSet.addProcess(8,11,"forth"));
        assertEquals("Overlap exists", exception6.getMessage());
        //覆盖不同进程下时间段为原有某时间段子集的情况
        Exception exception7 = assertThrows(IllegalArgumentException.class, () -> processIntervalSet.addProcess(10,11,"forth"));
        assertEquals("Overlap exists", exception7.getMessage());
        //覆盖不同进程下时间段覆盖某时间段子集的情况
        Exception exception8 = assertThrows(IllegalArgumentException.class, () -> processIntervalSet.addProcess(0,8,"forth"));
        assertEquals("Overlap exists", exception8.getMessage());
        //覆盖null进程的情况
        Exception exception9 = assertThrows(NullPointerException.class, () -> processIntervalSet.addProcess(6,9,null));
        assertEquals("label can't be null", exception9.getMessage());
        //覆盖起始时间大于终止时间的情况
        Exception exception10 = assertThrows(IllegalArgumentException.class, () -> processIntervalSet.addProcess(9,6,"first"));
        assertEquals("start can't larger than end", exception10.getMessage());
    }

    /**
     * 测试策略：
     * <p>
     * 按照进程个数划分：
     *      测试0个进程的情况：预期集合为空
     *      测试1个进程的情况：预期包含一个进程
     *      测试3个进程的情况：预期包含三个进程
     */
    @Test
    public void processesTest(){
        IProcessIntervalSet<String> processIntervalSet = emptyInstance();
        //覆盖0个进程的情况
        Set<String> expected = new HashSet<>();
        assertEquals(expected, processIntervalSet.processes());
        //覆盖1个进程的情况
        expected.add("first");
        processIntervalSet.addProcess(2,3,"first");
        assertEquals(expected, processIntervalSet.processes());
        //覆盖3个进程的情况
        expected.add("second");
        expected.add("third");
        processIntervalSet.addProcess(4,6,"second");
        processIntervalSet.addProcess(7,9,"third");
        assertEquals(expected, processIntervalSet.processes());
    }

    /**
     * 测试策略：
     * <p>
     * 按照同一进程时间段数划分：
     *     测试0个时间段的情况：预期为0
     *     测试1个时间段的情况：预期为该时间段数值
     *     测试3个时间段的情况：预期为所有时间段之和
     * 特殊情况：
     *     测试process为null的情况：预期抛出NullPointerException
     */
    @Test
    public void runTimeTest(){
        IProcessIntervalSet<String> processIntervalSet = emptyInstance();
        //覆盖0个时间段的情况
        assertEquals(0, processIntervalSet.runTime("first"));
        //覆盖1个时间段的情况
        processIntervalSet.addProcess(2,3,"first");
        assertEquals(2, processIntervalSet.runTime("first"));
        //覆盖3个时间段的情况
        processIntervalSet.addProcess(5,7,"first");
        processIntervalSet.addProcess(8,10,"first");
        assertEquals(8, processIntervalSet.runTime("first"));
        //覆盖process为null的情况
        Exception exception = assertThrows(NullPointerException.class, () -> processIntervalSet.runTime(null));
        assertEquals("progress can't be null", exception.getMessage());
    }

    /**
     * 测试策略：
     * <p>
     * 按照进程时间段数划分：
     *       测试0个时间段的情况：预期返回空集合
     *       测试1个时间段的情况：预期返回包含1个时间段的集合
     *       测试3个时间段的情况：预期返回包含3个时间段的集合
     * 特殊情况：
     *       时间段由大到小插入：预期返回从小到大的时间段的集合
     *       测试标签为null的情况：预期抛出NullPointerException异常
     */
    @Test
    public void checkProgressTest(){
        IProcessIntervalSet<String> processIntervalSet = emptyInstance();
        IntervalSet<Integer> expected = new CommonIntervalSet<>(0, 15);
        //覆盖0个时间段的情况
        assertEquals(expected, processIntervalSet.checkProgress("first"));
        //覆盖1个时间段的情况
        processIntervalSet.addProcess(8,9,"first");
        assertEquals(8, processIntervalSet.checkProgress("first").start(0));
        assertEquals(9, processIntervalSet.checkProgress("first").end(0));
        //覆盖3个时间段的情况（由大到小输入）
        processIntervalSet.addProcess(5,7,"first");
        assertEquals(5, processIntervalSet.checkProgress("first").start(0));
        assertEquals(7, processIntervalSet.checkProgress("first").end(0));
        assertEquals(8, processIntervalSet.checkProgress("first").start(1));
        assertEquals(9, processIntervalSet.checkProgress("first").end(1));
        processIntervalSet.addProcess(2,3,"first");
        assertEquals(2, processIntervalSet.checkProgress("first").start(0));
        assertEquals(3, processIntervalSet.checkProgress("first").end(0));
        assertEquals(5, processIntervalSet.checkProgress("first").start(1));
        assertEquals(7, processIntervalSet.checkProgress("first").end(1));
        assertEquals(8, processIntervalSet.checkProgress("first").start(2));
        assertEquals(9, processIntervalSet.checkProgress("first").end(2));
        //覆盖null的情况
        Exception exception = assertThrows(NullPointerException.class, () -> processIntervalSet.checkProgress(null));
        assertEquals("label can't be null", exception.getMessage());
    }

    /**
     * 测试是否修改成功限定的终止时间
     * 特殊情况：
     *      当over < begin时：预期抛出IllegalArgumentException异常
     *      当over = begin时：预期抛出IllegalArgumentException异常
     */
    @Test
    public void setOverTest(){
        IProcessIntervalSet<String> processIntervalSet = emptyInstance();
        //覆盖正常的情况
        processIntervalSet.setOver(5);
        //覆盖over < begin的情况
        Exception exception1 = assertThrows(IllegalArgumentException.class, () ->  processIntervalSet.setOver(-1));
        assertEquals("over must be larger than begin", exception1.getMessage());
        //覆盖begin = over的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () ->  processIntervalSet.setOver(0));
        assertEquals("over must be larger than begin", exception2.getMessage());
    }
}
