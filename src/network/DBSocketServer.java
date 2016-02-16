/*
 * DBSocketServer.java    version 1.0   date 16/12/2015
 * By rjb 
  */

package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * DBSocketServer passes socket client requests to the database. 
 * The class receives parameters in
 * <code>DBCommand</code> objects and returns results in
 * <code>DBResult</code> objects.
 *
 * @author rjb
 */
public class DBSocketServer extends Thread {
    private String dbLocation = null;
    private int port = 3000;

    /**
     * Starts the socket server.
     *
     * @param argv Command line arguments.
     */
    public static void main(String[] argv) {
        register(".", 3000);
    }

    /**
     * Registers a socket server, listening on the specified port, accessing
     * the specified data file.
     *
     * @param dbLocation the location of the data file on disk.
     * @param port the port to listen on.
     */
    public static void register(String dbLocation, int port) {
        new DBSocketServer(dbLocation, port).start();
    }

    /**
     * Creates a socket server, listening on the specified port, accessing
     * the specified data file.
     *
     * @param dbLocation the location of the data file on disk.
     * @param port the port to listen on.
     */
    public DBSocketServer(String dbLocation, int port) {
        this.dbLocation = dbLocation;
        this.port = port;
    }

    /**
     * Listens for connections, handling any errors.
     */
    public void run() {
        try {
            listenForConnections();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Listens for new client connections, creating a new thread to handle the
     * requests.
     *
     * @throws IOException on network error.
     */
    public void listenForConnections() throws IOException {
        ServerSocket aServerSocket = new ServerSocket(port);
        //block for 60,000 msecs or 1 minute
        aServerSocket.setSoTimeout(60000);

        while (true) {
            Socket aSocket = aServerSocket.accept();
            DBSocketRequest request = new DBSocketRequest(dbLocation, aSocket);
            request.start();
        }
    }
}

