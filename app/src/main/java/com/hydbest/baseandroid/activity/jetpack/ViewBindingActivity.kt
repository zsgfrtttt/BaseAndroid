package com.hydbest.baseandroid.activity.jetpack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hydbest.baseandroid.databinding.ActivityViewbindingBinding

class ViewBindingActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivityViewbindingBinding= ActivityViewbindingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setText("hahah")
    }
}