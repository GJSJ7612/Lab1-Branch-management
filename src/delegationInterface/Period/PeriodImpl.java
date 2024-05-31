package delegationInterface.Period;

public class PeriodImpl implements Period {
    private int times;

    // Abstraction function:
    //   AF(PeriodicIntervalSetImpl) = 实现了时间段集合存在周期性的接口，time表示重复的次数
    // Representation invariant:
    //   times要求大于0
    // Safety from rep exposure:
    //  times被private修饰

    public PeriodImpl(int times) {
        if(times <= 0) throw new IllegalArgumentException("times must be positive");
        this.times = times;
        checkRep();
    }

    /**
     * 检查表示不变量是否被保持。
     * 这个方法应该在构造器和修改内部状态的方法后被私有调用，以确保类的状态始终有效。
     */
    private void checkRep(){
        assert times > 0;
    }

    @Override
    public int getTimes() {
        checkRep();
        return times;
    }

    @Override
    public void setTimes(int times) throws IllegalArgumentException{
        if(times <= 0) throw new IllegalArgumentException("times must be positive");
        checkRep();
        this.times = times;
    }
}
