package com.flyan.swiftmcweb.demo.biz.demo.service;


import com.flyan.swiftmcweb.core.pojo.Message;
import com.flyan.swiftmcweb.core.pojo.ResponseMessage;
import com.flyan.swiftmcweb.core.web.service.BaseMessageHandleService;
import com.flyan.swiftmcweb.demo.biz.demo.api.dto.HiRespDTO;
import com.flyan.swiftmcweb.demo.biz.demo.api.vo.HiReqVO;

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
