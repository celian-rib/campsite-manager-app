module pt4.flotsblancs {
	requires javafx.base;
	requires javafx.controls;
	requires transitive javafx.graphics;

	requires ormlite.jdbc;
	requires java.sql;
	requires dotenv.java;
	
	requires AppleJavaExtensions;
	requires MaterialFX;
	requires lombok;
	
	opens pt4.flotsblancs to javafx.graphics,ormlite.jdbc;
	opens pt4.flotsblancs.database to javafx.graphics,ormlite.jdbc;
	opens pt4.flotsblancs.database.model to ormlite.jdbc;
	
	exports pt4.flotsblancs;
	exports pt4.flotsblancs.database;
	exports pt4.flotsblancs.database.model;
}