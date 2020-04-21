package example.com.furnitureproject.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class AccountBean implements Comparable<AccountBean>{

    public static final String TYPE_ALL = "all";
    public static final String TYPE_INCOME_SELL = "出售";
    public static final String TYPE_PAY_STOCK = "进货";
    public static final String TYPE_PAY_OTHER = "其他支出";

    public static final String NAME_ALL = "nameAll";

    @Id(autoincrement = true)
    private Long id;

    private Long typeId;

    private String type;

    private String name;

    private float price;        // 售货时，作为出售价格，进货时，作为支出价格

    private float count;

    private int picRes;

    private long time;

    private float primeCost;    //成本

    private String note;

    public AccountBean(){

    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Generated(hash = 7742413)
    public AccountBean(Long id, Long typeId, String type, String name, float price, float count, int picRes, long time, float primeCost,
            String note) {
        this.id = id;
        this.typeId = typeId;
        this.type = type;
        this.name = name;
        this.price = price;
        this.count = count;
        this.picRes = picRes;
        this.time = time;
        this.primeCost = primeCost;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
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

    public int getPicRes() {
        return picRes;
    }

    public void setPicRes(int picRes) {
        this.picRes = picRes;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public float getPrimeCost() {
        return primeCost;
    }

    public void setPrimeCost(float primeCost) {
        this.primeCost = primeCost;
    }

    @Override
    public int compareTo(AccountBean o) {
        if (this.price < o.price)
            return -1;
        else if (this.price > o.price)
            return 1;
        else
            return 0;
    }

}
