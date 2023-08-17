package com.arpak.kisileruygulamasi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
//import com.android.volley.Request
//import com.android.volley.toolbox.StringRequest
//import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KisilerAdapter(
    private val mContext: Context,
    private var kisilerList: List<Kisiler>,
    private var kisiDaoInterface: KisilerDaoInterface
//                     private val vt: VeritabaniYardimcisi
) : RecyclerView.Adapter<KisilerAdapter.CardTasarimTutucu>() {


    inner class CardTasarimTutucu(tasarim: View) : RecyclerView.ViewHolder(tasarim) {

        var textViewKisi: TextView
        var imageViewMore: ImageView

        init {
            textViewKisi = tasarim.findViewById(R.id.textViewKayit)
            imageViewMore = tasarim.findViewById(R.id.imageViewMore)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardTasarimTutucu {
        val tasarim =
            LayoutInflater.from(mContext).inflate(R.layout.cardview_tasarim, parent, false)
        return CardTasarimTutucu(tasarim)
    }

    override fun getItemCount(): Int {
        return kisilerList.size
    }

    override fun onBindViewHolder(holder: CardTasarimTutucu, position: Int) {
        val kisi = kisilerList[position]

        holder.textViewKisi.text = "${kisi.kisi_ad} - ${kisi.kisi_tel}"
        holder.imageViewMore.setOnClickListener {

            val popupMenu = PopupMenu(mContext, holder.imageViewMore)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_sil -> {

                        Snackbar.make(
                            holder.imageViewMore,
                            "${kisi.kisi_ad} : Silinsin mi",
                            Snackbar.LENGTH_SHORT
                        )
                            .setAction("EVET") {


                                kisiDaoInterface.kisiSil(kisi.kisi_id).enqueue(object : Callback<CRUDCevap>{

                                    override fun onResponse(
                                        call: Call<CRUDCevap>,
                                        response: Response<CRUDCevap>
                                    ) {
                                        tumKisilerIleRetrofit()
                                    }

                                    override fun onFailure(call: Call<CRUDCevap>, t: Throwable) {

                                    }
                                })
//                                deletePersons(kisi.kisi_id)

//                                KisilerDao().kisiSil(vt,kisi.kisi_id)
//                                kisilerList = KisilerDao().tumKisiler(vt)


                            }.show()
                        true
                    }
                    R.id.action_duzenle -> {


                        alertDialog(kisi)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

    }




    fun alertDialog(kisi: Kisiler) {
        val tasarim = LayoutInflater.from(mContext).inflate(R.layout.alertdialog_tasarimi, null)
        val editTextAd = tasarim.findViewById(R.id.editTextAd) as EditText
        val editTextTel = tasarim.findViewById(R.id.editTextTeliniz) as EditText

        editTextAd.setText(kisi.kisi_ad)
        editTextTel.setText(kisi.kisi_tel)
        val alertDialog = AlertDialog.Builder(mContext)
        alertDialog.setTitle("Kişi Güncelle")
        alertDialog.setView(tasarim)
        alertDialog.setPositiveButton("Güncelle") { dialogInterface, i ->

            val kisi_ad = editTextAd.text.toString().trim()
            val kisi_tel = editTextTel.text.toString().trim()


            kisiDaoInterface.kisiGuncelle(kisi.kisi_id,kisi_ad,kisi_tel).enqueue(object : Callback<CRUDCevap>{

                override fun onResponse(call: Call<CRUDCevap>, response: Response<CRUDCevap>) {

                    tumKisilerIleRetrofit()
                }

                override fun onFailure(call: Call<CRUDCevap>, t: Throwable) {

                }
            })

//                updatePersons(kisi.kisi_id, kisi.kisi_ad, kisi.kisi_tel)
//            KisilerDao().kisiGuncelle(vt,kisi.kisi_id,kisi_ad,kisi_tel)

            // Bu iki Kod satırı bize arayuzumdeki yapacagımız islemden sonra ilgili sayfayı guncel halini verir
//            KisilerDao().tumKisiler(vt)

            Toast.makeText(mContext, "$kisi_ad - $kisi_tel", Toast.LENGTH_LONG).show()

        }

        alertDialog.setNegativeButton("İptal") { dialogInterface, i ->

        }

        alertDialog.create().show()
    }




/*
        // If you want to Volley Library
 private infix fun deletePersons(kisi_id: Int) {
        val url = "http://kasimadalan.pe.hu/kisiler/delete_kisiler.php"

        val requestDelete = object : StringRequest(Method.POST, url, { response ->

            allPersonsWithVolley()
        }, { erorr -> }) {

            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["kisi_id"] = kisi_id.toString()
                return params
            }


        }
        val queueDelete = Volley.newRequestQueue(mContext)
        queueDelete.add(requestDelete)
    }

    private fun updatePersons(kisiId: Int, kisiAd: String, kisiTel: String) {

        val url = "http://kasimadalan.pe.hu/kisiler/update_kisiler.php"

        val requestUpdate = object : StringRequest(Method.POST, url, { response ->

            allPersonsWithVolley()
        }, { erorr -> }) {

            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["kisi_id"] = kisiId.toString()
                params["kisi_ad"] = kisiAd
                params["kisi_tel"] = kisiTel
                return params
            }


        }
        val queue = Volley.newRequestQueue(mContext)
        queue.add(requestUpdate)
    }

    private fun allPersonsWithVolley() {
        val url = "http://kasimadalan.pe.hu/kisiler/tum_kisiler.php"

        val request = StringRequest(Request.Method.GET, url, { response ->

            try {
                val tempListe = ArrayList<Kisiler>()
                val jsonObject = JSONObject(response)
                val persons = jsonObject.getJSONArray("kisiler")

                for (i in 0 until persons.length()) {

                    val k = persons.getJSONObject(i)
                    val person = Kisiler(
                        k.getInt("kisi_id"), k.getString("kisi_ad"), k.getString("kisi_tel")
                    )

                    tempListe.add(person)
                }
                kisilerList = tempListe

                notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, { error ->

            Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show()
        })
        val queue = Volley.newRequestQueue(mContext)
        queue.add(request)
    }

    */


    private fun tumKisilerIleRetrofit() {

        kisiDaoInterface.tumKisiler().enqueue(object : Callback<KisilerCevap>{

            override fun onResponse(call: Call<KisilerCevap>, response: Response<KisilerCevap>) {

                kisilerList = response.body()!!.kisiler

                notifyDataSetChanged()
            }

            override fun onFailure(call: Call<KisilerCevap>, t: Throwable) {

            }
        } )
    }
}

