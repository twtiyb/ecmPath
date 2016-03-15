package util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;

public class ReflectUtil {
	public static Object getFieldValue(Object object, String fieldName) {
		FieldToken token = new FieldToken(fieldName);
		
		Field field = getDeclaredField(object, token.getName());
		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

		makeAccessible(field);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
		}

		if (token.hasNext() && result != null) {
			return getFieldValue(result, token.getChildren());
		} else {
			return result;
		}
	}
	
	public static String getFieldValueString(Object object, String fieldName) {
		if (StringUtil.isEmpty(fieldName)) {
			return "";
		}
		Object obj = getFieldValue(object, fieldName);
		return getValueString(obj);
	}
	
	public static String getFieldValueString(Object object, String fieldName,String format) {
		if (StringUtil.isEmpty(fieldName)) {
			return "";
		}
		Object obj = getFieldValue(object, fieldName);
		return getValueString(obj,format);
	}
	/**
	 * Object 转成 String
	 * @param object
	 * @return
	 */
	public static String getValueString(Object object) {
		if (object instanceof Date) {
			return DateUtil.toDateTimeString((Date)object);
		} else if (object != null) {
			return object.toString();
		} else {
			return "";
		}
	}
	
	public static String getValueString(Object object,String format) {
		if (object instanceof Date) {
			return DateUtil.toDateString((Date)object,format);
		} else if (object != null) {
			return object.toString();
		} else {
			return "";
		}
	}
	
	public static void setFieldValue(Object object, String fieldName, Object value) {
		FieldToken token = new FieldToken(fieldName);
		
		Field field = getDeclaredField(object, token.getName());
		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

		makeAccessible(field);

		if (token.hasNext()) {
			try {
				Object result = field.get(object);
				setFieldValue(result, token.getChildren(), value);
			} catch (IllegalAccessException e) {
			}
		} else {
			try {
				field.set(object, value);
			} catch (IllegalAccessException e) {
			}
		}
	}
	
	public static Field getDeclaredField(Object object, String filedName) {
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				return superClass.getDeclaredField(filedName);
			} catch (NoSuchFieldException e) {
				//Field 不在当前类定义, 继续向上转
			}
		}
		return null;
	}
	
	private static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers())) {
			field.setAccessible(true);
		}
	}
}
