module pt4.flotsblancs {

	requires javafx.base;
	requires javafx.controls;
	requires ormlite.jdbc;
	requires java.sql;
	
	opens pt4.flotsblancs to javafx.graphics;
}