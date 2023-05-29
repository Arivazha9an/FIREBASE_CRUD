package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.reg.setOnClickListener{
            val name=binding.name.text.toString()
            val age=binding.age.text.toString()
            val addr=binding.addr.text.toString()
            database=FirebaseDatabase.getInstance().getReference("user")
            val user=fireview(name,age,addr)
            database.child(name).setValue(user).addOnSuccessListener {
                binding.name.text.clear()
                binding.age.text.clear()
                binding.addr.text.clear()
                Toast.makeText(this,"SUCCESS",Toast.LENGTH_LONG).show()

            }.addOnFailureListener{
                Toast.makeText(this,"FAILED",Toast.LENGTH_LONG).show()
            }
        }
       binding.upd.setOnClickListener{
           val name=binding.name.text.toString()
           val age=binding.age.text.toString()
           val addr=binding.addr.text.toString()
          updateData(name,age,addr)

       }
        binding.del.setOnClickListener{
            val name= binding.name.text.toString()
            database=FirebaseDatabase.getInstance().getReference("user")
            if (!TextUtils.isEmpty(name)){
                database.child(name).removeValue()
                binding.name.text.clear()
                Toast.makeText(this,"Successfully Deleted",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
            }
        }
        binding.view.setOnClickListener{
            val name:String=binding.name.text.toString()
            if (name.isNotEmpty()){
                readdata(name)
            }
            else{
                Toast.makeText(this,"Enter The Name Must",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun updateData(name: String, age: String, addr: String) {
        database=FirebaseDatabase.getInstance().getReference("user")
        val userUpd= mapOf<String,String>(
            "age" to age,
            "addr" to addr
        )
         database.child(name).updateChildren(userUpd).addOnSuccessListener {
            Toast.makeText(this,"Successfully Updated",Toast.LENGTH_LONG).show()

         }.addOnFailureListener{
             Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
         }

    }

    fun readdata(name:String?){
        database=FirebaseDatabase.getInstance().getReference("user")
        database.child(name.toString()).get().addOnSuccessListener {
            if (it.exists()) {
                val age = it.child("age").value
                val addr = it.child("addr").value
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
                binding.vname.text = name.toString()
                binding.vage.text = age.toString()
                binding.vaddr.text = addr.toString()

            }
            else{
                Toast.makeText(this, "No Data Found",Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
        }

    }
}