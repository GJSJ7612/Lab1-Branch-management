package APIs;

import Interval.IntervalSet;
import Interval.MultiIntervalSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class API<L> implements APIs<L> {

    @Override
    public double Similarity(MultiIntervalSet<L> s1, MultiIntervalSet<L> s2) {
        if(s1 == null || s2 == null) throw new NullPointerException("MultiIntervalSet can't be null");
        double cnt = 0;
        for(L label : s1.labels()){
            IntervalSet<Integer> intervalSet1 = s1.intervals(label);
            IntervalSet<Integer> intervalSet2 = s2.intervals(label);
            List<long[]> intervals1 = new ArrayList<>();
            List<long[]> intervals2 = new ArrayList<>();
            for(int label1 : intervalSet1.labels()){
                intervals1.add(new long[]{intervalSet1.start(label1), intervalSet1.end(label1)});
            }
            for(int label2 : intervalSet2.labels()) {
                intervals2.add(new long[]{intervalSet2.start(label2), intervalSet2.end(label2)});
            }
            for(long[] interval1 : intervals1){
                for(long[] interval2 :intervals2){
                    if((interval2[0] <= interval1[0] && interval1[0] <= interval2[1])
                        ||(interval2[0] <= interval1[1] && interval1[1] <= interval2[1])
                        ||(interval1[0] <= interval2[0] && interval2[1] <= interval1[1]))
                        cnt += Math.min(interval1[1], interval2[1]) - Math.max(interval1[0], interval2[0])+1;
                }
            }
        }
        double total = 0;
        total = Math.max(s1.getOver(), s2.getOver()) - Math.min(s1.getBegin(), s2.getBegin()) + 1;
        return cnt / total;
    }
}
