package com.sandhya.crud

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.sandhya.crud.databinding.ActivityMainBinding
import com.sandhya.crud.databinding.DialogItemBinding
import com.sandhya.crud.databinding.UpdateDialogItemBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity(), OnClickInterface {
    lateinit var binding : ActivityMainBinding
    lateinit var adapter: RecyclerViewAdapter
    var list = mutableListOf<Item>()
    var simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    var calender = Calendar.getInstance()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        editor = sharedPreferences.edit()
        adapter = RecyclerViewAdapter(list,this)
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = adapter

        binding.btnSave.setOnClickListener {
            if (binding.etname.text.toString().trim().isNullOrEmpty()){
                binding.etname.error = resources.getString(R.string.enter_name)
            }else{
                editor.putInt("Name",(binding.etname.text.toString()?:"").toInt())
                editor.commit()
                editor.apply()
                Toast.makeText(this,resources.getString(R.string.saved),Toast.LENGTH_SHORT).show()

            }
        }
        binding.fab.setOnClickListener {
            var dialog = Dialog(this)
            var dialogItemBinding = DialogItemBinding.inflate(layoutInflater)
            dialog.setContentView(dialogItemBinding.root)

            dialogItemBinding.etdate.setOnClickListener {
                val datePickerDialog = DatePickerDialog(this,
                    { _, year, month, dayOfMonth ->
                        calender.set(Calendar.YEAR, year)
                        calender.set(Calendar.MONTH, month)
                        calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                        val formattedDate = simpleDateFormat.format(calender.time)
                        dialogItemBinding.etdate.setText(formattedDate)
                    },
                    calender.get(Calendar.YEAR),
                    calender.get(Calendar.MONTH),
                    calender.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.show()

            }

            dialogItemBinding.btnSave.setOnClickListener {
                if (dialogItemBinding.etname.text.toString().trim().isNullOrEmpty()){
                    dialogItemBinding.etname.error = resources.getString(R.string.enter_name)
                }else if (dialogItemBinding.etdate.text.toString().trim().isNullOrEmpty()){
                    dialogItemBinding.etdate.error = resources.getString(R.string.select_date)
                }else{
                    var name = dialogItemBinding.etname.text.toString()
                    var date = dialogItemBinding.etdate.text.toString()
                    var listItem = Item(name = name, date = date)
                    adapter.addItem(listItem)
                    dialog.dismiss()
                }
            }
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.show()
        }
    }

    override fun onClick(item: Item) {
        var dialog = Dialog(this)
        var updatedialogItemBinding = UpdateDialogItemBinding.inflate(layoutInflater)
        dialog.setContentView(updatedialogItemBinding.root)
        updatedialogItemBinding.etname.setText(item.name)
        updatedialogItemBinding.etdate.setText(item.date)
        updatedialogItemBinding.etdate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    calender.set(Calendar.YEAR, year)
                    calender.set(Calendar.MONTH, month)
                    calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val formattedDate = simpleDateFormat.format(calender.time)
                    updatedialogItemBinding.etdate.setText(formattedDate)
                },
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()

        }

        updatedialogItemBinding.btnUpdate.setOnClickListener {
            if (updatedialogItemBinding.etname.text.toString().trim().isNullOrEmpty()){
                updatedialogItemBinding.etname.error = resources.getString(R.string.enter_name)
            }else if (updatedialogItemBinding.etdate.text.toString().trim().isNullOrEmpty()){
                updatedialogItemBinding.etdate.error = resources.getString(R.string.select_date)
            }else{
                var name = updatedialogItemBinding.etname.text.toString()
                var date = updatedialogItemBinding.etdate.text.toString()
                var updatedListItem = Item(name = name, date = date)
                val position = list.indexOf(item)
                adapter.updateItem(position,updatedListItem)
                dialog.dismiss()
            }
        }
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

        override fun onDeleteClick(item: Item) {
            val position = list.indexOf(item)
            if (position != -1){
                AlertDialog.Builder(this)
                    .setTitle("Are you sure to delete the item?")
                    .setPositiveButton("yes"){_,_,->
                        list.removeAt(position)
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("No"){_,_,->
                        Toast.makeText(this," Not Deleted",Toast.LENGTH_SHORT).show()
                    }.show()
            }else{
                Toast.makeText(this,"Item not Found",Toast.LENGTH_SHORT).show()
            }


        }

    }