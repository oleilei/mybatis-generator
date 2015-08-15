/**
 * <p>Copyright ® 中国证监会中央监管信息平台版权所有。</p>
 * 类名: PropertyConfig
 * 创建人: shawnley    创建时间: 2015年8月13日
 */

package org.mybatis.generator.internal.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * TODO <功能简述> <br/>
 * TODO <功能详细描述>
 * 
 * @author shawnley
 */
public class PropertyConfig {
    
    public static void main(String[] args) {
        System.out.println(PropertyConfig.getProterties().getProperty("copyright"));
    }
    
    private static Properties prop;

    private PropertyConfig() {
    }

    public static Properties getProterties() {
        if (prop != null) {
            return prop;
        }
        prop = new Properties();
        
        try {
            //prop.load(PropertyConfig.class.getResource("/config.properties").openStream());
            prop.load(PropertyConfig.class.getClassLoader().getResourceAsStream("config.properties"));
            transcode();
        } catch (IOException e) {
        }
        return prop;
    }
    
    /**
     * 
     * 对属性文件进行转码处理，解决中文乱码问题 <br/>
     * TODO <功能详细描述>
     */
    public static void transcode() {
        //转码处理  
        Set<Object> keyset = prop.keySet();  
        Iterator<Object> iter = keyset.iterator();  
        while (iter.hasNext()) {  
            String key = (String) iter.next();  
            String newValue = null;
            try {  
                //属性配置文件自身的编码  
                String propertiesFileEncode = "utf-8";  
                newValue = new String(prop.getProperty(key).getBytes("ISO-8859-1"),propertiesFileEncode);  
            } catch (UnsupportedEncodingException e) {  
                newValue = prop.getProperty(key);  
            }
            prop.setProperty(key, newValue);  
        }  
    }
}
