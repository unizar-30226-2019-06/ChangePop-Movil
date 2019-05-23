package com.example.kalepa

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.kalepa.common.extra
import com.example.kalepa.common.getIntent
import com.example.kalepa.common.loadImage
import com.example.kalepa.models.Product
import kotlinx.android.synthetic.main.activity_product_bid.*

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.kalepa.Preferences.SharedApp
import com.example.kalepa.models.User
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

import kotlinx.android.synthetic.main.fragment_product_image.view.*
import org.jetbrains.anko.toast
import org.json.JSONObject

class ProductBidActivity : AppCompatActivity() {

    var product: Product = Product()
    var user: User = User()
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var num_images = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val product_id: String? = intent.extras.getString(PRODUCT_ARG)
        setContentView(R.layout.activity_product_bid)

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog,null)
        val message = dialogView.findViewById<TextView>(R.id.message)
        message.text = "Cargando producto..."
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()

        val url = MainActivity().projectURL + "/product/" + product_id

        val req = url.httpGet().header(Pair("Cookie", SharedApp.prefs.cookie))
        req.responseJson { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    MainActivity.start(this)
                    toast("Error cargando el producto")
                }
                is Result.Success -> {
                    Initialize1(result.value)
                    dialog.dismiss()
                }
            }
        }

    }

    private fun Initialize1(jsonObject: JSONObject) {
        product.fromJSON(jsonObject)

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog,null)
        val message = dialogView.findViewById<TextView>(R.id.message)
        message.text = "Cargando usuario..."
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()

        val url = MainActivity().projectURL + "/user/" + product.user_id

        val req = url.httpGet().header(Pair("Cookie", SharedApp.prefs.cookie))
        req.responseJson { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    MainActivity.start(this)
                    toast("Error cargando el producto")
                }
                is Result.Success -> {
                    Initialize2(result.value)
                    dialog.dismiss()
                }
            }
        }

    }

    private fun Initialize2(jsonObject: JSONObject) {
        user.fromJSON(jsonObject)

        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog,null)
        val message = dialogView.findViewById<TextView>(R.id.message)
        message.text = "Cargando pujas..."
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()

        val url = MainActivity().projectURL + "/bid/" + product.id

        val req = url.httpGet().header(Pair("Cookie", SharedApp.prefs.cookie))
        req.responseJson { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    MainActivity.start(this)
                    toast("Error cargando el producto")
                }
                is Result.Success -> {
                    Initialize3(result.value)
                    dialog.dismiss()
                }
            }
        }
    }

    private fun Initialize3(jsonObject: JSONObject) {

        b_product_name.setText(product.title)
        b_product_price.setText(product.price.toString())
        b_product_bid.setText(jsonObject.get("max_bid").toString())
        b_product_bid_date.setText(product.bid_date)
        b_product_description.setText(product.descript)
        b_product_place.setText(product.place)
        b_product_date.setText(product.publish_date)
        b_product_seller.setText(user.nick)
        b_product_rating.rating = user.points.toFloat()

        b_product_seller.setOnClickListener {
            ProfileActivity.start(this, product.user_id.toString())
        }

        num_images = product.photo_urls.size

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        b_product_images_container.adapter = mSectionsPagerAdapter
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position , product.photo_urls)
        }

        override fun getCount(): Int {
            // Show 5 total pages.(we will use 5 pages so change it to 5)
            return num_images
        }
    }

    class PlaceholderFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val rootView = inflater.inflate(R.layout.fragment_product_image, container, false)

            val images = arguments!!.getStringArrayList(ARG_IMAGE_LIST)
            rootView.image_iv.loadImage(images[arguments!!.getInt(ARG_SECTION_NUMBER)])

            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"
            private val ARG_IMAGE_LIST = "image_list"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int, images: ArrayList<String?>): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                args.putStringArrayList(ARG_IMAGE_LIST, images)
                fragment.arguments = args
                return fragment
            }
        }
    }

    companion object {

        private const val bullet = '\u2022'
        private  const val PRODUCT_ARG = "com.example.kalepa.BidProductActivity.ProductArgKey"

        fun getIntent(context: Context, product_id: String) = context
            .getIntent<ProductBidActivity>()
            .apply { putExtra(PRODUCT_ARG, product_id) }

        fun start(context: Context, product_id: String) {
            val intent = getIntent(context, product_id)
            context.startActivity(intent)
        }
    }
}
