package example.com.furnitureproject.db.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import example.com.furnitureproject.db.bean.AccountBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ACCOUNT_BEAN".
*/
public class AccountBeanDao extends AbstractDao<AccountBean, Long> {

    public static final String TABLENAME = "ACCOUNT_BEAN";

    /**
     * Properties of entity AccountBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property TypeId = new Property(1, Long.class, "typeId", false, "TYPE_ID");
        public final static Property Type = new Property(2, String.class, "type", false, "TYPE");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property Price = new Property(4, float.class, "price", false, "PRICE");
        public final static Property Count = new Property(5, float.class, "count", false, "COUNT");
        public final static Property PicRes = new Property(6, int.class, "picRes", false, "PIC_RES");
        public final static Property Time = new Property(7, long.class, "time", false, "TIME");
        public final static Property PrimeCost = new Property(8, float.class, "primeCost", false, "PRIME_COST");
        public final static Property Note = new Property(9, String.class, "note", false, "NOTE");
    }


    public AccountBeanDao(DaoConfig config) {
        super(config);
    }
    
    public AccountBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ACCOUNT_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TYPE_ID\" INTEGER," + // 1: typeId
                "\"TYPE\" TEXT," + // 2: type
                "\"NAME\" TEXT," + // 3: name
                "\"PRICE\" REAL NOT NULL ," + // 4: price
                "\"COUNT\" REAL NOT NULL ," + // 5: count
                "\"PIC_RES\" INTEGER NOT NULL ," + // 6: picRes
                "\"TIME\" INTEGER NOT NULL ," + // 7: time
                "\"PRIME_COST\" REAL NOT NULL ," + // 8: primeCost
                "\"NOTE\" TEXT);"); // 9: note
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ACCOUNT_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AccountBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long typeId = entity.getTypeId();
        if (typeId != null) {
            stmt.bindLong(2, typeId);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(3, type);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        stmt.bindDouble(5, entity.getPrice());
        stmt.bindDouble(6, entity.getCount());
        stmt.bindLong(7, entity.getPicRes());
        stmt.bindLong(8, entity.getTime());
        stmt.bindDouble(9, entity.getPrimeCost());
 
        String note = entity.getNote();
        if (note != null) {
            stmt.bindString(10, note);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AccountBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long typeId = entity.getTypeId();
        if (typeId != null) {
            stmt.bindLong(2, typeId);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(3, type);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        stmt.bindDouble(5, entity.getPrice());
        stmt.bindDouble(6, entity.getCount());
        stmt.bindLong(7, entity.getPicRes());
        stmt.bindLong(8, entity.getTime());
        stmt.bindDouble(9, entity.getPrimeCost());
 
        String note = entity.getNote();
        if (note != null) {
            stmt.bindString(10, note);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public AccountBean readEntity(Cursor cursor, int offset) {
        AccountBean entity = new AccountBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // typeId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // type
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.getFloat(offset + 4), // price
            cursor.getFloat(offset + 5), // count
            cursor.getInt(offset + 6), // picRes
            cursor.getLong(offset + 7), // time
            cursor.getFloat(offset + 8), // primeCost
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // note
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AccountBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTypeId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setType(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPrice(cursor.getFloat(offset + 4));
        entity.setCount(cursor.getFloat(offset + 5));
        entity.setPicRes(cursor.getInt(offset + 6));
        entity.setTime(cursor.getLong(offset + 7));
        entity.setPrimeCost(cursor.getFloat(offset + 8));
        entity.setNote(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(AccountBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(AccountBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AccountBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
