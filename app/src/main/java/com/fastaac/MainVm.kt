package com.fastaac

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.GsonUtils
import com.fastaac.base.*
import com.fastaac.base.base.AbsViewModel
import com.fastaac.base.base.BaseResult
import com.google.gson.reflect.TypeToken
import com.jeremyliao.liveeventbus.LiveEventBus
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

/**
 * <pre>
 * @author : wutao
 * e-mail : 670831931@qq.com
 * time   : 2019/08/17
 * desc   :
 * version: 1.0
</pre> *
 */
class MainVm(application: Application) : AbsViewModel(application) {
    private val url = "https://wanandroid.com/wxarticle/chapters/json"
    val resultLiveData = MutableLiveData<BaseResult<List<TestBean>>>()
    private val client = OkHttpClient()

    fun requestNet() {
        try {
            stateActionEvent.postValue(LoadState)
            Thread.sleep(1000)
            val result = run(url)
            val data = GsonUtils.fromJson<BaseResult<List<TestBean>>>(result, object : TypeToken<BaseResult<List<TestBean?>?>?>() {}.type)
            stateActionEvent.postValue(ToastState("请求成功"))
            stateActionEvent.postValue(SuccessState)
            resultLiveData.postValue(data)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun run(url: String): String {
        val request = Request.Builder()
                .url(url)
                .build()
        client.newCall(request).execute().use { response -> return response.body!!.string() }
    }

    fun clickNoNet() {
        stateActionEvent.postValue(ErrorState("没有网络"))
    }

    fun clickNoData() {
        stateActionEvent.postValue(EmptyState)
    }
}