package ukas.cheapnetwork.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import ukas.cheapnetwork.models.NetworkNode;

/**
 * Created by usama on 4/30/16.
 */
public class GraphView extends View {
    private final int NODE_RADIUS = 150;

    private Paint mPaint;
    private NetworkNode<Integer> mRootNode;

    public GraphView(Context context) {
        super(context);
        init();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.rgb(255, 0, 0));

        mRootNode = new NetworkNode<>(null);

        for (int i = 0; i < 10; i++) {
            NetworkNode<Integer> c1 = new NetworkNode<>(mRootNode);
            c1.setData(i);
            mRootNode.addChild(c1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, NODE_RADIUS, mPaint);
    }
}
