package com.wzitech.Z7Bao.backend.result;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

public abstract class AbstractResult implements Result {
    
    private static final long serialVersionUID = 9155021581141889808L;
    
    /**
     * 执行处理结果信息的方法
     * @see Result#execute(ActionInvocation)
     * execute
     * @param arg0
     * @throws Exception
     * @since:
     */
    @Override
    public void execute(ActionInvocation arg0) throws Exception {
        
    }
    
}
