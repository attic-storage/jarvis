package se.lespinas.romain.jarvis;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Romain Lespinasse
 */
public class JarvisITCase {

    @Test
    public void load_a_basic_javaagent() throws ClassNotFoundException {
        System.out.println("load_a_basic_javaagent#start");
        Assert.assertTrue(Jarvis.loadAgent("javaagent.runtime.RuntimeJavaagent", ""));
        System.out.println("load_a_basic_javaagent#end");
    }
}
