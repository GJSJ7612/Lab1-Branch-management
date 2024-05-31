package DutyRosterAppTest;

import DutyRosterApp.Employee;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class EmployeeTest {

    /**
     * 测试策略：
     * 按正常特殊情况划分：
     *   正常情况：预期正常建立
     *   特殊情况：
     *      测试姓名为空白字符的情况：预期抛出IllegalArgumentException异常
     *      测试职位为空白字符的情况：预期抛出IllegalArgumentException异常
     *      测试电话号码不为11位的情况：预期抛出IllegalArgumentException异常
     */
    @Test
    public void constructorTest(){
        //覆盖正常的情况
        Employee employee = new Employee("abc", "god", "12345678998");
        assertEquals("abc", employee.getName());
        assertEquals("god", employee.getOffice());
        assertEquals("12345678998", employee.getPhoneNumber());
        //覆盖姓名为空白字符的情况
        Exception exception1 = assertThrows(IllegalArgumentException.class, () -> new Employee("", "god", "12345678998"));
        assertEquals("name can't be blank", exception1.getMessage());
        Exception exception2 = assertThrows(IllegalArgumentException.class, () -> new Employee("   ", "god", "12345678998"));
        assertEquals("name can't be blank", exception1.getMessage());
        //覆盖姓名为空白字符的情况
        Exception exception3 = assertThrows(IllegalArgumentException.class, () -> new Employee("abc", "", "12345678998"));
        assertEquals("office can't be blank", exception3.getMessage());
        Exception exception4 = assertThrows(IllegalArgumentException.class, () -> new Employee("abc", "    ", "12345678998"));
        assertEquals("office can't be blank", exception4.getMessage());
        //覆盖电话号码不为11的情况
        Exception exception5 = assertThrows(IllegalArgumentException.class, () -> new Employee("abc", "god", "123"));
        assertEquals("The phone number is not compliant", exception5.getMessage());
        Exception exception6 = assertThrows(IllegalArgumentException.class, () -> new Employee("abc", "god", "1248845678998"));
        assertEquals("The phone number is not compliant", exception6.getMessage());
        Exception exception7 = assertThrows(IllegalArgumentException.class, () -> new Employee("abc", "god", "sdfs"));
        assertEquals("The phone number is not compliant", exception7.getMessage());
    }

    /**
     * 测试是否正确返回员工姓名
     */
    @Test
    public void getNameTest(){
        Employee employee = new Employee("abc", "god", "12345678998");
        assertEquals("abc", employee.getName());
    }


    /**
     * 测试是否正确返回员工职务
     */
    @Test
    public void getOfficeTest(){
        Employee employee = new Employee("abc", "god", "12345678998");
        assertEquals("god", employee.getOffice());
    }


    /**
     * 测试是否正确返回员工手机号
     */
    @Test
    public void getPhoneNumberTest(){
        Employee employee = new Employee("abc", "god", "12345678998");
        assertEquals("12345678998", employee.getPhoneNumber());
    }


}
