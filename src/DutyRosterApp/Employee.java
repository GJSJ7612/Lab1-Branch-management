package DutyRosterApp;

public class Employee {
    private final String name;
    private final String office;
    private final String phoneNumber;

    // Abstraction function:
    //   AF(DutyIntervalSet) = 代表一个员工对象，其中包括员工的姓名、职务与电话号码，不可以对员工的属性进行修改
    // Representation invariant:
        //   要求姓名、职位不能为空，电话为一个11位数
    // Safety from rep exposure:
    //   name、office、phoneNumber均被private和final修饰，oneDay为固定常数

    /**
     * 创建一个员工对象
     *
     * @param name String, 员工的姓名
     * @param office String, 员工的职务
     * @param phoneNumber String, 员工的电话号码
     */
    public Employee(String name, String office, String phoneNumber){
        if(name.isBlank()) throw new IllegalArgumentException("name can't be blank");
        if(office.isBlank()) throw new IllegalArgumentException("office can't be blank");
        if(!phoneNumber.matches("[0-9]{11}")) throw new IllegalArgumentException("The phone number is not compliant");
        this.name = name;
        this.office = office;
        this.phoneNumber = phoneNumber;
        checkRep();
    }

    /**
     * 检查表示不变量是否被保持。
     * 这个方法应该在构造器和修改内部状态的方法后被私有调用，以确保类的状态始终有效。
     */
    private void checkRep(){
        assert !name.isBlank();
        assert !office.isBlank();
        assert String.valueOf(phoneNumber).matches("[0-9]{11}");
    }

    /**
     * 获取当前员工对象的姓名
     *
     * @return String, 当前员工对象的姓名
     */
    public String getName() {
        checkRep();
        return name;
    }

    /**
     * 获取当前员工的职务
     *
     * @return String, 当前员工的职位
     */
    public String getOffice() {
        checkRep();
        return office;
    }


    /**
     * 获取当前员工的手机号
     *
     * @return String, 当前员工的手机号
     */
    public String getPhoneNumber() {
        checkRep();
        return phoneNumber;
    }

}
