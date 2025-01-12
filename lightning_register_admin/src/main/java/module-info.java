module lightning_productivity {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
	requires jdk.httpserver;
	requires com.google.api.client;
	requires com.google.api.client.json.jackson2;
	requires com.google.api.client.auth;
	requires com.google.api.client.extensions.java6.auth;
	requires com.google.api.client.extensions.jetty.auth;
	requires google.api.client;
	requires google.api.services.gmail.v1.rev110;
	requires google.api.services.sheets.v4.rev614;
	requires com.google.zxing;
	requires com.google.zxing.javase;
	requires com.google.common;
	requires java.mail;
	requires java.desktop;
	requires org.apache.xmlgraphics.batik.codec;
	requires org.apache.xmlgraphics.batik.transcoder;
    opens lightning_productivity to javafx.fxml;

    exports lightning_productivity;
}
