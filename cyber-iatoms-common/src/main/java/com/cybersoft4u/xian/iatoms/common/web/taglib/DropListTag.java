package com.cybersoft4u.xian.iatoms.common.web.taglib;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;   
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;   
import javax.servlet.jsp.tagext.TagSupport;

/*import jdsl.core.api.Position;
import jdsl.core.api.PositionIterator;
import jdsl.core.api.Tree;*/

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import cafe.core.bean.Parameter;
import cafe.core.bean.Recursable;
import cafe.core.util.StringUtils;
import cafe.core.util.TreeBuilder;
/**
 * <p>用於<select>的自定義標籤.</p>
 * <p>Company:     CybersoftXi'an   </p>
 * <p>@author：    susanchen</p>
 * <b>@version：</b>
 *    1.0 2009/07/20   Create   by susanchen<br>
 */
public class DropListTag extends TagSupport {
	private static final Log log = LogFactory.getLog(DropListTag.class);
	//指定droplist需要定義的樣式.
	public String css;
	public boolean disabled;
	//控制下拉選單是否readonly
	public boolean readonly;
	//指定id
	public String id;
	public String javascript;
	public String multiple;
	public String name;
	public String size;
	public String style;
	public String title;
	//指定droplist中需要顯示的結果集
	public Collection result;
	//指定當用戶指定需要selected的值為空的時候, 需要預設選中的value.
	public String defaultValue;
	//指定取出數據需要設定的selected value.
	public String selectedValue;
	//指定取出數據需要設定的selected values.
	public List<String> selectedValues;
	//設定是否需要一個空白值.
	public boolean hasBlankValue;
	//設定空白選項呈現的內容.
	public String blankName;
	//需排除的直
	public String exclusiveValue;
	//下拉選單資料是否要含ID
	public boolean showValueWithName;
	
	public boolean showNest = false;
	
	public boolean includeRoot = false;
	
	boolean isExistForSelectValue = false;
	
	/**
	 * doStartTag.
	 * @param 
	 * @author susanchen
	 * @return int
	 * @throws JspException
	 */
    public int doStartTag() throws JspException {
    	try {
    		isExistForSelectValue = false;
    		//name不允許為空.
    		if (name == null || name.equals(""))
    			throw new JspTagException("invalid null or empty 'name'");
    		JspWriter jspwriter = pageContext.getOut();
    		
    		//如果需要下拉選單readonly,則增加span控制.
    		//updated by JudeJiao 2011/07/04 IPM-2807 - LOB6_CR_201105260001 修改鼠標時間觸發js的時機
   			if (readonly)
   	   			jspwriter.print(" <span onbeforeactivate='this.setCapture();return false;' onmousemove='this.setCapture();' " +
   	   					" onmouseout='this.releaseCapture();' " +
   	   					" onfocus='this.blur();' > ");
   			
    		//顯示select起始標籤
    		jspwriter.print("<select name='" + name + "' ");
    		if (css != null) jspwriter.print(" class='" + css + "' ");
   			if (disabled) jspwriter.print(" disabled='disabled' ");
   			if (id != null) jspwriter.print(" id='" + id + "' ");
   			if (javascript != null) jspwriter.print(" " + javascript + " ");
    		if (multiple != null) jspwriter.print(" multiple='" + multiple + "' ");
    		if (size != null) jspwriter.print(" size='" + size + "' ");
    		if (style != null) jspwriter.print(" style='" + style + "' ");
    		if (title != null) jspwriter.print(" title='" + title + "' ");
    		//<select ....> end
    		jspwriter.print(">");
    		//用戶是否需要一個空選項
    		if (hasBlankValue) {
    			jspwriter.print("<option  value='' >");
    			//空值顯示什麼內容, 比如:請選擇--
        		if (blankName == null) jspwriter.print("");
        		else jspwriter.print(blankName);
        		//option結束
        		jspwriter.print("</option>");
    		}
    		
    		if (result != null && !result.isEmpty()) {
    			
    			//如果用戶傳入的selectedValue有數據, 則
    			StringBuffer optionBuffer = new StringBuffer();
   			
    			if (showNest) {
    				/*TreeBuilder tb = new TreeBuilder();
    				Tree tree = tb.build(result);
    				if (tree != null) {
    					Position root = tree.root();
    					String optionStr = this.drawNest(tree, root, 0);
    					optionBuffer.append(optionStr);
    				}*/
    			} else {
        			Iterator<?> iter = this.result.iterator();
	    			while(iter.hasNext()){
	    	    		Object param = iter.next();
	    	    		String name = null;
	    	    		String value = null;
	    	    		if (param instanceof Parameter) {
		    	    		name = ((Parameter)param).getName();
		    	    		value = ((Parameter)param).getValue().toString();
	    	    		} else if (param instanceof Recursable) {
		    	    		name = ((Recursable)param).getName();
		    	    		value = ((Recursable)param).getId();
	    	    		}
	    	    		String optionStr = this.draw(0, name, value);
	    	    		optionBuffer.append(optionStr);
	    	    	}
    			}	
    	    	jspwriter.print(optionBuffer.toString());
    		}
    		jspwriter.print("</select>");
    		if(readonly) jspwriter.print(" </span> ");
    			
    	} catch (Exception exception) {
    		log.debug("DroplistTag.doStartTag()-->" + exception, exception);
    		throw new JspTagException(exception.getMessage());
    	}
    	return 0;
    }
    
