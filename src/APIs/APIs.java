package APIs;

import Interval.MultiIntervalSet;

public interface APIs<L> {
    /**
     * 计算两个MultiIntervalSet对象的相似度
     * <p>
     * 按照时间轴从早到晚的次序，针对同一个时间段内两个对象里的interval，若它们标注的label等价，则二者相似度为1，否则为0；
     * 若同一时间段内只有一个对象有 interval 或二者都没有，则相似度为 0。
     * 将各interval的相似度与interval的长度相乘后求和，除以总长度，即得到二者的整体相似度。
     * example：
     *  { A = [[0,5],[20,25]], B = [[10,20],[25,30]] }
     *  { A = [[20,35]], B = [[10,20]], C = [[0,5]] }
     *  它们的相似度计算如下：
     *  ( 5×0 + 5×0 + 10×1 + 5×1 + 5×0 + 5×0 ) ÷ ( 35 - 0 ) = 15 / 35 ≈ 0.42857
     * @param s1 MultiIntervalSet对象1
     * @param s2 MultiIntervalSet对象2
     * @return 两个MultiIntervalSet对象的相似度
     */
    public  double Similarity(MultiIntervalSet<L> s1, MultiIntervalSet<L> s2) ;

}
