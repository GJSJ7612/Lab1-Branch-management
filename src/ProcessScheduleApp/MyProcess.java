package ProcessScheduleApp;

public class MyProcess {
    private final int PID;
    private final String name;
    private final long minTime;
    private final long maxTime;


    // Abstraction function:
    //   AF(DutyIntervalSet) = 代表一个进程对象，其中包括进程的编号、名称、最小的运行时间与最大的运行时间，不可以对进程的属性进行修改
    //   一个进程可以在任意时刻被暂停，当进程的运行时间在最小与最大运行时间区间内该进程即可终止
    // Representation invariant:
    //   要求PID最高为5位数，进程名称不能为空，最大/最小运行时间必须为自然数,且最大运行时间要大于等于最小运行时间
    // Safety from rep exposure:
    //   PID、name、minTime、maxTime均被private和final修饰

    /**
     * 创建一个空的进程序列
     *
     * @param pid int ,进程号
     * @param name String, 进程的名称
     * @param minTime long, 进程的最小运行时间
     * @param maxTime long, 进程的最大运行时间
     */
    public MyProcess(int pid, String name, long minTime, long maxTime) {
        if(!String.valueOf(pid).matches("[0-9]{1,5}")) throw new IllegalArgumentException("PID is not compliant");
        if(name.isBlank()) throw new IllegalArgumentException("name can't be blank");
        if(minTime < 0) throw new IllegalArgumentException("minTime can't be negative");
        if(maxTime < 0) throw new IllegalArgumentException("maxTime can't be negative");
        if(minTime > maxTime) throw new IllegalArgumentException("maxTime be equal or greater than minTime");
        PID = pid;
        this.name = name;
        this.minTime = minTime;
        this.maxTime = maxTime;
        checkRep();
    }

    /**
     * 检查表示不变量是否被保持。
     * 这个方法应该在构造器和修改内部状态的方法后被私有调用，以确保类的状态始终有效。
     */
    private void checkRep(){
        assert String.valueOf(PID).matches("[0-9]{1,5}");
        assert !name.isBlank();
        assert minTime >= 0;
        assert maxTime >= 0;
        assert maxTime >= minTime;
    }

    /**
     * 获取当前进程的进程号
     * @return 当前进程的进程号
     */
    public int getPID(){
        checkRep();
        return PID;
    }

    /**
     * 获取当前进程的姓名
     * @return 当前进程的姓名
     */
    public String getname(){
        checkRep();
        return name;
    }

    /**
     * 获取当前进程的最小运行时间
     * @return 当前进程的最小运行时间
     */
    public long getminTime(){
        checkRep();
        return minTime;
    }

    /**
     * 获取当前进程的最大运行时间
     * @return 当前进程的最大运行时间
     */
    public long getmaxTime(){
        checkRep();
        return maxTime;
    }
}
