package com.arpak.kisileruygulamasi

import android.app.Person
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
//import com.android.volley.Request.Method
//import com.android.volley.Response
//import com.android.volley.toolbox.StringRequest
//import com.android.volley.toolbox.Volley
import com.arpak.kisileruygulamasi.databinding.ActivityMainBinding
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var kisilerListe: ArrayList<Kisiler>
    private lateinit var adapter: KisilerAdapter
    private lateinit var url: String
    private lateinit var kisiDaoInterface: KisilerDaoInterface

    //    private lateinit var vt: VeritabaniYardimcisi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.toolbarKisilerMain.title = "Kisiler Uygulaması"
        setSupportActionBar(binding.toolbarKisilerMain)

        binding.recyclerViewMain.setHasFixedSize(true)
        binding.recyclerViewMain.layoutManager = LinearLayoutManager(this)


        kisiDaoInterface = ApiUtils.getkisilerDaoInterface()


        tumKisilerRetrofit()


//        allPersonsWithVolley()
        /*
             Static Veri ekleme
        kisilerListe = ArrayList()
        val k1 = Kisiler(1,"Ahmet","8867465487")
        val k2 = Kisiler(1,"Yusuf","9894654564")

        kisilerListe.add(k1)
        kisilerListe.add(k2)



         */

//        vt = VeritabaniYardimcisi(this)

/*
        val dao = KisilerDao()
        kisilerListe = dao.tumKisiler(vt)
        */

        // Daha kısa bir ifadeyle

