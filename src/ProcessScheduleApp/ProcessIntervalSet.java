package ProcessScheduleApp;

import Interval.CommonMultiIntervalSet;
import Interval.IntervalSet;
import Interval.MultiIntervalSet;
import decorator.NonOverlapMultiIntervalSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ProcessIntervalSet<L> implements IProcessIntervalSet<L>{
    private final MultiIntervalSet<L> multiIntervalSet;

    // Abstraction function:
    //   AF(DutyIntervalSet) = 代表一个进程序列集合对象，要求进程间不存在冲突
    //   一个进程可以在任意时刻被暂停，当进程的运行时间在最小与最大运行时间区间内该进程即可终止
    // Representation invariant:
    //   要求multiIntervalSet中时间段不存在冲突
    // Safety from rep exposure:
    //   multiIntervalSet被private和final修饰

    public ProcessIntervalSet(long begin, long over){
        multiIntervalSet = new NonOverlapMultiIntervalSet<>(new CommonMultiIntervalSet<>(begin, over));
        checkRep();
    }

    private void checkRep(){
        List<long[]> intervals = new ArrayList<>();
        for(L l : multiIntervalSet.labels()){
            IntervalSet<Integer> intervalSet = multiIntervalSet.intervals(l);
            for(int i : intervalSet.labels()){
                intervals.add(new long[]{intervalSet.start(i), intervalSet.end(i)});
            }
        }
        intervals.sort(new Comparator<long[]>() {
            @Override
            public int compare(long[] a, long[] b) {
                return Long.compare(a[0], b[0]);
            }
        });
        for(int i = 1; i < intervals.size(); i++){
            assert intervals.get(i)[0] <= intervals.get(i)[1];
        }
    }

    @Override
    public void addProcess(long start, long end, L progress) {
        multiIntervalSet.insert(start, end, progress);
        checkRep();
    }

    @Override
    public Set<L> processes() {
        checkRep();
        return multiIntervalSet.labels();
    }

    @Override
    public long runTime(L progress) {
        if(progress == null) throw new NullPointerException("progress can't be null");
        long ret = 0;
        for(int cnt : multiIntervalSet.intervals(progress).labels()){
            ret += multiIntervalSet.intervals(progress).end(cnt) - multiIntervalSet.intervals(progress).start(cnt) + 1;
        }
        checkRep();
        return ret;
    }

    @Override
    public IntervalSet<Integer> checkProgress(L progress) {
        checkRep();
        return multiIntervalSet.intervals(progress);
    }

    @Override
    public void setOver(long over) {
        checkRep();
        multiIntervalSet.setOver(over);
    }
}
