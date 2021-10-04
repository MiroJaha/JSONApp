package com.example.jsonapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val enteredValue = findViewById<EditText>(R.id.etEntry)
        val dateView = findViewById<TextView>(R.id.tvDate)
        val order = findViewById<TextView>(R.id.tvOrder)
        val convert = findViewById<Button>(R.id.buttonConvert)
        val convertList = findViewById<Spinner>(R.id.convertSpinner)
        val result = findViewById<TextView>(R.id.tvResult)
        val change = findViewById<ImageButton>(R.id.change)

        var date =""
        val listOfCoverts = arrayListOf<String>()
        val listOfCovertsNO = arrayListOf<Double>()
        var selected: Int = 0
        var mode =0

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        val call: Call<ConvertDetails?>? = apiInterface!!.doGetListResources()
        call?.enqueue(object : Callback<ConvertDetails?> {
            override fun onResponse(
                call: Call<ConvertDetails?>?,
                response: Response<ConvertDetails?>
            ) {
                val resource: ConvertDetails? = response.body()
                date = resource?.date!!
                //Log.d("TAG","$date")
                val datumList = resource.eur
                //Log.d("TAG","$datumList")
                if (datumList != null) {
                    val datum = datumList.keySet()
                    //Log.d("TAG","$datum")
                    if (convertList != null) {
                        val adapter = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_spinner_item, datum.toTypedArray()
                        )
                        convertList.adapter = adapter

                        convertList.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View, position: Int, id: Long
                            ) {
                                //Log.d("TAG","$position")
                                selected = position
                                if (mode == 1){
                                    order.text = "Enter ${listOfCoverts[selected]} Value"
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                                Log.d("TAG","fail")
                            }
                        }
                    }
                    for (value in datum) {
                        listOfCovertsNO.add(datumList.get(value).toString().toDouble())
                        //Log.d("TAG","${datumList.get(value).toString().toDouble()}")
                        listOfCoverts.add(value)
                    }
                }
            }
            override fun onFailure(call: Call<ConvertDetails?>, t: Throwable?){
                call.cancel()
            }
        })

        change.setOnClickListener{
            if (mode == 0) {
                mode = 1
                order.text = "Enter ${listOfCoverts[selected]} Value"
            }
            else{
                mode = 0
                order.text = "Enter Euro Value"
            }
        }

        convert.setOnClickListener{
            dateView.text = "Date: $date"
            var number: Double = 0.0
            if (enteredValue.text.isNotBlank())
                number = enteredValue.text.toString().toDouble()
            var s = 0.0
            if (mode == 0){
                s = listOfCovertsNO[selected] * number
                result.text = "result $s ${listOfCoverts[selected]}"
            }
            else{
                s = number / listOfCovertsNO[selected]
                result.text = "result $s Euro"
            }
        }
    }
}