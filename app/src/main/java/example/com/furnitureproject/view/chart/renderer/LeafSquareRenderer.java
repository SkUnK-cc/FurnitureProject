package example.com.furnitureproject.view.chart.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import java.util.List;

import example.com.furnitureproject.view.chart.bean.Axis;
import example.com.furnitureproject.view.chart.bean.PointValue;
import example.com.furnitureproject.view.chart.bean.Square;
import example.com.furnitureproject.view.chart.support.LeafUtil;

/**
 * Created by chenliu on 2017/1/10.<br/>
 * 描述：
 * </br>
 */

public class LeafSquareRenderer extends AbsRenderer {

    public LeafSquareRenderer(Context context, View view) {
        super(context, view);
    }

    public void drawSquares(Canvas canvas, Square square, Axis axisX) {
        if (square != null) {
            //1.画直方图边界
            linePaint.setColor(square.getBorderColor());
            if(!square.isFill()){
                linePaint.setStrokeWidth(LeafUtil.dp2px(mContext, square.getBorderWidth()));
                linePaint.setStyle(Paint.Style.STROKE);
            }
            List<PointValue> values = square.getValues();
            float width = LeafUtil.dp2px(mContext, square.getWidth());
            for (PointValue point : values) {
                RectF rectF = new RectF(point.getOriginX() - width / 2,
                        point.getOriginY(), point.getOriginX() + width / 2, axisX.getStartY());

                canvas.drawRect(rectF, linePaint);
            }
        }
    }


}
