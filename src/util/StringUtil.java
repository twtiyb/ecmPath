package util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {

	public static boolean isEmpty(String str) {
		return (str == null) || (str.length() == 0) || (str == "");
	}
	
	public static boolean isNotEmpty(String str) {
		return (!(isEmpty(str)));
	}

	public static boolean isNumber(String arg0) {
		if (arg0 == null || arg0.length() == 0) return false;
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(arg0);
		return m.matches();
	}
	
	public static boolean isDouble(String arg0) {
		if (arg0 == null || arg0.length() == 0) return false;
		try{
			Double.parseDouble(arg0);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	public static String nullToEmpty(String arg) {
		if (arg==null || "null".equals(arg.toLowerCase())) {
			return "" ;
		} else {
			return arg;
		}
	}
	
	public static String joinString(String[] array, String separator) {
		String value = "";
		for (int i = 0; i < array.length; i++) {
			value += array[i] + separator;
		}
		if (value.length() > 1)
			value = value.substring(0, value.length() - separator.length());
		return value;
	}
	
	public static String joinString(List<String> list, String separator) {
		String value = "";
		for (int i = 0; i < list.size(); i++) {
			value += list.get(i) + separator;
		}
		if (value.length() > 1)
			value = value.substring(0, value.length() - separator.length());
		return value;
	}
	
	public static String joinString(Collection<String> coll, String separator) {
		String value = "";
		Iterator<String> iterator = coll.iterator();
		while (iterator.hasNext()) {
			value += iterator.next() + separator;
		}
		if (value.length() > 1)
			value = value.substring(0, value.length() - separator.length());
		return value;
	}
	
	public static String[] split(String str, String separator) {
		//return StringUtils.split(str, separator);
		//StringUtils.split  Bug 分隔后空值不返回
		List<String> list = splitToList(str,separator);
		return list.toArray(new String[list.size()]);
	}
	
	public static List<String> splitToList(String str, String separator) {
		List<String> list = new ArrayList<String>();
		if (StringUtil.isEmpty(str)) {
			return list;
		}
		
        if (StringUtil.isEmpty(separator)) {
        	list.add(str);
        	return list;
        }
        int lastIndex = -1;
        int index = str.indexOf(separator);
        if (-1 == index && str != null) {
        	list.add(str);
        	return list;
        }
        while (index >= 0) {
            if (index > lastIndex) {
            	list.add(str.substring(lastIndex + 1, index));
            } else {
            	list.add("");
            }
 
            lastIndex = index;
            index = str.indexOf(separator, index + 1);
            if (index == -1) {
            	list.add(str.substring(lastIndex + 1, str.length()));
            }
        }
        return list;
	}
	
	public static String lpad(String str,int length, String pad) {
		while (str.length() < length) {  
			str = pad + str;  
        }
		return str;
	}
	
	public static String rpad(String str, int length, String pad) {
		while (str.length() < length) {  
			str = str + pad;  
        }
		return str;
	}
	
	/**
	 * 首字母大写
	 * 
	 * @param srcStr
	 * @return
	 */
	public static String firstCharacterToUpper(String srcStr) {
		return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);
	}

	/**
	 * 替换字符串并让它的下一个字母为大写
	 * 
	 * @param srcStr
	 * @param org
	 * @param ob
	 * @return
	 */
	public static String replaceUnderlineAndfirstToUpper(String srcStr,
			String org, String ob) {
		String newString = "";
		int first = 0;
		while (srcStr.indexOf(org) != -1) {
			first = srcStr.indexOf(org);
			if (first != srcStr.length()) {
				newString = newString + srcStr.substring(0, first) + ob;
				srcStr = srcStr
						.substring(first + org.length(), srcStr.length());
				srcStr = firstCharacterToUpper(srcStr);
			}
		}
		newString = newString + srcStr;
		return newString;
	}
	
	public static boolean isMobileNo(String mobile){
		if(mobile == null || mobile.length() == 0)return false;
		Pattern p = Pattern.compile("^(1)\\d{10}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}
	
	public static boolean isPhoneNo(String phone){
		if(phone == null || phone.length() == 0) return false;
		Pattern p=Pattern.compile("^(\\d{3,4}-)?\\d{7,8}$");
		Matcher m = p.matcher(phone);
		return m.matches();
	}
	
	public static String stringCut(String aString, int aLen, String aHintStr) {
		if (aString== null) return aString;
		int lLen= aString.length(), i;
		for (i= 0; aLen>= 0&& i< lLen; ++i)
			if (isBigChar(aString.charAt(i))) aLen-= 2;
			else --aLen;
		if (aLen>= 0) return aString;
		if (aHintStr== null) return aString.substring(0, i- 1);

		aLen-= aHintStr.length();
		for (; aLen< 0&& --i>= 0;)
			if (isBigChar(aString.charAt(i))) aLen+= 2;
			else ++aLen;

		return aHintStr== null? aString.substring(0, i): aString
				.substring(0, i) + aHintStr;
	}
	
	public static boolean isBigChar(char c) {
		return c< 0|| c> 256;
	}
	
	public static String trim(String arg) {
		if (arg == null) return null;
		return arg.trim();
	}

	//+- / *=add/sub/divide/multiply
	public static double comDouble(Double v1, Double v2, char type) {
		BigDecimal ret = new BigDecimal("0.0");
		if(v1 != null) {
			ret = ret.add(new BigDecimal(v1.toString()));
		}
		if(v2 != null) {
			switch (type) {
				case '+': ret = ret.add(new BigDecimal(v2.toString()));break;
				case '-': ret = ret.subtract(new BigDecimal(v2.toString()));break;
				case '/': ret = ret.divide(new BigDecimal(v2.toString()), 10, BigDecimal.ROUND_HALF_EVEN);break;
				case '*': ret = ret.multiply(new BigDecimal(v2.toString()));break;
				default:break;
			}
		}
		return ret.doubleValue();
	}
	/**
	 * 将相同属性的值从源对象复制到目标对象上,源对象上字段为空则保留目标对象上的值
	 * @param source
	 * @param target
	 */
	public static void copyProperties(Object source, Object target ) {
		Field[] sourceFileds = source.getClass().getDeclaredFields() ;
		Class<?> sourceClass = source.getClass() ;
		Class<?> targetClass = target.getClass() ;
		for(Field f : sourceFileds){
			try {
				String fieldName = f.getName() ;
				Field field = sourceClass.getDeclaredField(fieldName) ;
				Class type = field.getType() ;
				String head = "" ;
				if (type.getName().contains("boolean")) {
					head = "is" ;
				}else {
					head = "get" ;
				}
				String sourceMethodName = head+fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1) ;
				Method sourceMethod = sourceClass.getMethod(sourceMethodName,null) ;
				Object fieldValue = sourceMethod.invoke(source, null) ;
				if (fieldValue!=null) {
					String targetMethodName = "set"+fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1) ;
					Method targetMethod = targetClass.getMethod(targetMethodName, fieldValue.getClass()) ;
					targetMethod.invoke(target, fieldValue) ;
				}
				//方案二
//				Field targetFiled = targetClass.getDeclaredField(fieldName) ;
//				f.setAccessible(true) ;
//				targetFiled.setAccessible(true) ;
//				targetFiled.set(target, f.get(source)) ;
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
//			} catch (NoSuchFieldException e) {
				System.out.println("目标对象无 "+f.getName()+"属性");
//				e.printStackTrace();
			} catch (IllegalAccessException e) {
				System.out.println("不能访问非公开属性");
//				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
//		BaUser u1 = new BaUser() ;
//		u1.setUserId(100L) ;
//		u1.setUserName("aa") ;
//		u1.setWorkstationId("") ;
//		BaUser u2 = new BaUser() ;
//		u2.setUserName("bb") ;
//		u2.setPassword("password") ;
//		copyProperties(u1,u2) ;
//		System.out.println(u2.getUserId()+u2.getUserName()+u2.getPassword());
//		System.out.println("getWorkstationId="+u2.getWorkstationId());
	}
}
