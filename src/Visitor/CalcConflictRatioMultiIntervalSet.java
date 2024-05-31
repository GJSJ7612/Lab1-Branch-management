package Visitor;

import Interval.MultiIntervalSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CalcConflictRatioMultiIntervalSet<L> implements MultiIntervalSetVisitor<L>{
    /**
     * 计算 MultiIntervalSet中的冲突比例
     * <p>
     * 该方法统计同一个时间段内安排了两个不同的MultiIntervalSet对象的时间段长度。
     * 用发生冲突的时间段总长度除于总长度，得到冲突比例，是一个[0,1]之间的值。
     *
     * @param multiIntervalSet MultiIntervalSet<L>,待检测时间段
     * @return double,冲突比例
     */
    @Override
    public double visit(MultiIntervalSet<L> multiIntervalSet) {
        long overlap = 0, total = multiIntervalSet.getOver() - multiIntervalSet.getBegin()+1;
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
        List<long[]> overlaps = new ArrayList<>();
        for(int i = 0; i < intervals.size(); i++){
            long end = intervals.get(i)[1];
            for(int j = i+1; j < intervals.size(); j++){
                if(intervals.get(j)[0] <= end){
                    long overlapEnd = Math.min(intervals.get(j)[1], end);
                    if(overlaps.isEmpty()) overlaps.add(new long[]{intervals.get(j)[0], overlapEnd});
                    if(intervals.get(j)[0] <= overlaps.get(overlaps.size()-1)[1])
                        overlaps.set(overlaps.size()-1, new long[]{overlaps.get(overlaps.size()-1)[0], Math.max(overlaps.get(overlaps.size()-1)[1], overlapEnd)});
                    else
                        overlaps.add(new long[]{intervals.get(j)[0], overlapEnd});
                }
            }
        }
        for(long[] ol : overlaps){
            overlap += ol[1] - ol[0]+1;
        }
        return (double) overlap / total;
    }
}
