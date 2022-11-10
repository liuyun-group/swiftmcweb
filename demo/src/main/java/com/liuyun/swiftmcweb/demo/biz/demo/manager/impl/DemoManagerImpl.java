package com.liuyun.swiftmcweb.demo.biz.demo.manager.impl;

import com.liuyun.swiftmcweb.core.annotation.MessageHandleManager;
import com.liuyun.swiftmcweb.demo.biz.demo.manager.DemoManager;

/**
 * @author flyan
 * @version 1.0
 * @date 11/10/22
 */
@MessageHandleManager
public class DemoManagerImpl implements DemoManager {

    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }

}
