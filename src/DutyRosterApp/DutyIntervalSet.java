package DutyRosterApp;

import Interval.CommonIntervalSet;
import Interval.IntervalSet;
import Visitor.CalcFreeTimeRatioInterval;
import Visitor.IntervalVisitor;
import delegationInterface.NoBlankIntervalSet.NoBlankIntervalSetImpl;
import delegationInterface.NonOverlapIntervalSet.IntervalConflictException;
import delegationInterface.NonOverlapIntervalSet.NonOverlapIntervalSetImpl;

import java.util.HashSet;
import java.util.Set;

public class DutyIntervalSet<L> implements IDutyIntervalSet<L> {
    private final long oneDay = 86400000; //一天对应long值

    private final IntervalSet<L> intervalSet;
    private final NonOverlapIntervalSetImpl<L> nois;
    private final NoBlankIntervalSetImpl<L> nbis;

    // Abstraction function:
    //   AF(DutyIntervalSet) = 代表一个排班表，允许进行增删改查，但在限定时间段内不允许产生空白与重叠
    // Representation invariant:
    //   要求在限制时间段中是不能留有空白且时间段间不允许重叠
    // Safety from rep exposure:
    //   nois、nbis均被private与final修饰，oneDay为固定常数

    public DutyIntervalSet(long begin, long over) {
        this.intervalSet = new CommonIntervalSet<>(begin, over + oneDay - 1);
        this.nois = new NonOverlapIntervalSetImpl<>();
        this.nbis = new NoBlankIntervalSetImpl<>();
        checkRep();
    }

    /**
     * 检查表示不变量是否被保持。
     * 这个方法应该在构造器和修改内部状态的方法后被私有调用，以确保类的状态始终有效。
     */
    private void checkRep(){
        try{
            nois.checkNoOverlap(intervalSet);
        }catch (IntervalConflictException ignored){
            throw new AssertionError("Overlap exists");
        }
    }

    @Override
    public void addEmployee(long start, long end, L employee) throws IntervalConflictException {
        long[] exist = null;
        end += oneDay - 1;
        if(intervalSet.labels().contains(employee))
            exist = new long[]{intervalSet.start(employee), intervalSet.end(employee)};
        intervalSet.insert(start, end, employee);
        try{
            nois.checkNoOverlap(intervalSet);
        }catch (IntervalConflictException e){
            intervalSet.remove(employee);
            if(exist != null) intervalSet.insert(exist[0], exist[1], employee);
            throw new IntervalConflictException();
        }

    }

    @Override
    public Set<L> Employees() {
        return new HashSet<>(intervalSet.labels());
    }

    @Override
    public boolean removeEmployee(L employee) {
        return intervalSet.remove(employee);
    }

    @Override
    public long[] checkEmployeeStartToEnd(L employee) {
        if(employee == null) throw new NullPointerException("employee can't be null");
        if(!intervalSet.labels().contains(employee)) return null;
        long[] ret = new long[2];
        ret[0] = intervalSet.start(employee);
        ret[1] = intervalSet.end(employee);
        return ret;
    }

    @Override
    public int checkEmployeeTime(L employee) {
        return (int) ((intervalSet.end(employee) - intervalSet.start(employee)) / oneDay + 1);
    }

    @Override
    public boolean checkNoBlank() {
        return nbis.checkNoBlank(intervalSet);
    }

    @Override
    public long getBegin() {
        return intervalSet.getBegin();
    }

    @Override
    public long getOver() {
        return intervalSet.getOver();
    }

    @Override
    public void setBegin(long begin) {
        intervalSet.setBegin(begin);
    }

    @Override
    public void setOver(long over) {
        intervalSet.setOver(over);
    }

    @Override
    public double calculateFreeTime() {
        IntervalVisitor<L> intervalVisitor = new CalcFreeTimeRatioInterval<>();
        return intervalSet.accept(intervalVisitor);
    }
}
