package com.breiter.movietowatchapp.ui.screen.saved

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.breiter.movietowatchapp.R


private const val TRANSITION_THRESHOLD = 0.5F
private const val VELOCITY_THRESHOLD = 5F
private const val ESCAPE_VELOCITY = 0.1F

abstract class SavedMovieSwipeCallback(
    private val context: Context,
    private val recyclerView: RecyclerView
) : ItemTouchHelper.SimpleCallback(
    0, ItemTouchHelper.LEFT
) {
    private lateinit var gestureDetector: GestureDetector
    private lateinit var gestureListener: GestureDetector.SimpleOnGestureListener
    private lateinit var currentViewHolder: RecyclerView.ViewHolder
    private lateinit var swipedItem: View
    private lateinit var buttonRect: RectF

    private var shouldListenEvents = false
    private var itemRect = Rect()
    private val buttonWidth = context.resources.getDimension(R.dimen.buttonWidth)
    private val buttonInset = context.resources.getDimension(R.dimen.buttonInset)
    private var buttonRectRight = 0F
    private var buttonRectLeft = 0F
    private var buttonRectTop = 0F
    private var buttonRectBottom = 0F
    private var buttonBuffer: MutableMap<Int, MutableList<SwipeButton>> = HashMap()
    private var buttonList: MutableList<SwipeButton> = ArrayList()
    private var swipeTreshold = 0.5F
    private var swipedPos = -1

    init {
        initGestureDetector()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        swipedPos = viewHolder.adapterPosition
        shouldListenEvents = true
        recyclerView.setTouchListener()

        if (buttonBuffer.containsKey(swipedPos))
            buttonList = buttonBuffer[swipedPos]!!
        else
            buttonList.clear()

        buttonBuffer.clear()
        swipeTreshold = TRANSITION_THRESHOLD * buttonList.size * buttonWidth
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun RecyclerView.setTouchListener() {
        setOnTouchListener { _, event ->
            if (!shouldListenEvents) return@setOnTouchListener false

            //Getting ViewItem bounding rectangle.
            currentViewHolder =
                recyclerView.findViewHolderForAdapterPosition(swipedPos)!!
            swipedItem = currentViewHolder.itemView
            swipedItem.getGlobalVisibleRect(itemRect)

            //Detecting user's motions on the screen and responding to it.
            val motionPoint = Point(event.rawX.toInt(), event.rawY.toInt())
            if (event.action == MotionEvent.ACTION_DOWN ||
                event.action == MotionEvent.ACTION_UP ||
                event.action == MotionEvent.ACTION_MOVE
            ) {
                if (itemRect.top < motionPoint.y &&
                    itemRect.bottom > motionPoint.y
                ) {
                    //No changes in swiping, start listening for the events.
                    gestureDetector.onTouchEvent(event)

                } else {
                    //Different item was swiped. Notify that the item has changed
                    //to prevent having multiple items on swipe at the same time).
                    recyclerView.adapter!!.notifyItemChanged(swipedPos)
                    shouldListenEvents = false
                }
            }
            false
        }
    }

    private fun initGestureDetector(){
        gestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(event: MotionEvent): Boolean {
                val motionEventX = event.x
                val motionEventY = event.y

                for (button in buttonList) {
                    if (button.onClick(motionEventX, motionEventY)) {
                        shouldListenEvents = false
                        break
                    }
                }
                return true
            }
        }
        gestureDetector = GestureDetector(context, gestureListener)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        var translationX = dX

        val currentPos = viewHolder.adapterPosition
        val currentItem = viewHolder.itemView

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                var buttons: MutableList<SwipeButton> = ArrayList()

                if (!buttonBuffer.containsKey(currentPos)) {
                    addSwipeButton(buttons)
                    buttonBuffer[currentPos] = buttons
                } else {
                    buttons = buttonBuffer[currentPos]!!
                }

                //Calculate amount of horizontal displacement in pixels caused by user on swipe event.
                //The displacement is limited to the amount needed to display swipe-button(s).
                translationX = dX * buttons.size * buttonWidth / currentItem.width

                drawButton(c, currentItem, buttons, currentPos, translationX)
            }
        }
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            translationX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    private fun drawButton(
        canvas: Canvas,
        itemView: View,
        buffer: MutableList<SwipeButton>,
        itemPos: Int,
        translationX: Float
    ) {
        buttonRectTop = itemView.top.toFloat()
        buttonRectRight = itemView.right.toFloat()
        buttonRectBottom = itemView.bottom.toFloat()

        val currentWidth = translationX / buffer.size

        for (button in buffer) {
            //Calculate the left edge location of this button depending on the
            // horizontal displacement caused by user on swipe event.
            buttonRectLeft = buttonRectRight + currentWidth

            buttonRect = RectF(
                buttonRectLeft,
                buttonRectTop,
                buttonRectRight - buttonInset,
                buttonRectBottom
            )

            button.onDraw(canvas, buttonRect, itemPos)
            buttonRectRight = buttonRectLeft
        }
    }

    abstract fun addSwipeButton(swipeButtons: MutableList<SwipeButton>)

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) = swipeTreshold

    override fun getSwipeEscapeVelocity(defaultValue: Float) = ESCAPE_VELOCITY * defaultValue

    override fun getSwipeVelocityThreshold(defaultValue: Float) = VELOCITY_THRESHOLD * defaultValue

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false


    /**
     *  Class representing button(s) revealed, when the ViewHolder is swiped.
     *  The color and icon will be set in the BindingAdapter.
     *  The listener will be implemented in hosting activity.
     */
    class SwipeButton(
        context: Context,
        imageResId: Int,
        private val bgColor: Int,
        private val listener: SwipeListener
    ) {
        private var position: Int = -1
        private val paint = Paint().apply {
            color = bgColor
        }
        private val cornerRadius = context.resources.getDimension(R.dimen.buttonCorner)
        private var icon: Drawable = ContextCompat.getDrawable(context, imageResId)!!
        private var bitmapIcon = drawableToBitmap(icon)
        private lateinit var extraCanvas: Canvas
        private lateinit var clickRegion: RectF

        fun onDraw(canvas: Canvas, rectF: RectF, pos: Int) {
            //Draw rectangle background
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)

            //Draw bitmapIcon in the middle of the button
            canvas.drawBitmap(
                bitmapIcon,
                (rectF.left + rectF.right) / 2f - icon.intrinsicWidth / 2f,
                (rectF.top + rectF.bottom) / 2f - icon.intrinsicHeight / 2f,
                paint
            )

            clickRegion = rectF
            position = pos
        }

        private fun drawableToBitmap(drawable: Drawable): Bitmap {
            val bitmap =
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicWidth,
                    Bitmap.Config.ARGB_8888
                )

            extraCanvas = Canvas(bitmap)
            drawable.setBounds(0, 0, extraCanvas.width, extraCanvas.height)
            drawable.draw(extraCanvas)

            return bitmap
        }

        fun onClick(motionEventX: Float, motionEventY: Float): Boolean {
            if (clickRegion.contains(motionEventX, motionEventY)) {
                listener.onSwiped(position)
                return true
            }
            return false
        }
    }

    class SwipeListener(val listener: (pos: Int) -> Unit) {
        fun onSwiped(pos: Int) = listener(pos)
    }
}