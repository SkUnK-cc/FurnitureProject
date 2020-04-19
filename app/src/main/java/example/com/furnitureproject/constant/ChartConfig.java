package example.com.furnitureproject.constant;

import android.graphics.Color;

import java.util.List;

import example.com.furnitureproject.view.chart.bean.Line;
import example.com.furnitureproject.view.chart.bean.PointValue;
import example.com.furnitureproject.view.chart.bean.SlidingLine;
import example.com.furnitureproject.view.chart.support.ShowMode;

/**
 * @author luo
 * @date 2017/10/30
 */
public class ChartConfig {
    /**
     * 选中线条配置
     */
    public static SlidingLine getSlideingLine() {
        SlidingLine slidingLine = new SlidingLine();
        slidingLine.setSlideLineColor(Color.parseColor("#508a74"))
                .setSlidePointColor(Color.parseColor("#11a06a"))
                .setSlidePointRadius(4);
        slidingLine.setDash(true);
        slidingLine.setOpenSlideSelect(true);
        return slidingLine;
    }

    /**
     *
     */
    public static Line getLine(List<PointValue> pointValues) {
        Line line = new Line(pointValues);
        line.setLineColor(Color.parseColor("#40846d"))
                .setLineWidth(1f)
                .setPointColor(Color.parseColor("#11a06a"))
                .setCubic(false)
                .setPointRadius(3)
                .setFill(true)
                .setHasPoints(true)
                .setFillColor(Color.parseColor("#cc40846d")) //80%不透明度
                .setHasLabels(true)
                .setLabelColor(Color.parseColor("#11a06a"))
                .setLabelRadius(12)
                .setShowMode(ShowMode.PADDING)
                .setLeftModePaddingY(0f)
                .setRightModePaddingY(0f);
        return line;
    }
}
