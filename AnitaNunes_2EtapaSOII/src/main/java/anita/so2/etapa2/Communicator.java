/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package anita.so2.etapa2;

/**
 *
 * @author anita
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Communicator extends Thread {

    String defaultIP;
    int defaultPort;

    private static final byte SAQUE = Config.SAQUE;
    private static final byte DEPOSITO = Config.DEPOSITO;
    private static final byte LISTAR = Config.LISTAR;

    SocketChannel clientChannel = null;
    ServerSocketChannel serverChannel = null;
    InetSocketAddress channelAddress = null;
    BlockingQueue<ByteBuffer> incoming = new LinkedBlockingQueue<ByteBuffer>();
    public static Map<String, SocketChannel> clientSocketList;
    private boolean active;

    public Communicator(String defaultIP, int defaultPort) {
        this.defaultPort = defaultPort;
        this.defaultIP = defaultIP;
        boolean created = false;
        clientSocketList = new LinkedHashMap<String, SocketChannel>();

        while (!created) {
            try {
                serverChannel = ServerSocketChannel.open();
                channelAddress = new InetSocketAddress(this.defaultIP, this.defaultPort);
                serverChannel.socket().bind(channelAddress);
                created = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.active = true;
        this.start();
    }

    public Communicator() {

    }

    public void connectServer(String hostDescription) {
        try {
            String vet[] = hostDescription.split(":");
            String hostname = vet[0];
            int port = Integer.parseInt(vet[1].trim());
            clientChannel = SocketChannel.open(new InetSocketAddress(hostname, port));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        rodaListener();
    }

    public void run() {
        try {
            while (this.active) {
                try {
                    clientChannel = serverChannel.accept();
                    clientSocketList.put(clientRemoteChannelDesc(), clientChannel);
                    rodaListener();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rodaListener() {
        try {
            Listener l = new Listener(this.clientChannel, this.incoming);
            l.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SocketChannel getSocket() {
        // TODO Auto-generated method stub
        try {
            return clientChannel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String serverChannelDescription() {
        // TODO Auto-generated method stub
        try {
            String hostAddress = channelAddress.getAddress().getHostAddress();
            String portAddress = Integer.toString(channelAddress.getPort());
            return hostAddress + ":" + portAddress;
        } catch (Exception e) {
        }
        return null;
    }

    public String clientLocalChannelDesc() {
        // TODO Auto-generated method stub
        try {
            String hostAddress = clientChannel.socket().getLocalAddress().getHostAddress();
            String portAddress = Integer.toString(clientChannel.socket().getLocalPort());
            return hostAddress + ":" + portAddress;
        } catch (Exception e) {
        }
        return null;
    }

    public String clientRemoteChannelDesc() {
        // TODO Auto-generated method stub
        try {
            String hostAddress = clientChannel.socket().getInetAddress().getHostAddress();
            String portAddress = Integer.toString(clientChannel.socket().getPort());
            return hostAddress + ":" + portAddress;
        } catch (Exception e) {
        }
        return null;
    }

    public ByteBuffer receiveMessages() {
        // TODO Auto-generated method stub
        try {
            return incoming.take();
        } catch (InterruptedException e) {
            System.exit(1);
            return null;
        }
    }

    //comunicador para o saque feito apenas por usuairo contas 
    public void MsgSend_Saque(SocketChannel channel, int conta, float valorSaque) {
        try {
            int tamMsg = 2 + 4 + 4 + 4;
            ByteBuffer writeBuffer = ByteBuffer.allocateDirect(tamMsg);
            writeBuffer.putShort(Communicator.SAQUE);
            writeBuffer.putInt(tamMsg);
            writeBuffer.putInt(conta);
            writeBuffer.putFloat(valorSaque);
            writeBuffer.rewind();
            channelWrite(channel, writeBuffer);
        } catch (Exception e) {
            System.out.println("Erro no Saquue! " + e.getLocalizedMessage());
        }
    }
    //comunicador para listar ccontas de uma determinadaa agencia 
public void MsgSend_Extrato(SocketChannel channel, int conta) {
        int tamMsg = 2 + 4 + 4 + 4;
        ByteBuffer writeBuffer = ByteBuffer.allocateDirect(tamMsg);
        writeBuffer.putShort(Communicator.LISTAR);
        writeBuffer.putInt(tamMsg);
        writeBuffer.putInt(conta);
        writeBuffer.rewind();
        channelWrite(channel, writeBuffer);
    }
    public void MsgSend_Deposito(SocketChannel channel, int conta, float valorDeposito) {
        //comunicador para um DEPOSITO
        try {
            int tamMsg = 2 + 4 + 4 + 4;
            ByteBuffer writeBuffer = ByteBuffer.allocateDirect(tamMsg);
            writeBuffer.putShort(Communicator.DEPOSITO);
            writeBuffer.putInt(tamMsg);
            writeBuffer.putInt(conta);
            // writeBuffer.putInt(agencia);
            writeBuffer.putFloat(valorDeposito);
            writeBuffer.rewind();
            channelWrite(channel, writeBuffer);
        } catch (Exception e) {
            System.out.println("Erro " + e.getLocalizedMessage());
        }
    }

    public void channelWrite(SocketChannel channel, ByteBuffer writeBuffer) {
        // TODO Auto-generated method stub
        try {
            long nbytes = 0;
            long toWrite = writeBuffer.remaining();
            try {
                while (nbytes != toWrite) {
                    nbytes += channel.write(writeBuffer);

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    }
                }
            } catch (ClosedChannelException cce) {
                cce.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            writeBuffer.rewind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readString(ByteBuffer in) {
        // TODO Auto-generated method stub
        try {
            Charset charset = Charset.forName("ISO-8859-1");
            CharsetDecoder decoder = charset.newDecoder();
            ByteBuffer buf = ByteBuffer.allocate(in.capacity());
            byte b;
            b = in.get();
            while (b != 0) {
                buf.put(b);
                b = in.get();
            }
            buf.rewind();
            String s = decoder.decode(buf).toString();
            return s;
        } catch (CharacterCodingException e) {
            System.exit(1);
            return null;
        }
    }
}
