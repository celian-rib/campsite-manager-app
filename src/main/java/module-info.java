module pt4.flotsblancs {
	
	requires org.kordamp.ikonli.fontawesome5;
	requires org.kordamp.ikonli.javafx;
	requires org.kordamp.ikonli.core;

	requires javafx.base;
	requires javafx.controls;
	requires transitive javafx.graphics;

	requires ormlite.jdbc;
	requires java.sql;
	requires dotenv.java;

	requires MaterialFX;
	requires lombok;
	requires javafaker;
	requires virtualizedfx;
	requires itextpdf;
	
	opens pt4.flotsblancs to javafx.graphics,ormlite.jdbc;
	opens pt4.flotsblancs.database to javafx.graphics,ormlite.jdbc;

	opens pt4.flotsblancs.database.model to ormlite.jdbc;

	exports pt4.flotsblancs;
	exports pt4.flotsblancs.database;
	exports pt4.flotsblancs.database.model;
	exports pt4.flotsblancs.database.model.types;
}
