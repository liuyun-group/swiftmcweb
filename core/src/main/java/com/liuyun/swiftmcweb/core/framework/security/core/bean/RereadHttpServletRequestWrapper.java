package com.liuyun.swiftmcweb.core.framework.security.core.bean;

import cn.hutool.extra.servlet.ServletUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 可以重读的 HttpServletRequestWrapper
 * 用于解决spring web原生的 HttpServletRequest.getReader() 后，就无法再次获取！
 *
 * @author flyan
 */
public class RereadHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] bodyBytes;

    public RereadHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.bodyBytes = ServletUtil.getBodyBytes(request);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
    
    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bodyBytes);
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

}
