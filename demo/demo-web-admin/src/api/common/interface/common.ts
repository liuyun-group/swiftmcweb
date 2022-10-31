/**
 * 公共的相关类型定义
 */

/**
 * 分页结果
 */
export interface PageResult<T> {

    /**
     * 数据
     */
    list: T[];

    /**
     * 总量
     */
    total: number;

    /**
     * 分页数量
     */
    pages: number;

}
