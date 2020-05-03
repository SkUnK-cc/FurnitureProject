package example.com.furnitureproject.db;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import example.com.furnitureproject.db.bean.AccountBean;
import example.com.furnitureproject.utils.Logger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class DatabaseDump {

    public static Observable<String> dumpAccountToFile(final String fileName) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                /**Sheet表, Excel表中的底部的表名*/
                WritableSheet mWritableSheet;
                /**Excel工作簿*/
                WritableWorkbook mWritableWorkbook;
                // 输出Excel的路径
                File file = new File(Environment.getExternalStorageDirectory(), fileName);
                // 新建一个文件
                try {
                    if(file.exists()){
                        file.delete();
                        file.createNewFile();
                    }
                    OutputStream os = new FileOutputStream(file);
                    // 创建Excel工作簿
                    mWritableWorkbook = Workbook.createWorkbook(os);
                    // 创建Sheet表
                    mWritableSheet = mWritableWorkbook.createSheet("第一张工作表", 0);
                    Logger.e("DatabaseDump", file.getAbsolutePath());
                    List<AccountBean> list = DbHelper.INSTANCE.getAccountManager().getAccountList();
                    insertTitle(mWritableSheet);
                    int row = 1;
                    for (int i = 0; i < list.size(); i++,row++) {
                        AccountBean item = list.get(i);
                        Number id = new Number(0, row, item.getId());
                        mWritableSheet.addCell(id);
                        Label type = new Label(1, row, item.getType());
                        mWritableSheet.addCell(type);
                        Label name = new Label(2, row, item.getName());
                        mWritableSheet.addCell(name);
                        Number typeId = new Number(3, row, item.getTypeId());
                        mWritableSheet.addCell(typeId);
                        Number price = new Number(4, row, item.getPrice());
                        mWritableSheet.addCell(price);
                        Number count = new Number(5, row, item.getCount());
                        mWritableSheet.addCell(count);
                        Number primeCost = new Number(6, row, item.getPrimeCost());
                        mWritableSheet.addCell(primeCost);
                        Label note = new Label(7, row, item.getNote());
                        mWritableSheet.addCell(note);
                    }
                    // 写入数据
                    mWritableWorkbook.write();
                    // 关闭文件
                    mWritableWorkbook.close();
                    emitter.onNext("写入成功");
                } catch (Exception e) {
                    emitter.onNext("写入失败");
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private static void insertTitle(WritableSheet mWritableSheet) throws WriteException {
        Label id = new Label(0, 0, "id");
        mWritableSheet.addCell(id);
        Label ipType = new Label(1, 0, "收支类型");
        mWritableSheet.addCell(ipType);
        Label name = new Label(2, 0, "类型");
        mWritableSheet.addCell(name);
        Label nameId = new Label(3, 0, "类型Id");
        mWritableSheet.addCell(nameId);
        Label price = new Label(4, 0, "价格");
        mWritableSheet.addCell(price);
        Label count = new Label(5, 0, "数量");
        mWritableSheet.addCell(count);
        Label primeCost = new Label(6, 0, "成本");
        mWritableSheet.addCell(primeCost);
        Label note = new Label(7, 0, "备注");
        mWritableSheet.addCell(note);
    }

}
