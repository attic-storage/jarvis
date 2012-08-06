package se.lespinas.romain.jarvis.javaagent;

import java.lang.instrument.Instrumentation;

/**
 * @author Romain Lespinasse
 */
public class JarvisLoaderAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Start JarvisLoaderAgent as JVM javaagent");
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("Start JarvisLoaderAgent as runtime javaagent");
    }
}
