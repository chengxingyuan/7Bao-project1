package com.wzitech.Z7Bao.backend.action.ShConfig;

import com.kb5173.framework.shared.utils.BeanMapper;
import com.m5173.framework.file.common.ExportExcel;
import com.wzitech.Z7Bao.backend.action.extjs.AbstractAction;
import com.wzitech.Z7Bao.backend.interceptor.ExceptionToJSON;
import com.wzitech.Z7Bao.backend.util.WebServerUtil;
import com.wzitech.Z7Bao.frontend.business.IZbaoFunDetailManager;
import com.wzitech.Z7Bao.frontend.entity.ZbaoFundDetail;
import com.wzitech.chaos.framework.server.common.exception.SystemException;
import com.wzitech.chaos.framework.server.dataaccess.pagination.GenericPage;
import com.wzitech.gamegold.common.enums.ZbaoFundDetailType;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangmin
 * Date:2017/8/24
 * 资金明细管理
 */
@Controller
@Scope("prototype")
@ExceptionToJSON
public class ZbaoFundDetailAction extends AbstractAction {
    private List<ZbaoFundDetail> detailList;
    private ZbaoFundDetail zbaoFundDetail;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Date startTime;

    private Date endTime;

    @Autowired
    IZbaoFunDetailManager zbaoFunDetailManager;

