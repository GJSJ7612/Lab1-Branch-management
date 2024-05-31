package delegationInterface.NoBlankIntervalSet;

import Interval.IntervalSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NoBlankIntervalSetImpl<L> implements NoBlankIntervalSet<L> {

    @Override
    public boolean checkNoBlank(IntervalSet<L> intervalSet) {
        if(intervalSet == null) throw new NullPointerException("IntervalSet can't be null");
        long filled = 0, total = intervalSet.getOver() - intervalSet.getBegin() + 1;
        List<long[]> intervals = new ArrayList<>();
        for(L label : intervalSet.labels()){
            intervals.add(new long[]{intervalSet.start(label), intervalSet.end(label)});
        }
        intervals.sort(new Comparator<long[]>() {
            @Override
            public int compare(long[] a, long[] b) {
                return Long.compare(a[0], b[0]);
            }
        });
        List<long[]> fill = new ArrayList<>();
        for(long[] interval : intervals){
            if(fill.isEmpty()) fill.add(interval);
            if(interval[0] <= fill.get(fill.size()-1)[1])
                fill.set(fill.size()-1, new long[]{fill.get(fill.size()-1)[0], Math.max(fill.get(fill.size()-1)[1], interval[1])});
            else
                fill.add(interval);
        }
        for(long[] interval : fill){
            filled += interval[1] - interval[0] + 1;
        }
        return filled == total;
    }
}
