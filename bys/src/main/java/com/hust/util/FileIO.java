package com.hust.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileIO {
	/**
	 * 读取excel文件内容，没一个单元格是一个string，每一行的所有单元格组成一个String数组
	 * @param filePath 文件绝对路径
	 * @return 文件读取失败是返回null
	 */
	public static List<String[]> excelRead(String filePath){
		File file = new File(filePath);
		if(!file.exists()){
			return null;
		}
		InputStream inputStream = null;
		List<String[]> res = new ArrayList<String[]>();
		try {
			inputStream = new FileInputStream(file);
			Workbook workbook = null;
			if (filePath.endsWith("xls")) {
				workbook = new HSSFWorkbook(inputStream);
			} else {
				workbook = new XSSFWorkbook(inputStream);
			}
			Sheet sheet = workbook.getSheetAt(0);
			// 行数
			int rowNum = sheet.getLastRowNum();
			// 列数
			int colNum = sheet.getRow(0).getLastCellNum();
			// excel首行为属性行
			String[] attrRow = convert(sheet.getRow(0), colNum);
			// url所在列位置
			int indexOfUrl = AttrUtil.findIndexOfUrl(attrRow);
			List<String> exitUrls = new ArrayList<String>();
			for (int i = 1; i <= rowNum; i++) {
				String[] row = convert(sheet.getRow(i), colNum);
				// 如果url为空则过滤该行数据
				if (StringUtils.isBlank(row[indexOfUrl])) {
					continue;
				}
				// url去重
				if (!exitUrls.contains(row[indexOfUrl])) {
					exitUrls.add(row[indexOfUrl]);
					res.add(row);
				}
			}
			inputStream.close();
			res.add(0, attrRow);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 读取txt文件，以换行符为准，每一行都是一个string，所有行组成List<String>
	 * @param filePath 文件绝对路径
	 * @return 文件读取失败返回null
	 */
	public static List<String> txtRead(String filePath){
		File file = new File(filePath);
		if(!file.exists()){
			return null;
		}
		List<String> res = new ArrayList<>();
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String string= null;
			while((string = br.readLine())!=null){
				res.add(string);
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}
	
	/**
	 * 以追加的方式写入txt文件，每一个string是一行，以追加的方式写入文件
	 * @param filePath	文件路径，没有则创建
	 * @param content 文件内容
	 * @return
	 */
	public static boolean txtWrite(String filePath,List<String> content){
		File file = new File(filePath);
		File filePatern = file.getParentFile();
		if(!file.exists()){
			if(!filePatern.exists()){
				filePatern.mkdirs();
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		FileOutputStream outputStream = null;
		FileWriter fw = null;
		try {
			outputStream = new FileOutputStream(file,true);
			fw = new FileWriter(file,true);
			for (String string : content) {
				fw.append(string+"\r\n");
			}
			fw.flush();
			fw.close();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally{
			try {
				fw.close();
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 以追加的方式写入txt文件，每一个string是一行，以追加的方式写入文件
	 * @param filePath	文件路径，没有则创建
	 * @param content 文件内容
	 * @return
	 */
	public static boolean txtWrite(String filePath,String content){
		File file = new File(filePath);
		File filePatern = file.getParentFile();
		if(!file.exists()){
			if(!filePatern.exists()){
				filePatern.mkdirs();
			}
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		FileOutputStream outputStream = null;
		FileWriter fw = null;
		try {
			outputStream = new FileOutputStream(file,true);
			fw = new FileWriter(file,true);
			fw.append(content+"\r\n");
			fw.flush();
			fw.close();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally{
			try {
				fw.close();
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	static String[] convert(Row row, int length) {
		String[] res = new String[length];
		for (int i = 0; i < length; i++) {
			try {
				Cell cell = row.getCell(i);
				if (cell.getCellType() == 0) {
					res[i] = TimeUtil.convert(cell);
				} else {
					res[i] = cell.toString();
				}
			} catch (Exception e) {
				res[i] = "";
			}
			res[i] = res[i].replaceAll("\n", "").trim();
			res[i] = res[i].replaceAll("\t", "").trim();
			res[i] = res[i].replaceAll("\r", "").trim();
			res[i] = res[i].replaceAll("\b", "").trim();
			res[i] = res[i].replaceAll("\f", "").trim();
		}
		return res;
	}
}
