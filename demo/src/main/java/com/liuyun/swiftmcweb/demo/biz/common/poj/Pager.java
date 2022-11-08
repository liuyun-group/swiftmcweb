package com.liuyun.swiftmcweb.demo.biz.common.poj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 内存分页工具
 *
 * @param <T>
 * @author Flyan
 */
public final class Pager<T> {
    private final List<T> data;
    private final int pageSize;

    /**
     * @param data     原始数据
     * @param pageSize 每页条数
     */
    public Pager(List<T> data, int pageSize) {
        this.data = data;
        this.pageSize = pageSize;
    }

    /**
     * 获取某页数据，从第1页开始
     *
     * @param pageNum 第几页
     * @return 分页数据
     */
    public List<T> page(int pageNum) {
        if (pageNum < 1) {
            pageNum = 1;
        }
        int from = (pageNum - 1) * pageSize;
        int to = Math.min(pageNum * pageSize, data.size());
        if (from > to) {
            from = to;
        }
        return data.subList(from, to);
    }

    /**
     * 获取总页数
     */
    public int getPageCount() {
        if (pageSize == 0) {
            return 0;
        }
        return data.size() % pageSize == 0 ? (data.size() / pageSize) : (data.size() / pageSize + 1);
    }

    public List<T> getData() {
        return data;
    }

    public int getPageSize() {
        return pageSize;
    }

    /**
     * 元素迭代器
     */
    public Iterator<List<T>> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<List<T>> {
        int page = 1;

        Itr() {  }

        @Override
        public boolean hasNext() {
            return page <= getPageCount();
        }

        @Override
        public List<T> next() {
            int i = page;
            if (i > getPageCount()) {
                return new ArrayList<>();
            }

            page = i + 1;
            return Pager.this.page(i);
        }
    }

}