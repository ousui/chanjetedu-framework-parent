package com.chanjet.edu.framework.spring.web.servlet.handler;

import com.chanjet.edu.framework.base.utils.StringUtils;
import com.google.common.collect.Maps;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * 根据映射规则，将一些映射自动映射到其他 view 上。
 * Created by shuai.w on 2016/6/15.
 */
public class IndexMappingSupportHandlerInterceptor extends HandlerInterceptorAdapter {
	private static final String PATH_SPLIT = "/";

	//	private Map<String, String> mappings = Maps.newLinkedHashMap();
	private Map<String, RequestMethodView> mappings = Maps.newLinkedHashMap();

	/**
	 * 默认支持的请求方式。
	 */
	@Setter
	private RequestMethod[] supportMethods = new RequestMethod[]{RequestMethod.GET};

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView == null) {
			return;
		}

		String path = request.getServletPath();

		if (null == path) {
			return;
		}

		if (!StringUtils.hasText(path)) {
			path = PATH_SPLIT;
		}

		String prefix = StringUtils.getFrontBeforeSp(path.trim(), PATH_SPLIT);

		StringBuilder viewsb = new StringBuilder();
		viewsb.append(prefix).append(PATH_SPLIT);

		for (String regex : mappings.keySet()) {
			if (path.matches(regex)) {// 只找第一个
				RequestMethodView rmv = mappings.get(regex);
				if(!rmv.hasMethod(request.getMethod())){ // 如果请求方式不匹配，跳出
					break;
				}
				viewsb.append(rmv.view);
				modelAndView.setViewName(viewsb.toString());
				break;
			}
		}
	}

	/**
	 * default use RequestMethod.GET
	 *
	 * @param partten
	 * @param toView
	 * @return
	 */
	public IndexMappingSupportHandlerInterceptor addMapping(String partten, String toView) {
		return addMapping(partten, toView, RequestMethod.GET);
	}

	public IndexMappingSupportHandlerInterceptor addMapping(String partten, String toView, RequestMethod... requestMethods) {
		if (requestMethods == null || requestMethods.length <= 0) {
			requestMethods = new RequestMethod[]{RequestMethod.GET};
		}
		mappings.put(partten, new RequestMethodView(toView, requestMethods));
		return this;
	}

	static class RequestMethodView {
		final String view;
		final RequestMethod[] requestMethods;

		private RequestMethodView(String view, RequestMethod[] requestMethods) {
			this.view = view;
			this.requestMethods = requestMethods;
		}

		boolean hasMethod(String method) {
			RequestMethod rm = RequestMethod.valueOf(method.trim().toUpperCase());
			return Arrays.binarySearch(this.requestMethods, rm) >= 0;
		}


	}

}
