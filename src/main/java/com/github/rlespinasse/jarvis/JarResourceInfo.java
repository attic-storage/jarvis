package com.github.rlespinasse.jarvis;

import java.util.Arrays;
import java.util.List;

/**
 * define a information (or a group of informations) of a jar resource.
 *
 * @author Romain Lespinasse (romain.lespinas.se)
 */
public enum JarResourceInfo {
    NAME,
    PATH,
    SIZE,
    COMPRESSED_SIZE,
    COMMENT,
    CHECKSUM,
    EXTRA,
    COMPRESSION_METHOD,
    TIME,
    MANIFEST_ATTRIBUTES,
    CERTIFICATES,
    CODE_SIGNERS,
    CONTENT,
    INCLUDE_DIRECTORIES,
    /**
     * default informations
     * @see #NAME
     */
    DEFAULT(NAME),
    /**
     * file informations
     * @see #NAME
     * @see #PATH
     * @see #SIZE
     */
    FILE_INFO(NAME,
        PATH,
        SIZE),
    /**
     * file informations with content
     * @see #NAME
     * @see #PATH
     * @see #SIZE
     * @see #CONTENT
     */
    FILE(NAME,
            PATH,
            SIZE,
            CONTENT),
    /**
     * basic informations
     * @see #NAME
     * @see #SIZE
     * @see #CHECKSUM
     * @see #TIME
     */
    BASIC_INFO(NAME,
            SIZE,
            CHECKSUM,
            TIME),
    /**
     * compression informations
     * @see #NAME
     * @see #CHECKSUM
     * @see #SIZE
     * @see #TIME
     */
    COMPRESSION_INFO(NAME,
            SIZE,
            COMPRESSED_SIZE,
            CHECKSUM,
            COMPRESSION_METHOD),
    /**
     * all informations without content
     * @see #NAME
     * @see #PATH
     * @see #SIZE
     * @see #COMPRESSED_SIZE
     * @see #COMMENT
     * @see #CHECKSUM
     * @see #EXTRA
     * @see #COMPRESSION_METHOD
     * @see #TIME
     * @see #MANIFEST_ATTRIBUTES
     * @see #CERTIFICATES
     * @see #CODE_SIGNERS
     */
    FULL_INFO(NAME,
        PATH,
        SIZE,
        COMPRESSED_SIZE,
        COMMENT,
        CHECKSUM,
        EXTRA,
        COMPRESSION_METHOD,
        TIME,
        MANIFEST_ATTRIBUTES,
        CERTIFICATES,
        CODE_SIGNERS),
    /**
     * all informations with content
     * @see #NAME
     * @see #PATH
     * @see #SIZE
     * @see #COMPRESSED_SIZE
     * @see #COMMENT
     * @see #CHECKSUM
     * @see #EXTRA
     * @see #COMPRESSION_METHOD
     * @see #TIME
     * @see #MANIFEST_ATTRIBUTES
     * @see #CERTIFICATES
     * @see #CODE_SIGNERS
     * @see #CONTENT
     */
    FULL(NAME,
        PATH,
        SIZE,
        COMPRESSED_SIZE,
        COMMENT,
        CHECKSUM,
        EXTRA,
        COMPRESSION_METHOD,
        TIME,
        MANIFEST_ATTRIBUTES,
        CERTIFICATES,
        CODE_SIGNERS,
        CONTENT,
        INCLUDE_DIRECTORIES);

    private List<JarResourceInfo> subinfos;
    
    private JarResourceInfo() {
        this.subinfos = null;
    }

    private JarResourceInfo(JarResourceInfo ... subinfos) {
        this.subinfos = Arrays.asList(subinfos);
    }

    /**
     * get the all {@link JarResourceInfo} of this one (usefull for group informations),<br/>
     * <i>You can use this method on single informations, it's will return a list with one item</i>
     * @return a list of {@link JarResourceInfo}
     */
    public List<JarResourceInfo> getComparableInfos() {
        return (subinfos!=null)?subinfos:Arrays.asList(this);
    }
}
