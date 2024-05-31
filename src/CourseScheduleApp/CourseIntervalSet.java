package CourseScheduleApp;

import Interval.CommonMultiIntervalSet;
import Interval.IntervalSet;
import delegationInterface.Period.Period;
import delegationInterface.Period.PeriodImpl;

import java.util.Set;

public class CourseIntervalSet<L> extends CommonMultiIntervalSet<L> implements ICourseIntervalSet<L>{
    private final Period cycle;

    // Abstraction function:
    //   AF(DutyIntervalSet) = 代表一个一周的课表，允许进行增删改查，允许课程间时间段产生重叠，但相同课程时间段不允许产生重叠，
    //   由于一周有7天，每天有5个时间段可以上课，每个时间段2h，故限定时间段范围为begin-begin+69
    //   其中（begin+）0-9代表星期一8-10时、10-12时、13-15时、15-17时、19-21时，其他6天以此类推
    // Representation invariant:
    //   要求相同课程时间段不允许产生重叠,每个课程的开始时间为偶数
    // Safety from rep exposure:
    //   cycle均被private与final修饰

    public CourseIntervalSet(long begin, long over, int cycle) {
        super(begin, over);
        this.cycle = new PeriodImpl(cycle);
        checkRep();
    }

    /**
     * 检查表示不变量是否被保持。
     * 这个方法应该在构造器和修改内部状态的方法后被私有调用，以确保类的状态始终有效。
     */
    private void checkRep(){
        for(L l : super.labels()){
            for(int t : super.intervals(l).labels()){
               if(t > 0)assert super.intervals(l).end(t-1) < super.intervals(l).start(t);
               assert intervals(l).start(t) % 2 == 0;
            }
        }
    }

    @Override
    public void addCourse(long start, L course) {
        start -= getBegin();
        if(start % 2 == 1) throw new IllegalArgumentException("start must be even");
        super.insert(start, start+1, course);
        checkRep();
    }

    @Override
    public boolean removeCourse(L course) {
        checkRep();
        return super.remove(course);
    }

    @Override
    public Set<L> courses() {
        return super.labels();
    }

    @Override
    public IntervalSet<Integer> checkCourse(L course) {
        checkRep();
        return super.intervals(course);
    }

    @Override
    public void setCycle(int cycle) {
        this.cycle.setTimes(cycle);
        checkRep();
    }

    @Override
    public int getCycle() {
        checkRep();
        return this.cycle.getTimes();
    }
}