    public String queryZbaoFundDetail() {
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            if (zbaoFundDetail != null) {
                if (StringUtils.isNotBlank(zbaoFundDetail.getLoginAccount())) {
                    queryMap.put("loginAccount", zbaoFundDetail.getLoginAccount().trim());
                }
                if (StringUtils.isNotBlank(zbaoFundDetail.getCgOrderId())) {
                    queryMap.put("cgOrderId", zbaoFundDetail.getCgOrderId().trim());
                }
                if (StringUtils.isNotBlank(zbaoFundDetail.getCzOrderId())) {
                    queryMap.put("czOrderId", zbaoFundDetail.getCzOrderId().trim());
                }
                if (StringUtils.isNotBlank(zbaoFundDetail.getTxOrderId())) {
                    queryMap.put("txOrderId", zbaoFundDetail.getTxOrderId().trim());
                }
                if (StringUtils.isNotBlank(zbaoFundDetail.getOutOrderId())) {
                    queryMap.put("outOrderId", zbaoFundDetail.getOutOrderId().trim());
                }
                if (zbaoFundDetail.getType() != null && zbaoFundDetail.getType() != 0) {
                    queryMap.put("type", zbaoFundDetail.getType());
                }
                queryMap.put("startTime", startTime);
                queryMap.put("endTime", WebServerUtil.oneDateLastTime(endTime));
            }


            GenericPage<ZbaoFundDetail> genericPage = zbaoFunDetailManager.queryPage(queryMap, this.limit, this.start, "create_time", false);
            detailList = genericPage.getData();
            totalCount = genericPage.getTotalCount();
            return this.returnSuccess();
        } catch (SystemException e) {
            return this.returnError(e);
        }
    }

    /**
     * 导出资金明细表
     *
     * @return
     */
    public void exportFundDetail() {
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            Map<String, Object> queryParam = new HashMap<String, Object>();
            queryParam.put("startTime", startTime);
            queryParam.put("endTime", WebServerUtil.oneDateLastTime(endTime));
            if (zbaoFundDetail != null) {
                if (StringUtils.isNotBlank(zbaoFundDetail.getLoginAccount())) {
                    queryParam.put("loginAccount", zbaoFundDetail.getLoginAccount().trim());
                }
                if (StringUtils.isNotBlank(zbaoFundDetail.getCgOrderId())) {
                    queryParam.put("cgOrderId", zbaoFundDetail.getCgOrderId().trim());
                }
                if (StringUtils.isNotBlank(zbaoFundDetail.getCzOrderId())) {
                    queryParam.put("czOrderId", zbaoFundDetail.getCzOrderId().trim());
                }
                if (StringUtils.isNotBlank(zbaoFundDetail.getTxOrderId())) {
                    queryParam.put("txOrderId", zbaoFundDetail.getTxOrderId().trim());
                }
                if (StringUtils.isNotBlank(zbaoFundDetail.getOutOrderId())) {
                    queryParam.put("outOrderId", zbaoFundDetail.getOutOrderId().trim());
                }
                if (zbaoFundDetail.getType() != null && zbaoFundDetail.getType() != 0) {
                    queryParam.put("type", zbaoFundDetail.getType());
                }
            }

            List<ZbaoFundDetail> eoList = zbaoFunDetailManager.quertExportData(queryParam);
            List<ZbaoFundDetail> voList = new ArrayList<ZbaoFundDetail>();
            if (CollectionUtils.isNotEmpty(eoList)) {
                for (ZbaoFundDetail entityEO : eoList) {
                    ZbaoFundDetail entityVO = BeanMapper.map(entityEO, ZbaoFundDetail.class);
                    entityVO.setLoginAccount(entityEO.getLoginAccount());
                    if (StringUtils.isBlank(entityEO.getCzOrderId())) {
                        entityEO.setCzOrderId("");
                    }
                    entityVO.setCzOrderId(entityEO.getCzOrderId());
                    if (StringUtils.isBlank(entityEO.getTxOrderId())) {
                        entityEO.setTxOrderId("");
                    }
                    entityVO.setTxOrderId(entityEO.getTxOrderId());
                    if (StringUtils.isBlank(entityEO.getCgOrderId())) {
                        entityEO.setCgOrderId("");
                    }
                    entityVO.setCgOrderId(entityEO.getCgOrderId());
                    if (StringUtils.isBlank(entityEO.getOutOrderId())) {
                        entityEO.setOutOrderId("");
                    }
                    entityVO.setOutOrderId(entityEO.getOutOrderId());
                    if (entityEO.getAmount() == null) {
                        entityEO.setAmount(BigDecimal.ZERO);
                    }
                    entityVO.setAmount(entityEO.getAmount());
                    entityVO.setLog(entityEO.getLog());
                    entityVO.setIsSuccess(entityEO.getIsSuccess());
                    entityVO.setCreateTime(entityEO.getCreateTime());
                    voList.add(entityVO);
                }
                export(voList, response);
//                download(response, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void export(List<ZbaoFundDetail> voList, HttpServletResponse response) {
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
        String headString = "资金明细表";
        int columnSize = 10;
        exportExcel.createNormalHead(0, headString, columnSize - 1);

        // 创建报表列
        String[] columnHeader = new String[]{"买家账号", "充值单号", "提现订单号", "采购转账订单号", "外部订单号", "资金类型", "金额", "日志详情", "操作是否成功", "操作时间"};
        exportExcel.createColumHeader(1, columnHeader);

        HSSFCellStyle cellstyle = wb.createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);


        // 循环创建中间的单元格的各项的值
        if (CollectionUtils.isNotEmpty(voList)) {
            int i = 2;
            for (ZbaoFundDetail fundVO : voList) {
                HSSFRow row = sheet.createRow((short) i++);
                exportExcel.createCell(wb, row, 0, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getLoginAccount()));
                exportExcel.createCell(wb, row, 1, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getCzOrderId()));
                exportExcel.createCell(wb, row, 2, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getTxOrderId()));
                exportExcel.createCell(wb, row, 3, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getCgOrderId()));
                exportExcel.createCell(wb, row, 4, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getOutOrderId()));
                if (fundVO.getType() == null) {
                    exportExcel.createCell(wb, row, 5, CellStyle.ALIGN_LEFT, String.valueOf(""));
                } else {
                    exportExcel.createCell(wb, row, 5, CellStyle.ALIGN_LEFT, String.valueOf(ZbaoFundDetailType.getTypeByCode(fundVO.getType()).getName()));
                }

                exportExcel.createCell(wb, row, 6, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getAmount()));
                exportExcel.createCell(wb, row, 7, CellStyle.ALIGN_LEFT, String.valueOf(fundVO.getLog()));
                if (fundVO.getIsSuccess() == null || fundVO.getIsSuccess() == false) {
                    exportExcel.createCell(wb, row, 8, CellStyle.ALIGN_LEFT, String.valueOf("失败"));
                } else {
                    exportExcel.createCell(wb, row, 8, CellStyle.ALIGN_LEFT, String.valueOf("成功"));
                }

                exportExcel.createCell(wb, row, 9, CellStyle.ALIGN_LEFT, String.valueOf(dataFormat.format(fundVO.getCreateTime())));
            }
        }

