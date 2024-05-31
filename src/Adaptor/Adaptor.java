package Adaptor;

public interface Adaptor {
    /**
     * 将形如YYYY-MM-DD的字符串转化成对应的long值
     * @param time String待转化的字符串
     * @return long, 转化后对应long值，当传入参数不符合格式时，返回-1
     */
    public long translatorStringToLong(String time);

    /**
     * 将long值转化成对应形如YYYY-MM-DD的字符串
     * @param time 待转化的long值
     * @return 转化后的字符串
     */
    public String translatorLongToString(long time);
}
