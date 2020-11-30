package com.javakc.pms.dispord.service;

import com.javakc.mes.commonutils.jpa.base.service.BaseService;
import com.javakc.mes.commonutils.jpa.dynamic.SimpleSpecificationBuilder;
import com.javakc.pms.dispord.dao.DispOrdDao;
import com.javakc.pms.dispord.entity.DispOrd;
import com.javakc.pms.dispord.vo.DispOrdQuery;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DispOrdService extends BaseService<DispOrdDao, DispOrd> {

    @Autowired
    private DispOrdDao dispOrdDao;


    public List<DispOrd> findAll() {
        List<DispOrd> list = dispOrdDao.findAll();
        return list;
    }

    /**
     * 根据条件进行分页查询 - 调度指令库
     * @param dispOrdQuery
     * @param pageNum
     * @param pageSize
     * @return page
     */
    public Page<DispOrd> findPageDispOrd(DispOrdQuery dispOrdQuery, int pageNum, int pageSize) {
        SimpleSpecificationBuilder<DispOrd> simpleSpecificationBuilder = new SimpleSpecificationBuilder<>();
        if (!StringUtils.isEmpty(dispOrdQuery.getOrderName())){
            /**
             * 可用操作符
             * = 等值、!= 不等值 (字符串、数字)
             * >=、<=、>、< (数字)
             * ge，le，gt，lt(字符串)
             * :表示like %v%
             * l:表示 v%
             * :l表示 %v
             * null表示 is null
             * !null表示 is not null
             */
            simpleSpecificationBuilder.and("orderName",":",dispOrdQuery.getOrderName());
        }
        // ## 创建时间 - 区间查询
        if (!StringUtils.isEmpty(dispOrdQuery.getBeginDate())){
            simpleSpecificationBuilder.and("gmtCreate","ge",dispOrdQuery.getBeginDate());
        }
        if (!StringUtils.isEmpty(dispOrdQuery.getEndDate())){
            simpleSpecificationBuilder.and("gmtCreate","lt",dispOrdQuery.getEndDate());
        }
        Page page = dao.findAll(simpleSpecificationBuilder.getSpecification(), PageRequest.of(pageNum - 1,pageSize));
        return page;
    }

    /**
     * POI导出
     * @param response
     */
    public void exportExcel(HttpServletResponse response) {
        // ## 设置表头
        String[] titles = {"指令名称","指令类型","优先级","指令描述"};
        // ## 1.创建工作簿
        HSSFWorkbook hssfWorbook = new HSSFWorkbook();
        // ## 2.在当前工作簿下创建 Sheet
        HSSFSheet hssfSheet = hssfWorbook.createSheet("指令列表");
        // ## 3.在当前的Sheet 下创建Row,创建第一行？第一行代表的是表头
        HSSFRow row = hssfSheet.createRow(0);
        // ## 4.设置表头数据
        for (int i = 0; i < titles.length ; i++) {
            row.createCell(i).setCellValue(titles[i]);
        }
        // ## 5.查询数据
        List<DispOrd> list = dao.findAll();
        // ## 6.设置行数据
        for (int i = 0; i < list.size(); i++) {
            // ## 7.取出数据
            DispOrd dispOrd = list.get(i);
            HSSFRow hssfRow = hssfSheet.createRow(i + 1);
            hssfRow.createCell(0).setCellValue(dispOrd.getOrderName());
            hssfRow.createCell(1).setCellValue(dispOrd.getSpecType());
            hssfRow.createCell(2).setCellValue(dispOrd.getPriority());
            hssfRow.createCell(3).setCellValue(dispOrd.getOrderDesc());
        }
        // ## 文件名 导出时间
        String fileName = new String(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        // ##
        try {
            response.setHeader("Content-disposition","attachement;filename=" + fileName + ".xls");
            ServletOutputStream outputStream = response.getOutputStream();
            // ## 下载
            hssfWorbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * POI导入
     * @param file
     */
    @Transactional
    public void importExcel(MultipartFile file) {

        try {
            // ## 1.获取文件流
            InputStream inputStream = file.getInputStream();
            // ## 2.创建工作簿接口
            Workbook workbook = null;
            // ## 3.把文件流的内容放入到工作簿当中
            if (file.getOriginalFilename().endsWith(".xlsx")) {
                // ## 2003年之后的 支持xlsx
                workbook = new XSSFWorkbook(inputStream);
            }else {
                // ## 2003年及之前
                workbook = new HSSFWorkbook(inputStream);
            }
            // ## 4.得到Sheet总数
            int numberOfSheet = workbook.getNumberOfSheets();
            for (int i = 0; i <numberOfSheet ; i++) {
                // ## 5.得到具体的Sheet
                Sheet sheet = workbook.getSheetAt(i);
                // ## 6.得到Row总行数
                int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                // ## 创建集合
                List<DispOrd> list = new ArrayList<>();
                //第一行不要 因为是表头
                for (int j = 1; j < physicalNumberOfRows; j++) {
                    // ## 创建对象
                    DispOrd dispOrd = new DispOrd();
                    Row row = sheet.getRow(j);
                    // ## 设置到对象当中
                    dispOrd.setOrderName(row.getCell(0).getStringCellValue());
                    dispOrd.setSpecType((int) row.getCell(1).getNumericCellValue());
                    dispOrd.setPriority((int) row.getCell(2).getNumericCellValue());
                    dispOrd.setOrderDesc(row.getCell(3).getStringCellValue());

                    // ## 把每一行的对象放到一个集合当中
                    list.add(dispOrd);
                }
                // 批量保存
                dao.saveAll(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
