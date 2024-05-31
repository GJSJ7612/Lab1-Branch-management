package delegationInterface.NonOverlapIntervalSet;

import Interval.IntervalSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NonOverlapIntervalSetImpl<L> implements NonOverlapIntervalSet<L>{

    public NonOverlapIntervalSetImpl(){}

    @Override
    public void checkNoOverlap(IntervalSet<L> intervalSet) throws IntervalConflictException{
        if(intervalSet == null) throw new NullPointerException("IntervalSet can't be null");
        List<long[]> intervals = new ArrayList<>();
        for(L label : intervalSet.labels()){
            intervals.add(new long[]{intervalSet.start(label), intervalSet.end(label)});
        }
        intervals.sort(new Comparator<long[]>() {
            @Override
            public int compare(long[] a, long[] b) {
                return Long.compare(a[0], b[0]); // 根据数组的第一个元素排序
            }
        });
        for(int i = 1; i < intervals.size(); i++){
            if(intervals.get(i)[0] <= intervals.get(i - 1)[1]){
                throw new IntervalConflictException("Overlap exists");
            }
        }
    }
}
