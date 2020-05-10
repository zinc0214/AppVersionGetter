package zinc0214.appversiongetter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException
import java.util.regex.Pattern

class MainViewModel : ViewModel(){
    private val _storeVersion = MutableLiveData<String>()

    val storeVersion: LiveData<String> = _storeVersion
    val currentVersion = BuildConfig.VERSION_NAME

    fun getStoreVersion(packageName : String) {
        GlobalScope.launch {
            var version = ""
            try {
                val doc = Jsoup.connect("https://play.google.com/store/apps/details?id=$packageName").get()
                val elements = doc.select(".htlgb")
                for (element in elements) {
                    version = element.text().trim { it <= ' ' }
                    if (Pattern.matches("^[0-9].[0-9].[0-9]$", version)) {
                        withContext(Dispatchers.Main) {
                            _storeVersion.value = version
                        }
                    }
                }
                if(version.isBlank()) {
                    withContext(Dispatchers.Main) {
                        _storeVersion.value = null
                    }
                }
            } catch (ex: IOException) {
                withContext(Dispatchers.Main) {
                    _storeVersion.value = null
                }
                ex.printStackTrace()
            }
        }
    }
}