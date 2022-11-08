package com.liuyun.swiftmcweb.demo.biz.demo.service;


import com.liuyun.swiftmcweb.core.pojo.Message;
import com.liuyun.swiftmcweb.core.pojo.ResponseMessage;
import com.liuyun.swiftmcweb.core.web.service.BaseMessageHandleService;
import com.liuyun.swiftmcweb.demo.biz.demo.api.dto.HiRespDTO;
import com.liuyun.swiftmcweb.demo.biz.demo.api.vo.HiReqVO;

/**
 * @author flyan
 * @version 1.0
 * @date 10/19/22
 */
public interface DemoService extends BaseMessageHandleService {

    /**
     * 说嗨~
     *
     * @param message
     * @return
     */
    ResponseMessage<HiRespDTO> sayHello(Message<HiReqVO> message);

}
