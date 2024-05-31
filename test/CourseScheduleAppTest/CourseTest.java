package CourseScheduleAppTest;

import CourseScheduleApp.Course;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CourseTest {

    /**
     * 测试策略：
     * 按正常特殊情况划分：
     *   正常情况：预期正常建立
     *   特殊情况：
     *      测试课程号为空白字符的情况：预期抛出IllegalArgumentException异常
     *      测试课程名称为空白字符的情况：预期抛出IllegalArgumentException异常
     *      测试教师姓名为空白字符的情况：预期抛出IllegalArgumentException异常
     *      测试地点为空白字符的情况：预期抛出IllegalArgumentException异常
     *      测试周学时数为奇数的情况：预期抛出IllegalArgumentException异常
     */
    @Test
    public void constructorTest() {
        //覆盖正常的情况
        Course course = new Course("12345", "abc", "xx", "zx",6);
        assertEquals("12345", course.getCourseID());
        assertEquals("abc", course.getCourseName());
        assertEquals("xx", course.getTeacherName());
        assertEquals(6, course.getWeekHour());
        //覆盖课程号为空白字符的情况
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> new Course("", "abc", "xx", "zx",6));
        assertEquals("courseID can't be blank", exception1.getMessage());
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> new Course("   ", "abc", "xx", "zx",6));
        assertEquals("courseID can't be blank", exception2.getMessage());
        //覆盖课程名称为空白字符的情况
        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> new Course("12345", "", "xx", "zx",6));
        assertEquals("courseName can't be blank", exception3.getMessage());
        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> new Course("12345", "   ", "xx", "zx",6));
        assertEquals("courseName can't be blank", exception4.getMessage());
        //覆盖教师姓名为空白字符的情况
        Exception exception5 = assertThrows(IllegalArgumentException.class, () -> new Course("12345", "abc", "", "zx",6));
        assertEquals("teacherName can't be blank", exception5.getMessage());
        Exception exception6 = assertThrows(IllegalArgumentException.class, () -> new Course("12345", "abc", "   ", "zx",6));
        assertEquals("teacherName can't be blank", exception6.getMessage());
        //覆盖地点为空白字符的情况
        Exception exception7 = assertThrows(IllegalArgumentException.class, () -> new Course("12345", "abc", "xx", "",6));
        assertEquals("location can't be blank", exception7.getMessage());
        Exception exception8 = assertThrows(IllegalArgumentException.class, () -> new Course("12345", "abc", "xx", "  ",6));
        assertEquals("location can't be blank", exception8.getMessage());
        //覆盖周学时数为奇数的情况
        Exception exception9 = assertThrows(IllegalArgumentException.class, () -> new Course("12345", "abc", "xx", "zx",3));
        assertEquals("weekHour must be even", exception9.getMessage());
    }

    /**
     * 测试是否正确返回课程号
     */
    @Test
    public void getCourseIDTest(){
        Course course = new Course("12345", "abc", "xx", "zx",6);
        assertEquals("12345", course.getCourseID());
    }

    /**
     * 测试是否正确返回课程名称
     */
    @Test
    public void getCourseNameTest(){
        Course course = new Course("12345", "abc", "xx", "zx",6);
        assertEquals("abc", course.getCourseName());
    }

    /**
     * 测试是否正确返回教师姓名
     */
    @Test
    public void getTeacherTest(){
        Course course = new Course("12345", "abc", "xx", "zx",6);
        assertEquals("xx", course.getTeacherName());
    }

    /**
     * 测试是否正确返回地点
     */
    @Test
    public void getLocationTest(){
        Course course = new Course("12345", "abc", "xx", "zx",6);
        assertEquals("zx", course.getLocation());
    }

    /**
     * 测试是否正确返回周学时数
     */
    @Test
    public void getWeekHourTest(){
        Course course = new Course("12345", "abc", "xx", "zx",6);
        assertEquals(6, course.getWeekHour());
    }
}
