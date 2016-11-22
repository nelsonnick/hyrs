package hyrs.controller;

import hyrs.entity.Card;
import hyrs.entity.Location;
import hyrs.entity.Operate;
import hyrs.entity.Person;
import hyrs.entity.User;
import hyrs.interceptor.InsureInterceptor;
import hyrs.interceptor.LoginInterceptor;
import hyrs.interceptor.PowerInterceptor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

public class ExportController extends Controller {
	
	/**
	 * 导出人员数据
	 * */
	@Before(LoginInterceptor.class)
	public void exportPerson() throws IOException {

		if (getPara("query") == null) {
			setSessionAttr("query", getSessionAttr("query"));
		} else {
			setSessionAttr("query", getPara("query"));
		}
		
		if (getPara("queryName") == null) {
			setSessionAttr("queryName", getSessionAttr("queryName"));
		} else {
			setSessionAttr("queryName", getPara("queryName"));
		}

		if (getPara("queryNumber") == null) {
			setSessionAttr("queryNumber", getSessionAttr("queryNumber"));
		} else {
			setSessionAttr("queryNumber", getPara("queryNumber"));
		}

		if (getPara("queryRadio") == null) {
			setSessionAttr("queryRadio", getSessionAttr("queryRadio"));
		} else {
			setSessionAttr("queryRadio", getPara("queryRadio"));
		}

		if (getPara("queryLocation") == null) {
			setSessionAttr("queryLocation", getSessionAttr("queryLocation"));
		} else {
			setSessionAttr("queryLocation", getPara("queryLocation"));
		}
		
		String[] title={"人员序号","人员姓名","证件号码","缴费基数","医保比例","联系电话","联系地址","办理地点","人员备注"};
		//创建Excel工作簿
		XSSFWorkbook workbook = new XSSFWorkbook();
		//创建一个工作表
		XSSFSheet sheet = workbook.createSheet();
		//创建第一行
		XSSFRow row =sheet.createRow(0);
		XSSFCell cell=null;
		//插入表头数据
		for(int i=0;i<title.length;i++){
			cell=row.createCell(i);
			cell.setCellValue(title[i]);
		}
		List<Person> p = Person.dao.paginate(getParaToInt(0, 1), 80000, getSessionAttr("queryName").toString(), getSessionAttr("queryNumber").toString(), getSessionAttr("queryRadio").toString(), getSessionAttr("queryLocation").toString()).getList();
	       
		for (int i = 0; i < p.size(); i++) {
			XSSFRow nextrow = sheet.createRow(i+1);
			XSSFCell cell2 = nextrow.createCell(0);
			System.out.println(p.get(i).get("id"));
			if (p.get(i).get("id") == null) {
				cell2.setCellValue("");
			} else {
				cell2.setCellValue(p.get(i).get("id").toString());
			}
			cell2 = nextrow.createCell(1);
			if (p.get(i).get("name") == null) {
				cell2.setCellValue("");
			} else {
				cell2.setCellValue(p.get(i).get("name").toString());
			}
			cell2 = nextrow.createCell(2);
			if (p.get(i).get("number") == null) {
				cell2.setCellValue("");
			} else {
				cell2.setCellValue(p.get(i).get("number").toString());
			}
			cell2 = nextrow.createCell(3);
			if (p.get(i).get("base") == null) {
				cell2.setCellValue("");
			} else {
				cell2.setCellValue(p.get(i).get("base").toString());
			}
			cell2 = nextrow.createCell(4);
			if (p.get(i).get("radio") == null) {
				cell2.setCellValue("");
			} else {
				cell2.setCellValue(p.get(i).get("radio").toString());
			}
			cell2 = nextrow.createCell(5);
			if (p.get(i).get("phone") == null) {
				cell2.setCellValue("");
			} else {
				cell2.setCellValue(p.get(i).get("phone").toString());
			}
			cell2 = nextrow.createCell(6);
			if (p.get(i).get("address") == null) {
				cell2.setCellValue("");
			} else {
				cell2.setCellValue(p.get(i).get("address").toString());
			}
			cell2 = nextrow.createCell(7);
			if (p.get(i).get("lname") == null) {
				cell2.setCellValue("");
			} else {
				cell2.setCellValue(p.get(i).get("lname").toString());
			}
			cell2 = nextrow.createCell(8);
			if (p.get(i).get("remark") == null) {
				cell2.setCellValue("");
			} else {
				cell2.setCellValue(p.get(i).get("remark").toString());
			}
		}
		
        HttpServletResponse response = getResponse();	
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=person.xlsx");
		OutputStream out = response.getOutputStream();
		workbook.write(out);
		out.flush();
		out.close();
		workbook.close();
		setAttr("userName", ((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber", ((User) getSessionAttr("user")).getStr("number"));

	
		setAttr("insurePage", Person.dao.paginate(getParaToInt(0, 1), 15, getSessionAttr("queryName").toString(), getSessionAttr("queryNumber").toString(), getSessionAttr("queryRadio").toString(), getSessionAttr("queryLocation").toString()));
		setAttr("queryName",getSessionAttr("queryName").toString());
		setAttr("queryNumber",getSessionAttr("queryNumber").toString());
		setAttr("queryRadio",getSessionAttr("queryRadio").toString());
		setAttr("queryLocation",getSessionAttr("queryLocation").toString());
		
		renderNull() ;
		
	}

	/**
	 * 导出业务数据
	 * */
	@Before(LoginInterceptor.class)
	public void exportOperate() throws IOException {
		if (getPara("query") == null) {
			setSessionAttr("query", getSessionAttr("query"));
		} else {
			setSessionAttr("query", getPara("query"));
		}
		
		if (getPara("queryName") == null) {
			setSessionAttr("queryName", getSessionAttr("queryName"));
		} else {
			setSessionAttr("queryName", getPara("queryName"));
		}

		if (getPara("queryNumber") == null) {
			setSessionAttr("queryNumber", getSessionAttr("queryNumber"));
		} else {
			setSessionAttr("queryNumber", getPara("queryNumber"));
		}

		if (getPara("queryContent") == null) {
			setSessionAttr("queryContent", getSessionAttr("queryContent"));
		} else {
			setSessionAttr("queryContent", getPara("queryContent"));
		}

		if (getPara("queryLocation") == null) {
			setSessionAttr("queryLocation", getSessionAttr("queryLocation"));
		} else {
			setSessionAttr("queryLocation", getPara("queryLocation"));
		}

		if (getPara("queryMonth") == null) {
			setSessionAttr("queryMonth", getSessionAttr("queryMonth"));
		} else {
			setSessionAttr("queryMonth", getPara("queryMonth"));
		}
		
		String[] title={"业务编号","人员姓名","证件号码","联系电话","联系地址","当前基数","当前比例","办理地点","办理内容","生效月份","办理时间","办理人员","之前基数","之前比例","之后基数","之后比例","社保备注"};
		//创建Excel工作簿
		XSSFWorkbook workbook = new XSSFWorkbook();
		//创建一个工作表
		XSSFSheet sheet = workbook.createSheet();
		//创建第一行
		XSSFRow row =sheet.createRow(0);
		XSSFCell cell=null;
		//插入表头数据
		for(int i=0;i<title.length;i++){
			cell=row.createCell(i);
			cell.setCellValue(title[i]);
		}
		//追加数据
		List<Operate> o = Operate.dao.paginate(getParaToInt(0, 1), 100000, getSessionAttr("queryName").toString(), getSessionAttr("queryNumber").toString(), getSessionAttr("queryContent").toString(), getSessionAttr("queryLocation").toString(), getSessionAttr("queryMonth").toString()).getList();
		for (int i = 0; i < o.size(); i++) {
			XSSFRow nextrow = sheet.createRow(i+1);
			XSSFCell cell2 = nextrow.createCell(0);
			cell2.setCellValue(o.get(i).get("id").toString());
			cell2 = nextrow.createCell(1);
			cell2.setCellValue(o.get(i).get("pname").toString());
			cell2 = nextrow.createCell(2);
			cell2.setCellValue(o.get(i).get("pnumber").toString());
			cell2 = nextrow.createCell(3);
			cell2.setCellValue(o.get(i).get("pphone").toString());
			cell2 = nextrow.createCell(4);
			cell2.setCellValue(o.get(i).get("paddress").toString());
			cell2 = nextrow.createCell(5);
			cell2.setCellValue(o.get(i).get("pbase").toString());
			cell2 = nextrow.createCell(6);
			cell2.setCellValue(o.get(i).get("pradio").toString());
			cell2 = nextrow.createCell(7);
			cell2.setCellValue(o.get(i).get("lname").toString());
			cell2 = nextrow.createCell(8);
			cell2.setCellValue(o.get(i).get("content").toString());
			
			cell2 = nextrow.createCell(9);
			cell2.setCellValue(o.get(i).get("month").toString());
			cell2 = nextrow.createCell(10);
			cell2.setCellValue(o.get(i).get("time").toString());
			cell2 = nextrow.createCell(11);
			cell2.setCellValue(o.get(i).get("uname").toString());
			cell2 = nextrow.createCell(12);
			cell2.setCellValue(o.get(i).get("baseBefore").toString());
			cell2 = nextrow.createCell(13);
			cell2.setCellValue(o.get(i).get("radioBefore").toString());
			cell2 = nextrow.createCell(14);
			cell2.setCellValue(o.get(i).get("baseAfter").toString());
			cell2 = nextrow.createCell(15);
			cell2.setCellValue(o.get(i).get("radioAfter").toString());
			cell2 = nextrow.createCell(16);
			if (o.get(i).get("remark") == null) {
				cell2.setCellValue("");
			} else {
				cell2.setCellValue(o.get(i).get("remark").toString());
			}
		}
		
        HttpServletResponse response = getResponse();	
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=operate.xlsx");
		OutputStream out = response.getOutputStream();  
		workbook.write(out);
		out.flush();
		out.close();
		workbook.close();
		setAttr("userName",((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber",((User) getSessionAttr("user")).getStr("number"));
			
		setAttr("operatePage", Operate.dao.paginate(getParaToInt(0, 1), 15, getSessionAttr("queryName").toString(), getSessionAttr("queryNumber").toString(), getSessionAttr("queryContent").toString(), getSessionAttr("queryLocation").toString(), getSessionAttr("queryMonth").toString()));
		setAttr("queryName",getSessionAttr("queryName").toString());
		setAttr("queryNumber",getSessionAttr("queryNumber").toString());
		setAttr("queryContent",getSessionAttr("queryContent").toString());
		setAttr("queryLocation",getSessionAttr("queryLocation").toString());
		setAttr("queryMonth",getSessionAttr("queryMonth").toString());
		setAttr("locationPage", Location.dao.paginate(getParaToInt(0, 1), 50));
		renderNull() ;
	}
	

	/**
	 * 导出开卡数据
	 * */
	@Before({LoginInterceptor.class,PowerInterceptor.class,InsureInterceptor.class})
	public void exportCard() throws IOException {

		// String[] title={"工号","中文姓名","英文姓名","金额","性别","证件类型","证件号码","联系电话"};
		String[] title={"序号","姓名","证件类型","证件号码","证件到期日","开户金额","邮编","工作单位地址","固定电话","手机号码","英文名称","性别"};
		//创建Excel工作簿
		XSSFWorkbook workbook = new XSSFWorkbook();
		//创建一个工作表
		XSSFSheet sheet = workbook.createSheet();
		//创建第一行
//		Row row =sheet.createRow(0);
//		Cell cell=null;
//		//插入表头数据
//		for(int i=0;i<title.length;i++){
//			cell=row.createCell(i);
//			cell.setCellValue(title[i]);
//		}
		//追加数据
		List<Card> c=Card.dao.paginate(getParaToInt(0, 1), 100000, 	((User) getSessionAttr("user")).getInt("locationId")).getList();
		for (int i = 0; i < c.size(); i++) {
			XSSFRow nextrow = sheet.createRow(i);
			XSSFCell cell2 = nextrow.createCell(0);
			cell2.setCellValue(c.get(i).get("id").toString());
			cell2 = nextrow.createCell(1);
			cell2.setCellValue(c.get(i).get("cname").toString());
			cell2 = nextrow.createCell(2);
			cell2.setCellValue(c.get(i).get("type").toString());
			cell2 = nextrow.createCell(3);
			cell2.setCellValue(c.get(i).get("number").toString());
			cell2 = nextrow.createCell(4);
			cell2.setCellValue(c.get(i).get("endDate").toString());
			cell2 = nextrow.createCell(5);
			cell2.setCellValue(c.get(i).get("money").toString());
			cell2 = nextrow.createCell(6);
			cell2.setCellValue(c.get(i).get("code").toString());
			cell2 = nextrow.createCell(7);
			cell2.setCellValue(c.get(i).get("address").toString());
			cell2 = nextrow.createCell(8);
			cell2.setCellValue("");
			cell2 = nextrow.createCell(9);
			cell2.setCellValue(c.get(i).get("phone").toString());
			cell2 = nextrow.createCell(10);
			cell2.setCellValue(c.get(i).get("ename").toString());
			cell2 = nextrow.createCell(11);
			cell2.setCellValue(c.get(i).get("sex").toString());
		}
		
        HttpServletResponse response = getResponse();	
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=card.xlsx");
		OutputStream out = response.getOutputStream();  
		workbook.write(out);
		out.flush();
		out.close();
		workbook.close();
		setAttr("userName",((User) getSessionAttr("user")).getStr("name"));
		setAttr("userNumber",((User) getSessionAttr("user")).getStr("number"));
			
		setAttr("cardPage", Card.dao.paginate(getParaToInt(0, 1), 15, ((User) getSessionAttr("user")).getInt("locationId")));

		renderNull() ;
	}
	

}


