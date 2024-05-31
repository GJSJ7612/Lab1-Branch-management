package CourseScheduleAppTest;

import CourseScheduleApp.CourseIntervalSet;
import CourseScheduleApp.ICourseIntervalSet;
import DutyRosterApp.IDutyIntervalSet;
import Interval.CommonIntervalSet;
import Interval.IntervalSet;
import Interval.MultiIntervalSet;
import IntervalTest.CommonMultiIntervalTest;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class CourseIntervalSetTest{

    public ICourseIntervalSet<String> emptyInstance() {
        return new CourseIntervalSet<>(0, 69, 18);
    }

    @Test(expected=AssertionError.class)
    public void AssertionEnabledTest() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void InitialEmptyTest() {
        assertEquals(Collections.emptySet(), ICourseIntervalSet.empty(0,18).courses());
    }

    /**
     * 测试策略：
     * <p>
     * 1.按照同一课程时间段个数划分：
     *    测试0个时间段的情况：预期集合为空
     *    测试1个时间段的情况：预期包含一个时间段
     *    测试3个时间段的情况：预期包含三个时间段
     * 2.按照课程数划分：
     *    测试0个课程的情况：预期集合为空
     *    测试1个课程的情况：预期包含一个课程
     *    测试3个课程的情况：预期包含三个课程
     * 3.按照同一课程所属时间段重叠的情况
     * 4.特殊情况：
     *    测试传入标签为null的情况：预期返回NullPointerException
     *    测试起始时间不为偶数的情况：预期返回IllegalArgumentException
     *    测试起始时间在限制时间之外的情况：
     */
    @Test
    public void addCourseTest(){
        ICourseIntervalSet<String> courseIntervalSet = emptyInstance();
        IntervalSet<Integer> expectedFirst = new CommonIntervalSet<>(0, 69);
        Set<String> expectedLabels = new HashSet<>();
        //覆盖0个标签、0个节点情况
        assertEquals(expectedFirst, courseIntervalSet.checkCourse("first"));
        assertEquals(expectedLabels, courseIntervalSet.courses());
        //覆盖1个标签、1个时间段情况
        courseIntervalSet.addCourse(2,"first");
        expectedLabels.add("first");
        assertEquals(expectedLabels, courseIntervalSet.courses());
        assertEquals(2, courseIntervalSet.checkCourse("first").start(0));
        assertEquals(3, courseIntervalSet.checkCourse("first").end(0));
        //覆盖1个标签、3个时间段情况（插入的时间段start等于某已有时间段的end）
        courseIntervalSet.addCourse(4, "first");
        courseIntervalSet.addCourse(6, "first");
        assertEquals(2, courseIntervalSet.checkCourse("first").start(0));
        assertEquals(3, courseIntervalSet.checkCourse("first").end(0));
        assertEquals(4, courseIntervalSet.checkCourse("first").start(1));
        assertEquals(5, courseIntervalSet.checkCourse("first").end(1));
        assertEquals(6, courseIntervalSet.checkCourse("first").start(2));
        assertEquals(7, courseIntervalSet.checkCourse("first").end(2));
        assertEquals(expectedLabels, courseIntervalSet.courses());
        //覆盖3个标签的情况
        courseIntervalSet.addCourse(4,"second");
        courseIntervalSet.addCourse(8, "third");
        expectedLabels.add("second");
        expectedLabels.add("third");
        assertEquals(expectedLabels, courseIntervalSet.courses());
        assertEquals(4, courseIntervalSet.checkCourse("second").start(0));
        assertEquals(5, courseIntervalSet.checkCourse("second").end(0));
        assertEquals(8, courseIntervalSet.checkCourse("third").start(0));
        assertEquals(9, courseIntervalSet.checkCourse("third").end(0));
        //覆盖同一课程所属时间段重叠的情况
        courseIntervalSet.addCourse(6,"first");
        assertEquals(2, courseIntervalSet.checkCourse("first").start(0));
        assertEquals(3, courseIntervalSet.checkCourse("first").end(0));
        assertEquals(4, courseIntervalSet.checkCourse("first").start(1));
        assertEquals(5, courseIntervalSet.checkCourse("first").end(1));
        assertEquals(6, courseIntervalSet.checkCourse("first").start(2));
        assertEquals(7, courseIntervalSet.checkCourse("first").end(2));
        //覆盖null标签的情况
        Exception exception1 = assertThrows(NullPointerException.class, () -> courseIntervalSet.addCourse(6,null));
        assertEquals("label can't be null", exception1.getMessage());
        //覆盖起始时间不为偶数的情况
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> courseIntervalSet.addCourse(9,"first"));
        assertEquals("start must be even", exception2.getMessage());
    }

    /**
     * 测试策略：
     * <p>
     * 按照课程存在性划分：
     *       测试课程存在于课表的情况：预期成功删除并返回true
     *       测试课程不存在于课表的情况：预期与原集合无区别并返回false
     * 按照时间段数划分：
     *       测试课程所属时间段个数为1的情况：预期成功删除并返回true
     *       测试课程所属时间段个数为3的情况：预期成功删除并返回true
     * 特殊情况：
     *       测试标签为null的情况：预期抛出NullPointerException异常
     */
    @Test
    public void removeCourseTest(){
        ICourseIntervalSet<String> courseIntervalSet = emptyInstance();
        Set<String> expectedSet = new HashSet<>();
        IntervalSet<Integer> expectedInterval = new CommonIntervalSet<>(0, 69);
        //覆盖标签不存在的情况
        assertFalse(courseIntervalSet.removeCourse("second"));
        //覆盖标签存在的情况（所属时间段个数为1）
        courseIntervalSet.addCourse(2,"first");
        expectedSet.add("first");
        assertEquals(expectedSet, courseIntervalSet.courses());
        assertEquals(2, courseIntervalSet.checkCourse("first").start(0));
        assertEquals(3, courseIntervalSet.checkCourse("first").end(0));
        assertTrue(courseIntervalSet.removeCourse("first"));
        expectedSet.remove("first");
        assertEquals(expectedSet, courseIntervalSet.courses());
        assertEquals(expectedInterval, courseIntervalSet.checkCourse("first"));
        //覆盖标签存在的情况（所属时间段个数为3）
        courseIntervalSet.addCourse(2,"first");
        courseIntervalSet.addCourse(6,"first");
        courseIntervalSet.addCourse(8,"first");
        expectedSet.add("first");
        assertEquals(expectedSet, courseIntervalSet.courses());
        assertEquals(2, courseIntervalSet.checkCourse("first").start(0));
        assertEquals(3, courseIntervalSet.checkCourse("first").end(0));
        assertEquals(6, courseIntervalSet.checkCourse("first").start(1));
        assertEquals(7, courseIntervalSet.checkCourse("first").end(1));
        assertEquals(8, courseIntervalSet.checkCourse("first").start(2));
        assertEquals(9, courseIntervalSet.checkCourse("first").end(2));
        assertTrue(courseIntervalSet.removeCourse("first"));
        expectedSet.remove("first");
        assertEquals(expectedSet, courseIntervalSet.courses());
        assertEquals(expectedInterval, courseIntervalSet.checkCourse("first"));
        //覆盖标签为null的情况
        Exception exception = assertThrows(NullPointerException.class, () -> courseIntervalSet.removeCourse(null));
        assertEquals("label can't be null", exception.getMessage());
    }

    /**
     * 测试策略：
     * <p>
     * 按照课程个数划分：
     *      测试0个课程的情况：预期集合为空
     *      测试1个课程的情况：预期包含一个课程
     *      测试3个课程的情况：预期包含三个课程
     */
    @Test
    public void coursesTest(){
        ICourseIntervalSet<String> courseIntervalSet = emptyInstance();
        //覆盖0个时间段的情况
        Set<String> expected = new HashSet<>();
        assertEquals(expected, courseIntervalSet.courses());
        //覆盖1个时间段的情况
        expected.add("first");
        courseIntervalSet.addCourse(2,"first");
        assertEquals(expected, courseIntervalSet.courses());
        //覆盖3个时间段的情况
        expected.add("second");
        expected.add("third");
        courseIntervalSet.addCourse(4,"second");
        courseIntervalSet.addCourse(6,"third");
        assertEquals(expected, courseIntervalSet.courses());
    }

    /**
     * 测试策略：
     * <p>
     * 按照课程关联时间段数划分：
     *       测试0个时间段的情况（标签不存在）：预期返回空集合
     *       测试1个时间段的情况：预期返回包含1个时间段的集合
     *       测试3个时间段的情况：预期返回包含3个时间段的集合
     * 特殊情况：
     *       时间段由大到小插入：预期返回从小到大的时间段的集合
     *       测试标签为null的情况：预期抛出NullPointerException异常
     */
    @Test
    public void checkCourseTest(){
        ICourseIntervalSet<String> courseIntervalSet = emptyInstance();
        IntervalSet<Integer> expected = new CommonIntervalSet<>(0, 69);
        //覆盖0个时间段的情况
        assertEquals(expected, courseIntervalSet.checkCourse("first"));
        //覆盖1个时间段的情况
        courseIntervalSet.addCourse(8,"first");
        assertEquals(8, courseIntervalSet.checkCourse("first").start(0));
        assertEquals(9, courseIntervalSet.checkCourse("first").end(0));
        //覆盖3个时间段的情况（由大到小输入）
        courseIntervalSet.addCourse(6,"first");
        assertEquals(6, courseIntervalSet.checkCourse("first").start(0));
        assertEquals(7, courseIntervalSet.checkCourse("first").end(0));
        assertEquals(8, courseIntervalSet.checkCourse("first").start(1));
        assertEquals(9, courseIntervalSet.checkCourse("first").end(1));
        courseIntervalSet.addCourse(2,"first");
        assertEquals(2, courseIntervalSet.checkCourse("first").start(0));
        assertEquals(3, courseIntervalSet.checkCourse("first").end(0));
        assertEquals(6, courseIntervalSet.checkCourse("first").start(1));
        assertEquals(7, courseIntervalSet.checkCourse("first").end(1));
        assertEquals(8, courseIntervalSet.checkCourse("first").start(2));
        assertEquals(9, courseIntervalSet.checkCourse("first").end(2));
        //覆盖null的情况
        Exception exception = assertThrows(NullPointerException.class, () -> courseIntervalSet.checkCourse(null));
        assertEquals("label can't be null", exception.getMessage());
    }

    /**
     * 测试是否修改成功限定的起始时间
     */
    @Test
    public void setBeginTest(){
        ICourseIntervalSet<String> courseIntervalSet = emptyInstance();
        //覆盖正常的情况
        courseIntervalSet.setBegin(5);
        assertEquals(5, courseIntervalSet.getBegin());
    }

    /**
     * 测试是否返回限定的起始时间
     */
    @Test
    public void getBeginTest(){
        ICourseIntervalSet<String> courseIntervalSet = emptyInstance();
        assertEquals(0, courseIntervalSet.getBegin());
    }

    /**
     * 测试是否修改成功学期周数
     */
    @Test
    public void setCycleTest(){
        ICourseIntervalSet<String> courseIntervalSet = emptyInstance();
        //覆盖正常的情况
        courseIntervalSet.setCycle(5);
        assertEquals(5, courseIntervalSet.getCycle());
    }

    /**
     * 测试是否返回学期周数
     */
    @Test
    public void getCycleTest(){
        ICourseIntervalSet<String> courseIntervalSet = emptyInstance();
        assertEquals(18, courseIntervalSet.getCycle());
    }
}
