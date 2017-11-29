package com.wzitech.Z7Bao.backend.interceptor;

import java.lang.annotation.*;

/**
 * 
 * 
*******************************************
* <b style="font-family:微软雅黑"><small>Description:权限控制注解，使用该注解表明无需判断权限</small></b>   </br>
* <b style="font-family:微软雅黑"><small>HISTORY</small></b></br>
* <b style="font-family:微软雅黑"><small> ID      DATE    PERSON     REASON</small></b><br>
********************************************
* <div style="font-family:微软雅黑,font-size:70%"> 
* 1  2011-3-27  steven.cheng 新增
* </div>  
********************************************
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExceptionToJSON {
    
}
