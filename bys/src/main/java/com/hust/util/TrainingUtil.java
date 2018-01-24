package com.hust.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
/**
 * filePath为根目录
 * data目录下是所有标记的数据集
 * test目录是由数据集平分后的数据集用来生成训练集和测试集
 * @author Jack
 *
 */
public class TrainingUtil {
	private static String filePath = System.getProperty("user.dir");
	
	/**
	 * 从excel文件中获取人工标记好的数据生成训练数据
	 * @param ExcelFilePath
	 * @return
	 */
	public static boolean generateTrainingData(String ExcelFilePath) {
		List<String[]> content= FileIO.excelRead(filePath+"/成都环保总表.xlsx");
		String[] attr = content.remove(0);
		int txtContentIndex = AttrUtil.findIndexOfSth(attr, "内容");//文本内容所在列的下标
		int typeIndex = AttrUtil.findIndexOfSth(attr, "分类");//污染分类所在列的下标
		HashMap<String,List<String>> map = new HashMap<>();//存储各类污染信息内容（key为污染类型，value为文本内容集合）
		for (String[] str : content) {
			List<String> list = null; 
			String key = str[typeIndex];
			String txt = str[txtContentIndex];
			if(map.containsKey(key)){
				list = map.get(key);
			}else{
				list = new ArrayList<String>();
			}
			list.add(txt);
			map.put(key, list);
		}
		Set<String> set = map.keySet();
		Iterator<String> it = set.iterator();
		while(it.hasNext()){
			String name = it.next();
			List<String> txt = map.get(name);
			if(!FileIO.txtWrite(filePath+"\\data\\"+name, txt)){
				System.out.println("写入失败！");
				return false;
			}
		}
		return true;
	}
	/**
	 * 将训练集均分为10份
	 */
	public static void createTestFile(){
		File file = new File(filePath+"\\data");
		File[] files = null;
		if(file.isDirectory()){
			files = file.listFiles();
		}
		if(null!=files && files.length>0){
			for (File file2 : files) {
				String path = file2.getAbsolutePath();
				List<String> content = FileIO.txtRead(path);
				int count = content.size();
				int size = count/10;
				for(int i=1;i<10;i++){
					for(int j = 0; j<size;j++){
						FileIO.txtWrite(filePath+"\\test\\"+i+"\\"+file2.getName(), content.remove(0));
					}
				}
				while(content.size()>0){
					FileIO.txtWrite(filePath+"\\test\\10\\"+file2.getName(), content.remove(0));
				}
			}
		}
	}
	/**
	 * 根据生成的10分数据集，生成10份训练集和测试集
	 * @return
	 */
	public static List<HashMap<String,HashMap<String,List<String>>>> generateTestData(){
		List<HashMap<String,HashMap<String,List<String>>>> res = new ArrayList<>();
		String path = filePath+"\\test";
		File file = new File(path);
		File[] files = null;
		if(file.isDirectory()){
			files = file.listFiles();
			//多次遍历文件夹，生成10个测试训练集
			for(int i=1;i<=10;i++){
				HashMap<String,HashMap<String,List<String>>> map = new HashMap<>();
				HashMap<String,List<String>> train = new HashMap<>();
				HashMap<String,List<String>> test = new HashMap<>();
				//遍历每个文件夹
				for (File file2 : files) {
					String fileName = file2.getName();
					//生成测试集
					if(fileName.equals(i+"")){
						File[] data = file2.listFiles();
						for (File file3 : data) {
							String name = file3.getName();
							List<String> list = FileIO.txtRead(file3.getAbsolutePath());
							test.put(name, list);
							
						}
					}else{
						//生成训练集
						File[] data = file2.listFiles();
						for (File file3 : data) {
							String name = file3.getName();
							List<String> content = FileIO.txtRead(file3.getAbsolutePath());
							List<String> list = train.get(name);
							if(null != list){
								list.addAll(content);
							}else{
								list = content;
							}
							train.put(name, list);
						}
					}
				}
				map.put("test", test);
				map.put("train", train);
				res.add(map);
			}
		}
		return res;		
	}
}
