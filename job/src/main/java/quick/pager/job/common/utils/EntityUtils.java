package quick.pager.job.common.utils;

import com.google.common.collect.Lists;
import com.xiaoleilu.hutool.util.StrUtil;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class EntityUtils {
    /**
     * 通过反射获取实体类的对应数据库所有列
     *
     * @param entityClazz class类
     */
    public static List<String> getFiled(Class<?> entityClazz, List<String> fieldColumns, Integer level) {
        if (fieldColumns == null) {
            fieldColumns = Lists.newArrayList();
        }
        if (level == null) {
            level = 0;
        }
        if (entityClazz.equals(Object.class)) {
            return fieldColumns;
        }
        Field[] fields = entityClazz.getDeclaredFields();
        int index = 0;
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers()) && !field.isAnnotationPresent(Transient.class)) {
                boolean columnPresent = field.isAnnotationPresent(Column.class);
                Column column = field.getAnnotation(Column.class);
                if (level != 0) {
                    //将父类的字段放在前面
                    if (columnPresent) {
                        fieldColumns.add(index, column.name());
                    } else {
                        fieldColumns.add(index, field.getName());
                    }
                    index++;
                } else {
                    if (columnPresent) {
                        fieldColumns.add(column.name());
                    } else {
                        fieldColumns.add(field.getName());
                    }
                }
            }
        }
        Class<?> superClass = entityClazz.getSuperclass();
        if (superClass != null
                && !superClass.equals(Object.class)
                && (superClass.isAnnotationPresent(Entity.class)
                || (!Map.class.isAssignableFrom(superClass)
                && !Collection.class.isAssignableFrom(superClass)))) {
            return getFiled(entityClazz.getSuperclass(), fieldColumns, ++level);
        }
        return fieldColumns;
    }

    /**
     * 获取主键
     *
     * @param entityClazz 实体类
     */
    public static String getPrimaryKey(Class<?> entityClazz) {
        Field[] fields = entityClazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                if (field.isAnnotationPresent(Column.class)) {
                    return field.getAnnotation(Column.class).name();
                } else {
                    return field.getName();
                }
            }
        }
        Class<?> superClass = entityClazz.getSuperclass();
        if (superClass != null
                && !superClass.equals(Object.class)
                && (superClass.isAnnotationPresent(Entity.class)
                || (!Map.class.isAssignableFrom(superClass)
                && !Collection.class.isAssignableFrom(superClass)))) {
            return getPrimaryKey(entityClazz.getSuperclass());
        }
        return null;
    }

    /**
     * 获取实体类的表名
     *
     * @param entityClazz 实体类
     */
    public static String getTableName(Class<?> entityClazz) {
        if (null == entityClazz) {
            return null;
        }
        if (entityClazz.isAnnotationPresent(Table.class)) {
            Table table = entityClazz.getAnnotation(Table.class);
            return table.name();
        }
        return StrUtil.lowerFirst(entityClazz.getSimpleName());
    }

    /**
     * 通过反射获取泛型的class对象
     *
     * @param mapperClazz Mapper Class对象
     */
    public static Class<?> getEntityClazz(Class<?> mapperClazz) {
        Type[] types = mapperClazz.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) type;
                if (t.getRawType() == mapperClazz || mapperClazz.isAssignableFrom((Class<?>) t.getRawType())) {
                    return (Class<?>) t.getActualTypeArguments()[0];
                }
            }
        }
        return null;
    }
}
