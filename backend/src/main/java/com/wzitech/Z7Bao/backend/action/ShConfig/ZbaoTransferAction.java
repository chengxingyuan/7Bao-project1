package com.wzitech.Z7Bao.backend.action.ShConfig;

import com.kb5173.framework.shared.utils.BeanMapper;
import com.m5173.framework.file.common.ExportExcel;
import com.wzitech.Z7Bao.backend.action.extjs.AbstractAction;
import com.wzitech.Z7Bao.backend.interceptor.ExceptionToJSON;
import com.wzitech.Z7Bao.backend.util.WebServerUtil;
import com.wzitech.Z7Bao.frontend.business.IZbaoTransferManager;
import com.wzitech.Z7Bao.frontend.entity.ZbaoTransfer;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ZbaoTransferType;
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
 * 支出明细
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class ZbaoTransferAction extends AbstractAction {
    @Autowired
    IZbaoTransferManager zbaoTransferManager;

    private ZbaoTransfer zbaoTransfer;

    private List<ZbaoTransfer> zbaoTransferList;

    private Date startTime;

    private Date endTime;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public String queryZbaoTransfer() {
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            if (zbaoTransfer != null) {
                if (StringUtils.isNotBlank(zbaoTransfer.getLoginAccount())) {
                    queryMap.put("loginAccount", zbaoTransfer.getLoginAccount().trim());
                }
                if (zbaoTransfer.getStatus() != null && zbaoTransfer.getStatus() != 0) {
                    queryMap.put("status", zbaoTransfer.getStatus());
                }
                if (StringUtils.isNotBlank(zbaoTransfer.getOrderId())) {
                    queryMap.put("orderId", zbaoTransfer.getOrderId().trim());
                }
                if (StringUtils.isNotBlank(zbaoTransfer.getShOrderId())) {
                    queryMap.put("shOrderId", zbaoTransfer.getShOrderId().trim());
                }
                queryMap.put("startTime", startTime);
                queryMap.put("endTime", WebServerUtil.oneDateLastTime(endTime));
            }
            GenericPage<ZbaoTransfer> genericPage = zbaoTransferManager.queryPage(queryMap, this.limit, this.start, "create_time", false);
            zbaoTransferList = genericPage.getData();
            totalCount = genericPage.getTotalCount();
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    public ZbaoTransfer getZbaoTransfer() {
        return zbaoTransfer;
    }

    public void setZbaoTransfer(ZbaoTransfer zbaoTransfer) {
        this.zbaoTransfer = zbaoTransfer;
    }

    public List<ZbaoTransfer> getZbaoTransferList() {
        return zbaoTransferList;
    }

    public void setZbaoTransferList(List<ZbaoTransfer> zbaoTransferList) {
        this.zbaoTransferList = zbaoTransferList;
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
     * 导出转账明细
     * */
    public void exportTransferExcel(){
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            Map<String, Object> queryParam = new HashMap<String, Object>();
            if (zbaoTransfer != null) {
                if (StringUtils.isNotBlank(zbaoTransfer.getLoginAccount())) {
                    queryParam.put("loginAccount", zbaoTransfer.getLoginAccount().trim());
                }
                if (zbaoTransfer.getStatus() != null && zbaoTransfer.getStatus() != 0) {
                    queryParam.put("status", zbaoTransfer.getStatus());
                }
                if (StringUtils.isNotBlank(zbaoTransfer.getOrderId())) {
                    queryParam.put("orderId", zbaoTransfer.getOrderId().trim());
                }
                if (StringUtils.isNotBlank(zbaoTransfer.getShOrderId())) {
                    queryParam.put("shOrderId", zbaoTransfer.getShOrderId().trim());
                }
            }
            queryParam.put("startTime", startTime);
            queryParam.put("endTime", WebServerUtil.oneDateLastTime(endTime));
            List<ZbaoTransfer> eoList = zbaoTransferManager.queryAllForExport(queryParam);
            List<ZbaoTransfer> voList = new ArrayList<ZbaoTransfer>();
            if (CollectionUtils.isNotEmpty(eoList)) {
                for (ZbaoTransfer entityEO : eoList) {
                    ZbaoTransfer entityVO = BeanMapper.map(entityEO, ZbaoTransfer.class);
                    entityVO.setOrderId(entityEO.getOrderId());
                    entityVO.setLoginAccount(entityEO.getLoginAccount());
                    entityVO.setShOrderId(entityEO.getShOrderId());
                    entityVO.setStatus(entityEO.getStatus());
                    entityVO.setAmount(entityEO.getAmount());
                    entityVO.setCreateTime(entityEO.getCreateTime());
                    entityVO.setDealTime(entityEO.getDealTime());
                    voList.add(entityEO);
                }
                 export(voList,response);
//                download(response, path);
            }
        }catch (Exception e){

        }
    }
    public void export(List<ZbaoTransfer> voList,HttpServletResponse response){
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
        int columnSize = 10;
        exportExcel.createNormalHead(0, headString, columnSize - 1);

        // 创建报表列
        String[] columnHeader = new String[]{"登入账号", "订单号", "5173订单号", "转账状态", "金额","创建时间","结束时间"};
        exportExcel.createColumHeader(1, columnHeader);
        HSSFCellStyle cellstyle = wb.createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
        // 循环创建中间的单元格的各项的值
        if (CollectionUtils.isNotEmpty(voList)) {
            int i = 2;
            for (ZbaoTransfer fundVO : voList) {
                HSSFRow row = sheet.createRow((short) i++);
                exportExcel.createCell(wb, row, 0, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getLoginAccount()));
                exportExcel.createCell(wb, row, 1, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getOrderId()));
                exportExcel.createCell(wb, row, 2, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getShOrderId()));
                if (fundVO.getStatus() == null){
                    exportExcel.createCell(wb, row, 3, CellStyle.ALIGN_LEFT, String.valueOf(""));
                }else {
                    exportExcel.createCell(wb, row, 3, CellStyle.ALIGN_LEFT, String.valueOf(ZbaoTransferType.getTypeByCode(fundVO.getStatus()).getName()));
                }

                exportExcel.createCell(wb, row, 4, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getAmount()));
                if (fundVO.getCreateTime()!=null)
                exportExcel.createCell(wb, row, 5, CellStyle.ALIGN_LEFT, String.valueOf(dataFormat.format(fundVO.getCreateTime())));
                if (fundVO.getDealTime()!=null)
                exportExcel.createCell(wb, row, 6, CellStyle.ALIGN_LEFT, String.valueOf(dataFormat.format(fundVO.getDealTime())));
            }
        }

        response.setContentType("application/msexcel");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
        response.addHeader("Content-Disposition", "attachment; filename=\"TransferExcel"+ format +".xls\"");
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
