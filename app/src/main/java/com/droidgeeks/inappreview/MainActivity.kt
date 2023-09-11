package com.droidgeeks.inappreview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.droidgeeks.inappreview.databinding.ActivityMainBinding
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task

class MainActivity : AppCompatActivity() {

    private lateinit var reviewManager: ReviewManager
    private lateinit var reviewInfo: ReviewInfo

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getReviewInfo()

        binding.reviewButton.setOnClickListener {
            startReviewFlow()
        }

    }

    /**
     * Call this method at app start to pre-cache the reviewInfo object to use to show
     * in-app review dialog later.
     */
    private fun getReviewInfo() {
        reviewManager = ReviewManagerFactory.create(applicationContext)
        val manager = reviewManager.requestReviewFlow()

        manager.addOnCompleteListener { task: Task<ReviewInfo?> ->
            if (task.isSuccessful) {
                reviewInfo = task.result
            } else {
                Log.d("InAppReview", "ReviewFlow failed to start")
            }
        }
    }

    /**
     * Call this method when you want to show the in-app rating dialog
     */
    private fun startReviewFlow() {
        if (::reviewManager.isInitialized) {
            val flow = reviewManager.launchReviewFlow(this, reviewInfo)
            flow.addOnCompleteListener {
                Log.d("InAppReview", "Rating complete")
            }
        }
    }
}