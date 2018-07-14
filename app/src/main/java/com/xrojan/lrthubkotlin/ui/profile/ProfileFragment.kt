package com.xrojan.lrthubkotlin.ui.profile

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xrojan.lrthubkotlin.App
import com.xrojan.lrthubkotlin.R
import com.xrojan.lrthubkotlin.constants.HTTP
import com.xrojan.lrthubkotlin.fragments.BaseFragment
import com.xrojan.lrthubkotlin.repository.entities.Feed
import com.xrojan.lrthubkotlin.repository.entities.UserProfile
import com.xrojan.lrthubkotlin.ui.main.MainViewModel
import com.xrojan.lrthubkotlin.viewmodel.FeedViewModel
import com.xrojan.lrthubkotlin.viewmodel.UserViewModel
import com.xrojan.lrthubkotlin.viewmodel.data.UIDataArray
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.settings_fragment.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.uiThread

/**
 * Created by Joshua de Guzman on 10/07/2018.
 */

class ProfileFragment : BaseFragment() {
    private val userViewModel: UserViewModel = App.injectUserViewModel()

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            initComponents()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)


    }

    private fun initComponents() {
// FIXME: Get user from local database
//        subscribe(userViewModel.getUserLocalData()
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.single())
//                .subscribe {
//                    if(it.isNotEmpty()){
//                        onSuccessFetchLocal(it[0].token, it[0].uid)
//                    }
//                })

        onSuccessFetchLocal("JWT eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjozLCJ1c2VybmFtZSI6ImRlbW8iLCJleHAiOjE1MzE1NjA3MTAsImVtYWlsIjoiZGVtb0B4cm9qYW4uY29tIiwib3JpZ19pYXQiOjE1MzE1NTcxMTB9.GTlZU3BdRZaucCtUn1a1o2vDny8Vhy7THGOoR4xuCSM",
                3)

//        onSuccessFetchLocal(userViewModel.getUserLocalData())

    }

    private fun onSuccessFetchLocal(token: String, uid: Int) {
        subscribe(userViewModel.getUserDetail(token, uid)
        !!.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe({
                    when (it.request.statusCode) {
                        HTTP.OK -> {
                            if(it.request.result.isNotEmpty()){
                                // show details and show update button
                                doAsync {
                                    uiThread {
                                        toast("Profile OK, show update button")
                                        btn_verify.text = "UPDATE PROFILE"
                                    }
                                }
                            }else{
                                // show add verify button
                                doAsync {
                                    uiThread {
                                        toast("Profile no result, Show add verify button")
                                        btn_verify.text = "VERIFY PROFILE"
                                    }
                                }
                            }
                        }

                        HTTP.FORBIDDEN -> {

                        }

                        HTTP.SERVICE_UNAVAILABLE -> {

                        }
                    }
                }, {
                    //                    onFailedLogin()
                    doAsync {
                        uiThread {
                            toast("FAILED RETRIEVING USER")
                        }
                    }
                }))
    }

    private fun showUserDetail(data: UIDataArray<List<UserProfile>>) {
        doAsync {
            uiThread {
                toast("USER DETAILS OK " + data.request.result[0])
            }
        }
    }

}