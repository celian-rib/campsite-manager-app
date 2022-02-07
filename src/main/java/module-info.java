module pt4.flotsblancs {

	requires javafx.base;
	requires javafx.controls;
	requires ormlite.jdbc;
	requires java.sql;
	requires dotenv.java;
	requires MaterialFX;
	
	opens pt4.flotsblancs to javafx.graphics,ormlite.jdbc;
	
	exports pt4.flotsblancs;
}