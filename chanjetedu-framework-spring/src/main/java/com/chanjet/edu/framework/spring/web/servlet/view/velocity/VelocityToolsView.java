package com.chanjet.edu.framework.spring.web.servlet.view.velocity;

import org.apache.velocity.context.Context;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.view.ViewToolContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.velocity.VelocityLayoutView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by shuai.w on 2016/6/20.
 */
public class VelocityToolsView extends VelocityLayoutView {

	private final Logger logger = LoggerFactory.getLogger(VelocityToolsView.class);

	@Override
	protected Context createVelocityContext(
			Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		ViewToolContext vtc = new ViewToolContext(getVelocityEngine(), request, response, getServletContext());

		vtc.putAll(model);


		// Load a Velocity Tools toolbox, if necessary.
		if (getToolboxConfigLocation() != null) {
			logger.debug("create toolbox config");
			ToolManager manager = new ToolManager();

			manager.setVelocityEngine(getVelocityEngine());
			manager.setUserCanOverwriteTools(true);
			manager.configure(getToolboxConfigLocation());

			addToolbox(vtc, manager, Scope.REQUEST);
			addToolbox(vtc, manager, Scope.APPLICATION);
			addToolbox(vtc, manager, Scope.SESSION);
		}

		return vtc;
	}

	private void addToolbox(ViewToolContext vtc, ToolManager manager, String scope) {
		if (manager.getToolboxFactory().hasTools(scope)) {
			vtc.addToolbox(manager.getToolboxFactory().createToolbox(
					scope));
		}
	}
}
