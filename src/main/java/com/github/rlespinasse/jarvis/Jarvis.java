package com.github.rlespinasse.jarvis;

import com.github.rlespinasse.jarvis.beans.JarCartography;
import com.github.rlespinasse.jarvis.beans.JarResource;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.regex.Pattern;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;
import static org.apache.commons.io.FilenameUtils.getName;

/**
 * helper to manipulate internals resources and informations of a jar
 *
 * @author Romain Lespinasse
 */
public final class Jarvis {
    private Jarvis() {}

    /**
     * get the jar url of a class
     * @param clazz class for jar search
     * @return a jar url
     * @throws IOException if an I/O exception occurs.
     */
    public static URL getJarFileURLOfClass(Class<? extends Object> clazz) throws IOException {
        if(clazz==null)
            return null;
        URL url = clazz.getResource("");
        JarURLConnection connection = (JarURLConnection) url.openConnection();
        url = connection.getJarFileURL();
        return url;
    }

    /**
     * get the jar filename of a class
     * @param clazz class for jar search
     * @return a jar filename
     * @throws IOException if an I/O exception occurs.
     */
    public static String getJarFilenameOfClass(Class<? extends Object> clazz) throws IOException {
        URL url = getJarFileURLOfClass(clazz);
        if(url==null)
            return null;
        return url.getFile();
    }

    /**
     * extract some files in a jar
     * @param filename path of the jar into filesystem (aka {@link java.io.File#getAbsolutePath()})
     * @param resourceFilterPattern pattern to filter the resources (no filter by default, see {@link java.util.regex.Pattern#matches(String, CharSequence)})
     * @param outputDirectory output directory for extraction
     * @param overwriteIfExists <code>true</code> if you want overwrite a existing file
     * @param respectFileTree <code>true</code> if you want copy the directory tree of a file
     * @return <code>true</code> if the extraction is done, <code>false</code> if <code>resourceFilterPattern</code> found no file matches into the jar
     * @throws IOException if an I/O error has occurred
     * @throws IOException if the file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading
     * @throws IOException if <code>outputDirectory</code> is a directory
     * @throws IOException if <code>outputDirectory</code> cannot be written
     * @throws IOException if <code>outputDirectory</code> needs creating but can't be
     * @throws IOException if an IO error occurs during copying
     */
    public static boolean extractFiles(String filename, String resourceFilterPattern, File outputDirectory, boolean overwriteIfExists, boolean respectFileTree) throws IOException {
        JarCartography cartography = getJarCartography(filename, resourceFilterPattern, JarResourceInfo.FILE);
        boolean extractSomeFiles = false;
        for(JarResource resource:cartography.getResources().values()) {
            byte[] resourceBytes = resource.getContent();
            String outputName = respectFileTree?resource.getPath():resource.getName();
            File output = new File(outputDirectory, outputName);
            if (overwriteIfExists || !output.exists())
                copyInputStreamToFile(new ByteArrayInputStream(resourceBytes), output);
            extractSomeFiles = true;
        }
        return extractSomeFiles;
    }

    /**
     * get a cartography of a jar
     * @param filename path of the jar into filesystem (aka {@link java.io.File#getAbsolutePath()})
     * @param informationFilters informations of the jar resources
     * @return a cartography of a jar
     * @throws IOException if an I/O error has occurred
     * @throws IOException if the file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading
     */
    public static JarCartography getJarCartography(String filename, JarResourceInfo ... informationFilters) throws IOException {
        JarCartography cartography = new JarCartography(filename, informationFilters);
        fill(cartography);
        return cartography;
    }

    /**
     * get a cartography of a jar
     * @param filename path of the jar into filesystem (aka {@link java.io.File#getAbsolutePath()})
     * @param withManifest <code>true</code> if you want the {@link java.util.jar.Manifest} informations
     * @param informationFilters informations of the jar resources
     * @return a cartography of a jar
     * @throws IOException if an I/O error has occurred
     * @throws IOException if the file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading
     */
    public static JarCartography getJarCartography(String filename, boolean withManifest, JarResourceInfo ... informationFilters) throws IOException {
        JarCartography cartography = new JarCartography(filename, withManifest, informationFilters);
        fill(cartography);
        return cartography;
    }

    /**
     * get a cartography of a jar
     * @param filename path of the jar into filesystem (aka {@link java.io.File#getAbsolutePath()})
     * @param resourceFilterPattern pattern to filter the resources (no filter by default, see {@link java.util.regex.Pattern#matches(String, CharSequence)})
     * @param informationFilters informations of the jar resources
     * @return a cartography of a jar
     * @throws IOException if an I/O error has occurred
     * @throws IOException if the file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading
     */
    public static JarCartography getJarCartography(String filename, String resourceFilterPattern, JarResourceInfo ... informationFilters) throws IOException {
        JarCartography cartography = new JarCartography(filename, resourceFilterPattern, informationFilters);
        fill(cartography);
        return cartography;
    }

