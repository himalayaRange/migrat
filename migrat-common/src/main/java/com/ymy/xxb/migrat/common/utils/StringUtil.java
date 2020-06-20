package com.ymy.xxb.migrat.common.utils;

import java.net.InetAddress;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>字符工具类</p>.
 */
public class StringUtil {
	public static String number2String(Object d){
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(4);
		return nf.format(d);
	}
	public static String lowFristChar(String str){
		StringBuffer sb = new StringBuffer(str);
		sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		str = sb.toString();
		return str;
	}
	/**
	 * 判断列表是否为空值 NULL、size为0均认为空值.
	 *
	 * @param list 传入list
	 * @return boolean 是否为空
	 */
	public static boolean isEmptyList(List<?> list) {
		return null == list || list.size() == 0;
	}


	/**
	 * 判断列表是否为空值 NULL、size为0均认为空值.
	 *
	 * @param list 传入list
	 * @return boolean 是否不为空
	 */
	public static boolean isNotEmptyList(List<?> list) {
		return !isEmptyList(list);
	}
	/**
	 * 功能：判断字符串是否为数字
	 *
	 * @param str
	 * @return
	 */
	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断对象是否为空字符串 NULL、空格均认为空字符串.
	 *
	 * @param value 待判定的对象
	 * @return 是否不为kong
	 */
	public static boolean isNotEmpty(Object value) {
		return !isEmpty(value);
	}
	