//        tumKisilerAl()
        binding.fabPersonAdd.setOnClickListener {

            alertDialog()

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val item = menu?.findItem(R.id.action_ara)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }


    fun alertDialog() {
        val tasarim = LayoutInflater.from(this).inflate(R.layout.alertdialog_tasarimi, null)
        val editTextAd = tasarim.findViewById(R.id.editTextAd) as EditText
        val editTextTel = tasarim.findViewById(R.id.editTextTeliniz) as EditText


        val alertDialog = AlertDialog.Builder(this)

        alertDialog.setTitle("Kişi EKle")
        alertDialog.setView(tasarim)
        alertDialog.setPositiveButton("Ekle") { dialogInterface, i ->

            val kisi_ad = editTextAd.text.toString().trim()
            val kisi_tel = editTextTel.text.toString().trim()

            kisiDaoInterface.kisiEkle(kisi_ad,kisi_tel).enqueue(object : Callback<CRUDCevap>{
                override fun onResponse(call: Call<CRUDCevap>, response: Response<CRUDCevap>) {

                    tumKisilerRetrofit()
                }

                override fun onFailure(call: Call<CRUDCevap>, t: Throwable) {

                }
            })

//            insertPersons(kisi_ad,kisi_tel)

//            KisilerDao().kisiEkle(vt,kisi_ad,kisi_tel)

//            tumKisilerAl()

            Toast.makeText(applicationContext, "$kisi_ad -$kisi_tel", Toast.LENGTH_LONG).show()

        }
        alertDialog.setNegativeButton("İptal") { dialogInterface, i ->

        }

        alertDialog.create().show()
    }

    override fun onQueryTextChange(newText: String): Boolean {

        aramaKisilerIleRetrofit(newText)
//        allPersonsSearchWithVolley(newText)
//        aramaYap(newText)
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {

        aramaKisilerIleRetrofit(query)
//        allPersonsSearchWithVolley(query)
//        aramaYap(query)
        return true
    }

    private fun tumKisilerRetrofit() {
        kisiDaoInterface.tumKisiler().enqueue(object : Callback<KisilerCevap> {

            override fun onResponse(call: Call<KisilerCevap>, response: Response<KisilerCevap>) {

                val list = response.body()?.kisiler

                if (list != null) {
                    adapter = KisilerAdapter(this@MainActivity, list,kisiDaoInterface)
                    binding.recyclerViewMain.adapter = adapter
                }
            }

            override fun onFailure(call: Call<KisilerCevap>, t: Throwable) {

            }
        })
    }

    private fun aramaKisilerIleRetrofit(kisiArama: String) {

        kisiDaoInterface.kisiArama(kisiArama).enqueue(object : Callback<KisilerCevap> {

            override fun onResponse(call: Call<KisilerCevap>, response: Response<KisilerCevap>) {

                val list = response.body()?.kisiler
                if (list != null) {
                    adapter = KisilerAdapter(this@MainActivity, list,kisiDaoInterface)
                    binding.recyclerViewMain.adapter = adapter
                }
            }

            override fun onFailure(call: Call<KisilerCevap>, t: Throwable) {

            }
        })
    }


    // If you want to use the volley library
/*

  private fun allPersonsWithVolley() {
        url = "http://kasimadalan.pe.hu/kisiler/tum_kisiler.php"

        val request = StringRequest(Method.GET, url, { response ->

            try {
                kisilerListe = ArrayList()
                val jsonObject = JSONObject(response)
                val persons = jsonObject.getJSONArray("kisiler")

                for (i in 0 until persons.length()) {

                    val k = persons.getJSONObject(i)
                    val person = Kisiler(
                        k.getInt("kisi_id"), k.getString("kisi_ad"), k.getString("kisi_tel")
                    )

                    kisilerListe.add(person)
                }
                adapter = KisilerAdapter(this@MainActivity, kisilerListe)
                binding.recyclerViewMain.adapter = adapter
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, { error ->

            Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
        })
        val queue = Volley.newRequestQueue(this@MainActivity)
        queue.add(request)
    }

    private  infix fun   allPersonsSearchWithVolley(searchWord: String) {

        url = "http://kasimadalan.pe.hu/kisiler/tum_kisiler_arama.php"
        val request = object : StringRequest(Method.POST, url, { response ->

            try {
                val jsonObject = JSONObject(response)
                val personsSearch = jsonObject.getJSONArray("kisiler")

                for (i in 0 until personsSearch.length()) {
                    kisilerListe = ArrayList()
                    val k = personsSearch.getJSONObject(i)

                    val personSearch = Kisiler(
                        k.getInt("kisi_id"), k.getString("kisi_ad"), k.getString("kisi_tel")
                    )

                    kisilerListe.add(personSearch)

                }
                adapter = KisilerAdapter(this@MainActivity, kisilerListe)
                binding.recyclerViewMain.adapter = adapter
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, { error -> }) {

            override fun getParams(): MutableMap<String, String> {

                val params = HashMap<String, String>()
                params["kisi_ad"] = searchWord
                return params
            }
        }
        val queueVolley = Volley.newRequestQueue(this@MainActivity)

        queueVolley.add(request)
    }

    private fun insertPersons(kisi_ad: String, kisi_tel: String) {

        url = "http://kasimadalan.pe.hu/kisiler/insert_kisiler.php"

        val requestInsert = object : StringRequest(Method.POST, url, Response.Listener { response ->
            allPersonsWithVolley()
        }, {}) {
            override fun getParams(): MutableMap<String, String>? {

                val params = HashMap<String, String>()
                params["kisi_ad"] = kisi_ad
                params["kisi_tel"] = kisi_tel
                return params
            }
        }
        val queueInsert = Volley.newRequestQueue(this@MainActivity)

        queueInsert.add(requestInsert)
    }



    */


//    fun tumKisilerAl(){
//        kisilerListe = KisilerDao().tumKisiler(vt)
//
//        adapter = KisilerAdapter(this,kisilerListe,vt)
//
//        binding.recyclerViewMain.adapter = adapter
//    }
//
//
//    fun aramaYap(aramaKelime: String){
//
//        kisilerListe = KisilerDao().kisiArama(vt,aramaKelime)
//
//        adapter = KisilerAdapter(this,kisilerListe,vt)
//
//        binding.recyclerViewMain.adapter = adapter
//    }


}