package com.wzitech.Z7Bao.frontend.entity;

import com.wzitech.chaos.framework.server.dataaccess.BaseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by wangmin
 * Date:2017/8/23
 */
@Component
public class ZbaoBank extends BaseEntity {
    /**
     * 银行名称
     */
   private String name;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 银行url
     * @return
     */
    private String imgUrl;

    /**
     * 银行code
     * @return
     */

    private Integer code;

    public Integer getCode() {

        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

}
