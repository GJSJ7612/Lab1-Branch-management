package Interval;

import Visitor.IntervalVisitor;

import java.util.*;

public class CommonIntervalSet<L> implements IntervalSet<L> {
    private long begin;
    private long over;
    private final Map<L, long[]> interval = new LinkedHashMap<>();

    // Abstraction function:
    //   AF(CommonIntervalSet) = 实现了一个由标签及其关联的一个时间段的集合，其中begin和over给出了限定了一个时间段
    //   初始状态下集合为空，代表不存在任何时间段
    // Representation invariant:
    //   interval是由LinkedHashMap数据结构实现的，其中键代表标签，值是一个大小为2的long类型数组
    //   其中第零号位置元素代表对应起始时间，第一号元素代表对应终止时间，并且起始时间要求小于等于终止时间
    //   要求begin严格小于over
    // Safety from rep exposure:
    //   interval被private与final修饰，且interval未被传递
    //   begin和over被private修饰,且都是long类型

    public CommonIntervalSet(long begin, long over){
        if(begin >= over) throw new IllegalArgumentException("begin must be less than over");
        this.begin = begin;
        this.over = over;
        checkRep();
    }

    /**
     * 检查表示不变量是否被保持。
     * 这个方法应该在构造器和修改内部状态的方法后被私有调用，以确保类的状态始终有效。
     */
    private void checkRep(){
        assert begin < over;
        for(long[] val : interval.values()){
            assert val.length == 2;
            assert val[0] <=  val[1];
        }
    }

    @Override
    public void insert(long start, long end, L label) {
        if(start < begin || end > over) return;
        if(label == null) throw new NullPointerException("label can't be null");
        if(start > end) throw new IllegalArgumentException("start can't larger than end");
        interval.put(label, new long[]{start, end});
        checkRep();
    }

    @Override
    public Set<L> labels() {
        checkRep();
        return interval.keySet();
    }

    @Override
    public boolean remove(L label) {
        if(label == null) throw new NullPointerException("label can't be null");
        if(!interval.containsKey(label)) return false;
        interval.remove(label);
        checkRep();
        return true;
    }

    @Override
    public long start(L label) {
        if(label == null) throw new NullPointerException("label can't be null");
        if(!interval.containsKey(label)) throw new RuntimeException("label doesn't exist");
        checkRep();
        return interval.get(label)[0];
    }

    @Override
    public long end(L label) {
        if(label == null) throw new NullPointerException("label can't be null");
        if(!interval.containsKey(label)) throw new RuntimeException("label doesn't exist");
        checkRep();
        return interval.get(label)[1];
    }

    @Override
    public long getBegin() {
        return this.begin;
    }

    @Override
    public long getOver() {
        return this.over;
    }

    @Override
    public void setBegin(long begin) {
        if(begin >= over) throw new IllegalArgumentException("begin must be less than over");
        this.begin = begin;
    }

    @Override
    public void setOver(long over) {
        if(over <= begin) throw new IllegalArgumentException("over must be larger than begin");
        this.over = over;
    }

    @Override
    public double accept(IntervalVisitor<L> v) {
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
     * There are 3 intervals in the intervalSet
     * first : 2->5
     * second : 3->6
     * third : 4->7
     *
     * @return 时间段集合的字符串表示，时间段数、时间段集合中的标签和对应时间段。
     */
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("There are ").append(interval.size()).append(" intervals in the intervalSet\n");
        for(L label : interval.keySet()){
            ret.append(label).append(" : ").append(interval.get(label)[0]).append("->").append(interval.get(label)[1]).append("\n");
        }
        return ret.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommonIntervalSet<?> that = (CommonIntervalSet<?>) o;
        return Objects.equals(interval, that.interval);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
