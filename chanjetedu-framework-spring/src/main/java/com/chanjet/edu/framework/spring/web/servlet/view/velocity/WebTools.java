package com.chanjet.edu.framework.spring.web.servlet.view.velocity;

import lombok.Setter;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.config.ValidScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by shuai.w on 2016/6/20.
 */
@DefaultKey("web")
@ValidScope(Scope.REQUEST)
public class WebTools extends AppTools {
	@Setter
	private HttpServletRequest request;
	@Setter
	private HttpServletResponse response;

	public void redirect(String url) throws IOException {
		response.sendRedirect(url);
	}

	public void error(int sc) throws IOException {
		response.sendError(sc);
	}

	public void error(int sc, String message) throws IOException {
		response.sendError(sc, message);
	}

}
