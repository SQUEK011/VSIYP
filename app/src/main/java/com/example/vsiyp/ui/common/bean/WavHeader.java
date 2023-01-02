package com.example.vsiyp.ui.common.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WavHeader {
    public final char[] fileID = {'R', 'I', 'F', 'F'};

    public int fileLength;

    public char[] wavTag = {'W', 'A', 'V', 'E'};

    public char[] fmtHdrID = {'f', 'm', 't', ' '};

    public int fmtHdrLeth;

    public short formatTag;

    public short channels;

    public int samplesPerSec;

    public int avgBytesPerSec;

    public short blockAlign;

    public short bitsPerSample;

    public char[] dataHdrID = {'d', 'a', 't', 'a'};

    public int dataHdrLeth;

    public byte[] getHeader() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writeChar(bos, fileID);
        writeInt(bos, fileLength);
        writeChar(bos, wavTag);
        writeChar(bos, fmtHdrID);
        writeInt(bos, fmtHdrLeth);
        writeShort(bos, formatTag);
        writeShort(bos, channels);
        writeInt(bos, samplesPerSec);
        writeInt(bos, avgBytesPerSec);
        writeShort(bos, blockAlign);
        writeShort(bos, bitsPerSample);
        writeChar(bos, dataHdrID);
        writeInt(bos, dataHdrLeth);
        bos.flush();
        byte[] r = bos.toByteArray();
        bos.close();
        return r;
    }

    private void writeShort(ByteArrayOutputStream bos, int s) throws IOException {
        byte[] mybyte = new byte[2];
        mybyte[1] = (byte) ((s << 16) >> 24);
        mybyte[0] = (byte) ((s << 24) >> 24);
        bos.write(mybyte);
    }

    private void writeInt(ByteArrayOutputStream bos, int n) throws IOException {
        byte[] buf = new byte[4];
        buf[3] = (byte) (n >> 24);
        buf[2] = (byte) ((n << 8) >> 24);
        buf[1] = (byte) ((n << 16) >> 24);
        buf[0] = (byte) ((n << 24) >> 24);
        bos.write(buf);
    }

    private void writeChar(ByteArrayOutputStream bos, char[] id) {
        for (int i = 0; i < id.length; i++) {
            char c = id[i];
            bos.write(c);
        }
    }
}
