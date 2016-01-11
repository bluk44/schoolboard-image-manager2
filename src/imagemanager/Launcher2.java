package imagemanager;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Odpala kontekst springa i cala aplikacje 
 * 
 * @author lucas
 *
 */

public class Launcher2 {
    public void launch() {
        String[] contextPaths = new String[] {"META-INF/app-context.xml"};
        ClassPathXmlApplicationContext ctx =  new ClassPathXmlApplicationContext(contextPaths);
        
    }
    
    public static void main(String[] args){
    	Launcher2 launcher = new Launcher2();
    	launcher.launch();
    }
}
