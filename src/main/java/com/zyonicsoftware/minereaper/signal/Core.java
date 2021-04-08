package com.zyonicsoftware.minereaper.signal;


import com.zyonicsoftware.minereaper.signal.client.Client;
import com.zyonicsoftware.minereaper.signal.packet.PacketRegistry;
import com.zyonicsoftware.minereaper.signal.server.Server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Core {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<Object, Object> stringIntegerMap = new HashMap<>();

    public static void main(final String[] args) throws IOException {
        final String input = Core.scanner.next();
        PacketRegistry.registerPacket(TestPacket.class);
        final int a = 20 - 10;
        for (int i = 0; i < 54; i++) {
            Core.stringIntegerMap.put("Lobby-" + i, new Random().nextInt(a) + 10);
        }
        final String s = Core.convertMapToString(Core.stringIntegerMap);
        System.out.println(Core.stringIntegerMap.toString());
        switch (input) {
            case "server":
                final Server server = new Server(9976);
                server.connect();
                System.out.println("server startet");
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        server.sendToAllClients(new TestPacket(Core.finishCompress(s)));
                    }
                }, 5000, 5000);
                break;
            case "client":
                final Client client = new Client("localhost", 9976);
                client.connect();
                System.out.println("client startet");
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        client.send(new TestPacket(Core.finishCompress(s)));
                    }
                }, 5000, 5000);
                break;
        }
        Core.scanner.close();
    }

    public static String convertMapToString(final Map<Object, Object> map) {
        return map.keySet().stream()
                .map(key -> key + ":" + map.get(key))
                .collect(Collectors.joining(",", "{", "}"));
    }

    public static byte[] finishCompress(final String input) {
        return Core.compress(input.getBytes(StandardCharsets.UTF_8));
    }

  /*  public static byte[] readyToCompress(final byte[] bArray) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (final DeflaterOutputStream dos = new DeflaterOutputStream(os)) {
            dos.write(bArray);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        return os.toByteArray();
    }*/

    public static byte[] compress(final byte[] data) {
        try {
            final Deflater deflater = new Deflater();
            deflater.setInput(data);
            //nice compression Deflater.FILTERED
            deflater.setLevel(Deflater.BEST_SPEED);
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            deflater.finish();
            final byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                final int count = deflater.deflate(buffer); // returns the generated code... index
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
            return outputStream.toByteArray();
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] decompress(final byte[] data) {
        try {
            final Inflater inflater = new Inflater();
            inflater.setInput(data);

            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            final byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                final int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
            return outputStream.toByteArray();
        } catch (final DataFormatException | IOException exception) {
            exception.printStackTrace();
        }
        return new byte[0];
    }

}
