package in.rongmz.memeappserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * @author Rounak Saha
 * (c) Copyright Rounak Saha.
 * Starting point of my app.
 */
public class App 
{

    final static Logger logger = LogManager.getLogger(App.class);

    public static void main( String[] args )
    {
        // take $PORT from arg-0
        int PORT = 8080;

        if(args.length>0) {
            PORT = Integer.parseInt(args[0]);
        }

        // Start the jetty server
        Server server = new Server(PORT);

        // configure REST activities
        ServletContextHandler restHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        restHandler.setContextPath("/");
        ServletHolder jerseyServlet = restHandler.addServlet(ServletContainer.class, "/api/*");
        jerseyServlet.setInitOrder(0);
        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(ServerProperties.PROVIDER_PACKAGES, "in.rongmz.memeappserver");


        // Create the ResourceHandler. It is the object that will actually handle the request for a given file. It is
        // a Jetty Handler object so it is suitable for chaining with other handlers as you will see in other examples.
        ResourceHandler resourceHandler = new ResourceHandler();

        // Configure the ResourceHandler. Setting the resource base indicates where the files should be served out of.
        // In this example it is the current directory but it can be configured to anything that the jvm has access to.
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
        resourceHandler.setResourceBase("public");

        // Add the ResourceHandler to the server.
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resourceHandler, restHandler });
        server.setHandler(handlers);

        try {
            // start g-drive apis
            //GDriveService.init();
            // start jetty
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        } finally {
            server.destroy();
        }
    }

}
