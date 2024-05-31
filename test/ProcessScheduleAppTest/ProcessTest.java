package ProcessScheduleAppTest;

import ProcessScheduleApp.MyProcess;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ProcessTest {

    /**
     * 测试策略：
     * 按正常特殊情况划分：
     *   正常情况：预期正常建立
     *   特殊情况：
     *      测试PID为非5位以内的数字：预期抛出IllegalArgumentException异常
     *      测试进程名称为空白字符的情况：预期抛出IllegalArgumentException异常
     *      测试最小运行时间为负数的情况：预期抛出IllegalArgumentException异常
     *      测试最大运行时间为负数的情况：预期抛出IllegalArgumentException异常
     *      测试最小运行时间要大于最大运行时间的情况：预期抛出IllegalArgumentException异常
     */
    @Test
    public void constructorTest() {
        //覆盖正常的情况
        MyProcess process = new MyProcess(12345, "abc", 2, 5);
        assertEquals(12345, process.getPID());
        assertEquals("abc", process.getname());
        assertEquals(2, process.getminTime());
        assertEquals(5, process.getmaxTime());
        //覆盖PID为非5位的数字
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> new MyProcess(-1, "abc", 2,5));
        assertEquals("PID is not compliant", exception1.getMessage());
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> new MyProcess(123456, "abc", 2,5));
        assertEquals("PID is not compliant", exception2.getMessage());
        //覆盖进程名称为空白字符的情况
        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> new MyProcess(12345, "", 2,5));
        assertEquals("name can't be blank", exception3.getMessage());
        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> new MyProcess(12345, "   ", 2,5));
        assertEquals("name can't be blank", exception4.getMessage());
        //覆盖最小运行时间为负数的情况
        Exception exception5 = assertThrows(IllegalArgumentException.class, () -> new MyProcess(12345, "abc", -1,5));
        assertEquals("minTime can't be negative", exception5.getMessage());
        //覆盖最大运行时间为负数的情况
        Exception exception6 = assertThrows(IllegalArgumentException.class, () -> new MyProcess(12345, "abc", 2,-2));
        assertEquals("maxTime can't be negative", exception6.getMessage());
        //覆盖最小运行时间要大于最大运行时间的情况
        Exception exception7 = assertThrows(IllegalArgumentException.class, () -> new MyProcess(12345, "abc", 6,2));
        assertEquals("maxTime be equal or greater than minTime", exception7.getMessage());
    }

    /**
     * 测试是否正确返回进程号
     */
    @Test
    public void getPIDTest(){
        MyProcess process = new MyProcess(12345, "abc", 2, 5);
        assertEquals(12345, process.getPID());
    }

    /**
     * 测试是否正确返回进程名称
     */
    @Test
    public void getNameTest(){
        MyProcess process = new MyProcess(12345, "abc", 2, 5);
        assertEquals("abc", process.getname());
    }

    /**
     * 测试是否正确返回最小运行时间
     */
    @Test
    public void getMinTimeTest(){
        MyProcess process = new MyProcess(12345, "abc", 2, 5);
        assertEquals(2, process.getminTime());
    }

    /**
     * 测试是否正确返回最大运行时间
     */
    @Test
    public void getMaxTimeTest(){
        MyProcess process = new MyProcess(12345, "abc", 2, 5);
        assertEquals(5, process.getmaxTime());
    }
}
