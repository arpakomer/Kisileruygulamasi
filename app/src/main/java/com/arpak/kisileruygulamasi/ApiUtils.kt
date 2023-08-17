package com.arpak.kisileruygulamasi

class ApiUtils {


    companion object{
        val BASE_URL = "http://kasimadalan.pe.hu/"

        fun getkisilerDaoInterface(): KisilerDaoInterface{
            return RetrofitClient.getClient(BASE_URL).create(KisilerDaoInterface::class.java)
        }


    }


}