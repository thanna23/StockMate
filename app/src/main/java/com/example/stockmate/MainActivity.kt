package com.example.stockmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.stockmate.orderlist.OrderListActivity
import com.example.stockmate.restock.RestockActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btStartingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btStartingButton = findViewById(R.id.btStartingButton)

        btStartingButton.setOnClickListener {
            showOptionsDialog()
        }
    }

    private fun showOptionsDialog() {

        val options = arrayOf("--> Restock <--", "--> Order List <--")
        val builder = AlertDialog.Builder(this, R.style.StartChoiceAlertDialogTheme)
        builder.setTitle("CHOOSE A TASK :")

        // Configurer les éléments du dialogue
        builder.setItems(options) { dialog, which ->
            val selectedOption = options[which]
            val intent: Intent
            when (selectedOption) {
                options[0] -> {
                    intent = Intent(this, RestockActivity::class.java)
                }

                options[1] -> {
                    intent = Intent(this, OrderListActivity::class.java)
                }

                else -> {
                    intent = Intent(this, MainActivity::class.java)
                }

            }

            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Afficher le dialogue
        val dialog = builder.create()
        dialog.show()
    }

}