//        File file = new File(FILES_EXPORT_PATH);
//        file.mkdirs();
//        String filePath = FILES_EXPORT_PATH + "/" + UUID.randomUUID().toString() + ".xls";
        try {
            response.setContentType("application/msexcel");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String format = simpleDateFormat.format(new Date());
            response.addHeader("Content-Disposition", "attachment; filename=\"FundDetailExcel"+ format +".xls\"");
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        exportExcel.outputExcel(filePath);
//        return filePath;
    }

//    public void download(HttpServletResponse response, String path) throws IOException {
//        HttpServletRequest request = ServletActionContext.getRequest();
//        // 读到流中
//        InputStream in = new FileInputStream(path);// 文件的存放路径
//        // 设置输出的格式
//        response.reset();
//        response.setContentType("application/msexcel");
//        response.addHeader("Content-Disposition", "attachment; filename=\"FundDetailExcel.xls\"");
//        OutputStream out = response.getOutputStream();
//
//            // 循环取出流中的数据
//            byte[] b = new byte[100];
//            int len;
//            try {
//                while ((len = in.read(b)) > 0)
//                    out.write(b, 0, len);
//            } finally {
//                if (in != null) {
//                    try {
//                        in.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//    }
//
//    /**
//     * @MethodName httpsConverBytes
//     * @Description https路径文件内容获取
//     *
//     * @param url
//     * @return
//     */
//    private byte[] httpsConverBytes(String url) {
//        BufferedInputStream inStream = null;
//        ByteArrayOutputStream outStream = null;
//
//        try {
//
//            TrustManager[] tm = { new TrustAnyTrustManager() };
//            SSLContext sc = SSLContext.getInstance("SSL", "SunJSSE");
//            sc.init(null, tm, new java.security.SecureRandom());
//            URL console = new URL(url);
//
//            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
//            conn.setSSLSocketFactory(sc.getSocketFactory());
//            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setRequestMethod("POST");
//            conn.connect();
//
//            inStream = new BufferedInputStream(conn.getInputStream());
//            outStream = new ByteArrayOutputStream();
//
//            byte[] buffer = new byte[1024];
//            int len = 0;
//            while ((len = inStream.read(buffer)) != -1) {
//                outStream.write(buffer, 0, len);
//            }
//
//            byte[] content = outStream.toByteArray();
//            return content;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            if (null != inStream) {
//                try {
//                    inStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (null != outStream) {
//                try {
//                    outStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return null;
//    }
//
//    private static class TrustAnyTrustManager implements X509TrustManager {
//        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
//
//        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
//
//        public X509Certificate[] getAcceptedIssuers() {
//            return new X509Certificate[] {};
//        }
//    }
//
//    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
//        public boolean verify(String hostname, SSLSession session) {
//            return true;
//        }
//    }

    public List<ZbaoFundDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<ZbaoFundDetail> detailList) {
        this.detailList = detailList;
    }

    public ZbaoFundDetail getZbaoFundDetail() {
        return zbaoFundDetail;
    }

    public void setZbaoFundDetail(ZbaoFundDetail zbaoFundDetail) {
        this.zbaoFundDetail = zbaoFundDetail;
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
