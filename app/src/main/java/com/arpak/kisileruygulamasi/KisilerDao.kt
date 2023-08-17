/*package com.arpak.kisileruygulamasi

import android.content.ContentValues

class KisilerDao {


    fun kisiSil(vt:VeritabaniYardimcisi, kisi_id: Int){

        val db = vt.writableDatabase
        db.delete("kisiler","kisi_id=?", arrayOf(kisi_id.toString()))
        db.close()
    }

    fun kisiEkle(vt: VeritabaniYardimcisi,kisi_ad: String,kisi_tel: String){

        val db = vt.writableDatabase
        val values = ContentValues()
        values.put("kisi_ad",kisi_ad)
        values.put("kisi_tel",kisi_tel)
        db.insertOrThrow("kisiler",null,values)
        db.close()
    }

    fun kisiGuncelle(vt: VeritabaniYardimcisi,kisi_id: Int,kisi_ad: String,kisi_tel: String){
        val db =  vt.writableDatabase
        val values = ContentValues()
        values.put("kisi_ad",kisi_ad)
        values.put("kisi_tel",kisi_tel)

        db.update("kisiler",values,"kisi_id=?", arrayOf(kisi_id.toString()))
        db.close()
    }

    fun tumKisiler(vt: VeritabaniYardimcisi): ArrayList<Kisiler>{

        val db = vt.writableDatabase
        val kisiListesi = ArrayList<Kisiler>()
        val cursor = db.rawQuery("SELECT * FROM kisiler",null)
        while (cursor.moveToNext()){
            val kisi = Kisiler(cursor.getInt(cursor.getColumnIndexOrThrow("kisi_id"))
                ,cursor.getString(cursor.getColumnIndexOrThrow("kisi_ad"))
                ,cursor.getString(cursor.getColumnIndexOrThrow("kisi_tel"))
            )
            kisiListesi.add(kisi)
        }
        return kisiListesi
    }

       fun kisiArama(vt: VeritabaniYardimcisi,aramaKelime: String): ArrayList<Kisiler> {
           val db = vt.writableDatabase
           val kisiListesi = ArrayList<Kisiler>()
           val cursor =
               db.rawQuery("SELECT * FROM kisiler WHERE kisi_ad LIKE '%$aramaKelime%'", null)

           while (cursor.moveToNext()) {
               val kisi = Kisiler(
                   cursor.getInt(cursor.getColumnIndexOrThrow("kisi_id")),
                   cursor.getString(cursor.getColumnIndexOrThrow("kisi_ad")),
                   cursor.getString(cursor.getColumnIndexOrThrow("kisi_tel"))
               )
               kisiListesi.add(kisi)
           }

            return kisiListesi
       }

}

*/

