module pt4.flotsblancs {
	requires transitive static javafx.base;
	requires transitive static javafx.controls;
	requires transitive static javafx.graphics;

	requires transitive static ormlite.jdbc;
	requires transitive static java.sql;
	requires transitive static dotenv.java;

	requires transitive static MaterialFX;
	requires transitive static static lombok;
	requires transitive static javafaker;
	requires transitive static virtualizedfx;
	requires transitive static itextpdf;
	
	opens pt4.flotsblancs to javafx.graphics,ormlite.jdbc;
	opens pt4.flotsblancs.database to javafx.graphics,ormlite.jdbc;

	requires transitive static org.kordamp.ikonli.core;
	requires transitive static org.kordamp.ikonli.fontawesome5;
	requires transitive static org.kordamp.ikonli.javafx;

	opens pt4.flotsblancs.database.model to ormlite.jdbc;

	exports pt4.flotsblancs;
	exports pt4.flotsblancs.database;
	exports pt4.flotsblancs.database.model;
	exports pt4.flotsblancs.database.model.types;
}
