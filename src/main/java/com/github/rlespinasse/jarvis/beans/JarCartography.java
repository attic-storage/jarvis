package com.github.rlespinasse.jarvis.beans;

import com.github.rlespinasse.jarvis.JarResourceInfo;

import java.util.*;
import java.util.jar.Manifest;

import static com.github.rlespinasse.jarvis.JarResourceInfo.DEFAULT;

/**
 * cartography of a jar
 *
 * @author Romain Lespinasse
 */
public class JarCartography {

    public static final String MANIFEST_MAIN_ATTRIBUTES = "MANIFEST_MAIN_ATTRIBUTES";

    private String filename;
    private String resourceFilterPattern;
    private boolean withManifest;
    private Manifest manifest;
    private List<JarResourceInfo> informations;
    private Map<String, JarResource> resources;
    private Map<String, Map<String, String>> entryAttributes;

    {
        // pattern to match any characters
        this.resourceFilterPattern = ".*";
        this.withManifest = false;
        this.manifest = null;
        this.informations = new ArrayList<JarResourceInfo>();
        this.resources = new HashMap<String, JarResource>();
        this.entryAttributes = new HashMap<String, Map<String, String>>();
    }

    /**
     * initialize the cartography parameters of a jar
     * @param filename path of the jar into filesystem (aka {@link java.io.File#getAbsolutePath()})
     * @param moreInformations some needed informations about the jar resources (by default {@link JarResourceInfo#DEFAULT})
     * @throws IllegalArgumentException if <code>filename</code> is null or empty
     */
    public JarCartography(String filename, JarResourceInfo ... moreInformations) {
        if(filename == null)
            throw new IllegalArgumentException("Unexpectedly null filename");
        if(filename.trim().length() == 0)
            throw new IllegalArgumentException("Unexpectedly empty filename");
        this.filename = filename;
        if(moreInformations != null) {
            for(JarResourceInfo moreInformation:moreInformations)
                this.informations.addAll(moreInformation.getComparableInfos());
        } else
            this.informations.addAll(DEFAULT.getComparableInfos());
    }

    /**
     * initialize the cartography parameters of a jar
     * @param filename path of the jar into filesystem (aka {@link java.io.File#getAbsolutePath()})
     * @param resourceFilterPattern pattern to filter the resources (no filter by default, see {@link java.util.regex.Pattern#matches(String, CharSequence)})
     * @param moreInformations some needed informations about the jar resources (by default {@link JarResourceInfo#DEFAULT})
     * @throws IllegalArgumentException if <code>filename</code> is null or empty
     * @throws IllegalArgumentException if <code>resourceFilterPattern</code> is empty
     */
    public JarCartography(String filename, String resourceFilterPattern, JarResourceInfo ... moreInformations) {
        this(filename, moreInformations);
        if(resourceFilterPattern != null && resourceFilterPattern.trim().length() == 0)
            throw new IllegalArgumentException("Unexpectedly empty resourceFilterPattern");
        this.resourceFilterPattern = resourceFilterPattern;
    }

    /**
     * initialize the cartography parameters of a jar
     * @param filename path of the jar into filesystem (aka {@link java.io.File#getAbsolutePath()})
     * @param withManifest <code>true</code> if you want the {@link Manifest} informations
     * @param moreInformations some needed informations about the jar resources (by default {@link JarResourceInfo#DEFAULT})
     * @throws IllegalArgumentException if <code>filename</code> is null or empty
     */
    public JarCartography(String filename, boolean withManifest, JarResourceInfo ... moreInformations) {
        this(filename, moreInformations);
        this.withManifest = withManifest;
    }

    /**
     * initialize the cartography parameters of a jar
     * @param filename path of the jar into filesystem (aka {@link java.io.File#getAbsolutePath()})
     * @param resourceFilterPattern pattern to filter the resources (no filter by default, see {@link java.util.regex.Pattern#matches(String, CharSequence)})
     * @param withManifest <code>true</code> if you want the {@link Manifest} informations
     * @param moreInformations some needed informations about the jar resources (by default {@link JarResourceInfo#DEFAULT})
     * @throws IllegalArgumentException if <code>filename</code> is null or empty
     * @throws IllegalArgumentException if <code>resourceFilterPattern</code> is empty
     */
    public JarCartography(String filename, String resourceFilterPattern, boolean withManifest, JarResourceInfo ... moreInformations) {
        this(filename, resourceFilterPattern, moreInformations);
        this.withManifest = withManifest;
    }

    public String getFilename() {
        return filename;
    }

    public String getResourceFilterPattern() {
        return resourceFilterPattern;
    }

    public List<JarResourceInfo> getInformations() {
        return informations;
    }

    public Map<String, JarResource> getResources() {
        return resources;
    }

    public Manifest getManifest() {
        return manifest;
    }

    public boolean withManifest() {
        return withManifest;
    }

    public void setManifest(Manifest manifest) {
        this.manifest = manifest;
    }

    /**
     * add a {@link JarResource} on this cartography
     * @param jarEntryName path of the resource into the jar
     * @param resource informations (and content) of a resource of the jar
     * @see java.util.jar.JarEntry#getName()
     */
    public void addJarResource(String jarEntryName, JarResource resource) {
        this.resources.put(jarEntryName, resource);
    }

    /**
     * get the main attributes of the jar manifest
     * @return a key/value representation of main attributes
     * @see JarCartography#MANIFEST_MAIN_ATTRIBUTES
     * @see java.util.jar.Manifest#getMainAttributes()
     */
    public Map<String, String> getManifestMainAttributes() {
        return getManifestEntryAttributes(MANIFEST_MAIN_ATTRIBUTES);
    }

    /**
     * get entries of a jar manifest
     * @return all entries of a jar manifest (except 'main' of course)
     */
    public Set<String> getManifestEntries() {
        Set<String> entries = entryAttributes.keySet();
        entries.remove(MANIFEST_MAIN_ATTRIBUTES);
        return entries;
    }

    /**
     * get the main attributes of the jar manifest
     * @param entry
     * @return a key/value representation of main attributes
     * @see java.util.jar.Manifest#getEntries()
     * @see java.util.jar.Manifest#getAttributes(String)
     */
    public Map<String, String> getManifestEntryAttributes(String entry) {
        return entryAttributes.get(entry);
    }

    /**
     * add a attributes map on this cartography
     * @param entry a entry into the jar manifest
     * @param attributes attributes associated at a entry
     * @see JarCartography#MANIFEST_MAIN_ATTRIBUTES
     * @see java.util.jar.Manifest#getMainAttributes()
     * @see java.util.jar.Manifest#getEntries()
     * @see java.util.jar.Manifest#getAttributes(String)
     */
    public void addAttributes(String entry, Map<String,String> attributes) {
        entryAttributes.put(entry, attributes);
    }
}
