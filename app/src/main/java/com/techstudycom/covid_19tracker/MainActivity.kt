package com.techstudycom.covid_19tracker

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.*

class MainActivity : AppCompatActivity() {
   /* lateinit var worldCasesTV:TextView
    lateinit var worldRecoveredTV:TextView
    lateinit var worldDeathsTV:TextView */

    lateinit var countryCasesTV:TextView
    lateinit var countryRecovered:TextView
    lateinit var countryDeathsTV:TextView

    lateinit var stateRV:RecyclerView
    lateinit var stateRVAdapter:StateRVAdapter
    lateinit var stateList: List<StateModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //worldCasesTV = findViewById(R.id.idTvWorldCases)
        //worldRecoveredTV = findViewById(R.id.idTvWorldRecovered)
       // worldDeathsTV = findViewById(R.id.idTVWorldDeaths)

        countryCasesTV = findViewById(R.id.idTvIndiaCases)
        countryRecovered= findViewById(R.id.idTvIndiaRecovered)
        countryDeathsTV= findViewById(R.id.idTvIndiaDeaths)

        stateRV = findViewById(R.id.idRVStates)
        stateList = ArrayList<StateModel>()
        getStateInfo()
        //getWorldInfo()


    }
    private fun getStateInfo(){

        val Url = "https://api.rootnet.in/covid19-in/stats/latest"
        val queue = Volley.newRequestQueue(this@MainActivity)
        val request =
            JsonObjectRequest(Request.Method.GET, Url, null, {
                response->
                try {
                    val dataObj = response.getJSONObject("data")
                    val summaryObj = dataObj.getJSONObject("summary")
                    val cases:Int = summaryObj.getInt("total")
                    val recovered:Int = summaryObj.getInt("discharged")
                    val deaths:Int = summaryObj.getInt("deaths")

                    countryCasesTV.text = cases.toString()
                    countryRecovered.text = recovered.toString()
                    countryDeathsTV.text = deaths.toString()

                    val regionalArray = dataObj.getJSONArray("regional")
                    for (i in 0 until regionalArray.length()){
                        val regionalObj = regionalArray.getJSONObject(i)
                        val stateName:String = regionalObj.getString("loc")
                        val cases:Int = regionalObj.getInt("totalConfirmed")
                        val deaths:Int = regionalObj.getInt("deaths")
                        val recovered:Int = regionalObj.getInt("discharged")

                        val stateModel = StateModel(stateName,recovered,deaths,cases)
                        stateList = stateList+stateModel

                    }
                    stateRVAdapter = StateRVAdapter(stateList)
                    stateRV.layoutManager = LinearLayoutManager(this)
                    stateRV.adapter = stateRVAdapter



                }catch (e:JSONException){
                    e.printStackTrace()

                }
            },{
                error ->

                    Toast.makeText(this, "Fail to get data", Toast.LENGTH_SHORT).show()

            })

        queue.add(request)

    }
   /* private fun getWorldInfo(){
        val url ="https://corona.lmao.ninja/v3/covid-19/all"
        val queue = Volley.newRequestQueue(this@MainActivity)
        val request =
            JsonObjectRequest(Request.Method.GET,url,null,{ response->
                try {
                    val worldCases :Int = response.getInt("cases")
                    val worldRecovered:Int = response.getInt("recovered")
                    val worldDeaths:Int = response.getInt("deaths")

                    worldRecoveredTV.text = worldRecovered.toString()
                    worldCasesTV.text = worldCases.toString()
                    worldDeathsTV.text = worldDeaths.toString()
                }
                catch (e: JSONException){
                    e.printStackTrace()
                }


            },{
                error->
                Toast.makeText(this, "Fail to get data", Toast.LENGTH_SHORT).show()

            })
        queue.add(request)

    }*/
}