	/**
	 * 判断元素是否在数组内.
	 *
	 */
	public static boolean isContain( String[] arrays,String str) {
		if (arrays == null || arrays.length == 0) {
			return false;
		}
		for (int i = 0; i < arrays.length; i++) {
			String aSource = arrays[i];
			if (aSource.equals(str)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 验证日期字符串是否是YYYY-MM-DD格式
	 *
	 * @param str
	 * @return
	 */
	public static boolean isDataFormat(String str) {
		boolean flag = false;
		// String
		// regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
		String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
		Pattern pattern1 = Pattern.compile(regxStr);
		Matcher isNo = pattern1.matcher(str);
		if (isNo.matches()) {
			flag = true;
		}
		return flag;
	}
	public static boolean isIdCard(String IDStr){
		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
		String Ai = "";
		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			return false;
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			Ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			return false;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份
		if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
			return false;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			return false;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			return false;
		}
		// =====================(end)=====================

		/*// ================ 地区码时候有效 ================
		Hashtable<String, String> h = GetAreaCode();
		if (h.get(Ai.substring(0, 2)) == null) {
			return false;
		}*/
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i))) * Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (Ai.equalsIgnoreCase(IDStr) == false) {
				return false;
			}
		} else {
			return true;
		}
		// =====================(end)=====================
		return true;
	}
	/**
	 * 判定中文字符串的長度.
	 *
	 * @param value
	 *            字符串
	 * @return int 长度
	 */
	public static int length(String value) {
		int valueLength = 0;
		if (value != null) {
			String chinese = "[\u0391-\uFFE5]";
			/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
			for (int i = 0; i < value.length(); i++) {
				/* 获取一个字符 */
				String temp = value.substring(i, i + 1);
				/* 判断是否为中文字符 */
				if (temp.matches(chinese)) {
					/* 中文字符长度为2 */
					valueLength += 2;
				} else {
					/* 其他字符长度为1 */
					valueLength += 1;
				}
			}
		}

		return valueLength;
	}

	/**
	 * 对象为Null时转换成""返回,其他返回其toString值 .
	 *
	 * @param value 待转字符串的对象
	 * @return 转换后字符串
	 */
	public static String null2String(Object value) {
		if (null == value) {
			return "";
		} else {
			return value.toString();
		}
	}

	/**
	 * 判断字符串是否为空值 NULL、空格均认为空值.
	 *
	 * @param value 待判定的对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(String value) {
		return null == value || "".equals(value.trim());
	}
	public static int countStr(String str1, String str2) {
    	int counter=0;
        if (str1.indexOf(str2) == -1) {
            return 0;
        }
            while(str1.indexOf(str2)!=-1){
            	counter++;
            	str1=str1.substring(str1.indexOf(str2)+str2.length());
            }
            return counter;
    }
	/**
	 * 判断对象是否为空字符串 NULL、空格均认为空字符串.
	 *
	 * @param value 待判定的对象
	 * @return 是否为空
	 */
	public static boolean isEmpty(Object value) {
		return null == value || "".equals(value.toString().trim());
	}


	/**
	 * 判断字符串是否为空值 NULL、空格均认为空值.
	 *
	 * @param value 待判定的字符串
	 * @return 是否不为空
	 */
	public static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}

	/**
	 * 重复字符串.
	 *
	 * @param src 字符串
	 * @param repeats 重复次数
	 * @return 重复后的字符串
	 * @return
	 */
	public static String repeatString(String src, int repeats) {
		if (null == src || repeats <= 0) {
			return src;
		} else {
			StringBuffer bf = new StringBuffer();
			for (int i = 0; i < repeats; i++) {
				bf.append(src);
			}
			return bf.toString();
		}
	}

	/**
	 * 左对齐字符串.
	 *
	 * @param src 字符串
	 * @param length 填补的长度
	 * @return 填补后的字符串
	 */
	public static String lpadString(String src, int length) {
		return lpadString(src, length, " ");
	}

	/**
	 * 以指定字符串填补空位，左对齐字符串.
	 *
	 * @param src 字符串
	 * @param length 填补的长度
	 * @param single 填补的字符
	 * @return 填补后的字符串
	 * @return
	 */
	public static String lpadString(String src, int length, String single) {
		if (src == null || length <= src.getBytes().length) {
			return src;
		} else {
			return repeatString(single, length - src.getBytes().length) + src;
		}
	}

	/**
	 * 右对齐字符串.
	 *
	 * @param src 字符串
	 * @param byteLength 填补的长度
	 * @return 填补后的字符串
	 */
	public static String rpadString(String src, int byteLength) {
		return rpadString(src, byteLength, " ");
	}

	/**
	 * 以指定字符串填补空位，右对齐字符串.
	 *
	 * @param src 字符串
	 * @param length 填补的长度
	 * @param single 填补的字符
	 * @return 填补后的字符串
	 */
	public static String rpadString(String src, int length, String single) {
		if (src == null || length <= src.getBytes().length) {
			return src;
		} else {
			return src + repeatString(single, length - src.getBytes().length);
		}
	}

	/**
	 * 去除,分隔符，用于金额数值去格式化.
	 *
	 * @param value 金额字符串
	 * @return 去格式化后的字符串
	 */
	public static String decimal(String value) {
		if (null == value || "".equals(value.trim())) {
			return "0";
		} else {
			return value.replaceAll(",", "");
		}
	}

	/**
	 *	在数组中查找字符串 .
	 *
	 * @param params 字符串数组
	 * @param name 待查找的字符串
	 * @param ignoreCase 是否区分大小写
	 * @return 查找到则返回所在序号，否则返回-1
	 */
	public static int indexOf(String[] params, String name, boolean ignoreCase) {
		if (params == null){
			return -1;
		}
		for (int i = 0, j = params.length; i < j; i++) {
			if (ignoreCase && params[i].equalsIgnoreCase(name)) {
				return i;
			} else if (params[i].equals(name)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 将字符转数组.
	 *
	 * @param str 含有,号的字符串
	 * @return 字符串数组
	 */
	public static String[] toArr(String str) {
		String inStr = str;
		String[] a = null;
		try {
			if (null != inStr) {
				StringTokenizer st = new StringTokenizer(inStr, ",");
				if (st.countTokens() > 0) {
					a = new String[st.countTokens()];
					int i = 0;
					while (st.hasMoreTokens()) {
						a[i++] = st.nextToken();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}


	/**
	 * 字符串数组包装成字符串 .
	 *
	 * @param ary 待包装的数组
	 * @param s 包装符号如 ' 或 "
	 * @return 包装后的字符串
	 */
	public static String toStr(String[] ary, String s) {
		if (ary == null || ary.length < 1){
			return "";
		}
		StringBuffer bf = new StringBuffer();
		bf.append(s);
		bf.append(ary[0]);
		for (int i = 1; i < ary.length; i++) {
			bf.append(s).append(",").append(s);
			bf.append(ary[i]);
		}
		bf.append(s);
		return bf.toString();
	}

	/**
	 * 设置MESSAGE中的变量{0}...{N}.
	 *
	 * @param msg 含有{N}的字符串
	 * @param vars 待替换的值
	 * @return 替换后的字符串
	 */
	public static String getMessage(String msg, Object... vars) {
		return MessageFormat.format(msg, vars);
	}
	public static String messageFormat(String msg, String[] vars) {
		for(int i=0;i<vars.length;i++){
			vars[i]=vars[i].trim();
		}
		return MessageFormat.format(msg, vars);
	}

	/**
	 * 将key=value;key2=value2……转换成Map.
	 *
	 * @param params 字符串
	 * @return map
	 */
	public static Map<Object, Object> gerneryParams(String params) {
		Map<Object, Object> args = new HashMap<Object, Object>();
		if (!isEmpty(params)) {
			try {
				String map, key, value;
				StringTokenizer st = new StringTokenizer(params, ";");
				StringTokenizer stMap;
				while (st.hasMoreTokens()) {
					map = st.nextToken();
					if (isEmpty(map.trim())){
						break;
					}
					stMap = new StringTokenizer(map, "=");
					key = stMap.hasMoreTokens() ? stMap.nextToken() : "";
					if (isEmpty(key.trim())){
						continue;
					}
					value = stMap.hasMoreTokens() ? stMap.nextToken() : "";
					args.put(key, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return args;
	}


	/**
	 * 解析文件的扩展名.
	 *
	 * @param oldName 文件名
	 * @return  扩展名
	 */
	public static String getFileExtName(String oldName) {
		String ext = "";
		int lastIndex = oldName.lastIndexOf(".");
		if (lastIndex > 0) {
			ext = oldName.substring(lastIndex);
		}
		return ext;
	}
	/**
	 * 随机生成指定长度的字符串.
	 */
	public static final String randomString(int length) {
		if (length < 1) {
			return null;
		}
		Random randGen = null;
		char[] numbersAndLetters = null;
		randGen = new Random();
		numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
				+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer);
	}

	/**
	 * 判定字符串是否是数字，允许负数，未做长度限制.
	 */
	public static boolean isNumericNT(String str) {
		Pattern pattern = Pattern.compile("(-)?[1-9][0-9]*");// [1-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 判定字符串是否由数字和字符组成.
	 */
	public static boolean isCharaterAndNumber(String str){
		String reg="[0-9a-zA-Z]*";
		Pattern pattern = Pattern.compile(reg);
	    return pattern.matcher(str).matches();

	}


	/**
	 * 获取本机IP地址.
	 */
	public static String getIp() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			String ip = addr.getHostAddress().toString();// 获得本机IP
			return ip;
		} catch (Exception e) {
			return "";
		}
	}


	/**
	 * 正则查找替换
	 * 使用方法：regReplace("${CRECODE}",\\$\\{(.+?)\\,{("CRECODE", "0-1111")}).
	 * @param str 字符串
	 * @param regex 正则表达式
	 * @param replaceMap 存放值得map
	 * @return 替换后的字符串
	 */
	public static String regReplace(String str,String regex,Map<String,?> replaceMap) {
		  Pattern pattern = Pattern.compile(regex);
	        Matcher matcher = pattern.matcher(str);
	        StringBuffer sb = new StringBuffer();
	        while (matcher.find()) {
	        	String key=str.substring(matcher.start()+2, matcher.end()-1);
	        	String value="";
	        	if(!replaceMap.containsKey(key)){
	        		matcher.appendReplacement(sb, value);
	        	}else{
	        		if(replaceMap.containsKey(key)){
	            		Object valueObj=replaceMap.get(key);
	            		if(valueObj!=null){
	            			value=String.valueOf(valueObj);
	            			matcher.appendReplacement(sb, value);
	            		}
	            	}
	        	}


	        }
	        matcher.appendTail(sb);
	        return sb.toString();
	}
	public static String regReplace(String str,Map<String,?> replaceMap) {
		return regReplace(str,"\\$\\{(.*?)}",replaceMap);
	}
	public static Boolean isContainStr(String listStr,String str){
		if(listStr==null||str==null){
			return false;
		}
		String[] arrays=toArr(listStr);
		return isContain(arrays, str);
	}
	/**
	 * 空数组判断 .
	 *
	 * @param array 待判定的数组
	 * @return boolean 結果
	 */
	public static boolean isEmptyArray(final Object[] array) {
		return array == null || array.length <= 0;
	}
	/**
	 * 字符串剪切指定长度  汉字2个长度。英语1个长度.
	 */
	public static String cutString(String str,Integer hLength){
		if(str==null||str.length()==0){
			return "";
		}
		String chinese = "[\u0391-\uFFE5]";
		int valueLength=0;
		StringBuffer sb=new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			/* 获取一个字符 */
			String temp = str.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
			if(valueLength<=hLength){
				sb.append(temp);
			}else{
				sb.append("...");
				break;
			}
		}
		return sb.toString();
	}
	/**
	 * 字符串剪切指定长度 .
	 */
	public static String subString(String str,Integer start,Integer hLength){
		if(str.length()<=start){
			return "";
		}
		if(str.length()<(start+hLength)){
			hLength=str.length()-start;
		}
		return str.substring(start,start+hLength);
	}
	public static String join(List<String> ary,String split){
		if (ary == null || ary.size() < 1){
			return "";
		}
		StringBuffer bf = new StringBuffer();
		bf.append(ary.get(0));
		for (int i = 1; i < ary.size(); i++) {
			bf.append(split);
			bf.append(ary.get(i));
		}
		return bf.toString();
	}
	/*public static List<String> string2List(String inStr,String split){
		 List<String> result=Lists.newArrayList();
		if (inStr == null || inStr.length() < 1){
			return result;
		}
		try {
			StringTokenizer st = new StringTokenizer(inStr, split);
			if (st.countTokens() > 0) {
				while (st.hasMoreTokens()) {
					result.add(st.nextToken());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}*/
	/**
	 * 带有HTML实体（例如：&nbsp；）字符串长度
	 *//*
	public static Integer htmlLength(String str){
		str = StringEscapeUtils.unescapeHtml4(str);
		str = str.replaceAll("<[^>]*>", "").trim();
		return str.length();
	}
	
	*//**
	 * 带有HTML实体（例如：&nbsp；）字符串长度
	 *//*
	public static String htmlToString(String str){
		str = StringEscapeUtils.unescapeHtml4(str);
		str = str.replaceAll("<[^>]*>", "").trim();
		return str;
	}
	*/

	/**
	 *  单个字符串是否包含单个字符串
	 * @param longStr
	 * @param shortStr
	 * @return
	 */
	public static Boolean isContainString(String longStr,String shortStr){
		if(longStr==null||shortStr==null){
			return false;
		}
		return longStr.contains(shortStr);
	}


	/**
	 * 通过标记取得数据库中存储的一/二级会商结果的值
	 * @param str
	 * @return
	 */
	public static String getValOfConsultationResult(String flag,String str){
		String returnStr="";
		if(str.trim()!=null && !"".equals(str.trim()) ){
			String[] strs=str.split("#");
			for(int i=0;i<strs.length;i++){
				String resultMaps=strs[i];//strs[i]存储“会商结果=几级会商的第几次”形式的数据
				String[] resultMap=resultMaps.split("=");
				if(resultMap[1].trim().equals(flag)){
					returnStr= resultMap[0];
				}
			}
		}
			return returnStr;
	}
	//handleConsultationResult
	/**
	 * 处理会商结果字符串
	 * @param str
	 * @return
	 */
	public static String handleConsultationResult(String str){
		String returnStr="";
		if(str.trim()!=null && !"".equals(str.trim()) ){
			String[] strs=str.split("#");
			for(String s:strs){
				if(isNotEmpty(s.split("=")[0])){
				 returnStr=returnStr+s.split("=")[0]+",";
				}
			}

		}
		if(isNotEmpty(returnStr)){
			return returnStr.substring(0,returnStr.length()-1);
		}else{
			return returnStr;
		}

	}
	
	/**
	 * 处理字符串
	 * @param str
	 * @return
	 */
	public static String disposeString(String str) {
		String returnStr = "";
		if (StringUtils.isNotEmpty(str)) {
			returnStr = str.trim().replaceAll("　", "");
		}
		return returnStr;
	}
	
	/**
	 * 截取字符串中的中文
	 * @param str
	 * @return
	 */
	public static String subStringChinese(String str) {
		String reg = "[^\u4e00-\u9fa5]";
		str = str.replaceAll(reg, "");
		return str;
	}
	
	/**
	 * obj转string
	 * @param obj
	 * @return
	 */
	public static String objNotEmpty(Object obj) {
		String returnStr = "";
		if (obj != null && !obj.toString().trim().equals("")) {
			returnStr = obj.toString().trim();
		}
		return returnStr;
	}
	
	public static String digitNotEmpty(Object obj) {
		String returnStr = "0";
		if (obj != null && !obj.toString().trim().equals("")) {
			returnStr = obj.toString().trim();
		}
		return returnStr;
	}
	
	public static String digitNotEmpty(Object obj, Object defaultDigit) {
		String returnStr = defaultDigit.toString();
		if (obj != null && !obj.toString().trim().equals("")) {
			returnStr = obj.toString().trim();
		}
		return returnStr;
	}
}
