package com.rafazolondek.monetario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById<TextView>(R.id.txt_result)

        val buttonConverter = findViewById<Button>(R.id.button_converter)

        fun converter(){
            val selectedCurrency = findViewById<RadioGroup>(R.id.radioGroup)

            val checked = selectedCurrency.checkedRadioButtonId

            val currency = when (checked) {
                R.id.radio_usd -> "USD"
                R.id.radio_eur -> "EUR"
                else -> "CLP"
            }

            val editField = findViewById<EditText>(R.id.edit_field)
            val value = editField.text.toString()

            if(value.isEmpty()) return

            result.text = value
            result.visibility = View.VISIBLE

            Thread{
                //aqui acontece em paralelo

                val url = URL("https://api.apilayer.com/currency_data/" +
                        "convert?to=BRL&from=${currency}&amount=${value}&apikey=x4S6ErCk7vvfE7nRtuR6RGI9UjL4npxQ")
                val conn = url.openConnection() as HttpsURLConnection

                try{

                    val data = conn.inputStream.bufferedReader().readText()

                    val obj = JSONObject(data)

                    runOnUiThread{
                        val res = obj.getDouble("result")

                        result.text = "R$${res.toString()}"
                        result.visibility = View.VISIBLE
                    }

                } finally {
                    conn.disconnect()
                }
            }.start()
        }

        buttonConverter.setOnClickListener{
            converter()
        }
    }
}