package com.wzitech.Z7Bao.backend.action.ShConfig;

import com.kb5173.framework.shared.utils.BeanMapper;
import com.m5173.framework.file.common.ExportExcel;
import com.wzitech.Z7Bao.backend.action.extjs.AbstractAction;
import com.wzitech.Z7Bao.backend.interceptor.ExceptionToJSON;
import com.wzitech.Z7Bao.backend.util.WebServerUtil;
import com.wzitech.Z7Bao.frontend.business.IZbaoWithdrawalsManager;
import com.wzitech.Z7Bao.frontend.entity.ZbaoWithdrawals;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ZbaoWithdrawTypeEnum;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangmin
 * Date:2017/8/28
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class zbaoWithdrawalsAction extends AbstractAction {
    @Autowired
    IZbaoWithdrawalsManager zbaoWithdrawalsManager;

    private ZbaoWithdrawals zbaoWithdrawals;

    private List<ZbaoWithdrawals> zbaoWithdrawalsList;

    private Date startTime;

    private Date endTime;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String queryZbaoWithdrawals() {
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            if (zbaoWithdrawals != null) {
                if (StringUtils.isNotBlank(zbaoWithdrawals.getLoginAccount())) {
                    queryMap.put("loginAccount", zbaoWithdrawals.getLoginAccount().trim());
                }
                if (zbaoWithdrawals.getType() != null && zbaoWithdrawals.getType() != 0) {
                    queryMap.put("type", zbaoWithdrawals.getType());
                }
                if (StringUtils.isNotBlank(zbaoWithdrawals.getCardCode())) {
                    queryMap.put("cardCode", zbaoWithdrawals.getCardCode().trim());
                }
                if (StringUtils.isNotBlank(zbaoWithdrawals.getOrderId())) {
                    queryMap.put("orderId", zbaoWithdrawals.getOrderId().trim());
                }
                if (StringUtils.isNotBlank(zbaoWithdrawals.getRealName())) {
                    queryMap.put("realName", zbaoWithdrawals.getRealName().trim());
                }
                if (StringUtils.isNotBlank(zbaoWithdrawals.getOpenbank())) {
                    queryMap.put("openbank", zbaoWithdrawals.getOpenbank().trim());
                }
                queryMap.put("startTime", startTime);
                queryMap.put("endTime", WebServerUtil.oneDateLastTime(endTime));
            }

            GenericPage<ZbaoWithdrawals> genericPage = zbaoWithdrawalsManager.queryPage(queryMap, this.limit, this.start, "create_time", false);
            zbaoWithdrawalsList = genericPage.getData();
            totalCount = genericPage.getTotalCount();
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public ZbaoWithdrawals getZbaoWithdrawals() {
        return zbaoWithdrawals;
    }

    public void setZbaoWithdrawals(ZbaoWithdrawals zbaoWithdrawals) {
        this.zbaoWithdrawals = zbaoWithdrawals;
    }

    public List<ZbaoWithdrawals> getZbaoWithdrawalsList() {
        return zbaoWithdrawalsList;
    }

    public void setZbaoWithdrawalsList(List<ZbaoWithdrawals> zbaoWithdrawalsList) {
        this.zbaoWithdrawalsList = zbaoWithdrawalsList;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


    /**
     * 导出提现明细
     */
    public void exportFundExcel() {
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            Map<String, Object> queryParam = new HashMap<String, Object>();
            if (zbaoWithdrawals != null) {
                if (StringUtils.isNotBlank(zbaoWithdrawals.getLoginAccount())) {
                    queryParam.put("loginAccount", zbaoWithdrawals.getLoginAccount().trim());
                }
                if (zbaoWithdrawals.getType() != null && zbaoWithdrawals.getType() != 0) {
                    queryParam.put("type", zbaoWithdrawals.getType());
                }
                if (StringUtils.isNotBlank(zbaoWithdrawals.getCardCode())) {
                    queryParam.put("cardCode", zbaoWithdrawals.getCardCode().trim());
                }
                if (StringUtils.isNotBlank(zbaoWithdrawals.getOrderId())) {
                    queryParam.put("orderId", zbaoWithdrawals.getOrderId().trim());
                }
                if (StringUtils.isNotBlank(zbaoWithdrawals.getRealName())) {
                    queryParam.put("realName", zbaoWithdrawals.getRealName().trim());
                }
                if (StringUtils.isNotBlank(zbaoWithdrawals.getOpenbank())) {
                    queryParam.put("openbank", zbaoWithdrawals.getOpenbank().trim());
                }
            }
            queryParam.put("startTime", startTime);
            queryParam.put("endTime", WebServerUtil.oneDateLastTime(endTime));
            List<ZbaoWithdrawals> eoList = zbaoWithdrawalsManager.queryAllForExport(queryParam);
            List<ZbaoWithdrawals> voList = new ArrayList<ZbaoWithdrawals>();
            if (CollectionUtils.isNotEmpty(eoList)) {
                for (ZbaoWithdrawals entityEO : eoList) {
                    ZbaoWithdrawals entityVO = BeanMapper.map(entityEO, ZbaoWithdrawals.class);
                    entityVO.setOrderId(entityEO.getOrderId());
                    entityVO.setLoginAccount(entityEO.getLoginAccount());
                    entityVO.setType(entityEO.getType());
                    entityVO.setAmount(entityEO.getAmount());
                    entityVO.setServiceAmount(entityEO.getServiceAmount());
                    entityVO.setLog(entityEO.getLog());
                    entityVO.setCreateTime(entityEO.getCreateTime());
                    entityVO.setDealTime(entityEO.getDealTime());
                    entityVO.setBankNameType(entityEO.getBankNameType());
                    entityVO.setBankName(entityEO.getBankName());
                    entityVO.setRealName(entityEO.getRealName());
                    entityVO.setAreacode(entityEO.getAreacode());
                    entityVO.setOpenbank(entityEO.getOpenbank());
                    entityVO.setProvinceName(entityEO.getProvinceName());
                    entityVO.setCityName(entityEO.getCityName());
                    voList.add(entityEO);
                }
                 export(voList,response);
//                download(response, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void export(List<ZbaoWithdrawals> voList,HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet();

        ExportExcel exportExcel = new ExportExcel(wb, sheet);

        // 创建单元格样式
        HSSFCellStyle cellStyle = wb.createCellStyle();

        // 指定单元格居中对齐
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // 指定单元格垂直居中对齐
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 指定当单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);

        // 设置单元格字体
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 400);
        cellStyle.setFont(font);

        // 创建报表头部
        String headString = "提现明细表";
        int columnSize = 15;
        exportExcel.createNormalHead(0, headString, columnSize - 1);

        // 创建报表列
        String[] columnHeader = new String[]{"主站登入账号", "订单号", "提现账号", "提现状态", "服务费", "提现金额","在途资金",
                "渠道名称", "开户姓名", "省份", "城市",/* "区号", "开户银行",*/
                "日志信息", "创建时间", "结束时间"};
        exportExcel.createColumHeader(1, columnHeader);
        HSSFCellStyle cellstyle = wb.createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
        // 循环创建中间的单元格的各项的值
        if (CollectionUtils.isNotEmpty(voList)) {
            int i = 2;
            for (ZbaoWithdrawals fundVO : voList) {
                HSSFRow row = sheet.createRow((short) i++);
                exportExcel.createCell(wb, row, 0, CellStyle.ALIGN_LEFT, fundVO.getLoginAccount());
                exportExcel.createCell(wb, row, 1, CellStyle.ALIGN_LEFT, fundVO.getOrderId());
                exportExcel.createCell(wb, row, 2, CellStyle.ALIGN_LEFT, fundVO.getCardCode());
                if (fundVO.getType() == null){
                    exportExcel.createCell(wb, row, 3, CellStyle.ALIGN_LEFT, String.valueOf(""));
                }else {
                    exportExcel.createCell(wb, row, 3, CellStyle.ALIGN_LEFT, String.valueOf(ZbaoWithdrawTypeEnum.getTypeByCode(fundVO.getType()).getDescribe()));
                }
                exportExcel.createCell(wb, row, 4, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getServiceAmount()));
                exportExcel.createCell(wb, row, 5, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getAmount()));
                if (fundVO.getFundOnWay() == null){
                    exportExcel.createCell(wb, row, 6, CellStyle.ALIGN_LEFT, String.valueOf(0));
                }else {
                    exportExcel.createCell(wb, row, 6, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getFundOnWay()));
                }

                exportExcel.createCell(wb, row, 7, CellStyle.ALIGN_LEFT, fundVO.getBankName());
                exportExcel.createCell(wb, row, 8, CellStyle.ALIGN_LEFT, fundVO.getRealName());
                exportExcel.createCell(wb, row, 9, CellStyle.ALIGN_LEFT, fundVO.getProvinceName());
                exportExcel.createCell(wb, row, 10, CellStyle.ALIGN_LEFT, fundVO.getCityName());
                /*exportExcel.createCell(wb, row, 10, CellStyle.ALIGN_LEFT, fundVO.getAreacode());
                exportExcel.createCell(wb, row, 11, CellStyle.ALIGN_LEFT, fundVO.getOpenbank());*/
                exportExcel.createCell(wb, row, 11, CellStyle.ALIGN_LEFT, fundVO.getLog());
                if (fundVO.getCreateTime() != null)
                    exportExcel.createCell(wb, row, 12, CellStyle.ALIGN_LEFT, dataFormat.format(fundVO.getCreateTime()));
                if (fundVO.getDealTime() != null)
                    exportExcel.createCell(wb, row, 13, CellStyle.ALIGN_LEFT, dataFormat.format(fundVO.getDealTime()));
                logger.info("循环了第{}次", i - 2);
            }
        }

        response.setContentType("application/msexcel");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
        response.addHeader("Content-Disposition", "attachment; filename=\"WithdrawalsExcel"+ format +".xls\"");
        try {
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        File file = new File(FILES_EXPORT_PATH);
//        file.mkdirs();
//        String filePath = FILES_EXPORT_PATH + "/" + UUID.randomUUID().toString() + ".xls";
//        exportExcel.outputExcel(filePath);
//        return filePath;
    }

//    public void download(HttpServletResponse response, String path) throws IOException {
//        // 读到流中
//        InputStream in = new FileInputStream(path);// 文件的存放路径
//        // 设置输出的格式
//        response.reset();
//        response.setContentType("application/msexcel");
//        response.addHeader("Content-Disposition", "attachment; filename=\"FundBalanceExcel.xls\"");
//        OutputStream out = response.getOutputStream();
//        // 循环取出流中的数据
//        byte[] b = new byte[100];
//        int len;
//        try {
//            while ((len = in.read(b)) > 0)
//                out.write(b, 0, len);
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

}
