package com.animoto.android.dgv;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.animoto.android.dgvSample.DraggableGridViewAdapter;
import com.animoto.android.dgvSample.GridItem;

import java.util.ArrayList;
import java.util.HashSet;

public class DraggableGridView extends AdapterView<DraggableGridViewAdapter> implements
View.OnTouchListener, OnItemClickListener, View.OnLongClickListener {
    // layout vars
    private static final int TOP_ROW_PADDING = 4;
    private static final int BOTTOM_ROW_PADDING = 4;

    public static String LOG_TAG = "dgv";
    public static float childRatio = .9f;
    protected int colCount, childSize, padding, dpi = 0;
    protected float lastDelta = 0;
    protected Handler handler = new Handler();
    // dragging vars
    protected int dragged = -1, lastX = -1, lastY = -1, lastTarget = -1;
    protected boolean enabled = true, touching = false;
    // anim vars
    public static int animT = 150;
    protected ArrayList<Integer> newPositions = new ArrayList<Integer>();
    // listeners
    protected OnRearrangeListener onRearrangeListener;
    private OnItemClickListener onItemClickListener;

    protected DraggableGridViewAdapter mAdapter;

    // CONSTRUCTOR AND HELPERS
    public DraggableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
        setOnLongClickListener(this);
        setChildrenDrawingOrderEnabled(true);

        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
        .getMetrics(metrics);
        dpi = metrics.densityDpi;
        Log.i(LOG_TAG, "finished creating DraggableGridView widget");
    }

    /*******************************************************************************************************
     * Adapter view overrides
     */

    @Override
    public void setAdapter(DraggableGridViewAdapter adapter) {
        mAdapter = adapter;
        onRearrangeListener = adapter;
        requestLayout();
    }

    @Override
    public DraggableGridViewAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public View getSelectedView() {
        return null;
    }

    @Override
    public void setSelection(int position) {

    }

    // OVERRIDES
    @Override
    public void addView(View child) {
        super.addView(child);
        newPositions.add(-1);
    };

    @Override
    public void removeViewAt(int index) {
        super.removeViewAt(index);
        newPositions.remove(index);
    };

    protected void removeAndRecycleCell(View childCell) {
        this.removeViewInLayout(childCell);
    }

    @Override
    public boolean addViewInLayout(View child, int index, LayoutParams params) {
        newPositions.add(-1);
        return super.addViewInLayout(child, index, params);
    }

    // LAYOUT
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // compute width of view, in dp
        float w = (r - l) / (dpi / 160f); // 160 is standard dpi and represents
        // conversion rate from google.

        // determine number of columns, at least 2
        colCount = 2;
        int sub = 240; // 240 dip units will be taken up by each column.
        w -= 280; // assuming each column starts off taking 140 dip.
        while (w > 0) {
            colCount++;
            w -= sub;
            sub += 40;
        }

        // determine childSize and padding, in px
        childSize = (r - l) / colCount;
        childSize = Math.round(childSize * childRatio);
        padding = ((r - l) - (childSize * colCount)) / (colCount + 1);

        int rowHeight = childSize + 2 * padding;
        int topRow = 0;
        int dgvHeight = this.getHeight();
        int bottomRow = 4;
        // Log.i("dgv", "Row Height = " + rowHeight + "; Top Row = " + topRow +
        // "; View Heigh = + " + dgvHeight + "; bottomRow = " + bottomRow);

        int topRowLoaded = topRow; // the top row loaded, including any padding
        // of rows at the beginning.
        int bottomRowLoaded = bottomRow; // Will also include any padding later
        // for perf.
        int firstCellPosition = Math.max(0, (topRowLoaded - TOP_ROW_PADDING)
                * colCount);
        int finalCellPosition = Math.min(mAdapter.getCount() - 1,
                (bottomRowLoaded + BOTTOM_ROW_PADDING) * colCount);

        HashSet addedPositions = new HashSet();

        int totalAvailableCells = mAdapter.getCount();

        //Loop through all the children; relayout the ones that should stay; remove the ones that are no longer in our range.
        for (int i = 0; i < this.getChildCount(); i++) {
            View child = getChildAt(i);
            int childPositionInData = -1;

            childPositionInData = ((GridItem)child).getPositionInData();


            if (childPositionInData == dragged)
                continue;

            if (childPositionInData >= firstCellPosition && childPositionInData <= finalCellPosition) {
                Point xy = getCoorFromIndex(childPositionInData); //This probably needs to be renormalized based on what is being shown.
                child.measure(MeasureSpec.makeMeasureSpec(child.getLayoutParams().width, MeasureSpec.UNSPECIFIED),
                        MeasureSpec.makeMeasureSpec(child.getLayoutParams().height, MeasureSpec.UNSPECIFIED));

                child.layout(xy.x, xy.y, xy.x + childSize, xy.y + childSize); //view group will layout the children based on the sizes determined for available columns
                child.invalidate();
                addedPositions.add(new Integer(childPositionInData));
            } else {
                this.removeAndRecycleCell(child);
            }
        }

        for (int i = firstCellPosition; i <= finalCellPosition; i++) {
            if (i == dragged)
                continue;
            if (!addedPositions.contains(new Integer(i)) && i >= 0 && i < totalAvailableCells && getIndexFromPositionInData(i) == -1) {
                addedPositions.add(new Integer(i));
                //Log.i("dgv", "Now creating view for position: " + i);
                View child = mAdapter.getView(i, null, this);
                LayoutParams params = child.getLayoutParams();
                if (params == null) {
                    params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                }
                this.addViewInLayout(child, -1, params);
                Point xy = getCoorFromIndex(i); // This probably needs to be renormalized based on what is being shown.
                child.measure(MeasureSpec.makeMeasureSpec(child.getLayoutParams().width, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec( child.getLayoutParams().height, MeasureSpec.UNSPECIFIED));
            }
        }
    }

    protected void reorderChildren() {
        if (onRearrangeListener != null)
            onRearrangeListener.onRearrange(dragged, lastTarget);

        if (lastTarget < dragged)
            for (int i = 0; i < getChildCount(); i++)
            {
                getChildAt(i).clearAnimation();
                int pos = getPositionInData(i);
                if (pos >= lastTarget && pos < dragged)
                {
                    ((GridItem) getChildAt(i)).changeDataForCell(mAdapter.getIcon(pos +1));
                    Log.i("dgv", "Changing photo at pos " + pos + " to pos " + (pos + 1));
                }
                else if (pos == dragged)
                {
                    ((GridItem) getChildAt(i)).changeDataForCell(mAdapter.getIcon(lastTarget));
                    Log.i("dgv", "Changing photo at pos " + pos + " to pos " + lastTarget);
                }
            }
        else if (dragged < lastTarget)
            for (int i = 0; i < getChildCount(); i++)
            {
                getChildAt(i).clearAnimation();
                int pos = getPositionInData(i);
                if (pos > dragged && pos <= lastTarget)
                {
                    ((GridItem) getChildAt(i)).changeDataForCell(mAdapter.getIcon(pos - 1));
                    Log.i("dgv", "Changing photo at pos " + pos + " to pos " + (pos - 1));
                }
                else if (pos == dragged)
                {
                    ((GridItem) getChildAt(i)).changeDataForCell(mAdapter.getIcon(lastTarget));
                    Log.i("dgv", "Changing photo at pos " + pos + " to pos " + lastTarget);
                }
            }
        invalidate();
        onLayout(true, getLeft(), getTop(), getRight(), getBottom());
    }

    protected Point getCoorFromIndex(int index) {
        int col = index % colCount;
        int row = index / colCount;
        return new Point(padding + (childSize + padding) * col, padding
                + (childSize + padding) * row);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (dragged == -1)
            return i;

        if (i == childCount - 1)
            return getIndexFromPositionInData(dragged);
        else if (i >= getIndexFromPositionInData(dragged))
            return i + 1;
        return i;
    }

    public int getIndexFromCoor(int x, int y) {
        int col = getColOrRowFromCoor(x), row = getColOrRowFromCoor(y);
        if (col == -1 || row == -1) // touch is between columns or rows
            return -1;
        int index = row * colCount + col;
        if (index >= mAdapter.getCount())
            return -1;
        return index;
    }

    protected int getColOrRowFromCoor(int coor) {
        coor -= padding;
        for (int i = 0; coor > 0; i++) {
            if (coor < childSize)
                return i;
            coor -= (childSize + padding);
        }
        return -1;
    }

    protected int getTargetFromCoor(int x, int y) {
        if (getColOrRowFromCoor(y) == -1) // touch is between rows
            return -1;
        // if (getIndexFromCoor(x, y) != -1) //touch on top of another visual
        // return -1;

        int leftPos = getIndexFromCoor(x - (childSize / 4), y);
        int rightPos = getIndexFromCoor(x + (childSize / 4), y);
        if (leftPos == -1 && rightPos == -1) // touch is in the middle of
            // nowhere
            return -1;
        if (leftPos == rightPos) // touch is in the middle of a visual
            return -1;

        int target = -1;
        if (rightPos > -1)
            target = rightPos;
        else if (leftPos > -1)
            target = leftPos + 1;
        if (dragged < target)
            return target - 1;

        // Toast.makeText(getContext(), "Target: " + target + ".",
        // Toast.LENGTH_SHORT).show();
        return target;
    }

    protected int getPositionInData(int index) {
        return ((GridItem) getChildAt(index)).getPositionInData();
    }

    public int getIndexOf(View child) {
        for (int i = 0; i < getChildCount(); i++)
            if (getChildAt(i) == child)
                return i;
        return -1;
    }

    public int getIndexFromPositionInData(int positionInData) {
        for (int i = 0; i < getChildCount(); i++){
            if (((GridItem) getChildAt(i)).getPositionInData() == positionInData)
                return i;
        }
        return -1;
    }

    // EVENT HANDLERS
    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        if (onItemClickListener != null){
            onItemClickListener.onItemClick(adapter, view, position, id);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (!enabled)
            return false;
        int index = getLastIndex();
        if (index != -1)
            for (int i = 0; i < getChildCount(); i++)
                if (getPositionInData(i) == index)
                {
                    dragged = getPositionInData(i);
                    animateDragged();
                    return true;
                }
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                enabled = true;
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                touching = true;
                break;
            case MotionEvent.ACTION_MOVE:
                int delta = lastY - (int) event.getY();
                if (dragged != -1) {
                    // change draw location of dragged visual
                    int x = (int) event.getX(), y = (int) event.getY();
                    int l = x - (3 * childSize / 4), t = y - (3 * childSize / 4);
                    getChildAt(getIndexFromPositionInData(dragged)).layout(l, t, l + (childSize * 3 / 2), t + (childSize * 3 / 2));

                    // check for new target hover
                    int target = getTargetFromCoor(x, y);
                    if (lastTarget != target) {
                        if (target != -1) {
                            animateGap(target);
                            lastTarget = target;
                        }
                    }
                } else {
                    if (Math.abs(delta) > 2)
                        enabled = false;
                    onLayout(true, getLeft(), getTop(), getRight(), getBottom());
                }
                lastX = (int) event.getX();
                lastY = (int) event.getY();
                lastDelta = delta;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (dragged != -1) {
                    int draggedIndex= getIndexFromPositionInData(dragged);
                    View v = getChildAt(draggedIndex);
                    if (lastTarget != -1)
                        reorderChildren();
                    else {
                        Point xy = getCoorFromIndex(draggedIndex);
                        v.layout(xy.x, xy.y, xy.x + childSize, xy.y + childSize);
                    }
                    v.clearAnimation();
                    if (v instanceof ImageView)
                        ((ImageView) v).setAlpha(255);
                    lastTarget = -1;
                    dragged = -1;
                }
                touching = false;
                break;
        }
        if (dragged != -1)
            return true;
        return false;
    }

    // EVENT HELPERS
    protected void animateDragged() {
        View v = getChildAt(getIndexFromPositionInData(dragged));
        Point coor = getCoorFromIndex(dragged);
        int x = coor.x + childSize / 2, y = coor.y + childSize / 2;
        int l = x - (3 * childSize / 4), t = y - (3 * childSize / 4);
        v.layout(l, t, l + (childSize * 3 / 2), t + (childSize * 3 / 2));
        AnimationSet animSet = new AnimationSet(true);
        ScaleAnimation scale = new ScaleAnimation(.667f, 1, .667f, 1,
                childSize * 3 / 4, childSize * 3 / 4);
        scale.setDuration(animT);
        AlphaAnimation alpha = new AlphaAnimation(1, .5f);
        alpha.setDuration(animT);

        animSet.addAnimation(scale);
        animSet.addAnimation(alpha);
        animSet.setFillEnabled(true);
        animSet.setFillAfter(true);

        v.clearAnimation();
        v.startAnimation(animSet);
        invalidate();
    }

    protected void animateGap(int target) {
        Log.i("dgv", "Starting gap animation with dragged: " + dragged);
        for (int i = 0; i < getChildCount(); i++) {
            int pos = getPositionInData(i);
            View v = getChildAt(i);
            if (pos == dragged)
                continue;
            int newPos = pos;
            if (dragged < target && pos >= dragged + 1 && pos <= target)
                newPos--;
            else if (target < dragged && pos >= target && pos < dragged)
                newPos++;

            //Log.i("dgv", "Animating view with position: " + pos);

            // animate
            int oldPos = pos;
            if (newPositions.get(pos) != -1)
                oldPos = newPositions.get(pos);
            if (oldPos == newPos)
                continue;

            Point oldXY = getCoorFromIndex(oldPos);
            Point newXY = getCoorFromIndex(newPos);
            Point oldOffset = new Point(oldXY.x - v.getLeft(), oldXY.y - v.getTop());
            Point newOffset = new Point(newXY.x - v.getLeft(), newXY.y - v.getTop());

            TranslateAnimation translate = new TranslateAnimation(Animation.ABSOLUTE, oldOffset.x, Animation.ABSOLUTE, newOffset.x, Animation.ABSOLUTE, oldOffset.y, Animation.ABSOLUTE, newOffset.y);
            translate.setDuration(animT);
            translate.setFillEnabled(true);
            translate.setFillAfter(true);
            v.clearAnimation();
            v.startAnimation(translate);

            newPositions.set(pos, newPos);
        }
    }

    public int getLastIndex() {
        return getIndexFromCoor(lastX, lastY);
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener l){
        this.onItemClickListener = l;
    }
}