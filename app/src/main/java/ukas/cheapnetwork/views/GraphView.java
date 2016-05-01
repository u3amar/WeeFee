package ukas.cheapnetwork.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.text.NumberFormat;
import java.util.List;

import ukas.cheapnetwork.R;
import ukas.cheapnetwork.models.GraphNode;
import ukas.cheapnetwork.models.NetworkNode;

/**
 * Created by usama on 4/30/16.
 */
public class GraphView extends View {
    private final String TAG = "GraphView";
    private final float NODE_RADIUS = 150,
            MAX_DISTANCE = 500,
            MIN_DISTANCE = 100,
            ACCEL_FACTOR = -.005f;

    private Paint mNodePaint, mConnectionPaint, mTextPaint;
    private NetworkNode<GraphNode> mRootNode;
    private float mWidth = -1, mHeight = -1;

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

    private void init() {
        mNodePaint = new Paint();
        mNodePaint.setColor(Color.rgb(255, 0, 0));

        mConnectionPaint = new Paint();
        mConnectionPaint.setColor(Color.BLACK);
        mConnectionPaint.setStrokeWidth(5.0f);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(45.0f);

        mRootNode = new NetworkNode<>(null);
        NumberFormat.getInstance().setMaximumFractionDigits(3);
    }

    public void addNode(long bytesTransferred) {
        GraphNode data = new GraphNode((float) Math.random() * mWidth, (float) Math.random() * mHeight, 50);
        data.setColor(getResources().getColor(R.color.colorAccent));
        data.setAccelX(ACCEL_FACTOR);
        data.setAccelY(ACCEL_FACTOR);
        data.setData(bytesTransferred);

        NetworkNode<GraphNode> child = new NetworkNode<>(mRootNode);
        child.setData(data);
        mRootNode.addChild(child);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mWidth == -1) {
            mWidth = canvas.getWidth();
            mHeight = canvas.getHeight();
            initRootNode();
        }

        for (NetworkNode<GraphNode> childNode : mRootNode.getChildren()) {
            drawNode(childNode, canvas);
        }

        drawNode(mRootNode, canvas);
    }

    private void drawNode(NetworkNode<GraphNode> node, Canvas canvas) {
        GraphNode graphNode = node.getData();
        mNodePaint.setColor(graphNode.getColor());

        GraphNode rootNode = mRootNode.getData();
        canvas.drawLine(graphNode.getX(), graphNode.getY(), rootNode.getX(),
                rootNode.getY(), mConnectionPaint);

        float xDist = graphNode.getX() - rootNode.getX();
        float yDist = graphNode.getY() - rootNode.getY();
        float dist = (float) Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));

        float velX = 0.0f, velY = 0.0f;
        if (dist > MAX_DISTANCE) {
            velX -= xDist * .005f;
            velY -= yDist * .005f;
        } else if (dist < MIN_DISTANCE) {
            velX += xDist * .005f;
            velY += yDist * .005f;
        } else {
            velX -= graphNode.getAccelX();
            velY -= graphNode.getAccelY();
        }

        if (isOverlapping(graphNode)) {
            velX *= -Math.random() * 50;
            velY *= -Math.random() * 50;
        }

        graphNode.setVelX(velX);
        graphNode.setVelY(velY);

        float newX = graphNode.getX() + graphNode.getVelX();
        float newY = graphNode.getY() + graphNode.getVelY();

        graphNode.setX(newX);
        graphNode.setY(newY);
        canvas.drawCircle(graphNode.getX(), graphNode.getY(), NODE_RADIUS, mNodePaint);

        Rect bounds = new Rect();
        String transferredData = NumberFormat.getInstance().format(getTransferredMB(graphNode.getData())) + " MB";
        mTextPaint.getTextBounds(transferredData, 0, transferredData.length(), bounds);

        float dataX = graphNode.getX() - bounds.width() / 2;
        float dataY = graphNode.getY() + bounds.height() / 2;
        canvas.drawText(transferredData, dataX, dataY, mTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float cx = event.getX();
        float cy = event.getY();

        if (isTouchRoot(cx, cy)) {
            GraphNode rootNode = mRootNode.getData();
            rootNode.setX(cx);
            rootNode.setY(cy);
            invalidate();
        }

        return true;
    }

    private void initRootNode() {
        GraphNode rootData = new GraphNode(mWidth / 2, mHeight / 2, NODE_RADIUS);
        rootData.setColor(getResources().getColor(R.color.colorPrimary));
        mRootNode.setData(rootData);
    }

    private boolean isTouchRoot(double x, double y) {
        GraphNode rootNode = mRootNode.getData();
        double cx = rootNode.getX();
        double cy = rootNode.getY();
        double width = rootNode.getRadius();
        return x > cx - width && x < cx + width && y > cy - width && y < cy + width;
    }

    private boolean isOverlapping(GraphNode graphNode) {
        List<NetworkNode<GraphNode>> childNodes = mRootNode.getChildren();
        for (NetworkNode<GraphNode> n : childNodes) {
            if (!n.getData().equals(graphNode)) {
                GraphNode item = n.getData();
                if (item.doesOverlap(graphNode)) {
                    return true;
                }
            }
        }

        return false;
    }

    private double getTransferredMB(long bytes) {
        return bytes / (1024.0 * 1024.0);
    }
}
