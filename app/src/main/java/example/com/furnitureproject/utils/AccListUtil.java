package example.com.furnitureproject.utils;

import java.util.ArrayList;
import java.util.List;

import example.com.furnitureproject.db.bean.AccountBean;

/**
 * @author luo
 * @date 2017/11/1
 */
public class AccListUtil {

    /**
     * count求和
     * @param list
     * @return
     */
    public static float sum(List<AccountBean> list) {
        float sum = 0f;
        if (list != null && list.size() > 0) {
            for (AccountBean accountModel : list) {
                sum += accountModel.getPrice()*accountModel.getCount();
            }
        }
        return sum;
    }

    /**
     * list count列最大值
     * @param list
     * @return
     */
    public static float max(List<AccountBean> list) {
//        return Collections.max(list).getCount();
        float max = 0f;
        if (list != null && list.size() > 0) {
            for (AccountBean accountModel : list) {
                max = Math.max(max, accountModel.getPrice()*accountModel.getCount());
            }
        }
        return max;
    }


    public static List<AccountBean> removeRepeat(List<AccountBean> accList){
        if (accList != null && accList.size() > 0) {
            List<AccountBean> list = new ArrayList<>(accList);
            for (int i = 0; i < list.size()-1; i++) {
                for (int j = list.size()-1; j > i; j--) {
                    if (list.get(j).getName().equals(list.get(i).getName())) {
                        list.remove(j);
                    }
                }
            }
            return list;
        } else
            return new ArrayList<>();

    }
}
