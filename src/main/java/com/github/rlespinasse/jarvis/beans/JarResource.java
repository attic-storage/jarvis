package com.github.rlespinasse.jarvis.beans;

import java.security.CodeSigner;
import java.security.cert.Certificate;
import java.util.List;
import java.util.jar.Attributes;

/**
 * bean definition of a jar resource
 *
 * @author Romain Lespinasse (romain.lespinas.se)
 */
public class JarResource {
    private long time;
    private long size;
    private String name;
    private String path;
    private int compressionMethod;
    private byte[] extra;
    private long checksum;
    private long compressedSize;
    private String comment;
    private List<CodeSigner> codeSigners;
    private List<Certificate> certificates;
    private Attributes manifestAttributes;
    private byte[] content;
    private boolean directory;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * get the compression method of a jar resource
     * @return compression method
     * @see java.util.jar.JarEntry#STORED
     * @see java.util.jar.JarEntry#DEFLATED
     */
    public int getCompressionMethod() {
        return compressionMethod;
    }

    public void setCompressionMethod(int compressionMethod) {
        this.compressionMethod = compressionMethod;
    }

    public byte[] getExtra() {
        return extra;
    }

    public void setExtra(byte[] extra) {
        this.extra = extra;
    }

    public long getChecksum() {
        return checksum;
    }

    public void setChecksum(long checksum) {
        this.checksum = checksum;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public void setCompressedSize(long compressedSize) {
        this.compressedSize = compressedSize;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<CodeSigner> getCodeSigners() {
        return codeSigners;
    }

    public void setCodeSigners(List<CodeSigner> codeSigners) {
        this.codeSigners = codeSigners;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
    }

    public Attributes getManifestAttributes() {
        return manifestAttributes;
    }

    public void setManifestAttributes(Attributes manifestAttributes) {
        this.manifestAttributes = manifestAttributes;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    /**
     * get a string representation of a jar resource
     * <pre>
     *  _name={
     *      time=_time, 
     *      size=_size,
     *      path='_path',
     *      compressionMethod=_compressionMethod,
     *      extra=_extra,
     *      checksum=_checksum,
     *      compressedSize=_compressedSize,
     *      comment='_comment',
     *      codeSigners=_codeSigners,
     *      certificates=_certificates,
     *      manifestAttributes=_manifestAttributes,
     *      content.length=_content.length,
     *      directory=_directory
     *  }
     * </pre>
     * @return a string representation of a jar resource
     */
    @Override
    public String toString() {
        return name +"={" +
                "time=" + time +
                ", size=" + size +
                ", path='" + path + '\'' +
                ", compressionMethod=" + compressionMethod +
                ", extra=" + extra +
                ", checksum=" + checksum +
                ", compressedSize=" + compressedSize +
                ", comment='" + comment + '\'' +
                ", codeSigners.size=" + (codeSigners!=null?codeSigners.size():0) +
                ", certificates.size=" + (certificates!=null?certificates.size():0) +
                ", manifestAttributes.size=" + (manifestAttributes!=null?manifestAttributes.size():0) +
                ", content.length=" + (content!=null?content.length:0) +
                ", directory=" + directory +
                '}';
    }
}
