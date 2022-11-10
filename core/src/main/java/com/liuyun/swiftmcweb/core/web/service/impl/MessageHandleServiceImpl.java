package com.liuyun.swiftmcweb.core.web.service.impl;

import com.liuyun.swiftmcweb.core.web.manager.IMessageHandleManager;
import com.liuyun.swiftmcweb.core.web.service.IMessageHandleService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author flyan
 * @version 1.0
 * @date 11/10/22
 */
public class MessageHandleServiceImpl<M extends IMessageHandleManager>
        implements IMessageHandleService {

    @Autowired
    protected M baseManager;

    public M getBaseManager() {
        return baseManager;
    }

}
