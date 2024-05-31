package Interval;

import Visitor.MultiIntervalSetVisitor;
import delegationInterface.NonOverlapIntervalSet.IntervalConflictException;

import java.util.*;

public class CommonMultiIntervalSet<L> implements MultiIntervalSet<L> {
    private final IntervalSet<L> intervalSet;
    private final Map<L, List<long[]>> mapIntervals = new LinkedHashMap<>();

    // Abstraction function:
    //   AF(CommonMultiIntervalSet) = 实现了一个由标签及其关联的多个时间段的集合
    //   初始状态下集合可以为空，代表不存在任何时间段；也可传入已有时间段，保留原有时间段和标签
    // Representation invariant:
    //   intervalSet是为复用IntervalSet<L>而建立起的链接
    //   mapIntervals是一个标签和其所有的时间段的映射，采用LinkedHashMap<>()结构，其中键代表标签，值为List<long[]>类型
    //   其中List<long[]>包含的元素为long[]，且大小为2，对应时间段不会重叠且按从小到大排放
    //   其中第零号位置元素代表对应起始时间，第一号元素代表对应终止时间，并且起始时间要求小于等于终止时间
    // Safety from rep exposure:
    //   intervalSet和mapIntervals均被private与final修饰

    public CommonMultiIntervalSet(long begin, long over){
        intervalSet = new CommonIntervalSet<>(begin,over);
        checkRep();
    }
    public CommonMultiIntervalSet(IntervalSet<L> Initial){
        if(Initial == null) throw new NullPointerException("IntervalSet can't be null");
        intervalSet = Initial;
        for(L label : intervalSet.labels()){
            mapIntervals.put(label, new ArrayList<>());
            mapIntervals.get(label).add(new long[]{intervalSet.start(label), intervalSet.end(label)});
        }
        checkRep();
    }

    /**
     * 检查表示不变量是否被保持。
     * 这个方法应该在构造器和修改内部状态的方法后被私有调用，以确保类的状态始终有效。
     */
    private void checkRep(){
        for(L label : mapIntervals.keySet()){
            for(int i = 1; i < mapIntervals.get(label).size(); i++){
                assert mapIntervals.get(label).get(i)[0] >= mapIntervals.get(label).get(i-1)[1];
            }
        }
    }

    @Override
    public void insert(long start, long end, L label){
        if(start < intervalSet.getBegin() || end > intervalSet.getOver()) return;
        intervalSet.insert(start, end, label);
        if(!mapIntervals.containsKey(label)){
            mapIntervals.put(label, new ArrayList<>());
        }
        for(long[] interval : mapIntervals.get(label)){
            if((interval[0] <= start && start <= interval[1]) || (interval[0] <= end && end <= interval[1]) ||
                    (start <= interval[0] && interval[1] <= end)) return;
        }
        mapIntervals.get(label).add(new long[]{start,end});
        mapIntervals.get(label).sort(new Comparator<long[]>() {
            @Override
            public int compare(long[] a, long[] b) {
                return Long.compare(a[0], b[0]); // 根据数组的第一个元素排序
            }
        });
        checkRep();
    }

    @Override
    public Set<L> labels() {
        checkRep();
        return intervalSet.labels();
    }

    @Override
    public boolean remove(L label) {
        if(label == null) throw new NullPointerException("label can't be null");
        if(!intervalSet.labels().contains(label)) return false;
        intervalSet.remove(label);
        mapIntervals.remove(label);
        checkRep();
        return true;
    }

    @Override
    public IntervalSet<Integer> intervals(L label){
        if(label == null) throw new NullPointerException("label can't be null");
        IntervalSet<Integer> ret = new CommonIntervalSet<>(intervalSet.getBegin(), intervalSet.getOver());
        if( mapIntervals.get(label) == null) return ret;
        for(int i = 0; i < mapIntervals.get(label).size(); i++){
            ret.insert(mapIntervals.get(label).get(i)[0], mapIntervals.get(label).get(i)[1], i);
        }
        return ret;
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
    public double accept(MultiIntervalSetVisitor<L> v) {
        return v.visit(this);
    }

    /**
     * 返回时间段集合的当前状态的字符串表示。
     * <p>
     * 字符串包含以下信息：
     * - 时间段数
     * - 时间段集合中的标签和对应时间段
     * <p>
     * 格式示例：
     * There are 6 intervals in the intervalSet
     * first :
     * 	    2->3
     * 	    4->6
     * 	    8->9
     * second :
     * 	    2->6
     * 	    6->7
     * third :
     * 	    1->9
     *
     * @return 时间段集合的字符串表示，时间段数、时间段集合中的标签和对应时间段。
     */
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        int size = 0;
        for(L label : mapIntervals.keySet()){
            size += mapIntervals.get(label).size();
        }
        ret.append("There are ").append(size).append(" intervals in the intervalSet\n");
        for(L label : mapIntervals.keySet()){
            ret.append(label).append(" : \n");
            for(long[] interval : mapIntervals.get(label)){
                ret.append("\t").append(interval[0]).append("->").append(interval[1]).append("\n");
            }
        }
        return ret.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
