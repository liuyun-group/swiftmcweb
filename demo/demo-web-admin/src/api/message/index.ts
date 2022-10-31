import { AxiosPromise } from "axios";
import request from "/@/utils/request";
import { Message, ResponseMessage } from "./interface/message";

export const message_api = {

    /**
     * 发送一条消息给某服务，请求某功能，并接收其响应的消息
     * 
     * @param message 消息
     * @returns 
     */
    sendrec:<T, R> (message: Message<T>): AxiosPromise<ResponseMessage<R>> => {
        return request({
            url: "/sendrec",
            method: "post",
            data: message,
        });
    },
    
}