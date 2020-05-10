package zinc0214.appversiongetter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import zinc0214.appversiongetter.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    //Enter the package name of the app you want to get
    private val myPackageName = "Your Pacakge Name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.storeVersion.observe(this, Observer {
            dataBinding.versionInfo = "현재버전 : ${viewModel.currentVersion}\n스토어버전 : ${viewModel.storeVersion.value}"
        })
        dataBinding.goToStore = goToPlayStore()

        viewModel.getStoreVersion(myPackageName)
    }

    private fun goToPlayStore() = View.OnClickListener {
        try {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$myPackageName"))
            )
        } catch (ex: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$myPackageName")
                )
            )
        }
    }
}
