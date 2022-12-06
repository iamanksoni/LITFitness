package com.litmethod.android.ui.WorkoutHistory

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.os.CountDownTimer
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.litmethod.android.R
import com.litmethod.android.databinding.ActivityWorkoutHistoryBinding
import com.litmethod.android.shared.BaseActivity
import com.litmethod.android.ui.Dashboard.HomeTabScreen.HomeTabFragmentScreen.WorkoutGoalHeaderAdapter
import com.litmethod.android.utlis.SpacesItemDecoration

class WorkoutHistoryActivity : BaseActivity() {
    lateinit var binding:ActivityWorkoutHistoryBinding
    val workProgressItem: ArrayList<WorkoutProgressModel> = ArrayList<WorkoutProgressModel>()

    private var layoutworkProgress: RecyclerView.LayoutManager? = null
    private var workoutProgressAdapter: WorkoutProgressAdapter? = null
    val workOutItem: ArrayList<String> = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_workout_history)
        setUpUi()
    }

    private fun setUpUi() {
        workOutItem.clear()
        workOutItem.add("")
        workOutItem.add("")
        workOutItem.add("")
        binding.rvWorkoutSummary.apply {
            layoutworkProgress = GridLayoutManager(this@WorkoutHistoryActivity, 2)
            this.layoutManager = layoutworkProgress
            workoutProgressAdapter = WorkoutProgressAdapter(workOutItem,this@WorkoutHistoryActivity)
            this.adapter = workoutProgressAdapter
        }

    }
}