package com.intellifusion.service;

/**
 * 人员组搜索
 *
 * @author SanheDashen
 * @date 2022/04/13 16:14
 */

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 结构：
 *      一个CharColumn<T>类型的数组挂载一个Map，其中T代表不被分词的索引的数据结构,此处存储节点id
 *      添加元素时，字符串会被遍历成一个一个的字符，每个字符对应一个 ASCII 码值,假如插入c字符时,c字符对应的 ASCII 码是99
 *      那么就会在数组索引为99的地方创建一个空的CharColumn对象
 *      将我们的key值和我们的c字符在字符串中的索引位置作为k-v对放入我们99位置对应的ConcurrentHashMap中
 *      原理就是一个使用ngram分词算法的倒排索引
 *
 *      插入 1 ,"hcc"
 *      插入 2 ,"c"
 *
 *  数组                    c          h
 *      -------------------99--------104-------------------
 *  Map                    |          |
 *                     1,byte[1,2]   1,byte[0]
 *                     2,byte[0]
 *
 * byte[]中的值代表此字符在字符串中的索引位置
 * @param <T>
 */
public class PersonLikeSearch<T> {

    //初始化一个CharColumn<T>类型的数组
    private final CharColumn<T>[] columns = new CharColumn[Character.MAX_VALUE];

    public CharColumn<T>[] getColumns() {
        return columns;
    }


    public void put(T t, String value) {
        //将需要分词的value转化为char数组
        char[] chars = value.toCharArray();
        //遍历char数组中每个字符
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            CharColumn<T> column = columns[c];
            if (null == column) {
                column = new CharColumn<T>();
                columns[c] = column;
            }
            //维护一个字符串多个相同字符的情况
            column.add(t, (byte) i);
        }
    }

    /**
     * 修改数据.
     *
     * @param id       主键id
     * @param newValue 新字符串
     */
    public void update(T id, String newValue) {
        remove(id);
        put(id, newValue);
    }

    public void clear() {
        for (CharColumn<T> column : columns)
            if (column != null) column.clear();
    }


    /**
     * 删除数据.
     *
     * @param id 主键id
     * @return
     */
    public boolean remove(T id) {
        boolean sign = false;
        for (CharColumn<T> column : columns) {
            if (column != null) {
                if (column.remove(id)) sign = true;
            }
        }
        return sign;
    }


    /***
     *
     * @param word
     * @param limit
     * @return
     */
    public Collection<T> search(String word, int limit) {
        char[] chars = word.toCharArray();
        int n = word.length();
        Context context = new Context();
        //将columns中存在word字符串中字符的数据拿出来
        for (int i = 0; i < chars.length; i++) {
            CharColumn<T> column = columns[chars[i]];
            if (column == null) break;
            if (!context.filter(column)) break;
            n--;
        }
        if (n == 0) return context.limit(limit);
        return Collections.emptySet();
    }

    private class Context {
        Map<T, byte[]> result;
        boolean used = false;
        private boolean filter(CharColumn<T> columns) {
            if (!this.used) {
                this.result = new TreeMap<T, byte[]>(columns.poxIndex);
                this.used = true;
                return true;
            }
            boolean flag = false;
            Map<T, byte[]> newResult = new TreeMap<>();
            Set<Map.Entry<T, byte[]>> entrySet = columns.poxIndex.entrySet();
            for (Map.Entry<T, byte[]> entry : entrySet) {
                T id = entry.getKey();
                byte[] charPox = entry.getValue();

                //一个字符串的不同字符在存储的时候拥有相同的id
                if (!result.containsKey(id)) {
                    continue;
                }
                byte[] before = result.get(id);
                boolean in = false;
                //如果这个新数组中存在与旧数组中某个元素索引位置相邻且位于其后的元素 就把该元素加入新map
                for (byte pox : before) {
                    if (contain(charPox, (byte) (pox + 1))) {
                        in = true;
                        break;
                    }
                }
                if (in) {
                    flag = true;
                    newResult.put(id, charPox);
                }
            }
            result = newResult;
            return flag;
        }

        public Collection<T> limit(int limit) {
            if (result.size() <= limit) {
                return result.keySet();
            }
            Collection<T> ids = new TreeSet<T>();
            for (T id : result.keySet()) {
                ids.add(id);
                if (ids.size() >= limit) {
                    break;
                }
            }
            return ids;
        }
    }

    private static class CharColumn<T> {
        ConcurrentHashMap<T, byte[]> poxIndex = new ConcurrentHashMap<>();
        /***
         *
         * @param t
         * @param pox
         */
        private void add(T t, byte pox) {
            //第一次进来的时候此字符位置上挂的map为null需要初始化一个
            byte[] arr = poxIndex.get(t);
            if (null == arr) {
                arr = new byte[]{pox};
            } else {
                //以c字符为例，如果此时已存在c字符且c字符下的map的key值中对应的byte数组不为空
                arr = copy(arr, pox);
            }
            poxIndex.put(t, arr);

        }

        private boolean remove(T id) {
            if (poxIndex.remove(id) != null) {
                return true;
            }
            return false;
        }

        private void clear(){
            poxIndex.clear();
        }
    }


    private static byte[] copy(byte[] arr, byte value) {
        Arrays.sort(arr);
        //原来的arr中包含新的key值
        if (contain(arr, value)) {
            return arr;
        }
        //不包含新的key值就创建新的arr长度比原来大1
        byte[] newArr = new byte[arr.length + 1];
        //把新来的值放在arr的最后一个位置
        newArr[newArr.length - 1] = value;
        //把旧的arr元素拷入新的arr
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        Arrays.sort(newArr);
        return newArr;
    }

    private static boolean contain(byte[] arr, byte value) {
        int pox = Arrays.binarySearch(arr, value);
        return pox >= 0;
    }

}

