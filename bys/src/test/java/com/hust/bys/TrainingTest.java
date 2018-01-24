package com.hust.bys;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.hust.util.FileIO;
import com.hust.util.TrainingUtil;


public class TrainingTest{

	public void createTest(){
		String filePath = System.getProperty("user.dir");
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

	@Test
	public void test(){
		List<HashMap<String,HashMap<String,List<String>>>> list= TrainingUtil.generateTestData();
		for (HashMap<String, HashMap<String, List<String>>> hashMap : list) {
			BayesClassifier bc = new BayesClassifier();
			bc.setTrainindData(hashMap.get("train"));
			double d = bc.ClassifierTest(hashMap.get("test"));
			System.out.println(d);
		}
	}
	public void test1(){
		String str = "r";
		System.out.println(str.matches("n.*|v.*"));
	}
}
