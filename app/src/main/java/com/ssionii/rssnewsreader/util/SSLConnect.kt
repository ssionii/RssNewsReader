package com.ssionii.rssnewsreader.util

import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

class SSLConnect {
    val _hostnameVerifier = HostnameVerifier { hostName, session ->
        HttpsURLConnection.getDefaultHostnameVerifier().run{
            verify(hostName, session)
        }
    }

    fun trustAllHosts(){
        var trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager{
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {

            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {

            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }

        })

        // all-trusting trust manager 설치
        try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    fun postHttps(url: String, connTimeout: Int, rdTimeout : Int) : HttpsURLConnection?{
        trustAllHosts()

        var https : HttpsURLConnection? = null
        try{
            https = URL(url).openConnection() as HttpsURLConnection
            https.apply {
                hostnameVerifier = _hostnameVerifier
                connectTimeout = connTimeout
                readTimeout = rdTimeout
            }
        }catch (e: MalformedURLException){
            e.printStackTrace()
            return null
        } catch (e: IOException){
            e.printStackTrace()
            return null
        }

        return https
    }
}