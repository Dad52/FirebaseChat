package com.ands.firebasechat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ands.firebasechat.databinding.ActivityAddNewCorrespBinding

class AddNewCorrespActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewCorrespBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewCorrespBinding.inflate(layoutInflater).also { setContentView(it.root) }



    }


}