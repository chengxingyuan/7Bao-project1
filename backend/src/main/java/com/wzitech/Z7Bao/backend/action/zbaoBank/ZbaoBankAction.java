package com.wzitech.Z7Bao.backend.action.zbaoBank;

import com.wzitech.Z7Bao.backend.action.extjs.AbstractAction;
import com.wzitech.Z7Bao.backend.interceptor.ExceptionToJSON;
import com.wzitech.Z7Bao.backend.util.WebServerUtil;
import com.wzitech.Z7Bao.frontend.business.IZbaoBankManager;
import com.wzitech.Z7Bao.frontend.entity.ZbaoBank;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ResponseCodes;
import com.wzitech.gamegold.common.file.IFileManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangmin
 * Date:2017/8/23
 * 7bao银行管理action
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class ZbaoBankAction extends AbstractAction {

    private ZbaoBank zbaoBank;
    @Autowired
    IZbaoBankManager zbaoBankManager;

    @Autowired
    private IFileManager fileManager;

    // 封装上传文件域的属性
    private File bankImg;

    // 封装上传文件域的属性
    private String imgUrl;

    private Long id;
    private List<ZbaoBank> zbaoBankList;
    private String name;
    private Boolean enable;

    /**
     * 增加银行信息
     *
     * @return
     */
    public String addBank() {
        try {
            saveImg();
            ZbaoBank zbaoBank = new ZbaoBank();
           zbaoBank.setEnable(enable);
            zbaoBank.setName(name);
            zbaoBank.setCreateTime(new Date());
            zbaoBank.setImgUrl(imgUrl);
            //imgUrl = imgUrl.substring(imgUrl.indexOf("/"),imgUrl.length()-1);
            zbaoBankManager.add(zbaoBank);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 修改银行信息
     *
     * @return
     */
    public String modifyBank() {
        try {
            saveImg();
            if (id != null){
                zbaoBank = zbaoBankManager.selectByIdBank(id);
            }
            if (StringUtils.isBlank(imgUrl)){
                return this.returnSuccess();
            }else {
                zbaoBank.setName(name);
                zbaoBank.setImgUrl(imgUrl);
                zbaoBank.setEnable(enable);
                zbaoBankManager.update(zbaoBank);
                return this.returnSuccess();
            }

        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 删除银行信息
     *
     * @return
     */
    public String deleteBank() {
        try {
            if (id != null) {
                zbaoBankManager.delete(id);
            }
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public String queryBank() {
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            if (!"全部".equals(name)) {
                queryMap.put("name", name);
            }
            GenericPage<ZbaoBank> genericPage = zbaoBankManager.queryPage(queryMap, this.limit, this.start, "id", true);
            zbaoBankList = genericPage.getData();
            totalCount = genericPage.getTotalCount();
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 禁用
     *
     * @return
     */
    public String disenable() {
        try {
            zbaoBankManager.disableUser(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 启用
     *
     * @return
     */
    public String enabled() {
        try {
            zbaoBankManager.qyUser(id);
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public void saveImg() {
        String url = null;
        try {
            if (bankImg == null || bankImg.equals("")) return;
            BufferedImage sourceImg   =   javax.imageio.ImageIO.read(bankImg);//可得到宽高
            if ( sourceImg.getWidth() != 103 && sourceImg.getHeight() != 35){
                throw new SystemException(ResponseCodes.NOTALLOWEDUPLOAD.getCode(), ResponseCodes.NOTALLOWEDUPLOAD.getMessage());
            }
            url = fileManager.saveAvatar(WebServerUtil.changeFileToByteArray(bankImg), name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        url = url.substring( url.indexOf("/",2),url.length());
        setImgUrl(url);
    }



    public ZbaoBank getZbaoBank() {
        return zbaoBank;
    }

    public void setZbaoBank(ZbaoBank zbaoBank) {
        this.zbaoBank = zbaoBank;
    }

    public List<ZbaoBank> getZbaoBankList() {
        return zbaoBankList;
    }

    public void setZbaoBankList(List<ZbaoBank> zbaoBankList) {
        this.zbaoBankList = zbaoBankList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getImgUrl() {
        return imgUrl;
    }

    public File getBankImg() {
        return bankImg;
    }

    public void setBankImg(File bankImg) {
        this.bankImg = bankImg;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
