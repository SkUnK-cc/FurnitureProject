package example.com.furnitureproject.db.bean;

import com.contrarywind.interfaces.IPickerViewData;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

/**
 * 商品类型种类 Bean 比如 椅子1 椅子2 椅子3
 */
@Entity
public class DetailTypeBean implements IPickerViewData {

    public static final String TYPE_ALL = "all";
    public static final String TYPE_COMMODITY = "商品";
    public static final String TYPE_PAY_OTHER = "其他支出";

    @Id(autoincrement = true)
    private Long id;

    private String type;

    private String name;

    private Date time;

    // 成本
    private float primeCost;

    // 库存
    private int stockCount;

    private String note;

    public DetailTypeBean(){

    }

    @Generated(hash = 9408859)
    public DetailTypeBean(Long id, String type, String name, Date time, float primeCost, int stockCount, String note) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.time = time;
        this.primeCost = primeCost;
        this.stockCount = stockCount;
        this.note = note;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrimeCost() {
        return primeCost;
    }

    public void setPrimeCost(float primeCost) {
        this.primeCost = primeCost;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    @Override
    public String getPickerViewText() {
        if(type.equals(TYPE_COMMODITY)) {
            return name + ",成本 " + primeCost + ",库存 " + stockCount;
        } else if (type.equals(TYPE_PAY_OTHER)) {
            return name;
        }
        return name;
    }
}
