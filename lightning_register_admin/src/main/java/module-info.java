module lightning_productivity {
	requires org.apache.xmlgraphics.batik.constants;
	requires com.google.api.client;
	requires com.google.api.client.auth;
	requires com.google.api.client.extensions.java6.auth;
	requires com.google.api.client.extensions.jetty.auth;
	requires com.google.api.client.json.jackson2;
	requires com.google.common;
	requires com.google.zxing;
	requires com.google.zxing.javase;
	requires google.api.client;
	requires java.desktop;
	requires java.mail;
	requires java.xml;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires jdk.httpserver;
	requires org.apache.xmlgraphics.batik.codec;
	requires org.apache.xmlgraphics.batik.transcoder;
	requires org.apache.xmlgraphics.batik.anim;
	requires org.apache.xmlgraphics.batik.gvt;
	requires org.apache.xmlgraphics.batik.brdige;
	requires org.apache.xmlgraphics.batik.awt.util;
	requires org.apache.xmlgraphics.batik.util;
	requires org.apache.xmlgraphics.batik.dom;
	requires org.apache.xmlgraphics.batik.css;
	requires org.apache.xmlgraphics.batik.svgdom;
	requires xml.apis.ext;
	requires axiom.api;
	requires jdk.xml.dom;
	requires com.google.auth.oauth2;
	requires com.google.auth;
	requires com.google.api.client.json.gson;

	opens lightning_productivity to javafx.fxml;

	exports lightning_productivity;
}
