package com.hust.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ansj.domain.Term;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

/**
 * @author Chan
 * @description 利用Ansj分词工具将文本分词 {@link} http://nlpchina.github.io/ansj_seg/
 */
public class SplitWordUtil {
	
	private static StopRecognition filter;
	private SplitWordUtil() {
	}
	
	static {
		synchronized (SplitWordUtil.class) {
			File stopwords = new File("library/stop.dic");
			try {
				FileReader fr = new FileReader(stopwords);
				BufferedReader br = new BufferedReader(fr);
				String line;
				filter = new StopRecognition();
				while ((line =br.readLine())!= null) {
					filter.insertStopNatures("ns","nr","w");
					filter.insertStopWords(line);
				}
				br.close();
				fr.close();
			} catch (IOException e) {
				System.out.println("停用词加载失败！");
			}
		}
	}

	/**
	 * 将文本进行分词。
	 * 
	 * @param file 输入的文本文件
	 * @return
	 */
	public static List<String> getFileSplit(String content) {
		List<String> finalRes = new ArrayList<String>();
		//System.out.println(content);
			if(content != null ) {
				List<Term> splitRes = ToAnalysis.parse(content).recognition(filter).getTerms();
				for (Term t : splitRes) {
	//				if(!t.getNatureStr().matches("n.*|v.*"))continue;
				//	System.out.print(t+"  ");
					finalRes.add(t.getName());
				}
//				System.out.println();
			}
		return finalRes;
	}
	
}
