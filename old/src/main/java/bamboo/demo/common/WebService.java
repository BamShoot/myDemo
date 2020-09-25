package bamboo.demo.common;


import cn.hutool.core.util.StrUtil;
import org.apache.axis.client.Service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;


@Controller
@RequestMapping("demo/interfaces")
public class WebService {


	// 参数
	// String s = "1";
	// String x = "2017-01-01 00:00:00";
	// String y = "2017-10-05 16:31:30";
	// 请求webservice例子
	@RequestMapping("addcc")
	@ResponseBody
	public String add(String s, String x, String y) {
		String ret = "";
		try {
			Service service = new Service();
			Call call = service.createCall();
			String url = "http://118.178.118.120:8080/open/services/ExportStatisticsData?wsdl";
			call.setTargetEndpointAddress(url);
			// 指定方法名称
			call.setOperationName(QName.valueOf("exportStatisticsData"));
			// 指定参数
			call.addParameter("statisticsType", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("startTime", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("endTime", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);

			// 返回处理结果
			ret = StrUtil.toString(call.invoke(new Object[] { s, x, y }));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
}
