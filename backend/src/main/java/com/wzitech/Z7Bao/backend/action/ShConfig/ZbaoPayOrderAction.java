package com.wzitech.Z7Bao.backend.action.ShConfig;

import com.kb5173.framework.shared.utils.BeanMapper;
import com.m5173.framework.file.common.ExportExcel;
import com.wzitech.Z7Bao.backend.action.extjs.AbstractAction;
import com.wzitech.Z7Bao.backend.interceptor.ExceptionToJSON;
import com.wzitech.Z7Bao.backend.util.WebServerUtil;
import com.wzitech.Z7Bao.frontend.business.IZbaoPayOrderManager;
import com.wzitech.Z7Bao.frontend.entity.ZbaoPayOrder;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.FundType;
import com.wzitech.gamegold.common.enums.ZBaoPayType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.wzitech.Z7Bao.backend.action.contant.WebServerContants.FILES_EXPORT_PATH;


/**
 * Created by wangmin
 * Date:2017/8/24
 * 充值明细
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class ZbaoPayOrderAction extends AbstractAction {
    @Autowired
    IZbaoPayOrderManager zbaoPayOrderManager;

    private ZbaoPayOrder zbaoPayOrder;

    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private List<ZbaoPayOrder> zbaoPayOrderList;

    private Date startTime;

    private Date endTime;

    public String queryPayOrder() {

        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            if (zbaoPayOrder != null) {
                if (StringUtils.isNotBlank(zbaoPayOrder.getLoginAccount())) {
                    queryMap.put("loginAccount", zbaoPayOrder.getLoginAccount().trim());
                }
                if (StringUtils.isNotBlank(zbaoPayOrder.getOrderId())) {
                    queryMap.put("orderId", zbaoPayOrder.getOrderId().trim());
                }
                if (StringUtils.isNotBlank(zbaoPayOrder.getOutOrderId())) {
                    queryMap.put("outOrderId", zbaoPayOrder.getOutOrderId().trim());
                }
                if (StringUtils.isNotBlank(zbaoPayOrder.getZzOrderId())) {
                    queryMap.put("zzOrderId", zbaoPayOrder.getZzOrderId().trim());
                }
                if (zbaoPayOrder.getOrderType() != null && zbaoPayOrder.getOrderType() != 0) {
                    queryMap.put("orderType", zbaoPayOrder.getOrderType());
                }
                if (zbaoPayOrder.getStatus() != null && zbaoPayOrder.getStatus() != 0) {
                    queryMap.put("status", zbaoPayOrder.getStatus());
                }
                queryMap.put("startTime", startTime);
                queryMap.put("endTime", WebServerUtil.oneDateLastTime(endTime));
            }

            GenericPage<ZbaoPayOrder> genericPage = zbaoPayOrderManager.queryPage(queryMap, this.limit, this.start, "create_time", false);
            zbaoPayOrderList = genericPage.getData();
            totalCount = genericPage.getTotalCount();
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 导出充值明细表
     *
     * @return
     */
    public void exportPayOrderFundDetail() {
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            Map<String, Object> queryParam = new HashMap<String, Object>();
            if (zbaoPayOrder != null) {
                if (StringUtils.isNotBlank(zbaoPayOrder.getLoginAccount())) {
                    queryParam.put("loginAccount", zbaoPayOrder.getLoginAccount().trim());
                }
                if (StringUtils.isNotBlank(zbaoPayOrder.getOrderId())) {
                    queryParam.put("orderId", zbaoPayOrder.getOrderId().trim());
                }
                if (StringUtils.isNotBlank(zbaoPayOrder.getOutOrderId())) {
                    queryParam.put("outOrderId", zbaoPayOrder.getOutOrderId().trim());
                }
                if (StringUtils.isNotBlank(zbaoPayOrder.getZzOrderId())) {
                    queryParam.put("zzOrderId", zbaoPayOrder.getZzOrderId().trim());
                }
                if (zbaoPayOrder.getOrderType() != null && zbaoPayOrder.getOrderType() != 0) {
                    queryParam.put("orderType", zbaoPayOrder.getOrderType());
                }
                if (zbaoPayOrder.getStatus() != null && zbaoPayOrder.getStatus() != 0) {
                    queryParam.put("status", zbaoPayOrder.getStatus());
                }
            }
            queryParam.put("startTime", startTime);
            queryParam.put("endTime", WebServerUtil.oneDateLastTime(endTime));
            List<ZbaoPayOrder> eoList = zbaoPayOrderManager.queryPayOrderExport(queryParam);
            List<ZbaoPayOrder> voList = new ArrayList<ZbaoPayOrder>();
            if (CollectionUtils.isNotEmpty(eoList)) {
                for (ZbaoPayOrder entityEO : eoList) {
                    ZbaoPayOrder entityVO = BeanMapper.map(entityEO, ZbaoPayOrder.class);
                    entityVO.setLoginAccount(entityEO.getLoginAccount());
                    if (StringUtils.isBlank(entityEO.getOrderId())) {
                        entityEO.setOrderId("");
                    }
                    entityVO.setOrderId(entityEO.getOrderId());
                    if (StringUtils.isBlank(entityEO.getZzOrderId())) {
                        entityEO.setZzOrderId("");
                    }
                    entityVO.setZzOrderId(entityEO.getZzOrderId());
                    if (entityEO.getOrderType() == 0 || entityEO.getOrderType() == null) {
                        entityEO.setOrderType(Integer.parseInt(""));
                    }
                    entityVO.setOrderType(entityEO.getOrderType());
                    if (StringUtils.isBlank(entityEO.getOutOrderId())) {
                        entityEO.setOutOrderId("");
                    }
                    entityVO.setOutOrderId(entityEO.getOutOrderId());
                    if (entityEO.getAmount() == null) {
                        entityEO.setAmount(BigDecimal.ZERO);
                    }
                    entityVO.setAmount(entityEO.getAmount());
                    entityVO.setStatus(entityEO.getStatus());
                    if (StringUtils.isBlank(entityEO.getPaymentAccount())) {
                        entityEO.setPaymentAccount("");
                    }
                    entityVO.setPaymentAccount(entityEO.getPaymentAccount());
                    if (StringUtils.isBlank(entityEO.getRemark())) {
                        entityEO.setRemark("");
                    }
                    entityVO.setRemark(entityEO.getRemark());
                    entityVO.setDealTime(entityEO.getDealTime());
                    entityVO.setCreateTime(entityEO.getCreateTime());
                    voList.add(entityVO);
                }
                export(voList,response);
//                download(response, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void export(List<ZbaoPayOrder> voList,HttpServletResponse response) {
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
        String headString = "充值明细表";
        int columnSize = 15;
        exportExcel.createNormalHead(0, headString, columnSize - 1);

        // 创建报表列
        String[] columnHeader = new String[]{"主站登录账号", "订单号", "外部订单号", "主站订单号", "充值类型", "订单状态", "金额" /*"备注", "支付账户"*/, "创建时间", "结束时间"};
        exportExcel.createColumHeader(1, columnHeader);

        HSSFCellStyle cellstyle = wb.createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);


        // 循环创建中间的单元格的各项的值
        if (CollectionUtils.isNotEmpty(voList)) {
            int i = 2;
            for (ZbaoPayOrder fundVO : voList) {
                HSSFRow row = sheet.createRow((short) i++);
                exportExcel.createCell(wb, row, 0, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getLoginAccount()));
                exportExcel.createCell(wb, row, 1, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getOrderId()));
                exportExcel.createCell(wb, row, 2, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getOutOrderId()));
                exportExcel.createCell(wb, row, 3, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getZzOrderId()));
                if (fundVO.getOrderType() == null) {
                    exportExcel.createCell(wb, row, 4, CellStyle.ALIGN_LEFT, String.valueOf(""));
                } else {
                    exportExcel.createCell(wb, row, 4, CellStyle.ALIGN_LEFT, String.valueOf(ZBaoPayType.getTypeByCode(fundVO.getOrderType()).getName()));
                }
                if (fundVO.getStatus() == null){
                    exportExcel.createCell(wb, row, 5, CellStyle.ALIGN_LEFT, String.valueOf(""));
                }else {
                    exportExcel.createCell(wb, row, 5, CellStyle.ALIGN_LEFT, String.valueOf(FundType.getTypeByCode(fundVO.getStatus()).getName()));
                }
                exportExcel.createCell(wb, row, 6, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getAmount()));
               /* exportExcel.createCell(wb, row, 7, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getRemark()));
                exportExcel.createCell(wb, row, 8, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getPaymentAccount()));*/
                if (fundVO.getCreateTime() == null){
                    exportExcel.createCell(wb, row, 7, CellStyle.ALIGN_LEFT, String.valueOf(""));
                }else {
                    exportExcel.createCell(wb, row, 7, CellStyle.ALIGN_LEFT, String.valueOf(dataFormat.format(fundVO.getCreateTime())));
                }
                if (fundVO.getDealTime() == null){
                    exportExcel.createCell(wb, row, 8, CellStyle.ALIGN_LEFT, String.valueOf(""));
                }else {
                    exportExcel.createCell(wb, row, 8, CellStyle.ALIGN_LEFT, String.valueOf(dataFormat.format(fundVO.getDealTime())));
                }

            }
        }

        response.setContentType("application/msexcel");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
        response.addHeader("Content-Disposition", "attachment; filename=\"PayOrderExcel"+ format +".xls\"");
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

    public void download(HttpServletResponse response, String path) throws IOException {
        // 读到流中
        InputStream in = new FileInputStream(path);// 文件的存放路径
        // 设置输出的格式
        response.reset();
        response.setContentType("application/msexcel");
        response.addHeader("Content-Disposition", "attachment; filename=\"PayOrderExcel.xls\"");
        OutputStream out = response.getOutputStream();
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = in.read(b)) > 0)
                out.write(b, 0, len);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public ZbaoPayOrder getZbaoPayOrder() {
        return zbaoPayOrder;
    }

    public void setZbaoPayOrder(ZbaoPayOrder zbaoPayOrder) {
        this.zbaoPayOrder = zbaoPayOrder;
    }

    public List<ZbaoPayOrder> getZbaoPayOrderList() {
        return zbaoPayOrderList;
    }

    public void setZbaoPayOrderList(List<ZbaoPayOrder> zbaoPayOrderList) {
        this.zbaoPayOrderList = zbaoPayOrderList;
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
}
