package example.com.furnitureproject.view.chart.support;


import example.com.furnitureproject.view.chart.bean.Axis;

/**
 * Created by chenliu on 2016/7/15.<br/>
 * 描述：
 * </br>
 */
public interface Chart {

    void setAxisX(Axis axisX);

    void setAxisY(Axis axisY);

    Axis getAxisX();

    Axis getAxisY();
}
