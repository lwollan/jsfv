package org.clh.jsfv.file;

public class SfvChecksum {

    private Long crc32;

    public SfvChecksum(String hexValue) {
        setCRC32(Long.parseLong(hexValue.trim(), 16));
    }

    private void setCRC32(Long crc32) {
        this.crc32 = crc32;
    }

    public Long getCrc32() {
        return crc32;
    }

}
