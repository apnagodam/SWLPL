package com.apnagodam.staff.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.core.view.indices
import androidx.core.view.isNotEmpty
import androidx.databinding.DataBindingUtil
import com.apnagodam.staff.R
import com.apnagodam.staff.databinding.ActivityUpdatePvBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdatePv : AppCompatActivity() {
    lateinit var binding: ActivityUpdatePvBinding
    var listOfLayout = arrayListOf<LinearLayout>()
    var layoutId = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_pv)

        binding.btAdd.setOnClickListener {

            binding!!.llParent.addView(addParentLayouts())
            addParentLayouts().forEach {

                Toast.makeText(this,it.id.toString(),Toast.LENGTH_SHORT).show();

            }
            if (addParentLayouts().isNotEmpty()) {
                for (i in addLayouts().indices) {
                    var edittext = findViewById<EditText>(addLayouts()[i].id)
                    if (edittext != null) {
                        Toast.makeText(this, edittext.text.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }
    }

    private fun setUI() {

    }

    private fun setObservers() {

    }

    private fun addLayouts(): LinearLayout {
        val linearLayout = LinearLayout(this)

        val textView = EditText(this)
        textView.setHint("Dhang")
        textView.id = 0;

        val textView2 = EditText(this)
        textView2.setHint("Danda")
        textView2.id = 1;

        val textView3 = EditText(this)
        textView3.setHint("Height")
        textView3.id = 2;

        val textView4 = EditText(this)
        textView4.setHint("Plus Minus")
        textView4.id = 3;

        val textView5 = EditText(this)
        textView5.setHint("Remark")
        textView5.id = 4

        linearLayout.addView(textView)

        linearLayout.addView(textView2)

        linearLayout.addView(textView3)

        linearLayout.addView(textView4)

        linearLayout.addView(textView5)
        return linearLayout
    }

    private fun addParentLayouts(): LinearLayout {

        layoutId++;
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.id = layoutId
        linearLayout.addView(addLayouts())
        return linearLayout

    }
}