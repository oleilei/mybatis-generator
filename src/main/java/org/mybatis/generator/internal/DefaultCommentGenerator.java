/*
 *  Copyright 2008 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.mybatis.generator.internal;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.PropertyConfig;

/**
 * @author Jeff Butler
 * 
 */
public class DefaultCommentGenerator implements CommentGenerator {

    private Properties properties;
    private boolean suppressDate;
    
    public DefaultCommentGenerator() {
        super();
        properties = new Properties();
        suppressDate = false;
    }

    /**
     * 这块注释可以自由发挥，根据各自公司的情况来设定
     */
    public void addJavaFileComment(CompilationUnit compilationUnit) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        compilationUnit.addFileCommentLine("/*");
        compilationUnit.addFileCommentLine(" * " + PropertyConfig.getProterties().getProperty("copyright"));
        compilationUnit.addFileCommentLine(" * 类名: " + compilationUnit.getType().getShortName());
        compilationUnit.addFileCommentLine(" * 创建人: " + PropertyConfig.getProterties().getProperty("user") + "    创建时间: " + format.format(new Date()));
        compilationUnit.addFileCommentLine(" */");
    }

    /**
     * Adds a suitable comment to warn users that the element was generated, and
     * when it was generated.
     */
    public void addComment(XmlElement xmlElement) {
        
    }

    public void addRootComment(XmlElement rootElement) {
        // add no document level comments by default
        return;
    }

    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);

        suppressDate = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));
    }

    /**
     * This method adds the custom javadoc tag for. You may do nothing if you do
     * not wish to include the Javadoc tag - however, if you do not include the
     * Javadoc tag then the Java merge capability of the eclipse plugin will
     * break.
     * 
     * @param javaElement
     *            the java element
     */
    protected void addJavadocTag(JavaElement javaElement,
            boolean markAsDoNotDelete) {
        javaElement.addJavaDocLine(" *"); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        sb.append(" * "); //$NON-NLS-1$
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge"); //$NON-NLS-1$
        }
        String s = getDateString();
        if (s != null) {
            sb.append(' ');
            sb.append(s);
        }
        javaElement.addJavaDocLine(sb.toString());
    }

    /**
     * This method returns a formated date string to include in the Javadoc tag
     * and XML comments. You may return null if you do not want the date in
     * these documentation elements.
     * 
     * @return a string representing the current timestamp, or null
     */
    protected String getDateString() {
        if (suppressDate) {
            return null;
        } else {
            return new Date().toString();
        }
    }

    /**
     * 这里我用的是MySQL数据库表的注释信息作为说明文字
     */
    public void addClassComment(InnerClass innerClass,
            IntrospectedTable introspectedTable) {
        innerClass.addJavaDocLine("/**");
        String remarks = introspectedTable.getFullyQualifiedTable().getRemarks();
        remarks = (remarks == null || "".equals(remarks) ? introspectedTable.getTableConfiguration().getTableName() : remarks);
        innerClass.addJavaDocLine(" * " + remarks + " PO类<br/>");
        innerClass.addJavaDocLine(" * 该PO类对应" + introspectedTable.getTableConfiguration().getTableName());
        innerClass.addJavaDocLine(" * @author " + PropertyConfig.getProterties().getProperty("user"));
        innerClass.addJavaDocLine(" */");
    }
    
    public void addEnumComment(InnerEnum innerEnum,
            IntrospectedTable introspectedTable) {
    }

    public void addFieldComment(Field field,
            IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn) {
		if (introspectedColumn.getRemarks() != null
				&& !"".equals(introspectedColumn.getRemarks())) {
			field.addJavaDocLine("/**");
			//field.addJavaDocLine(" * " + introspectedColumn.getRemarks() + " " + introspectedColumn.getActualColumnName());
			field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
			field.addJavaDocLine(" */");
		} else {
		    field.addJavaDocLine("/**");
            field.addJavaDocLine(" * " + introspectedColumn.getActualColumnName());
            field.addJavaDocLine(" */");
        }
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
    }

    public void addGeneralMethodComment(Method method,
            IntrospectedTable introspectedTable) {
    }

    public void addGetterComment(Method method,
            IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn) {
        if (introspectedColumn.getRemarks() != null
                && !"".equals(introspectedColumn.getRemarks())) {
            method.addJavaDocLine("/**");
            //method.addJavaDocLine(" * @return Returns the " + introspectedColumn.getJavaProperty() + " " + introspectedColumn.getRemarks());
            method.addJavaDocLine(" * @return Returns the " + introspectedColumn.getRemarks());
            method.addJavaDocLine(" */");
        } else {
            method.addJavaDocLine("/**");
            method.addJavaDocLine(" * @return Returns the " + introspectedColumn.getJavaProperty());
            method.addJavaDocLine(" */");
        }
    }

    public void addSetterComment(Method method,
            IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn) {
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * @param " + introspectedColumn.getJavaProperty() + " The " + introspectedColumn.getJavaProperty() + " to set.");
        method.addJavaDocLine(" */");
    }

    public void addClassComment(InnerClass innerClass,
            IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
    }
}
