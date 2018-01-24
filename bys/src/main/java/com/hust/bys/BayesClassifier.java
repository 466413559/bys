package com.hust.bys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.hust.util.SplitWordUtil;

/**
 * 给定训练集和测试集，根据训练集对测试集进行分类，并计算其分类的准确率
 * @author Jack
 *
 */
public class BayesClassifier {
	/**
	 * 分词后的训练集，key为类，value为类内文本分词后的结果
	 */
	private static HashMap<String,List<List<String>>> trainData;
	/**
	 * 分词后的训练集词频统计，key为类名，value为类内词频数
	 */
	private static HashMap<String,HashMap<String,Integer>> wordFrequence;
	/**
	 * 各个类中的词频总数
	 */
	private static HashMap<String,Integer> wordCount;
	/**
	 * 各个类中文档总数
	 */
	private static HashMap<String,Integer> docCount;
	/**
	 * 训练集中所有文档总数
	 */
	private static Integer count;
	/**
	 * 训练集中各个类的先验概率
	 */
	private static HashMap<String,Double> priPro;
	
	/**
	 * 预处理训练集，并初始化各个值
	 * @param map
	 */
	public void setTrainindData(HashMap<String,List<String>> map){
		trainData = new HashMap<>();
		wordFrequence = new HashMap<>();
		wordCount = new HashMap<>();
		docCount = new HashMap<>();
		priPro = new HashMap<>();
		count = 0;
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			List<List<String>> list = new ArrayList<>();
			HashMap<String,Integer> wordMap = new HashMap<>();
			int wordCountTmp = 0;
			int docCountTmp = 0;
			String name = it.next();
			List<String> doc = map.get(name);
			for (String string : doc) {
				count++;
				List<String> tmp = SplitWordUtil.getFileSplit(string);
				if(null!= tmp &&tmp.size()>0){
					wordCountTmp+=tmp.size();
					docCountTmp++;
					list.add(tmp);		
					for (String string2 : tmp) {
						Integer wordF = wordMap.get(string2);
						if(null == wordF){
							wordMap.put(string2, 0);
						}else{
							wordMap.put(string2,wordF+1);
						}
					}
				}
			}
			trainData.put(name, list);
			wordFrequence.put(name, wordMap);
			wordCount.put(name, wordCountTmp);
			docCount.put(name, docCountTmp);
		}
		InitPriPro();
	}
	/**
	 * 初始化先验概率
	 */
	private void InitPriPro(){
		Iterator<String> it = trainData.keySet().iterator();
		while(it.hasNext()){
			String name = it.next();
			if(count==0){
				System.out.println(count);
			}
			Double d = 1.0*docCount.get(name)/count;
			priPro.put(name, d);
		}
	}
	/**
	 * 计算给定测试集分类正确的概率
	 * @param testData
	 * @return
	 */
	public double ClassifierTest(HashMap<String,List<String>> testData){
		int count = 0;
		int rightCount = 0;
		Iterator<String> it = testData.keySet().iterator();
		while(it.hasNext()){
			String name = it.next();
			System.out.println("-------------------------------------测试数据人工标记的类别："+name+"-----------------------------------");
			List<String> data = testData.get(name);
			for (String string : data) {
				count++;
				String res = Classifier(string);
				System.out.println(string);
				System.out.println("测试数据机器分类的类别："+res);
				if(res.equals(name)){
					rightCount++;
				}
			}
		}
		return 1.0*rightCount/count;
	}
	/**
	 * 对data数据进行分类，返回类别名称
	 * @param data
	 * @return
	 */
	private String Classifier(String data){
		List<String> words = SplitWordUtil.getFileSplit(data);
		double res = 0;
		String resName = "";
		Iterator<String> it = wordFrequence.keySet().iterator();
		while(it.hasNext()){
			double tmpRes = 0 ;
			String name = it.next();
			System.out.println("类别名："+name);
			HashMap<String,Integer> wordF = wordFrequence.get(name);
			int totalCount = wordCount.get(name);
			double pri = priPro.get(name);
			for (String string : words) {
				if(wordF.containsKey(string)){
					tmpRes += Math.log((wordF.get(string)+1)*1.0/totalCount);
				}else{
					tmpRes += Math.log(1.0/totalCount);
				}
			}
			tmpRes += Math.log(pri);
	//		System.out.println("属于该类别的概率："+tmpRes);
			if(res == 0){
				res = tmpRes;
				resName = name;
			}
			else{
				if(tmpRes>res){
					res = tmpRes;
					resName = name;
				}
			}
		}
		return resName;
	}
	
}
