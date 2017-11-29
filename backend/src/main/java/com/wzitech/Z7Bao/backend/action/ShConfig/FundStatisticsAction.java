package com.wzitech.Z7Bao.backend.action.ShConfig;

import com.kb5173.framework.shared.utils.BeanMapper;
import com.m5173.framework.file.common.ExportExcel;
import com.wzitech.Z7Bao.backend.action.extjs.AbstractAction;
import com.wzitech.Z7Bao.backend.interceptor.ExceptionToJSON;
import com.wzitech.Z7Bao.backend.util.WebServerUtil;
import com.wzitech.Z7Bao.frontend.business.IFundStatisticsManager;
import com.wzitech.Z7Bao.frontend.entity.FundStatistics;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.utils.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.wzitech.Z7Bao.backend.action.contant.WebServerContants.FILES_EXPORT_PATH;

/**
 * 资金平衡表
 * Created by 340032 on 2017/8/28.
 */


@Controller
@Scope("prototype")
@ExceptionToJSON
public class FundStatisticsAction extends AbstractAction {
    private FundStatistics fundStatistics;
    private List<FundStatistics>  fundStatisticsList;
    private Date startTime;
    private Date endTime;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    IFundStatisticsManager fundStatisticsManager;
    //查询资金信息  FundQueryStatistics
    public String queryFundStatistics(){
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("createStartTime", startTime);
        queryMap.put("createEndTime", WebServerUtil.oneDateLastTime(endTime));
        GenericPage<FundStatistics> genericPage= fundStatisticsManager.queryListInPage(queryMap, this.start,this.limit,"start_time", false);
        fundStatisticsList=genericPage.getData();
        totalCount = genericPage.getTotalCount();
        return this.returnSuccess();
    }
    /**
     * 导出资金平衡表
     * */
    public void exportFundExcel(){
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            Map<String, Object> queryParam = new HashMap<String, Object>();
            queryParam.put("createStartTime", startTime);
            queryParam.put("createEndTime", WebServerUtil.oneDateLastTime(endTime));
            List<FundStatistics> eoList = fundStatisticsManager.queryAllForExport(queryParam);
            List<FundStatistics> voList = new ArrayList<FundStatistics>();
            if (CollectionUtils.isNotEmpty(eoList)) {
                for (FundStatistics entityEO : eoList) {
                    FundStatistics entityVO = BeanMapper.map(entityEO, FundStatistics.class);
                    entityVO.setQcBalance(entityEO.getQcBalance());
                    entityVO.setTxAmount(entityEO.getTxAmount());
                    entityVO.setFkAmount(entityEO.getFkAmount());
                    entityVO.setTxServiceAmount(entityEO.getTxServiceAmount());
                    entityVO.setSdZfAmount(entityEO.getSdZfAmount());
                    entityVO.setQmBalance(entityEO.getQmBalance());
                    entityVO.setStartTime(entityEO.getStartTime());
                    entityVO.setEndTime(entityEO.getEndTime());
                    entityVO.setProcessing(entityEO.getProcessing());
                    voList.add(entityVO);
                }
                 export(voList,response);
//                download(response, path);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void export(List<FundStatistics> voList,HttpServletResponse response){
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
        String headString = "资金平衡表";
        int columnSize = 10;
        exportExcel.createNormalHead(0, headString, columnSize - 1);

        // 创建报表列
        String[] columnHeader = new String[]{"期初余额", "支付金额", "提现金额", "付款金额", "提现服务费",
                "售得充值", "期末余额","提现处理中金额", "期初时间", "期末时间"};
        exportExcel.createColumHeader(1, columnHeader);

        HSSFCellStyle cellstyle = wb.createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);

        // 循环创建中间的单元格的各项的值
        if (CollectionUtils.isNotEmpty(voList)) {
            int i = 2;
            for (FundStatistics fundVO : voList) {
                HSSFRow row = sheet.createRow((short) i++);
                exportExcel.createCell(wb, row, 0, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getQcBalance()));
                exportExcel.createCell(wb, row, 1, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getZfAmount()));
                exportExcel.createCell(wb, row, 2, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getTxAmount()));
                exportExcel.createCell(wb, row, 3, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getFkAmount()));
                exportExcel.createCell(wb, row, 4, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getTxServiceAmount()));
                exportExcel.createCell(wb, row, 5, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getSdZfAmount()));
                exportExcel.createCell(wb, row, 6, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getQmBalance()));
                exportExcel.createCell(wb, row, 7, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getProcessing()));
                if (fundVO.getStartTime()!=null)
                exportExcel.createCell(wb, row, 8, CellStyle.ALIGN_LEFT, String.valueOf(dataFormat.format(fundVO.getStartTime())));
                if (fundVO.getEndTime()!=null)
                exportExcel.createCell(wb, row, 9, CellStyle.ALIGN_LEFT, String.valueOf(dataFormat.format(fundVO.getEndTime())));
            }
        }
        response.setContentType("application/msexcel");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
        response.addHeader("Content-Disposition", "attachment; filename=\"FundStatisticsExcel"+ format +".xls\"");
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
    public FundStatistics getFundStatistics() {
        return fundStatistics;
    }

    public void setFundStatistics(FundStatistics fundStatistics) {
        this.fundStatistics = fundStatistics;
    }

    public List<FundStatistics> getFundStatisticsList() {
        return fundStatisticsList;
    }

    public void setFundStatisticsList(List<FundStatistics> fundStatisticsList) {
        this.fundStatisticsList = fundStatisticsList;
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
