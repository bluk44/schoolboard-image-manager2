package imagemanager;

import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**O
 * Odpala kontekst springa i cala aplikacje 
 * 
 * @author lucas
 *
 */

public class Launcher2 {
    public void launch() {
        String[] contextPaths = new String[] {"META-INF/app-context.xml"};
        //ClassPathXmlApplicationContext ctx =  new ClassPathXmlApplicationContext(contextPaths);
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(contextPaths);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    
    public static void main(String[] args){
    	Launcher2 launcher = new Launcher2();
    	launcher.launch();
    }
}
