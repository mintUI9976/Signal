package com.zyonicsoftware.minereaper.signal.compression;

import com.zyonicsoftware.minereaper.signal.exception.SignalException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Compression {

    private static final Compression compression = new Compression();


    /**
     * @param data byte[] will be used to compress the information at the defalter algorithm.
     * @return an compressed information byte[].
     */
    public byte[] compress(final byte... data) {
        try {
            final Deflater deflater = new Deflater();
            deflater.setInput(data);
            deflater.setLevel(Deflater.BEST_SPEED);
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
            deflater.finish();
            final byte[] bytesBuffer = new byte[1024];
            while (!deflater.finished()) {
                final int amount = deflater.deflate(bytesBuffer);
                byteArrayOutputStream.write(bytesBuffer, 0, amount);
            }
            final byte[] compress = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return compress;
        } catch (final IOException exception) {
            throw new SignalException("Failed to compressed byte array!", exception);
        }
    }

    /**
     * @param data byte[] will be used to decompress the information at the inflater algorithm.
     * @return an decompress string
     */
    public String decompressAsString(final byte[] data) {
        try {
            final Inflater inflater = new Inflater();
            inflater.setInput(data);
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
            final byte[] bytesBuffer = new byte[1024];
            while (!inflater.finished()) {
                final int count = inflater.inflate(bytesBuffer);
                byteArrayOutputStream.write(bytesBuffer, 0, count);
            }
            final String decompress = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
            byteArrayOutputStream.close();
            return decompress;
        } catch (final DataFormatException | IOException exception) {
            throw new SignalException("Failed to decompressed byte array!", exception);
        }
    }

    /**
     * @param data byte[] will be used to decompress the information at the inflater algorithm.
     * @return an decompress byte[]
     */
    public byte[] decompressAsByteArray(final byte... data) {
        try {
            final Inflater inflater = new Inflater();
            inflater.setInput(data);
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(data.length);
            final byte[] bytesBuffer = new byte[1024];
            while (!inflater.finished()) {
                final int count = inflater.inflate(bytesBuffer);
                byteArrayOutputStream.write(bytesBuffer, 0, count);
            }
            final byte[] decompress = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return decompress;
        } catch (final DataFormatException | IOException exception) {
            throw new SignalException("Failed to decompressed byte array!", exception);
        }
    }

    public static Compression getCompression() {
        return Compression.compression;
    }
}
