package pt4.flotsblancs.orm.model;

import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.field.DatabaseField;

import lombok.*;

@DatabaseTable(tableName = "stock_bills")
public class StockBill {

    @Getter
    @DatabaseField(columnName = "stock_id")
    private int stockId;

    @Getter
    @DatabaseField(columnName = "bill_id")
    private int billId;

    public StockBill() {}
}
