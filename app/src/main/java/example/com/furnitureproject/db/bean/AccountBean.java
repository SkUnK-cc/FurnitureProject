package example.com.furnitureproject.db.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import example.com.furnitureproject.R;

@Entity
public class AccountBean implements Comparable<AccountBean>,Parcelable{

    public static final String TYPE_ALL = "all";
    public static final String TYPE_INCOME_SELL = "出售";
    public static final String TYPE_PAYOUT = "所有支出";
    public static final String TYPE_PAY_STOCK = "进货";
    public static final String TYPE_PAY_OTHER = "其他支出";

    public static final String NAME_ALL = "nameAll";

    @Id(autoincrement = true)
    private Long id;

    private Long typeId;

    private String type;

    private String name;

    private float price;        // 售货时，作为出售价格，进货时，作为支出价格

    private long count = 1;

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

    @Generated(hash = 2047453385)
    public AccountBean(Long id, Long typeId, String type, String name, float price, long count, int picRes, long time,
            float primeCost, String note) {
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

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
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
        if(type==null || type.equals(""))return picRes;
        if(type.equals(TYPE_INCOME_SELL)){
            return R.drawable.ic_income;
        }else if (type.equals(TYPE_PAY_STOCK) || type.equals(TYPE_PAY_OTHER)){
            return R.drawable.ic_payout;
        }
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.typeId);
        dest.writeString(this.type);
        dest.writeString(this.name);
        dest.writeFloat(this.price);
        dest.writeLong(this.count);
        dest.writeInt(this.picRes);
        dest.writeLong(this.time);
        dest.writeFloat(this.primeCost);
        dest.writeString(this.note);
    }

    protected AccountBean(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.typeId = (Long) in.readValue(Long.class.getClassLoader());
        this.type = in.readString();
        this.name = in.readString();
        this.price = in.readFloat();
        this.count = in.readLong();
        this.picRes = in.readInt();
        this.time = in.readLong();
        this.primeCost = in.readFloat();
        this.note = in.readString();
    }

    public static final Creator<AccountBean> CREATOR = new Creator<AccountBean>() {
        @Override
        public AccountBean createFromParcel(Parcel source) {
            return new AccountBean(source);
        }

        @Override
        public AccountBean[] newArray(int size) {
            return new AccountBean[size];
        }
    };
}