    /**
     * get a cartography of a jar
     * @param filename path of the jar into filesystem (aka {@link java.io.File#getAbsolutePath()})
     * @param resourceFilterPattern pattern to filter the resources (no filter by default, see {@link java.util.regex.Pattern#matches(String, CharSequence)})
     * @param withManifest <code>true</code> if you want the {@link java.util.jar.Manifest} informations
     * @param informationFilters informations of the jar resources
     * @return a cartography of a jar
     * @throws IOException if an I/O error has occurred
     * @throws IOException if the file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading
     */
    public static JarCartography getJarCartography(String filename, String resourceFilterPattern, boolean withManifest, JarResourceInfo ... informationFilters) throws IOException {
        JarCartography cartography = new JarCartography(filename, resourceFilterPattern, withManifest, informationFilters);
        fill(cartography);
        return cartography;
    }

    private static void fill(JarCartography cartography) throws IOException {
        JarFile jarFile = new JarFile(cartography.getFilename());
        fillManifest(cartography, jarFile);
        fillJarResources(cartography, jarFile);
        jarFile.close();
        fillJarResourceContents(cartography);
    }

    private static void fillJarResourceContents(JarCartography cartography) throws IOException {
        if(cartography.getInformations().contains(JarResourceInfo.MANIFEST_ATTRIBUTES)) {
            FileInputStream jarFileInputStream = new FileInputStream(cartography.getFilename());
            BufferedInputStream jarBufferedInputStream = new BufferedInputStream(jarFileInputStream);
            JarInputStream jarInputStream = new JarInputStream(jarBufferedInputStream);
            JarEntry jarEntry;
            while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
                JarResource resource = cartography.getResources().get(jarEntry.getName());
                if(jarEntry.isDirectory())
                    continue;
                int size = (int) (resource!=null?resource.getSize():jarEntry.getSize());
                byte[] content = new byte[size];
                int offset = 0;
                int readedBytes = 0;
                while ((size - offset) > 0) {
                    readedBytes = jarInputStream.read(content, offset, size-offset);
                    if (readedBytes == -1) break;
                    offset += readedBytes;
                }
                if(resource != null)
                    resource.setContent(content);
            }
        }
    }

    private static void fillJarResources(JarCartography cartography, JarFile jarFile) throws IOException {
        Enumeration<? extends JarEntry> e = jarFile.entries();
        List<JarResourceInfo> infos = cartography.getInformations();
        while (e.hasMoreElements()) {
            JarEntry jarEntry = e.nextElement();
            boolean matches = cartography.getResourceFilterPattern() == null || Pattern.matches(cartography.getResourceFilterPattern(), jarEntry.getName());
            boolean included = infos.contains(JarResourceInfo.INCLUDE_DIRECTORIES) || !jarEntry.isDirectory();
            if(matches && included) {
                JarResource resource = new JarResource();
                resource.setDirectory(jarEntry.isDirectory());
                if(infos.contains(JarResourceInfo.NAME)) {
                    String name = jarEntry.getName();
                    if(resource.isDirectory())
                        name = name.substring(0, name.length()-1);
                    name = getName(name);
                    resource.setName(name);
                }
                if(infos.contains(JarResourceInfo.MANIFEST_ATTRIBUTES))
                    resource.setManifestAttributes(jarEntry.getAttributes());
                if(infos.contains(JarResourceInfo.CERTIFICATES) && jarEntry.getCertificates() != null)
                    resource.setCertificates(Arrays.asList(jarEntry.getCertificates()));
                if(infos.contains(JarResourceInfo.CODE_SIGNERS) && jarEntry.getCodeSigners() != null)
                    resource.setCodeSigners(Arrays.asList(jarEntry.getCodeSigners()));
                if(infos.contains(JarResourceInfo.COMMENT))
                    resource.setComment(jarEntry.getComment());
                if(infos.contains(JarResourceInfo.COMPRESSED_SIZE))
                    resource.setCompressedSize(jarEntry.getCompressedSize());
                if(infos.contains(JarResourceInfo.CHECKSUM))
                    resource.setChecksum(jarEntry.getCrc());
                if(infos.contains(JarResourceInfo.EXTRA))
                    resource.setExtra(jarEntry.getExtra());
                if(infos.contains(JarResourceInfo.COMPRESSION_METHOD))
                    resource.setCompressionMethod(jarEntry.getMethod());
                if(infos.contains(JarResourceInfo.PATH))
                    resource.setPath(jarEntry.getName());
                if(infos.contains(JarResourceInfo.SIZE))
                    resource.setSize(jarEntry.getSize());
                if(infos.contains(JarResourceInfo.TIME))
                    resource.setTime(jarEntry.getTime());
                cartography.addJarResource(jarEntry.getName(), resource);
            }
        }
    }

    private static void fillManifest(JarCartography cartography, JarFile jarFile) throws IOException {
        if(cartography.withManifest()) {
            cartography.setManifest(jarFile.getManifest());
            Map<String, Attributes> fullEntries = jarFile.getManifest().getEntries();
            fullEntries.put(JarCartography.MANIFEST_MAIN_ATTRIBUTES, jarFile.getManifest().getMainAttributes());
            for(Map.Entry<String, Attributes> entry: fullEntries.entrySet()) {
                Map<String, String> attributes = new HashMap<String, String>();
                for(Map.Entry<Object, Object> attribute: entry.getValue().entrySet())
                    attributes.put(attribute.getKey().toString(), (String) attribute.getValue());
                cartography.addAttributes(entry.getKey(), attributes);
            }
        }
    }
}
