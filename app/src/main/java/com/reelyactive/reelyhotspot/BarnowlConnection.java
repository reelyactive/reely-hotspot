package com.reelyactive.reelyhotspot;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

class BarnowlConnection extends HandlerThread {
    public static final int MSG_CONNECT = 0;
    public static final int MSG_SEND_BYTES = 1;

    public static final String DATA_BYTES = "data_bytes";
    public static final String DATA_IP = "data_ip";
    public static final String DATA_PORT = "data_ port";

    private Handler mHandler = null;
    private DatagramSocket socket;

    public BarnowlConnection(String name) {
        super(name);
    }

    public Handler getHandler() {
        return mHandler;
    }

    public synchronized void waitUntilReady() {
        if (mHandler == null) {
            mHandler = new Handler(getLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    switch (msg.what) {
                        case MSG_CONNECT:
                            if (socket == null) {
                                _connect(msg.getData().getString(DATA_IP), msg.getData().getInt(DATA_PORT));
                            }
                            break;
                        case MSG_SEND_BYTES:
                            if (socket != null) {
                                byte data[] = msg.getData().getByteArray(DATA_BYTES);
                                try {
                                    socket.send(new DatagramPacket(data, data.length));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                    }
                    return false;
                }
            });
        }
    }

    private void _connect(String ip, int port) {
        try {
            socket = new DatagramSocket();
            socket.connect(InetAddress.getByName(ip), port);
        } catch (SocketException e) {
            e.printStackTrace();
            socket = null;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            socket = null;
        }
    }

    public void connect(String ip, int port) {
        Message message = new Message();
        message.what = MSG_CONNECT;
        Bundle data = new Bundle();
        data.putString(DATA_IP, ip);
        data.putInt(DATA_PORT, port);
        message.setData(data);
        mHandler.sendMessage(message);
    }

    public void sendReelStatistics(byte[] beaconId) {
        byte bytes[] = new byte[26];
        bytes[0] = (byte) 0xaa;
        bytes[1] = (byte) 0xaa;
        bytes[2] = 0x78; // Code
        bytes[3] = 0x00; // Offset
        bytes[4] = 0x00; // Unused
        System.arraycopy(beaconId, 0, bytes, 5, 3); // ID
        Message message = new Message();
        message.what = MSG_SEND_BYTES;
        Bundle data = new Bundle();
        data.putByteArray(DATA_BYTES, bytes);
        message.setData(data);
        mHandler.sendMessage(message);
    }

    public void sendScanRecord(byte[] record, int rssi) {
        int payloadLength = getRecordLength(record);
        byte bytes[] = new byte[6 + payloadLength];
        bytes[0] = (byte) 0xaa;
        bytes[1] = (byte) 0xaa;
        bytes[2] = (byte) (0x000000ff & payloadLength);
        bytes[3] = 0x01;
        System.arraycopy(record, 0, bytes, 4, payloadLength);
        System.arraycopy(intToByteArray(rssi), 2, bytes, 4 + payloadLength, 2);
        Message message = new Message();
        message.what = MSG_SEND_BYTES;
        Bundle data = new Bundle();
        data.putByteArray(DATA_BYTES, bytes);
        message.setData(data);
        mHandler.sendMessage(message);
    }

    private int getRecordLength(byte[] record) {
        int length = 0;
        int nextPacket = 0;
        do {
            nextPacket = record[length];
            length += nextPacket + 1;
        } while (nextPacket != 0);
        return length;
    }

    private static byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value};
    }
}