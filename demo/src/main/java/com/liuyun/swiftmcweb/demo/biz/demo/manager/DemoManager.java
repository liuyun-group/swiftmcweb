package com.liuyun.swiftmcweb.demo.biz.demo.manager;

import com.liuyun.swiftmcweb.core.web.manager.IMessageHandleManager;

/**
 * @author flyan
 * @version 1.0
 * @date 11/10/22
 */
public interface DemoManager extends IMessageHandleManager {

    String sayHello(String name);

}
