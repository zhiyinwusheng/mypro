package me.gacl.modules.test.jetty;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

import com.google.common.collect.Lists;

public class JettyFactory {

	private static final String DEFAULT_WEBAPP_PATH = "src/main/webapp";
	private static final String WINDOWS_WEBDEFAULT_PATH = "jetty/webdefault-windows.xml";

	public static Server createServerInSource(int port ,String contextPath){
		Server server = new Server();
		
		//设置jvm退出时关闭Jetty钩子
		server.setStopAtShutdown(true);
		
		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		connector.setReuseAddress(false);
		server.setConnectors(new Connector[]{connector});
		
		WebAppContext webContext = new WebAppContext(DEFAULT_WEBAPP_PATH,contextPath);
		webContext.setDefaultsDescriptor(WINDOWS_WEBDEFAULT_PATH);
		server.setHandler(webContext);
		
		return server;
	}
	
	public static void setTdlJarNames(Server server,String... jarNames){
		WebAppContext context = (WebAppContext) server.getHandler();
		List<String> jarNameExpressions = Lists.newArrayList(".*/jstl-[^/]*\\.jar$", ".*/.*taglibs[^/]*\\.jar$");
		for(String name :jarNames){
			jarNameExpressions.add(".*/" + name + "-[^/]*\\.jar$");
		}
		context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
				StringUtils.join(jarNameExpressions, '|'));
	}
	
	public static void reloadContext(Server server) throws Exception{
		WebAppContext context = (WebAppContext) server.getHandler();
		System.out.println("[INFO] Application reloading");
		context.stop();
		
		WebAppClassLoader classLoader = new WebAppClassLoader(context);
		classLoader.addClassPath("target/classes");
		classLoader.addClassPath("target/test-classes");
		context.setClassLoader(classLoader);
		
		context.start();
		System.out.println("[INFO] Application reloaded");
	}
}
