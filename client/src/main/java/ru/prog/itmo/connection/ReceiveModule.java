package ru.prog.itmo.connection;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class ReceiveModule {
    private DatagramSocket socket;
    private static final int PACKET_SIZE = 4096;
    private static final byte STOP_BYTE = 0x60;

    public ReceiveModule(DatagramSocket socket) {
        this.socket = socket;
    }

    public synchronized Response<?> getResponse(){
        ByteBuffer fromServer = receiveResponse();
        var response = deserializeResponse(fromServer);
        if (response.isNotAuthorized())
            exit();
        return deserializeResponse(fromServer);
    }

    private void exit(){
        JOptionPane.showMessageDialog(null, "You need to re-sign in to the app");
        System.exit(0);
    }

    private ByteBuffer receiveResponse() {
        try {
            var packets = receivePackets();
            var packetsStream = getAllPacketsStream(packets);
            return ByteBuffer.wrap(packetsStream.toByteArray());
        } catch (IOException e) {
            throw new InvalidConnectionException("Не удалось получить ответ от сервера");
        }
    }

    private ByteArrayOutputStream getAllPacketsStream(ArrayList<ByteBuffer> packets) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (ByteBuffer currentBuffer : packets) {
            byte[] currentBytes = currentBuffer.array();
            baos.write(currentBytes);
        }
        return baos;
    }

    private ArrayList<ByteBuffer> receivePackets() throws IOException {
        ArrayList<ByteBuffer> packets = new ArrayList<>();
        byte[] fromServer = new byte[PACKET_SIZE];
        boolean isReceivingDone = false;
        while (!isReceivingDone) {
            isReceivingDone = doLoop(fromServer, packets);
        }
        return packets;
    }

    private boolean doLoop(byte[] fromServer, ArrayList<ByteBuffer> packets) throws IOException {
        var packet = new DatagramPacket(fromServer, fromServer.length);
        socket.receive(packet);
        ByteArrayInputStream bais = new ByteArrayInputStream(fromServer);
        ByteBuffer data = ByteBuffer.wrap(bais.readNBytes(PACKET_SIZE - 1));
        packets.add(data);
        return fromServer[PACKET_SIZE - 1] == STOP_BYTE;
    }

    private Response<?> deserializeResponse(ByteBuffer buffer) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Response<?>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new InvalidConnectionException(Arrays.toString(e.getStackTrace()));
        }
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }
}
