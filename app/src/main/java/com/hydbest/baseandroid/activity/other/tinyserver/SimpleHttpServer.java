package com.hydbest.baseandroid.activity.other.tinyserver;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleHttpServer {

    private static WebConfiguration mWebConfiguration;

    private ExecutorService mThreadPool;
    private volatile boolean mIsEnable;
    private int mCurrentParallels;
    private ServerSocket mServerSocket;
    private Set<IResourceHandler> mResourceHandler;

    public static final class Holder {
        private static final SimpleHttpServer SERVER = new SimpleHttpServer();
    }

    private SimpleHttpServer() {
        mThreadPool = Executors.newCachedThreadPool();
        mResourceHandler = new HashSet<>();
    }

    public static SimpleHttpServer initConfig(WebConfiguration webConfiguration) {
        SimpleHttpServer server = getInstance();
        mWebConfiguration = webConfiguration;
        return server;
    }

    public static SimpleHttpServer getInstance() {
        return Holder.SERVER;
    }

    public void startAsync() {
        mIsEnable = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                doProcAsync();
            }
        }).start();
    }

    public void stopAsync() {
        if (!mIsEnable) {
            return;
        }
        mIsEnable = false;
        try {
            mServerSocket.close();
            mServerSocket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doProcAsync() {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(mWebConfiguration.getPort());
            mServerSocket = new ServerSocket();
            mServerSocket.bind(socketAddress);
            while (mIsEnable && mCurrentParallels <= mWebConfiguration.getMaxParallels()) {
                final Socket socket = mServerSocket.accept();
                mThreadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        onAcceptRemotePeer(socket);
                    }
                });
            }
        } catch (IOException e) {
            stopAsync();
            e.printStackTrace();
        }
    }

    public void registerResourceHandler(IResourceHandler handler) {
        mResourceHandler.add(handler);
    }

    private void onAcceptRemotePeer(Socket socket) {
        try {
            //socket.getOutputStream().write("客户端发来贺电".getBytes());
            synchronized (this) {
                mCurrentParallels++;
            }
            Log.i("csz","mCurrentParallels:"+mCurrentParallels);
            HttpContext httpContext = new HttpContext();
            httpContext.setUnderlySocket(socket);
            InputStream inputStream = socket.getInputStream();
            String line = null;
            String url = StreamUtil.readLine(inputStream);//GET /static/jd.htm HTTP/1.1
            String uri = url.split(" ")[1];
            while (!TextUtils.isEmpty(line = StreamUtil.readLine(inputStream))) {
                if ("\r\n".equals(line)) break;
                String[] pair = line.split(": ");
                httpContext.addRequestHeader(pair[0], pair[1]);
            }

            for (IResourceHandler handler : mResourceHandler) {
                if (handler.accept(uri)) {
                    handler.handle(uri, httpContext);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            synchronized (this) {
                mCurrentParallels--;
            }
            Log.i("csz","mCurrentParallels:"+mCurrentParallels);
            if (!socket.isClosed()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
