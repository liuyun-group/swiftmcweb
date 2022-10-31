/**
 * 消息通信相关类型定义
 */


/**
 * 消息通信传输的消息
 * 
 * @author flyan
 */
export interface Message<T> {
    /**
     * 请求/响应服务
     */
    service: string;

    /**
     * 消息类型
     */
    messtype: string;

    /**
     * 消息体
     */
    data: T | null;
}

/**
 * 消息通信接口响应的消息，继承自 {@link Message}，单独定义该消息对象是因为接口
 * 响应的信息并不是所有情况都需要的，例如对于内部消息通信或微服务下的远程调用，它们都有
 * 可能只使用普通消息传递即可。
 * 
 * @author flyan
 */
export interface ResponseMessage<T> {
    /**
     * 错误码
     */
    errcode: number;
    
    /**
     * 错误提示，用户可阅读
     */
    msg: string;

    /**
     * @see {@link Message}
     */
    service: string;
    messtype: string;
    data: T | null;
} 



