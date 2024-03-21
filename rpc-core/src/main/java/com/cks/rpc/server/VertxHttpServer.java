package com.cks.rpc.server;

import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer{
    public void doStart(int port){
        Vertx vertx = Vertx.vertx();
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        server.requestHandler(new HttpServerHandler());

        server.listen(port, httpServerAsyncResult -> {
           if(httpServerAsyncResult.succeeded()){
               System.out.println("Server is listening");
           } else {
               System.err.println("Failed");
           }
        });
    }
}
