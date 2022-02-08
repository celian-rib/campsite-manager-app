module pt4.flotsblancs {
	requires javafx.base;
	requires javafx.controls;
	requires transitive javafx.graphics;

	requires ormlite.jdbc;
	requires java.sql;
	requires dotenv.java;
	
	requires MaterialFX;
	requires lombok;
	
	opens pt4.flotsblancs to javafx.graphics,ormlite.jdbc;
	opens pt4.flotsblancs.orm to javafx.graphics,ormlite.jdbc;
	opens pt4.flotsblancs.orm.model to ormlite.jdbc;
	
	exports pt4.flotsblancs;
	exports pt4.flotsblancs.orm;
	exports pt4.flotsblancs.orm.model;
	requires junit;
}