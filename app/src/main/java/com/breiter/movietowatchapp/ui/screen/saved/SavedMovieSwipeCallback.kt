package com.breiter.movietowatchapp.ui.screen.saved

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.breiter.movietowatchapp.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class SavedMovieSwipeCallback(
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val listener: DeleteButtonListener
) : ItemTouchHelper.SimpleCallback(
    0, ItemTouchHelper.LEFT
) {
    private lateinit var gestureDetector: GestureDetector
    private lateinit var recoverQueue: LinkedList<Int>
    private var buttonList: MutableList<DeleteMovieButton>? = null
    private var buttonBuffer: MutableMap<Int, MutableList<DeleteMovieButton>>
    private var swipedPos = -1
    private var swipeTreshold = 0.5F

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent?): Boolean {

            for (button in buttonList!!) {
                if (button.onClick(e!!.x, e.y)) {
                    swipedPos = -1
                    break
                }
            }
            return true
        }
    }

    private val onTouchListener = View.OnTouchListener { _, event ->
        if (swipedPos < 0) return@OnTouchListener false

        val point = Point(event.rawX.toInt(), event.rawY.toInt())
        val swipeViewHolder =
            recyclerView.findViewHolderForAdapterPosition(swipedPos)

        val swipedItem = swipeViewHolder!!.itemView
        val rect = Rect()
        swipedItem.getGlobalVisibleRect(rect)

        if (event.action == MotionEvent.ACTION_DOWN ||
            event.action == MotionEvent.ACTION_UP ||
            event.action == MotionEvent.ACTION_MOVE
        ) {
            if (rect.top < point.y && rect.bottom > point.y)
                gestureDetector.onTouchEvent(event)
            else {
                recoverQueue.add(swipedPos)
                swipedPos = -1
                recoverSwipeItem()
            }

        }

        false
    }

    init {
        this.buttonList = ArrayList()
        this.gestureDetector = GestureDetector(context, gestureListener)
        this.buttonBuffer = HashMap()
        this.recoverQueue = IntLinkedList()

        attachSwipe()
    }

    private fun recoverSwipeItem() {
        while (!recoverQueue.isEmpty()) {
            val pos = recoverQueue.poll()!!
            if (pos > -1)
                recyclerView.adapter!!.notifyItemChanged(pos)
        }
    }

    private fun attachSwipe() {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    class IntLinkedList : LinkedList<Int>() {
        override fun add(element: Int): Boolean {
            return if (contains(element))
                false
            else super.add(element)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        recyclerView.setOnTouchListener(onTouchListener)

        val pos = viewHolder.adapterPosition

        if (swipedPos != pos)
            recoverQueue.add(swipedPos)

        swipedPos = pos

        if (buttonBuffer.containsKey(swipedPos))
            buttonList = buttonBuffer[swipedPos]
        else
            buttonList!!.clear()

        buttonBuffer.clear()
        swipeTreshold = TRANSITION_THRESHOLD * buttonList!!.size * BUTTON_WIDTH
        recoverSwipeItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) =  swipeTreshold

    override fun getSwipeEscapeVelocity(defaultValue: Float) = ESCAPE_VELOCITY * defaultValue

    override fun getSwipeVelocityThreshold(defaultValue: Float) = VELOCITY_THRESHOLD * defaultValue

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val position = viewHolder.adapterPosition
        var translationX = dX
        val itemView = viewHolder.itemView

        if (position < 0) {
            swipedPos = position
            return
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                var buffer: MutableList<DeleteMovieButton> = ArrayList()

                if (!buttonBuffer.containsKey(position)) {
                    buffer.add(DeleteMovieButton(context, listener))
                    buttonBuffer[position] = buffer

                } else {
                    buffer = buttonBuffer[position]!!
                }

                translationX = dX * buffer.size * BUTTON_WIDTH / itemView.width
                drawButton(c, itemView, buffer, position, translationX)
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
        c: Canvas,
        itemView: View,
        buffer: MutableList<DeleteMovieButton>,
        pos: Int,
        translationX: Float
    ) {
        var right = itemView.right.toFloat()
        val dButtonWidth = -1 * translationX / buffer.size

        for (button in buffer) {
            val left = right - dButtonWidth
            button.onDraw(
                c,
                RectF(left, itemView.top.toFloat(), right, itemView.bottom.toFloat()),
                pos
            )
            right = left
        }
    }

    /**
     *  Class representing under-layout button
     */
    class DeleteMovieButton(private val context: Context, private val listener: DeleteButtonListener) {
        private var pos: Int = 0
        private var clickRegion: RectF? = null

        fun onDraw(canvas: Canvas, rectF: RectF, pos: Int) {
            val paint = Paint()

            //Draw red background
            paint.color = Color.RED
            canvas.drawRoundRect(rectF, CORNER_RADIUS, CORNER_RADIUS, paint)

            //Draw trash icon in the middle of swipe button
            val imageResId: Int = R.drawable.ic_delete
            val d = ContextCompat.getDrawable(context, imageResId)
            val bitmap = drawableToBitmap(d)

            canvas.drawBitmap(
                bitmap,
                (rectF.left + rectF.right) / 2 - d!!.intrinsicWidth / 2,
                (rectF.top + rectF.bottom) / 2 - d.intrinsicHeight / 2,
                paint
            )

            clickRegion = rectF
            this.pos = pos
        }

        private fun drawableToBitmap(d: Drawable?): Bitmap {
            if (d is BitmapDrawable)
                return d.bitmap

            val bitmap =
                Bitmap.createBitmap(
                    d!!.intrinsicWidth,
                    d.intrinsicWidth,
                    Bitmap.Config.ARGB_8888
                )

            val canvas = Canvas(bitmap)
            d.setBounds(0, 0, canvas.width, canvas.height)
            d.draw(canvas)

            return bitmap
        }

        fun onClick(dX: Float, dY: Float): Boolean {
            if (clickRegion != null && clickRegion!!.contains(dX, dY)) {
                listener.onSwiped(pos)
                return true
            }
            return false
        }
    }

    internal companion object {
        private const val BUTTON_WIDTH = 200
        private const val TRANSITION_THRESHOLD = 0.5F
        private const val VELOCITY_THRESHOLD = 5F
        private const val ESCAPE_VELOCITY = 0.1F
        const val CORNER_RADIUS = 10f
    }

    class DeleteButtonListener(val listener: (pos: Int) -> Unit) {
        fun onSwiped(pos: Int) = listener(pos)
    }
}
