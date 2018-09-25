package com.yidouinc.ydl.workflow.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HttpServletRequestReplacedFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger("requestLogInterceptor");

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		ServletRequest requestWrapper = null;
		if (request instanceof HttpServletRequest) {
			requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);
		}
		if (null == requestWrapper) {
			chain.doFilter(request, response);
		} else {
			BufferedReader br = requestWrapper.getReader();
			String str = null, retStr = "";
			while ((str = br.readLine()) != null) {
				retStr += str;
			}

			String ip = ((HttpServletRequest) request).getHeader("X-Real-IP");// 先从nginx自定义配置获取
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = ((HttpServletRequest) request).getHeader("X-Forwarded-For");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			StringBuffer url = ((HttpServletRequest) request).getRequestURL();
			StringBuilder logSb = new StringBuilder();
			logSb.append("ip:").append(ip).append("||");
			logSb.append("url:").append(url.toString()).append("||");
			if (StringUtils.isNotBlank(retStr)) {
				logSb.append("content:").append(retStr);
			} else {
				logSb.append("content:{");
				for (Entry<String, String[]> en : request.getParameterMap().entrySet()) {
					logSb.append(en.getKey()).append("=");
					for (String s : en.getValue()) {
						logSb.append(s).append(",");
					}
					logSb.append(";");
				}
				logSb.append("}");
			}
			// Map<String,String> jo = new HashMap<String, String>();
			// jo.put("ip", ip);
			// jo.put("url", url.toString());
			// jo.put("content", retStr);
			logger.info(logSb.toString());
			chain.doFilter(requestWrapper, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
