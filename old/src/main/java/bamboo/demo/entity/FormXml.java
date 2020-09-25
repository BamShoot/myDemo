package bamboo.demo.entity;

import java.util.HashMap;

/**
 * 表单信息
 * 
 * @author dell
 * 
 */
public class FormXml {

	/**
	 * 表单名称
	 */
	private String name;

	/**
	 * 表单内容
	 */
	private HashMap<String, String> content = new HashMap<String, String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, String> getContent() {
		return content;
	}

	public void setContent(HashMap<String, String> content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "FormXml [name=" + name + ", content=" + content + "]";
	}


}
