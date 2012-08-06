package javaagent.runtime;

import java.lang.instrument.Instrumentation;

/**
 * Test for javaagent load
 *
 * @author Romain Lespinasse
 */
public class RuntimeJavaagent {

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Start RuntimeJavaagent as JVM javaagent");
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("Start RuntimeJavaagent as runtime javaagent");
    }
}
