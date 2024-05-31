package Visitor;

import Interval.MultiIntervalSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CalcFreeTimeRatioMultiIntervalSet<L> implements MultiIntervalSetVisitor<L>{
    /**
     * 计算MultiIntervalSet中的空白比例
     * <p>
     * 该方法统计一个时间段内未安排的时间段长度。
     * 用空白的时间段总长度除于总长度，得到冲突比例，是一个[0,1]之间的值。
     *
     * @param multiIntervalSet MultiIntervalSet<L>,待检测时间段
     * @return double,空白比例
     */
    @Override
    public double visit(MultiIntervalSet<L> multiIntervalSet) {
        long filled = 0, total = multiIntervalSet.getOver() - multiIntervalSet.getBegin() + 1;
        List<long[]> intervals = new ArrayList<>();
        for(L label : multiIntervalSet.labels()){
            for(int l : multiIntervalSet.intervals(label).labels()){
                intervals.add(new long[]{multiIntervalSet.intervals(label).start(l), multiIntervalSet.intervals(label).end(l)});
            }
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
        return (double)(total-filled)/total;
    }
}
