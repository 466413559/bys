package com.hust.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class AttrUtil {

	/**
	 * title、url、time
	 * 
	 * @param attrs
	 * @return
	 */
	/**
	 * 标题 "标题|内容"
	 */
	public static final String TITLE_PATTERN = "标题|内容";
	/**
	 * url "链接|网址|域名|微博链接|[Uu][Rr][Ll]"
	 */
	public static final String URL_PATTERN = "来源链接|网址|域名|微博链接|[Uu][Rr][Ll]";
	/**
	 * 时间 "发布时间|发贴时间|时间"
	 */
	public static final String TIME_PATTERN = "发布时间|发帖时间|发贴时间|时间";
	/**
	 * 发帖人 "发帖人|发贴人"
	 */
	public static final String POSTING = "发帖人|发贴人|作者|发布人|来源/发布人|发布者昵称";
	/**
	 * 网站名称 "网站|媒体名称"
	 */
	public static final String WEBNAME_PATTERN = "网站|媒体名称";
	/**
	 * 网站类型 "来源|类型|资源类型"
	 */
	public static final String TYPE_PATTERN = "来源|类型|资源类型|媒体类型";
	/**
	 * 网站所属模块 "板块|频道"
	 */
	public static final String COLUMN_PATTERN = "板块|频道|频道分类";
	/**
	 * 网站级别 "媒体级别"
	 */
	public static final String RANK_PATTERN = "媒体级别";
	/**
	 * 网站权重 "权重"
	 */
	public static final String WEIGHT_PATTERN = "权重";
	/**
	 * 网站影响范围 "影响范围"
	 */
	public static final String INCIDENCE_PATTERN = "影响范围";

	public static int[] findEssentialIndex(String[] attrs) {
		int indexOfTitle = findIndexOfTitle(attrs);
		int indexOfUrl = findIndexOfUrl(attrs);
		int indexOfTime = findIndexOfTime(attrs);

		return new int[] { indexOfTitle, indexOfUrl, indexOfTime };
	}

	public static boolean isImp(String attr) {
		return Pattern.matches(TITLE_PATTERN + "|" + URL_PATTERN + "|" + TIME_PATTERN + "|" + WEBNAME_PATTERN + "|" + TYPE_PATTERN + "|"
				+ COLUMN_PATTERN + "|" + RANK_PATTERN + "|" + POSTING + "|" + RANK_PATTERN + "|" + WEIGHT_PATTERN + "|" + INCIDENCE_PATTERN, attr);
	}

	public static int findIndexOfSth(String[] attrs, String sth) {
		for (int i = 0; i < attrs.length; i++) {
			if (Pattern.matches(sth, attrs[i])) {
				return i;
			}
		}

		return -1;
	}

	public static int findIndexOfSth(List<String> attrs, String sth) {
		int size = attrs.size();
		for (int i = 0; i < size; i++) {
			if (Pattern.matches(sth, attrs.get(i))) {
				return i;
			}
		}

		return -1;
	}

	public static int findIndexOfTitle(String[] attrs) {
		for (int i = 0; i < attrs.length; i++) {
			if (Pattern.matches(TITLE_PATTERN, attrs[i])) {
				return i;
			}
		}

		return -1;
	}

	public static int findIndexOfTitle(List<String> attrs) {
		for (int i = 0; i < attrs.size(); i++) {
			if (Pattern.matches(TITLE_PATTERN, attrs.get(i))) {
				return i;
			}
		}

		return -1;
	}

	public static int findIndexOfUrl(String[] attrs) {
		for (int i = 0; i < attrs.length; i++) {
			if (Pattern.matches(URL_PATTERN, attrs[i])) {
				return i;
			}
		}

		return -1;
	}

	public static int findIndexOfUrl(List<String> attrs) {
		for (int i = 0; i < attrs.size(); i++) {
			if (Pattern.matches(URL_PATTERN, attrs.get(i))) {
				return i;
			}
		}

		return -1;
	}

	public static int findIndexOfTime(String[] attrs) {
		for (int i = 0; i < attrs.length; i++) {
			if (Pattern.matches(TIME_PATTERN, attrs[i])) {
				return i;
			}
		}

		return -1;
	}

	public static int findIndexOfTime(List<String> attrs) {
		for (int i = 0; i < attrs.size(); i++) {
			if (Pattern.matches(TIME_PATTERN, attrs.get(i))) {
				return i;
			}
		}

		return -1;
	}

	public static int findIndexOfWebName(String[] attrs) {
		for (int i = 0; i < attrs.length; i++) {
			if (Pattern.matches(WEBNAME_PATTERN, attrs[i])) {
				return i;
			}
		}

		return -1;
	}

	public static int findIndexOfWebName(List<String> attrs) {
		for (int i = 0; i < attrs.size(); i++) {
			if (Pattern.matches(WEBNAME_PATTERN, attrs.get(i))) {
				return i;
			}
		}

		return -1;
	}

	public static boolean isTitle(String attr) {
		if (Pattern.matches(TITLE_PATTERN, attr)) {
			return true;
		}

		return false;
	}

	public static boolean isUrl(String attr) {
		if (Pattern.matches(URL_PATTERN, attr)) {
			return true;
		}

		return false;
	}

	public static boolean isTime(String attr) {
		if (Pattern.matches(TIME_PATTERN, attr)) {
			return true;
		}

		return false;
	}

	public static boolean isSth(String attr, String pattern) {
		if (Pattern.matches(pattern, attr)) {
			return true;
		}

		return false;
	}

	/**
	 * 统计日期-数量与来源-数量
	 * 
	 * @param content
	 * @return
	 */
	/*public static Map<String, TreeMap<String, Integer>> statistics(List<String[]> content, ConcurrentHashMap<String, Domain> domains) {
		HashMap<String, TreeMap<String, Integer>> map = new HashMap<String, TreeMap<String, Integer>>();
		int indexOfUrl = findIndexOfUrl(content.get(0));
		int indexOfTime = findIndexOfTime(content.get(0));
		int indexOfType = findIndexOfSth(content.get(0), TYPE_PATTERN);
		TreeMap<String, Integer> timeMap = new TreeMap<String, Integer>();
		TreeMap<String, Integer> webMap = new TreeMap<String, Integer>();

		int len = content.size();
		for (int i = 1; i < len; i++) {
			String[] row = content.get(i);
			// 如果不为空行
			if (row.length != 0) {
				String time = TimeUtil.getTimeKey(row[indexOfTime], Interval.DAY);
				// 统计日期-数量
				Integer timeCount = timeMap.get(time);
				if (timeCount != null) {
					timeMap.put(time, timeCount + 1);
				} else {
					timeMap.put(time, 1);
				}

				// 统计类型-数量
				// 先根据url查询域名表是否包含此条url
				Domain domain = domains.get(row[indexOfUrl]);
				String type = row[indexOfType].trim();
				if (domain != null) {
					String tmp = domain.getType();
					if (!tmp.equals("其他") && !tmp.equals("")) {
						type = tmp;
					}
				}

				Integer webCount = webMap.get(type);
				if (webCount != null) {
					webMap.put(type, webCount + 1);
				} else {
					webMap.put(type, 1);
				}
			}
		}
		map.put(AttrID.TIME, timeMap);
		map.put(AttrID.TYPE, webMap);
		return map;
	}*/
}
