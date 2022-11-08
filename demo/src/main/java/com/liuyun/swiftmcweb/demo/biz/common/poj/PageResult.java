package com.liuyun.swiftmcweb.demo.biz.common.poj;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 分页结果
 * @param <T>   数据项
 */
@Data
@Accessors(chain = true)
public final class PageResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -1099017336907618429L;

    /**
     * 数据
     */
    private List<T> list;

    /**
     * 总量
     */
    private Long total;

    /**
     * 分页数量
     */
    private Long pages;

    public PageResult() {
    }

    public PageResult(List<T> list, Long total, Long pages) {
        this.list = list;
        this.total = total;
        this.pages = pages;
    }

    public PageResult(Long total) {
        this.list = new ArrayList<>();
        this.total = total;
    }

    public <R> PageResult<R> map(Function<T, R> mapper) {
        PageResult<R> ans = new PageResult<>();
        ans.total = this.total;
        ans.list = this.list.stream().map(mapper).toList();
        ans.pages = this.pages;
        return ans;
    }

    public static <T> PageResult<T> empty() {
        return new PageResult<>(0L);
    }

    public static <T> PageResult<T> empty(Long total) {
        return new PageResult<>(total);
    }

}
