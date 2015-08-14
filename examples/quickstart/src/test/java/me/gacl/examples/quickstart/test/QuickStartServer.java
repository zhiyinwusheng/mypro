package me.gacl.examples.quickstart.test;

import me.gacl.modules.test.jetty.JettyFactory;

import org.eclipse.jetty.server.Server;

public class QuickStartServer {

	public static final int PORT = 8088;
	public static final String CONTEXT = "/quickstart";
	public static final String[] TLD_JAR_NAMES = new String[] { "sitemesh", "spring-webmvc", "shiro-web",
			"springside-core" };
	
	public static void main(String[] args) throws Exception {
		// 启动Jetty
		Server server = JettyFactory.createServerInSource(PORT, CONTEXT);
		JettyFactory.setTdlJarNames(server, TLD_JAR_NAMES);
		
		try{
			server.start();
			
			System.out.println("[INFO] Server running at http://localhost:" + PORT + CONTEXT);
			System.out.println("[HINT] Hit Enter to reload the application quickly");
			
			while(true){
				char c = (char) System.in.read();
				if(c == '\n'){
					JettyFactory.reloadContext(server);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
}