    private String draw(int depth, String name, String value) {
    	if (this.exclusiveValue != null && this.exclusiveValue.equals(value)) return "";
    	StringBuffer optionBuffer = new StringBuffer("");
		optionBuffer.append("<option");
		optionBuffer.append(" value='" + value + "'");
		if(!isExistForSelectValue && value.equals(defaultValue)){
			optionBuffer.append(" selected='selected' ");
		}
		if (StringUtils.hasText(selectedValue)) {
			if (value.equals(selectedValue)){
				isExistForSelectValue = true;
				optionBuffer = new StringBuffer(optionBuffer.toString().replace("selected='selected'",""));
				optionBuffer.append(" selected='selected' ");
			}
		}
		
		if (!CollectionUtils.isEmpty(selectedValues)) {
			if (selectedValues.contains(value)) {
				isExistForSelectValue = true;
				optionBuffer = new StringBuffer(optionBuffer.toString().replace("selected='selected'",""));
				optionBuffer.append(" selected='selected' ");
			}
		}
		optionBuffer.append(">");
		if (depth > 1) {
			for (int i = 1; i < depth; i++) {
				optionBuffer.append("　");
			}
		}
		optionBuffer.append(((this.showValueWithName) ? value+" " : "") + name);
		optionBuffer.append("</option>");
		return optionBuffer.toString();
    }
    
    /*public String drawNest(Tree tree, Position pos, int index) {
			StringBuffer sb = new StringBuffer("");
			if (pos == null || pos.element() == null) return "";
			
			Recursable element = (Recursable)pos.element();

			PositionIterator pi = tree.children(pos);
			
			if(!tree.isRoot(pos) || this.includeRoot) {
				index++;
				String str = this.draw(index, element.getName(), element.getId());
				sb.append(str);
			}	
			if (pi != null && pi.hasNext()) {
				while (pi.hasNext()) {
					Position child = pi.nextPosition();
					String s = drawNest(tree, child, index);
					sb.append(s);
				}
			}	
			return sb.toString();
    }*/
	//*******************************************************************************

    /**
     * @return the result
     */
    public Collection getResult() {
    		return result;
    }
    /**
     * @param result the result to set
     */
    public void setResult(Collection result) {
    		this.result = result;
    }
    
    public void setResult(List result) {
			this.result = result;
    }
    /**
     * @param disabled the disabled to set
     */
    public void setDisabled(boolean disabled) {
    		this.disabled = disabled;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
    		this.id = id;
    }
    /**
     * @param multiple the multiple to set
     */
    public void setMultiple(String multiple) {
    		this.multiple = multiple;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
    		this.name = name;
    }
    /**
     * @param size the size to set
     */
    public void setSize(String size) {
    		this.size = size;
    }
    /**
     * @param style the style to set
     */
    public void setStyle(String style) {
    		this.style = style;
    }
    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
    		this.title = title;
    }
    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
    		this.defaultValue = defaultValue;
    }
    /**
     * @param selectedValue the selectedValue to set
     */
    public void setSelectedValue(String selectedValue) {
    		this.selectedValue = selectedValue;
    }

    /**
     * @param css the css to set
     */
    public void setCss(String css) {
    		this.css = css;
    }
    
    /**
     * @param javascript the javascript to set
     */
    public void setJavascript(String javascript) {
    		this.javascript = javascript;
    }
    
    /**
     * @param hasBlankValue the hasBlankValue to set
     */
    public void setHasBlankValue(boolean hasBlankValue) {
    		this.hasBlankValue = hasBlankValue;
    }
    
    /**
     * @param blankName the blankName to set
     */
    public void setBlankName(String blankName) {
    		this.blankName = blankName;
    }

    /**
     * @param readonly the readonly to set
     */
    public void setReadonly(boolean readonly) {
    		this.readonly = readonly;
    }
	
	public void setExclusiveValue(String exclusiveValue) {
			this.exclusiveValue = exclusiveValue;
	}

	public void setShowValueWithName(boolean showValueWithName) {
		this.showValueWithName = showValueWithName;
	}
	public void setShowNest(boolean showNest) {
		this.showNest = showNest;
	}
	public void setIncludeRoot(boolean includeRoot) {
		this.includeRoot = includeRoot;
	}

	/**
	 * @param selectedValues the selectedValues to set
	 */
	public void setSelectedValues(List<String> selectedValues) {
		this.selectedValues = selectedValues;
	}	
	
	
}
