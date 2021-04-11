package com.zyonicsoftware.minereaper.signal.compression;

import com.zyonicsoftware.minereaper.signal.exception.SignalException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.*;

public class Compression {

    private static final Compression compression = new Compression();


    /**
     * @param data byte[] will be used to compress the information at the defalter algorithm.
     * @return an compressed information byte[].
     */
    @Deprecated
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
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (final IOException exception) {
            throw new SignalException("Failed to compressed byte array!", exception);
        }
    }

    /**
     * @param data byte[] will be used to decompress the information at the inflater algorithm.
     * @return an decompress string
     */
    @Deprecated
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
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
        } catch (final DataFormatException | IOException exception) {
            throw new SignalException("Failed to decompressed byte array!", exception);
        }
    }

    /**
     * @param data byte[] will be used to decompress the information at the inflater algorithm.
     * @return an decompress byte[]
     */
    @Deprecated
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
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (final DataFormatException | IOException exception) {
            throw new SignalException("Failed to decompressed byte array!", exception);
        }
    }

    public String decompress(final String string) {
        try {
            final GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(string)));
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream, StandardCharsets.UTF_8));
            final StringBuilder outString = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                outString.append(line);
            }
            return outString.toString();
        } catch (final IOException exception) {
            throw new SignalException(exception);
        }
    }

    public String compress(final String string) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gzipOutputStream.write(string.getBytes(StandardCharsets.UTF_8));
            gzipOutputStream.close();
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
        } catch (final IOException exception) {
            throw new SignalException(exception);
        }
    }

    public static Compression getCompression() {
        return Compression.compression;
    }
}
